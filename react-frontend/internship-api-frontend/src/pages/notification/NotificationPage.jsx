import { useNavigate } from "react-router-dom";
import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { notificationService } from "../../api";
import {
  FaSearch,
  FaEdit,
  FaTrash,
  FaChevronLeft,
  FaChevronRight,
  FaSyncAlt
} from "react-icons/fa";
import "./NotificationPage.css";
import noImagePlaceholder from "../../assets/no_image_placeholder.png";
import { getImageSrc } from "../../utils/StringHelpers";
import ConfirmDialog from "../../utils/ConfirmDialog";

const NotificationPage = () => {
  const navigate = useNavigate();
  const [notificationTypeFilter, setNotificationTypeFilter] = useState("");
  const [headingFilter, setHeadingFilter] = useState("");
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);

  // Pagination state
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const pageSize = 3; // 3 items per page as requested

  const fetchNotifications = async (filter = {}) => {
    try {
      setLoading(true);
      const response = await notificationService.getAll(filter);
      const items = response.data.items || [];
      setNotifications(items);
      setTotalItems(items.length);
      const calculatedTotalPages = Math.ceil(items.length / pageSize);
      setTotalPages(calculatedTotalPages);
      if (currentPage >= calculatedTotalPages && calculatedTotalPages > 0) {
        setCurrentPage(calculatedTotalPages - 1);
      }
      setError(null);
    } catch (err) {
      console.error("Error fetching notifications:", err);
      setError("Failed to load notifications. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    var filter=notificationTypeFilter !== ""
    ? {
        forClient : notificationTypeFilter == "user" ? 1 : 0,
        title: headingFilter,
      }
    : {
        title: headingFilter,
      };
    fetchNotifications(filter);
  }, [currentPage]); // Re-fetch when page changes

  const handleAddNotification = () => {
    navigate("/notification/add");
  };
  const confirmDelete = async () => {
    try {
      await notificationService.delete(deleteId);
      toast.success("Notification deleted successfully");
    } catch (err) {
      console.error("Error deleting notification:", err);
      toast.error("Failed to delete notification. Please try again.");
    }
  };
  const handleFilter = () => {
    // Reset to first page when filtering
    setCurrentPage(0);
    var filter =
     notificationTypeFilter !== ""
        ? {
            forClient : notificationTypeFilter == "user" ? 1 : 0,
            title: headingFilter,
          }
        : {
            title: headingFilter,
          };
    fetchNotifications(filter);
  };

  const handleEditNotification = (notification) => {
    navigate("/notification/edit", { state: { notification: notification } });
  };

  const handleDeleteNotification = (id) => {
    setDeleteId(id);
    setShowDialog(true);
  };
  const handleRefresh = () => {
  setHeadingFilter("");
  setNotificationTypeFilter("");
  setCurrentPage(0); // Reset to first page
  fetchNotifications({}); // Fetch without filters
};

  const handlePageChange = (newPage) => {
    // Simple validation to prevent going to invalid pages
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
  };
    // Paginated reviews for current page
    const paginatedNotifications = notifications.slice(
      currentPage * pageSize,
      currentPage * pageSize + pageSize
    );

  return (
    <MasterPage currentRoute="Notification">
      <div className="notification-section">
        <div className="section-header">
          <h3>Notification Management</h3>
          <button className="add-button" onClick={handleAddNotification}>
            Add New Notification
          </button>
        </div>
        {/*Filter controls*/}
        <div className="filter-container">
          <div className="filter-row">
            <div className="filter-group">
              <div className="form-group">
                <label htmlFor="headingFilter">Heading:</label>
                <input
                  type="text"
                  id="headingFilter"
                  value={headingFilter}
                  onChange={(e) => setHeadingFilter(e.target.value)}
                  placeholder="Filter by heading"
                />
                <label htmlFor="notificationTypeFilter">
                  Notification Type
                </label>
                <select
                  id="notificationTypeFilter"
                  name="notificationTypeFilter"
                  style={{ fontWeight: "bold" }}
                  value={notificationTypeFilter}
                  onChange={(e) => setNotificationTypeFilter(e.target.value)}
                >
                  <option value="" style={{ fontWeight: "bold" }}>
                    All types
                  </option>
                  <option value="user" style={{ fontWeight: "bold" }}>
                    User
                  </option>
                  <option value="driver" style={{ fontWeight: "bold" }}>
                    Driver
                  </option>
                </select>
              </div>
            </div>
 <FaSyncAlt className="refresh-button" onClick={handleRefresh} />
            <button className="filter-button" onClick={handleFilter}>
              <FaSearch className="filter-icon" /> Filter
            </button>
           
          </div>
        </div>
        {loading ? (
          <div className="loading-indicator">Loading notification data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="notification-table-container">
            <table className="notification-table table table-hover">
              <thead>
                <tr>
                  <th style={{ width: "80px" }}>Heading</th>
                  <th style={{ width: "80px" }}>Content</th>
                  <th style={{ width: "100px" }}>Image</th>
                  <th style={{ width: "150px" }}>Date</th>
                  <th style={{ width: "120px" }}>For</th>
                  <th style={{ width: "100px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {paginatedNotifications.map((notification) => (
                  <tr key={notification.id}>
                    <td className="tooltip-cell">
                      {notification.title != null
                        ? notification.title.length > 15
                          ? notification.title.substring(0, 15) + "..."
                          : notification.title
                        : "-"}
                      {notification.title && notification.title.length > 15 && (
                        <span className="tooltip-text">
                          {notification.title}
                        </span>
                      )}
                    </td>
                    <td className="tooltip-cell">
                      {notification.content != null
                        ? notification.content.length > 25
                          ? notification.content.substring(0, 25) + "..."
                          : notification.content
                        : "-"}
                      {notification.content &&
                        notification.content.length > 25 && (
                          <span className="tooltip-text">
                            {notification.content}
                          </span>
                        )}
                    </td>
                    <td>
                      {notification?.image ? (
                        <img
                          src={getImageSrc(notification.image)}
                          alt={`${notification.image}`}
                          className="notifiaction-image"
                          style={{
                            width: "60px",
                            height: "40px",
                            objectFit: "cover",
                            borderRadius: "6px",
                          }}
                        />
                      ) : (
                        <div className="no-image-placeholders">
                          <img
                            src={noImagePlaceholder}
                            alt="No Image"
                            className="notification-no-image"
                            style={{
                              width: "60px",
                              height: "40px",
                              objectFit: "cover",
                              borderRadius: "6px",
                            }}
                          />
                        </div>
                      )}
                    </td>
                    <td>
                      {notification.addingDate != null
                        ? notification.addingDate.toString().substring(0, 10)
                        : "-"}
                    </td>

                    <td>
                      <span
                        className={`status-pill ${
                          notification.forClient == 1 ? "user" : "driver"
                        }`}
                      >
                        {notification.forClient == 1 ? "User" : "Driver"}
                      </span>
                    </td>

                    <td className="action-buttons">
                      {/* Edit button */}
                      <button
                        className="edit-button"
                        onClick={() => handleEditNotification(notification)}
                        title="Edit notification"
                      >
                        <FaEdit style={{ color: "blue" }} />
                      </button>

                      {/* Delete button */}
                      <button
                        className="delete-button"
                        onClick={() =>
                          handleDeleteNotification(notification.id)
                        }
                        title="Delete notifiaction"
                      >
                        <FaTrash style={{ color: "red" }} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {/* Pagination controls */}
            {notifications.length > 0 && (
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

export default NotificationPage;
