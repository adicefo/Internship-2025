import { useKeycloak } from "@react-keycloak/web";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  FaCar,
  FaUsers,
  FaRoute,
  FaCarAlt,
  FaBell,
  FaStar,
  FaShoppingCart,
  FaBars,
  FaSignOutAlt,
  FaCog,
  FaChartBar,
  FaMoneyBillWave,
  FaCarSide,
  FaEdit,
  FaTrash,
  FaEye,
  FaSearch,
  FaHome,
  FaAccessibleIcon,
  FaSearchPlus,
} from "react-icons/fa";
import { vehicleService } from "../../api";
import MasterPage from "../../components/layout/MasterPage";
import ConfirmDialog from "../../utils/ConfirmDialog";
import toast from "react-hot-toast";
import noImagePlaceholder from "../../assets/no_image_placeholder.png";
import { getImageSrc } from "../../utils/StringHelpers";
const VehiclePage = () => {
  const { keycloak } = useKeycloak();
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showDialog, setShowDialog] = useState(false);
  const [deleteId,setDeleteId]=useState(0);
  const navigate = useNavigate();

  // Filter state
  const [availableFilter, setAvailableFilter] = useState("");

  const fetchVehicles = async (filter = {}) => {
    try {
      setLoading(true);
      const response = await vehicleService.getAll(filter);
      console.log("API response:", response);
      setVehicles(response.data.items || []);
      setError(null);
    } catch (err) {
      console.error("Error fetching vehicles:", err);
      setError("Failed to load vehicles. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchVehicles();
  }, []);

  const confirmDelete = async (id) => {
    setShowDialog(false);
    try {
      await vehicleService.delete(deleteId);
      toast.success("Vehicle deleted successfully");
      fetchVehicles();
    } catch {
      toast.error("Error deleting vehcile");
    }
  };
  const handleEditVehicle = (vehicle) => {
    navigate("/vehicle/edit", { state: { vehicle } });
  };

  const handleDeleteVehicle = (id) => {
    setShowDialog(true);
    setDeleteId(id);
  };

  const handleAddVehicle = () => {
    navigate("/vehicle/add");
  };

  const handleAvailableFilter = (e) => {
    setAvailableFilter(e.target.value);
  };

  const handleFilter = () => {
    var filter = {
      available: availableFilter == "Available" ? true : false,
    };
    fetchVehicles(filter);
  };

  return (
    <MasterPage currentRoute="Vehicles">
      <div className="drivers-section">
        <div className="section-header">
          <h3>Vehicles Management</h3>
          <button className="add-button" onClick={handleAddVehicle}>
            Add New Vehicle
          </button>
        </div>
        {/* Filter controls */}
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
                    id="availableFilter"
                    name="availableFilter"
                    style={{ fontWeight: "bold" }}
                    value={availableFilter}
                    onChange={handleAvailableFilter}
                  >
                    <option value="Available" style={{ fontWeight: "bold" }}>
                      Available
                    </option>
                    <option value="Unavailable" style={{ fontWeight: "bold" }}>
                      Unavailable
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
          <div className="loading-indicator">Loading drivers data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="drivers-table-container">
            <table className="drivers-table table table-hover">
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
                    <td>{vehicle.available ? "Available" : "Unavailable"}</td>
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
};

export default VehiclePage;
