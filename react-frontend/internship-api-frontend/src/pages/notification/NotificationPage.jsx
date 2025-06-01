import { useNavigate } from "react-router-dom";
import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { notificationService } from "../../api";
import { FaSearch, FaEdit, FaTrash } from "react-icons/fa";
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

  const fetchNotifications = async (filter) => {
    try {
      setLoading(true);
      const response = await notificationService.getAll(filter);
      console.log("API response:", response);
      setNotifications(response.data.items || []);
      setError(null);
    } catch (err) {
      console.error("Error fetching notifications:", err);
      setError("Failed to load notifications. Please try again.");
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    fetchNotifications();
  }, []);
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
    var filter =
      notificationTypeFilter !== ""
        ? {
            forClient: notificationTypeFilter == "user" ? 1 : 0,
            heading: headingFilter,
          }
        : {
            heading: headingFilter,
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
                {notifications.map((notification) => (
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
