import { useNavigate } from "react-router-dom";
import MasterPage from "../../components/layout/MasterPage";
import { useState,useEffect } from "react";
import {
  FaSearch,
  FaSearchPlus,
  FaEdit,
  FaTrash,
  FaPlay,
} from "react-icons/fa";
import "./RentPage.css";
import { rentService } from "../../api";
import {toast} from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";
const RentPage = () => {
  const navigate = useNavigate();
  const [rents, setRents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);
  const [statusFilter, setStatusFilter] = useState("");


  const fetchRents= async (filter)=>{
    try {
      setLoading(true);
      const response = await rentService.getAll(filter);
      console.log("API response:", response);
      setRents(response.data.items || []);
      setError(null);
    } catch (err) {
      console.error("Error fetching rents:", err);
      setError("Failed to load rents. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(()=>{
    fetchRents();
  },[]);

  const handleAddRent = () => {
    navigate("/rent/add");
  };
  
  
  const handleFilter = () => {
    console.log("Filtering with status:", statusFilter);
    var filter={status:statusFilter};
    fetchRents(filter);}

  const handleStatusFilter = (e) => {
    setStatusFilter(e.target.value);
  };

  const handleActivateRent = (rent) => {
    if (rent.status === "wait") {
      navigate(`/rent/activate`, { state: { rent } });
    } else {
      alert("Unable operation. Your status is not 'wait'!");
    }
  };

  const handleEditRent = (rent) => {
    if (rent.status !== "finished") {
      navigate(`/rent/edit`, { state: { rent } });
    } else {
      alert("Unable operation. You cannot edit when your status is finished!");
    }
  };

  const handleDeleteRent = (rentId, status) => {
    if (status !== "active") {
      setDeleteId(rentId);
      setShowDialog(true);
    } else {
      alert("Cannot delete rent when status is active");
    }
  };

  const confirmDelete = async () => {
    try {
      await rentService.delete(deleteId);
      toast.success("Rent deleted successfully");
    } catch (err) {
      console.error("Error deleting rent:", err);
      toast.error("Failed to delete rent. Please try again.");
    }
  };

  return (
    <MasterPage currentRoute="Rent">
      <div className="routes-section">
        <div className="section-header">
          <h3>Rent Management</h3>
          <button className="add-button" onClick={handleAddRent}>
            Add New Rent
          </button>
        </div>
        {/*Filter controls*/}
        <div className="filter-container">
          <div className="filter-row">
            <div className="filter-group">
              <div className="form-group">
                <label htmlFor="statusFilter">Status</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon">
                    <FaSearchPlus />
                  </i>
                  <select
                    id="statusFilter"
                    name="stautsFilter"
                    style={{ fontWeight: "bold" }}
                    value={statusFilter}
                    onChange={handleStatusFilter}
                  >
                    <option value="" style={{ fontWeight: "bold" }}>
                      All statuses
                    </option>
                    <option value="wait" style={{ fontWeight: "bold" }}>
                      Wait
                    </option>
                    <option value="active" style={{ fontWeight: "bold" }}>
                      Active
                    </option>
                    <option value="finished" style={{ fontWeight: "bold" }}>
                      Finished
                    </option>
                  </select>
                </div>
              </div>
            </div>

            <button className="filter-button" onClick={handleFilter}>
              <FaSearch className="filter-icon" /> Filter
            </button>
          </div>
        </div>
        {/*Rent list*/}
        {loading ? (
          <div className="loading-indicator">Loading rent data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="rent-table-container">
            <table className="rent-table table table-hover">
              <thead>
                <tr>
                  <th style={{ width: "80px" }}>Start Date</th>
                  <th style={{ width: "80px" }}>End Date</th>
                  <th style={{ width: "100px" }}>Price</th>
                  <th style={{ width: "150px" }}>Status</th>
                  <th style={{ width: "120px" }}>Vehicle</th>
                  <th style={{ width: "100px" }}>Client</th>
                  <th style={{ width: "100px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {rents.map((rent) => (
                  <tr key={rent.id}>
                    <td>{rent.rentDate?.toString().substring(0, 10)}</td>
                    <td>{rent.endDate?.toString().substring(0, 10)}</td>
                    <td>{rent.fullPrice.toFixed(2) + " KM"}</td>
                    <td>
                      <span
                        className={`status-pill ${rent.status.toLowerCase()}`}
                      >
                        {rent.status.toUpperCase()}
                      </span>
                    </td>
                    <td>{rent.vehicle?.name ?? "-"}</td>
                    <td>
                      {rent.client?.user?.name} {rent.client?.user?.surname}
                    </td>

                    <td className="action-buttons">
                      {/* Activate button */}
                      {rent.status === "wait" ? (
                        <button
                          className="activate-button"
                          onClick={() => handleActivateRent(rent)}
                          title="Activate rent"
                        >
                          <FaPlay style={{ color: "blue" }} />
                        </button>
                      ) : (
                        <button
                          className="activate-button-disabled"
                          onClick={() => handleActivateRent(rent)}
                          title="Status not 'wait'"
                        >
                          <FaPlay style={{ color: "grey" }} />
                        </button>
                      )}

                      {/* Edit button */}
                      {rent.status !== "finished" ? (
                        <button
                          className="edit-button"
                          onClick={() => handleEditRent(rent)}
                          title="Edit rent"
                        >
                          <FaEdit style={{ color: "blue" }} />
                        </button>
                      ) : (
                        <button
                          className="edit-button-disabled"
                          onClick={() => handleEditRent(rent)}
                          title="Cannot edit finished rent"
                        >
                          <FaEdit style={{ color: "grey" }} />
                        </button>
                      )}

                      {/* Delete button */}
                      {rent.status !== "active" ? (
                        <button
                          className="delete-button"
                          onClick={() => handleDeleteRent(rent.id, rent.status)}
                          title="Delete rent"
                        >
                          <FaTrash style={{ color: "red" }} />
                        </button>
                      ) : (
                        <button
                          className="delete-button-disabled"
                          onClick={() => handleDeleteRent(rent.id, rent.status)}
                          title="Cannot delete active rent"
                        >
                          <FaTrash style={{ color: "grey" }} />
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

{showDialog && (
                        <ConfirmDialog
                          title="Delete Confirmation"
                          message="Are you sure you want to delete this item?"
                          onConfirm={() => confirmDelete()}
                          onCancel={() => setShowDialog(false)}
                        />
                      )}
      </div>
    </MasterPage>
  );
};
export default RentPage;
