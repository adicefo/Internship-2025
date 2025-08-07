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
  FaChevronLeft,
  FaChevronRight,
} from "react-icons/fa";
import { driverService } from "../../api";
import MasterPage from "../../components/layout/MasterPage";
import "./DriverPage.css";
import ConfirmDialog from "../../utils/ConfirmDialog";
import toast from "react-hot-toast";

const DriverPage = () => {
  const { keycloak } = useKeycloak();
  const [drivers, setDrivers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);
  const navigate = useNavigate();

  // Filter state
  const [nameFilter, setNameFilter] = useState("");
  const [surnameFilter, setSurnameFilter] = useState("");

  // Pagination state
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const pageSize = 3; // 3 items per page as requested

  const fetchDrivers = async (filter = {}) => {
    try {
      setLoading(true);
      const response = await driverService.getAll(filter);
      const items = response.data.items || [];
      setDrivers(items);
      setTotalItems(items.length);
      const calculatedTotalPages = Math.ceil(items.length / pageSize);
      setTotalPages(calculatedTotalPages);
      if (currentPage >= calculatedTotalPages && calculatedTotalPages > 0) {
        setCurrentPage(calculatedTotalPages - 1);
      }
      setError(null);
    } catch (err) {
      console.error("Error fetching rents:", err);
      setError("Failed to load rents. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDrivers({ name: nameFilter, surname: surnameFilter });
  }, [currentPage]);

  const confirmDelete = async (id) => {
    setShowDialog(false);
    try {
      await driverService.delete(deleteId);
      toast.success("Driver deleted successfully");
      fetchDrivers();
    } catch {
      toast.error("Error deleting driver");
    }
  };
  const handleEditDriver = (driver) => {
    navigate("/driver/edit", { state: { driver } });
  };

  const handleDeleteDriver = (id) => {
    setShowDialog(true);
    setDeleteId(id);
  };

  const handleAddDriver = () => {
    navigate("/driver/add");
  };

  const handleFilter = () => {
    // Reset to first page when filtering
    setCurrentPage(0);
    var filter = {
      name: nameFilter,
      surname: surnameFilter,
    };
    fetchDrivers(filter);
  };

  const handlePageChange = (newPage) => {
    // Simple validation to prevent going to invalid pages
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
  };
  // Paginated reviews for current page
  const paginatedDrivers = drivers.slice(
    currentPage * pageSize,
    currentPage * pageSize + pageSize
  );
  return (
    <MasterPage currentRoute="Drivers">
      <div className="drivers-section">
        <div className="section-header">
          <h3>Drivers Management</h3>
          <button className="add-button" onClick={handleAddDriver}>
            Add New Driver
          </button>
        </div>
        {/* Filter controls */}
        <div className="filter-container">
          <div className="filter-row">
            <div className="filter-group">
              <label htmlFor="nameFilter">Name:</label>
              <input
                type="text"
                id="nameFilter"
                value={nameFilter}
                onChange={(e) => setNameFilter(e.target.value)}
                placeholder="Filter by name"
              />
            </div>

            <div className="filter-group">
              <label htmlFor="surnameFilter">Surname:</label>
              <input
                type="text"
                id="surnameFilter"
                value={surnameFilter}
                onChange={(e) => setSurnameFilter(e.target.value)}
                placeholder="Filter by surname"
              />
            </div>

            <button className="filter-buttond" onClick={handleFilter}>
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
                  <th style={{ width: "80px" }}>Name</th>
                  <th style={{ width: "80px" }}>Surname</th>
                  <th style={{ width: "100px" }}>Username</th>
                  <th style={{ width: "150px" }}>Email</th>
                  <th style={{ width: "120px" }}>Phone</th>
                  <th style={{ width: "80px" }} title="Number of driving hours">
                    Hours
                  </th>
                  <th
                    style={{ width: "80px" }}
                    title="Number of clients served"
                  >
                    Clients
                  </th>
                  <th style={{ width: "100px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {paginatedDrivers.map((driver) => (
                  <tr key={driver.id}>
                    <td>{driver.user.name}</td>
                    <td>{driver.user.surname}</td>
                    <td>{driver.user.username}</td>
                    <td>{driver.user.email}</td>
                    <td>{driver.user.telephoneNumber}</td>
                    <td>{driver.numberOfHoursAmount}</td>
                    <td>{driver.numberOfClientsAmount}</td>
                    <td className="action-buttons">
                      <button
                        className="edit-button"
                        onClick={() => handleEditDriver(driver)}
                        title="Edit driver"
                      >
                        <FaEdit />
                      </button>
                      <button
                        className="delete-button"
                        onClick={() => handleDeleteDriver(driver.id)}
                        title="Delete driver"
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

            {/* Pagination controls */}
            {drivers.length > 0 && (
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

export default DriverPage;
