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
  FaChevronLeft,
  FaChevronRight,
} from "react-icons/fa";
import { vehicleService } from "../../api";
import MasterPage from "../../components/layout/MasterPage";
import ConfirmDialog from "../../utils/ConfirmDialog";
import toast from "react-hot-toast";
import noImagePlaceholder from "../../assets/no_image_placeholder.png";
import { getImageSrc } from "../../utils/StringHelpers";
import "./VehiclePage.css";
const VehiclePage = () => {
  const { keycloak } = useKeycloak();
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);
  const navigate = useNavigate();

  // Filter state
  const [availableFilter, setAvailableFilter] = useState("");

  // Pagination state
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const pageSize = 3; // 3 items per page as requested

  const fetchVehicles = async (filter = {}) => {
    try {
      setLoading(true);
      // Add pagination parameters to filter
      const paginatedFilter = {
       ...filter,
        pageNumber: currentPage,
        pageSize: pageSize,
      };
      const response = await vehicleService.getAll(paginatedFilter);
      console.log("API response:", response);

      // Store current items
      const currentItems = response.data.items || [];
      setVehicles(currentItems);

      // Simplified pagination logic
      let total = 0;
      if (response.data.count !== undefined) {
        // If backend provides total items
        total = response.data.count;
      } 

      setTotalItems(total);
      // Calculate total pages and ensure we don't navigate to empty pages
      const calculatedTotalPages = Math.ceil(total / pageSize);
      setTotalPages(calculatedTotalPages);

      // If current page is beyond valid pages, go back to last valid page
      if (currentPage >= calculatedTotalPages && calculatedTotalPages > 0) {
        setCurrentPage(calculatedTotalPages - 1);
      }

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
  }, [currentPage]); // Re-fetch when page changes

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
    // Reset to first page when filtering
    setCurrentPage(0);
    var filter = {
      available: availableFilter == "Available" ? true : false,
    };
    fetchVehicles(filter);
  };

  const handlePageChange = (newPage) => {
    // Simple validation to prevent going to invalid pages
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
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
          <div className="vehicles-table-container">
            <table className="vehicles-table table table-hover">
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
                          vehicle.available
                            ? "status-available"
                            : "status-unavailable"
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

            {/* Pagination controls - Simplified */}
            {vehicles.length > 0 && (
              <div className="pagination-container">
                <div className="pagination-info">
                  Page {currentPage + 1} of {totalPages} ({totalItems} total
                  items)
                </div>
                <div className="pagination-controls">
                  {/* Previous page button - disabled on first page */}
                  <button
                    className="pagination-button pagination-arrow"
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 0}
                    title="Previous page"
                  >
                    <FaChevronLeft size={16} />
                  </button>

                  {/* Page buttons - limited to realistic values */}
                  {Array.from({ length: totalPages }, (_, i) => i).map(
                    (page) => (
                      <button
                        key={page}
                        className={`pagination-button ${
                          currentPage === page ? "active" : ""
                        }`}
                        onClick={() => handlePageChange(page)}
                      >
                        {page + 1}
                      </button>
                    )
                  )}

                  {/* Next page button - disabled on last page */}
                  <button
                    className="pagination-button pagination-arrow"
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={currentPage >= totalPages - 1}
                    title="Next page"
                  >
                    <FaChevronRight size={16} />
                  </button>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </MasterPage>
  );
};

export default VehiclePage;
