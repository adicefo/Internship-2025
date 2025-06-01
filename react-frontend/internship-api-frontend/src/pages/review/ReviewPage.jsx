import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { reviewService } from "../../api";
import { FaSearch, FaEdit, FaTrash, FaStar, FaRegStar } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { toast } from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";
import "./ReviewPage.css";

// Star Rating Component
const StarRating = ({ rating }) => {
  const totalStars = 5;
  const activeStars = Math.round(rating) || 0;

  return (
    <div className="star-rating">
      {[...Array(totalStars)].map((_, index) => {
        return index < activeStars ? (
          <FaStar key={index} style={{ color: "#FFD700" }} />
        ) : (
          <FaRegStar key={index} style={{ color: "#FFD700" }} />
        );
      })}
      <span className="rating-number">{rating}</span>
    </div>
  );
};

const ReviewPage = () => {
  const navigate = useNavigate();
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [driverNameFilter, setDriverNameFilter] = useState("");
  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);

  const fetchReviews = async (filter) => {
    try {
      setLoading(true);
      const response = await reviewService.getAll(filter);
      console.log("API response:", response);
      setReviews(response.data.items || []);
      setError(null);
    } catch (err) {
      console.error("Error fetching reviews:", err);
      setError("Failed to load reviews. Please try again.");
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    fetchReviews();
  }, []);
  const handleEditReview = (review) => {
    navigate("/review/edit", { state: { review: review } });
  };
  const confirmDelete = async () => {
    try {
      await reviewService.delete(deleteId);
      toast.success("Review deleted successfully");
    } catch (err) {
      console.error("Error deleting review:", err);
      toast.error("Failed to delete review. Please try again.");
    }
  };
  const handleDeleteReview = (id) => {
    setDeleteId(id);
    setShowDialog(true);
  };

  const handleAddReview = () => {
    navigate("/review/add");
  };
  const handleFilter = () => {
    var filter = {
      reviewedName: driverNameFilter,
    };
    fetchReviews(filter);
  };
  return (
    <MasterPage currentRoute="Review">
      <div className="review-section">
        <div className="section-header">
          <h3>Review Management</h3>
          <button className="add-button" onClick={handleAddReview}>
            Add New Review
          </button>
        </div>

        {/*Filter controls*/}
        <div className="filter-container">
          <div className="filter-row">
            <div className="filter-group">
              <label htmlFor="driverNameFilter">Driver name:</label>
              <input
                type="text"
                id="driverNameFilter"
                value={driverNameFilter}
                onChange={(e) => setDriverNameFilter(e.target.value)}
                placeholder="Filter by driver name"
              />
            </div>

            <button className="filter-button" onClick={handleFilter}>
              <FaSearch className="filter-icon" /> Filter
            </button>
          </div>
        </div>
        {loading ? (
          <div className="loading-indicator">Loading review data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="review-table-container">
            <table className="review-table table table-hover">
              <thead>
                <tr>
                  <th style={{ width: "80px" }}>Driver</th>
                  <th style={{ width: "80px" }}>Client</th>
                  <th style={{ width: "100px" }}>Rating</th>
                  <th style={{ width: "150px" }}>Comment</th>
                  <th style={{ width: "100px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {reviews.map((review) => (
                  <tr key={review.id}>
                    <td>
                      {review.driver?.user?.name} {review.driver?.user?.surname}
                    </td>
                    <td>
                      {review.client?.user?.name} {review.client?.user?.surname}
                    </td>
                    <td>
                      <StarRating rating={review.value} />
                    </td>
                    <td>{review.description}</td>

                    <td className="action-buttons">
                      {/* Edit button */}
                      <button
                        className="edit-button"
                        onClick={() => handleEditReview(review)}
                        title="Edit review"
                      >
                        <FaEdit style={{ color: "blue" }} />
                      </button>

                      {/* Delete button */}
                      <button
                        className="delete-button"
                        onClick={() => handleDeleteReview(review.id)}
                        title="Delete review"
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
export default ReviewPage;
