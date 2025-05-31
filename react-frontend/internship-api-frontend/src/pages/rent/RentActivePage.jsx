import MasterPage from "../../components/layout/MasterPage";
import { useLocation, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import {
  FaArrowLeft,
  FaCheckCircle,
  FaTimesCircle,
  FaCheck,
  FaTimes,
} from "react-icons/fa";
import { toast } from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";
import { rentService } from "../../api";
import "./RentActivePage.css";
import noImagePlaceholder from "../../assets/no_image_placeholder.png";
import { getImageSrc } from "../../utils/StringHelpers";
const RentActivePage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const rent = location.state?.rent || null;

  // State for availability check and UI
  const [vehicleAvailability, setVehicleAvailability] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAcceptDialog, setShowAcceptDialog] = useState(false);
  const [showRejectDialog, setShowRejectDialog] = useState(false);

  // Fetch rent details and check availability
  useEffect(() => {
    const fetchData = async () => {
      if (!rent) {
        navigate("/rents");
        return;
      }

      try {
        setLoading(true);

        // Check if vehicle is available for the requested dates
        const availabilityRequest = {
          vehicle_id: rent.vehicle?.id,
          rentDate: rent.rentDate,
          endDate: rent.endDate,
        };

        const availabilityResponse = await rentService.checkAvailability(
          rent.id,
          availabilityRequest
        );
        setVehicleAvailability(availabilityResponse.data);
      } catch (error) {
        console.error("Error fetching data:", error);
        toast.error("Failed to load necessary data");
        setError("Failed to check vehicle availability");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [rent, navigate]);

  // Format date for display
  const formatDate = (dateString) => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    return date.toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  };

  // Handle rent activation
  const handleAcceptRent = async () => {
    try {
      setShowAcceptDialog(false);
      const response = await rentService.activate(rent.id);
      toast.success("Rent request activated successfully");
      navigate("/rents");
    } catch (error) {
      console.error("Error activating rent:", error);
      toast.error("Failed to activate rent request");
    }
  };

  // Handle rent rejection
  const handleRejectRent = async () => {
    try {
      setShowRejectDialog(false);
      await rentService.reject(rent.id);
      toast.success("Rent request rejected");
      navigate("/rents");
    } catch (error) {
      console.error("Error rejecting rent:", error);
      toast.error("Failed to reject rent request");
    }
  };

  // Handle navigation back to rent list
  const handleGoBack = () => {
    navigate("/rents");
  };

  // Render the availability banner
  const renderAvailabilityBanner = () => {
    if (vehicleAvailability === null) {
      return null;
    }

    return (
      <div
        className={`availability-banner ${
          vehicleAvailability ? "available" : "unavailable"
        }`}
      >
        <div className="banner-icon">
          {vehicleAvailability ? (
            <FaCheckCircle size={24} />
          ) : (
            <FaTimesCircle size={24} />
          )}
        </div>
        <div className="banner-text">
          {vehicleAvailability
            ? "Vehicle is available for the requested dates"
            : "Vehicle is NOT available for the requested dates"}
        </div>
      </div>
    );
  };

  return (
    <MasterPage currentRoute="Activate Rent">
      <div className="rent-activate-container">
        <div className="section-header">
          <h3>Rent Activation</h3>
        </div>

        {loading ? (
          <div className="loading-indicator">Loading data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <>
          <div className="rent-active-details-container">
            {/* Availability Banner */}
            {renderAvailabilityBanner()}

            <div className="rent-details-body">
              {/* Left side - Rent Details */}
              <div className="rent-info-section">
                <h4 className="details-title">Rent Details</h4>

                <div className="detail-item">
                  <span className="detail-label">Request Date:</span>
                  <span className="detail-value">
                    {formatDate(rent.rentDate)}
                  </span>
                </div>

                <div className="detail-item">
                  <span className="detail-label">End Date:</span>
                  <span className="detail-value">
                    {formatDate(rent.endDate)}
                  </span>
                </div>

                <div className="detail-item">
                  <span className="detail-label">Duration:</span>
                  <span className="detail-value">
                    {Math.ceil(
                      (new Date(rent.endDate) - new Date(rent.rentDate)) /
                        (1000 * 60 * 60 * 24)
                    )}{" "}
                    days
                  </span>
                </div>

                <div className="detail-item">
                  <span className="detail-label">Price:</span>
                  <span className="detail-value">
                    {rent.fullPrice?.toFixed(2)} KM
                  </span>
                </div>

                <h4 className="details-title">Client Information</h4>

                <div className="detail-item">
                  <span className="detail-label">Name:</span>
                  <span className="detail-value">
                    {rent.client?.user?.name} {rent.client?.user?.surname}
                  </span>
                </div>

                <div className="detail-item">
                  <span className="detail-label">Email:</span>
                  <span className="detail-value">
                    {rent.client?.user?.email || "-"}
                  </span>
                </div>

                <div className="detail-item">
                  <span className="detail-label">Phone:</span>
                  <span className="detail-value">
                    {rent.client?.user?.telephoneNumber || "-"}
                  </span>
                </div>

                <h4 className="details-title">Vehicle Information</h4>

                <div className="detail-item">
                  <span className="detail-label">Vehicle:</span>
                  <span className="detail-value">
                    {rent.vehicle?.name || "-"}
                  </span>
                </div>

               
              </div>

              {/* Right side - Vehicle Image */}
              <div className="vehicle-image-section">
                {rent.vehicle?.image ? (
                  <img
                    src={getImageSrc(rent.vehicle?.image)}
                    alt={`${rent.vehicle?.name}`}
                    className="vehicle-image"
                  />
                ) : (
                  <div className="no-image-placeholder">
                    <img
                      src={noImagePlaceholder}
                      alt="No Image"
                      className="no-image"
                    />
                  </div>
                )}
              </div>
            </div>

            {/* Action Buttons */}
            <div className="rent-activate-actions">
              <button
                type="button"
                className="back-button"
                onClick={handleGoBack}
              >
                <FaArrowLeft /> Go Back
              </button>

              <div className="action-buttons">
                <button
                  type="button"
                  className="reject-button"
                  onClick={() => setShowRejectDialog(true)}
                  disabled={!rent || loading}
                >
                  <FaTimes /> Reject Request
                </button>

                <button
                  type="button"
                  className="accept-button"
                  onClick={() => setShowAcceptDialog(true)}
                  disabled={!rent || loading || !vehicleAvailability}
                >
                  <FaCheck /> Accept Request
                </button>
              </div>
            </div>
            </div>
          </>
        )}

        {/* Confirm Accept Dialog */}
        {showAcceptDialog && (
          <ConfirmDialog
            title="Confirm Activation"
            message="Are you sure you want to accept this rent request?"
            onConfirm={handleAcceptRent}
            onCancel={() => setShowAcceptDialog(false)}
          />
        )}

        {/* Confirm Reject Dialog */}
        {showRejectDialog && (
          <ConfirmDialog
            title="Confirm Rejection"
            message="Are you sure you want to reject this rent request?"
            onConfirm={handleRejectRent}
            onCancel={() => setShowRejectDialog(false)}
          />
        )}
       
      </div>
      
    </MasterPage>
  );
};

export default RentActivePage;
