import MasterPage from "../../components/layout/MasterPage";
import { useLocation, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { reviewService, driverService, clientService } from "../../api";
import {
  FaStar,
  FaSave,
  FaArrowLeft,
  FaUsers,
  FaUserAlt,
} from "react-icons/fa";
import { toast } from "react-hot-toast";
import "./ReviewDetailsPage.css";

const ReviewDetailsPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const review = location.state?.review || null;
  const isAddMode = !review;

  const [formData, setFormData] = useState({
    id: review?.id || 0,
    value: review?.value || "",
    description: review?.description || "",
    client_id: review?.client?.id || "",
    driver_id: review?.driver?.id || "",
  });

  const [drivers, setDrivers] = useState([]);
  const [clients, setClients] = useState([]);
  const [errors, setErrors] = useState({});
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const driversResponse = await driverService.getAll();
        setDrivers(driversResponse.data.items || []);

        const clientsResponse = await clientService.getAll();
        setClients(clientsResponse.data.items || []);

        // If in edit mode, set the form data
        if (!isAddMode && review) {
          setFormData({
            id: review.id,
            value: review.value || "",
            description: review.description || "",
            client_id: review.client?.id || "",
            driver_id: review.driver?.id || "",
          });
        }
      } catch (error) {
        console.error("Error fetching data:", error);
        toast.error("Failed to load data. Please try again.");
      }
    };

    fetchData();
  }, [isAddMode, review]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));

    // Clear error when field is changed
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.value) {
      newErrors.value = "Rating is required";
    }

    if (!formData.description) {
      newErrors.description = "Description is required";
    }

    if (!formData.driver_id) {
      newErrors.driver_id = "Please select a driver";
    }

    if (!formData.client_id) {
      newErrors.client_id = "Please select a client";
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
      if (isAddMode) {
        var request={
            value:formData.value,
            description:formData.description,
            reviews_id:formData.client_id,
            reviewed_id:formData.driver_id
        };
        await reviewService.create(request);
        toast.success("Review added successfully!");
      } else {
        var request={
            value:formData.value,
            description:formData.description,
        };
        await reviewService.update(formData.id, request);
        toast.success("Review updated successfully!");
      }
      navigate("/reviews");
    } catch (error) {
      console.error("Error saving review:", error);
      toast.error("Failed to save review. Please try again.");
    } finally {
      setIsSubmitting(false);
      setShowConfirmModal(false);
    }
  };

  const handleCancel = () => {
    navigate("/reviews");
  };

  const handleConfirmEdit = () => {
    // Continue with form submission
    handleSubmit({ preventDefault: () => {} });
  };

  const handleCloseModal = () => {
    setShowConfirmModal(false);
  };

  const getClientName = (id) => {
    const client = clients.find((c) => c.id === parseInt(id));
    return client
      ? `${client.user?.name || ""} ${client.user?.surname || ""}`
      : "";
  };

  const getDriverName = (id) => {
    const driver = drivers.find((d) => d.id === parseInt(id));
    return driver
      ? `${driver.user?.name || ""} ${driver.user?.surname || ""}`
      : "";
  };

  return (
    <MasterPage currentRoute={isAddMode ? "Add Review" : "Edit Review"}>
      <div className="review-details-container">
        <h2 className="section-title">Review Information</h2>

        <form onSubmit={handleSubmit}>
          {/* Rating Details Card */}
          <div className="card">
            <div className="card-header">
              <FaStar className="card-header-icon" />
              <h3 className="card-title">Rating Details</h3>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="value">Rating Value</label>
                <select
                  id="value"
                  name="value"
                  className="rating-dropdown"
                  value={formData.value}
                  onChange={handleChange}
                >
                  <option value="">Select rating</option>
                  {[1, 2, 3, 4, 5].map((num) => (
                    <option key={num} value={num}>
                      {num} {Array(num).fill("★").join("")}
                    </option>
                  ))}
                </select>
                {errors.value && (
                  <div className="error-message">{errors.value}</div>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="description">Review Description</label>
                <textarea
                  id="description"
                  name="description"
                  className="form-control"
                  value={formData.description}
                  onChange={handleChange}
                  placeholder="Enter review description"
                />
                {errors.description && (
                  <div className="error-message">{errors.description}</div>
                )}
              </div>
            </div>
          </div>

          {/* People Involved Card */}
          <div className="card">
            <div className="card-header">
              <FaUsers className="card-header-icon blue" />
              <h3 className="card-title">People Involved</h3>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Driver (Person being reviewed)</label>
                <select
                  id="driver_id"
                  name="driver_id"
                  className="form-control"
                  value={formData.driver_id}
                  onChange={handleChange}
                  disabled={!isAddMode}
                >
                  <option value="">Select driver</option>
                  {drivers.map((driver) => (
                    <option key={driver.id} value={driver.id}>
                      {driver.user?.name} {driver.user?.surname}
                    </option>
                  ))}
                </select>
                {errors.driver_id && (
                  <div className="error-message">{errors.driver_id}</div>
                )}
              </div>

              <div className="form-group">
                <label>Client (Reviewer)</label>
                <select
                  id="client_id"
                  name="client_id"
                  className="form-control"
                  value={formData.client_id}
                  onChange={handleChange}
                  disabled={!isAddMode}
                >
                  <option value="">Select client</option>
                  {clients.map((client) => (
                    <option key={client.id} value={client.id}>
                      {client.user?.name} {client.user?.surname}
                    </option>
                  ))}
                </select>
                {errors.client_id && (
                  <div className="error-message">{errors.client_id}</div>
                )}
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
              className="save-button"
              disabled={isSubmitting}
            >
              <FaSave /> Save
            </button>
          </div>
        </form>

        {/* Confirmation Modal */}
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

export default ReviewDetailsPage;
