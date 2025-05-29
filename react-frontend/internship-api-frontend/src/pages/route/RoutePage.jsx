import MasterPage from "../../components/layout/MasterPage";
import toast from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

import { FaTrash, FaEye, FaSearch ,FaEdit,FaSearchPlus} from "react-icons/fa";import { routeService } from "../../api";
const RoutePage=()=>{
    const navigate = useNavigate();
    const[routes,setRoutes]=useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showDialog, setShowDialog] = useState(false);
    const [deleteId,setDeleteId]=useState(0);
    const [statusFilter,setStatusFilter]=useState('');


    const fetchRoutes=async(filter={})=>{
        try {
            setLoading(true);
            const response = await routeService.getAll(filter);
            console.log("API response:", response);
            setRoutes(response.data.items || []);
            setError(null);
          } catch (err) {
            console.error("Error fetching routes:", err);
            setError("Failed to load routes. Please try again.");
          } finally {
            setLoading(false);
          }
    }
    useEffect(() => {
        fetchRoutes();
      }, []);
    const handleAddRoute=()=>{
        navigate('/route/add');
    }
    const handleStatusFilter=(e)=>{
        setStatusFilter(e.target.value);
    }
    const handleFilter=()=>{
        var filter={
            status:statusFilter
        };
        fetchRoutes(filter);
    }
    return(
        <MasterPage currentRoute="Route">
            <div className="routes-section">

 <div className="section-header">
          <h3>Routes Management</h3>
          <button className="add-button" onClick={handleAddRoute}>
            Add New Route
          </button>
        </div>
        {/*Filter controls*/}
        <div className="filter-container">
          <div className="filter-row">
            <div className="filter-group">
              <div className="form-group">
                <label htmlFor="availableFilter">Availability</label>
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
                    <option value="Wait" style={{ fontWeight: "bold" }}>
                      Wait
                    </option>
                    <option value="Active" style={{ fontWeight: "bold" }}>
                      Active
                    </option>
                    <option value="Finished" style={{ fontWeight: "bold" }}>
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
        {loading ? (
          <div className="loading-indicator">Loading vehicles data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="routes-table-container">
            <table className="routes-table table table-hover">
              <thead>
                <tr>
                  <th style={{ width: "80px" }}>Status</th>
                  <th style={{ width: "80px" }}>Name</th>
                  <th style={{ width: "100px" }}>Image</th>
                  <th style={{ width: "150px" }}>Price</th>
                  <th style={{ width: "120px" }}>Consumption</th>
                  <th style={{ width: "100px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {vehicles.map((vehicle) => (
                  <tr key={vehicle.id}>
                    <td>
  <span
    className={`status-pill ${
      vehicle.available ? "status-available" : "status-unavailable"
    }`}
  >
    {vehicle.available ? "Available" : "Unavailable"}
  </span>
</td>
                    <td>{vehicle.name}</td>
                    <td>
                      <img
                        src={getImageSrc(vehicle.image)}
                        alt="vehicle"
                        style={{
                          width: "60px",
                          height: "40px",
                          objectFit: "cover",
                          borderRadius: "6px",
                        }}
                        onError={(e) => {
                          e.target.onerror = null;
                          e.target.src = noImagePlaceholder;
                        }}
                      />
                    </td>

                    <td>{vehicle.price}</td>
                    <td>{vehicle.averageFuelConsumption}</td>
                    <td className="action-buttons">
                      <button
                        className="edit-button"
                        onClick={() => handleEditVehicle(vehicle)}
                        title="Edit vehicle"
                      >
                        <FaEdit />
                      </button>
                      <button
                        className="delete-button"
                        onClick={() => handleDeleteVehicle(vehicle.id)}
                        title="Delete vehicle"
                      >
                        <FaTrash />
                      </button>
                      {showDialog && (
                        <ConfirmDialog
                          title="Delete Confirmation"
                          message="Are you sure you want to delete this item?"
                          onConfirm={() => confirmDelete()}
                          onCancel={() => setShowDialog(false)}
                        />
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
            </div>
        </MasterPage>
    );
}
export default RoutePage;