import MasterPage from "../../components/layout/MasterPage";
import { useLocation, useNavigate } from "react-router-dom";
import { useState, useEffect, useRef } from "react";
import { notificationService } from "../../api";
import {

  FaFileAlt,
  FaImage,
  FaCheck,
  FaSave,
  FaArrowLeft,
  FaPhotoVideo,
} from "react-icons/fa";
import { toast } from "react-hot-toast";
import noImagePlaceholder from "../../assets/no_image_placeholder.png";
import { getImageSrc, getMimeTypeFromBase64 } from "../../utils/StringHelpers";
import "./NotificationDetailsPage.css";

const NotificationDetailsPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const notification = location.state?.notification || null;
  const isAddMode = !notification;
  const fileInputRef = useRef(null);

  const [formData, setFormData] = useState({
    id: notification?.id || 0,
    title: notification?.title || "",
    content: notification?.content || "",
    forClient: notification?.forClient === 1,
  });

  const [imageFile, setImageFile] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [errors, setErrors] = useState({});
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (!isAddMode && notification) {
      setFormData({
        id: notification.id,
        title: notification.title || "",
        content: notification.content || "",
        forClient: notification.forClient === 1,
      });

      if (notification.image) {
        setImagePreview(getImageSrc(notification.image));
      }
    }
  }, [isAddMode, notification]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    const newValue = type === "checkbox" ? checked : value;

    setFormData((prev) => ({
      ...prev,
      [name]: newValue,
    }));

    // Clear error when field is changed
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    // Check file size (2MB limit)
    if (file.size > 2 * 1024 * 1024) {
      toast.error("Image size should be less than 2MB");
      return;
    }

    setImageFile(file);

    // Create preview
    const reader = new FileReader();
    reader.onload = () => {
      setImagePreview(reader.result);
    };
    reader.readAsDataURL(file);
  };

  const handleImageClick = () => {
    fileInputRef.current?.click();
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.title.trim()) {
      newErrors.title = "Notification heading is required";
    }

    if (!formData.content.trim()) {
      newErrors.content = "Notification content is required";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    if (!isAddMode && !showConfirmModal) {
      setShowConfirmModal(true);
      return;
    }

    setIsSubmitting(true);

    try {
      // Convert image file to base64 if there is one
      let imageData = null;
      if (imageFile) {
        imageData = await convertFileToBase64(imageFile);
      }

      const requestData = {
        title: formData.title,
        content: formData.content,
        forClient: formData.forClient ? 1 : 0,
      };

      if (imageData) {
        requestData.image = imageData.split(",")[1]; // Remove data:image/xyz;base64, part
      }

      if (isAddMode) {
        await notificationService.create(requestData);
        toast.success("Notification added successfully!");
      } else {
        await notificationService.update(formData.id, requestData);
        toast.success("Notification updated successfully!");
      }

      navigate("/notifications");
    } catch (error) {
      console.error("Error saving notification:", error);
      toast.error("Failed to save notification. Please try again.");
    } finally {
      setIsSubmitting(false);
      setShowConfirmModal(false);
    }
  };

  const convertFileToBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
    });
  };

  const handleCancel = () => {
    navigate("/notifications");
  };

  const handleConfirmEdit = () => {
    // Continue with form submission
    handleSubmit({ preventDefault: () => {} });
  };

  const handleCloseModal = () => {
    setShowConfirmModal(false);
  };

  return (
    <MasterPage
      currentRoute={isAddMode ? "Add Notification" : "Edit Notification"}
    >
      <div className="notification-details-container">
        <form onSubmit={handleSubmit}>
          <div className="form-container">
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="title">
                  Notification Heading
                </label>
                <textarea
                  id="title"
                  name="title"
                  className="form-control"
                  value={formData.title}
                  onChange={handleChange}
                  placeholder="Enter notification heading"
                  rows="5"
                />
                {errors.title && (
                  <div className="error-message">{errors.title}</div>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="content">
                  <FaFileAlt className="icon" /> Notification Content
                </label>
                <textarea
                  id="content"
                  name="content"
                  className="form-control"
                  value={formData.content}
                  onChange={handleChange}
                  placeholder="Enter notification content"
                  rows="5"
                />
                {errors.content && (
                  <div className="error-message">{errors.content}</div>
                )}
              </div>
            </div>

            <div className="form-row">
              <div className="checkbox-container">
                <input
                  type="checkbox"
                  id="forClient"
                  name="forClient"
                  checked={formData.forClient}
                  onChange={handleChange}
                />
                <label htmlFor="forClient">Notification for client</label>
              </div>
            </div>
          </div>

          {/* Image Section */}
          <div className="image-section">
            <h3 className="image-section-title">Notification Image</h3>
            <div className="image-content">
              <div className="image-preview" onClick={handleImageClick}>
                {imagePreview ? (
                  <img src={imagePreview} alt="Notification preview" />
                ) : (
                  <div className="placeholder-icon">
                    <FaImage />
                  </div>
                )}
              </div>
              <div className="image-upload-info">
                <h4 className="image-upload-title">
                  Upload a notification image
                </h4>
                <p className="image-upload-hint">
                  Recommended size: less than 2MB
                </p>
                <button
                  type="button"
                  className="btn btn-upload"
                  onClick={handleImageClick}
                >
                  <FaPhotoVideo /> Select Image
                </button>
                <input
                  type="file"
                  ref={fileInputRef}
                  onChange={handleImageChange}
                  accept="image/*"
                  style={{ display: "none" }}
                />
              </div>
            </div>
          </div>

          <div className="btn-container">
            <button
              type="button"
              className="btn btn-cancel"
              onClick={handleCancel}
              disabled={isSubmitting}
            >
              <FaArrowLeft /> Go Back
            </button>
            <button
              type="submit"
              className="btn btn-save"
              disabled={isSubmitting}
            >
              <FaSave /> Save
            </button>
          </div>
        </form>

        {/* Confirmation Modal for Edit */}
        {showConfirmModal && (
          <div className="modal-overlay">
            <div className="modal-content">
              <div className="modal-header">
                <h4>Confirm Edit</h4>
                <button className="modal-close" onClick={handleCloseModal}>
                  &times;
                </button>
              </div>
              <div className="modal-body">
                <p>Are you sure you want to edit this item?</p>
              </div>
              <div className="modal-footer">
                <button className="btn btn-cancel" onClick={handleCloseModal}>
                  <FaArrowLeft /> Cancel
                </button>
                <button className="btn btn-save" onClick={handleConfirmEdit}>
                  <FaSave /> Confirm
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </MasterPage>
  );
};

export default NotificationDetailsPage;
