import MasterPage from "../../components/layout/MasterPage";
import toast from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

import { FaTrash, FaEye, FaSearch, FaEdit, FaSearchPlus } from "react-icons/fa";
import { routeService } from "../../api";
import "./RoutePage.css";


const RoutePage = () => {
  const navigate = useNavigate();
  const [routes, setRoutes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);
  const [statusFilter, setStatusFilter] = useState("");

  const fetchRoutes = async (filter = {}) => {
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
  };
  useEffect(() => {
    fetchRoutes();
  }, []);
  const handleAddRoute = () => {
    navigate("/route/add");
  };
  const handleEditRoute=(route)=>{
    navigate('/route/edit',{state:{route}});
  }
  const handleDeleteRoute=(id)=>{
    setDeleteId(id);
    setShowDialog(true);
  }
  const confirmDelete=async()=>{
    try{
      await routeService.delete(deleteId);
      toast.success("Route deleted successfully");
    }catch(err){
      console.error("Error deleting route:",err);
      toast.error("Failed to delete route. Please try again.");
    }
  }
  const handleStatusFilter = (e) => {
    setStatusFilter(e.target.value);
  };
  const handleFilter = () => {
    var filter = {
      status: statusFilter,
    };
    fetchRoutes(filter);
  };
  return (
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
        {loading ? (
          <div className="loading-indicator">Loading route data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="routes-table-container">
            <table className="routes-table table table-hover">
              <thead>
                <tr>
                  <th style={{ width: "80px" }}>Client</th>
                  <th style={{ width: "80px" }}>Driver</th>
                  <th style={{ width: "100px" }}>Status</th>
                  <th style={{ width: "150px" }}>Start Date</th>
                  <th style={{ width: "120px" }}>End Date</th>
                  <th style={{ width: "100px" }}>Duration</th>
                  <th style={{ width: "100px" }}>Distance</th>
                  <th style={{ width: "100px" }}>Price</th>
                  <th style={{ width: "100px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {routes.map((route) => (
                  <tr key={route.id}>
                    <td>{route.client.user.name} {route.client.user.surname}</td>
                    <td>{route.driver.user.name} {route.driver.user.surname}</td>
                    <td><span className={`status-pill ${route.status.toLowerCase()}`}>{route.status.toUpperCase()}</span></td>
                    <td>{route.startDate?.toString().substring(0,16)??"-"}</td>
                    <td>{route.endDate?.toString().substring(0,16)??"-"}</td>
                    <td>{route.duration?.toString()+" min"??0}</td>
                    <td>{route.numberOfKilometers!=0?route.numberOfKilometers.toFixed(2)+"km":"0"}</td>
                    <td>{route.fullPrice.toFixed(2)+"KM"??"0"}</td>
                    <td className="action-buttons">
                      <button
                        className="edit-button"
                        onClick={() => handleEditRoute(route)}
                        title="Edit route"
                      >
                        <FaEdit />
                      </button>
                      <button
                        className="delete-button"
                        onClick={() => handleDeleteRoute(route.id)}
                        title="Delete route"
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
};
export default RoutePage;
