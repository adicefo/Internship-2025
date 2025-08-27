import { useKeycloak } from "@react-keycloak/web";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  FaSearch,
  FaEdit,
  FaTrash,
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
  const pageSize = 3;

  const fetchVehicles = async (filter = {}) => {
    try {
      setLoading(true);
      const paginatedFilter = {
        ...filter,
        pageNumber: currentPage,
        pageSize: pageSize,
      };
      const response = await vehicleService.getAll(paginatedFilter);

      const currentItems = response.data.items || [];
      setVehicles(currentItems);

      const total = response.data.count ?? currentItems.length;
      setTotalItems(total);

      const calculatedTotalPages = Math.ceil(total / pageSize);
      setTotalPages(calculatedTotalPages);

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
  }, [currentPage]);

  const confirmDelete = async () => {
    setShowDialog(false);
    try {
      await vehicleService.delete(deleteId);
      toast.success("Vehicle deleted successfully");
      fetchVehicles();
    } catch {
      toast.error("Error deleting vehicle");
    }
  };

  const handleEditVehicle = (vehicle) => {
    navigate("/vehicle/edit", { state: { vehicle } });
  };

  const handleDeleteVehicle = (id) => {
    setDeleteId(id);
    setShowDialog(true);
  };

  const handleAddVehicle = () => {
    navigate("/vehicle/add");
  };

  const handleAvailableFilter = (e) => {
    setAvailableFilter(e.target.value);
  };

  const handleFilter = () => {
    setCurrentPage(0);
    const filter =
      availableFilter === ""
        ? {}
        : { available: availableFilter === "Available" };
    fetchVehicles(filter);
  };

  const handlePageChange = (newPage) => {
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
                  value={availableFilter}
                  onChange={handleAvailableFilter}
                  style={{ fontWeight: "bold" }}
                >
                  <option value="">All</option>
                  <option value="Available">Available</option>
                  <option value="Unavailable">Unavailable</option>
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
                  <th>Status</th>
                  <th>Name</th>
                  <th>Image</th>
                  <th>Price</th>
                  <th>Consumption</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {vehicles.length > 0 ? (
                  vehicles.map((vehicle) => (
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
                            e.currentTarget.onerror = null;
                            e.currentTarget.src = noImagePlaceholder;
                          }}
                        />
                      </td>
                      <td>{vehicle.price + " KM"}</td>
                      <td>{vehicle.averageFuelConsumption + " l/100km"}</td>
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
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="6" className="no-results-message">
                      There are no{" "}
                      {availableFilter
                        ? availableFilter.toUpperCase()
                        : "matching"}{" "}
                      vehicles.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>

            {/* Pagination controls */}
            {vehicles.length > 0 && (
              <div className="pagination-container">
                <div className="pagination-info">
                  Page {currentPage + 1} of {totalPages} ({totalItems} total
                  items)
                </div>
                <div className="pagination-controls">
                  <button
                    className="pagination-button pagination-arrow"
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 0}
                  >
                    <FaChevronLeft size={16} />
                  </button>
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
                  <button
                    className="pagination-button pagination-arrow"
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={currentPage >= totalPages - 1}
                  >
                    <FaChevronRight size={16} />
                  </button>
                </div>
              </div>
            )}
          </div>
        )}

        {/* ConfirmDialog rendered once */}
        {showDialog && (
          <ConfirmDialog
            title="Delete Confirmation"
            message="Are you sure you want to delete this item?"
            onConfirm={confirmDelete}
            onCancel={() => setShowDialog(false)}
          />
        )}
      </div>
    </MasterPage>
  );
};

export default VehiclePage;
