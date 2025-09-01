import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { companyPriceService } from "../../api";
import {
  FaSearch,
  FaEdit,
  FaTrash,
  FaCheckCircle,
  FaSave,
  FaArrowLeft,
} from "react-icons/fa";
import { toast } from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";
import "./CompanyPricePage.css";

const CompanyPricePage = () => {
  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);
  const [priceFilter, setPriceFilter] = useState(0);
  const [prices, setPrices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPrice, setCurrentPrice] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [showAddModal, setShowAddModal] = useState(false);
  const [newPrice, setNewPrice] = useState("");
  const [priceError, setPriceError] = useState("");

  const fetchPrices = async (filter) => {
    try {
      setLoading(true);
      const response = await companyPriceService.getAll(filter);
      setPrices(response.data.items);
      setError(null);
    } catch (err) {
      console.error("Error fetching prices:", err);
      setError("Failed to load prices. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const fetchCurrentPrice = async () => {
    try {
      const response = await companyPriceService.getCurrentPrice();
      setCurrentPrice(response.data);
    } catch (err) {
      console.error("Error fetching current price:", err);
    }
  };

  useEffect(() => {
    fetchPrices();
    fetchCurrentPrice();
  }, []);

  const handleEditPrice = () => {
    toast.error(
      "Edit is not available for price entity. You can either delete or add new price."
    );
  };

  const confirmDelete = async () => {
    try {
      await companyPriceService.delete(deleteId);
      toast.success("Price deleted successfully");
      fetchPrices();
      fetchCurrentPrice();
      setShowDialog(false);
    } catch (err) {
      console.error("Error deleting price:", err);
    }
  };

  const handleDeletePrice = (id) => {
    setShowDialog(true);
    setDeleteId(id);
  };

  const handleAddPrice = () => {
    setShowModal(true);
    setNewPrice("");
    setPriceError("");
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const validatePrice = (price) => {
    const numPrice = parseFloat(price);
    if (isNaN(numPrice)) {
      setPriceError("Please enter a valid number");
      return false;
    }
    if (numPrice < 1) {
      setPriceError("Price must be at least 1");
      return false;
    }
    if (numPrice > 10) {
      setPriceError("Price must not exceed 10");
      return false;
    }
    setPriceError("");
    return true;
  };
  const confirmAddPrice = async () => {
    if (validatePrice(newPrice)) {
      try {
        await companyPriceService.create({
          pricePerKilometer: parseFloat(newPrice),
        });
        toast.success("New price added successfully");
        setShowModal(false);
        fetchPrices();
        fetchCurrentPrice();
      } catch (err) {
        console.error("Error adding new price:", err);
        toast.error("Failed to add new price. Please try again.");
      }
    }
  };
  const handleSavePrice = async () => {
    setShowAddModal(true);
  };

  const handleFilter = () => {
    var filter = {
      pricePerKilometer: priceFilter,
    };
    fetchPrices(filter);
  };

  return (
    <MasterPage currentRoute="Company Price">
      <div className="company-price-container">
        <div className="section-header">
          <h3 style={{color:"var(--text-color) !important"}}>Company Price Management</h3>
          <button className="add-button" onClick={handleAddPrice}>
            Add New Price
          </button>
        </div>

        {/* Current Price Display */}
        {currentPrice && (
          <div className="current-price-container">
            <FaCheckCircle className="current-price-icon" />
            <span className="current-price-text">
              Current Price: {currentPrice.pricePerKilometer?.toFixed(2)} KM
            </span>
          </div>
        )}

        {/*Filter controls*/}
        <div className="filter-container">
          <div className="filter-row">
            <div className="filter-group">
              <label htmlFor="priceFilter">Price:</label>
              <input
                type="number"
                id="priceFilter"
                value={priceFilter}
                onChange={(e) => setPriceFilter(e.target.value)}
                placeholder="Filter by price"
              />
            </div>

            <button className="filter-buttonco" onClick={handleFilter}>
              <FaSearch className="filter-icon" /> Filter
            </button>
          </div>
        </div>
        {loading ? (
          <div className="loading-indicator">Loading prices data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="price-table-container">
            <table className="price-table table table-hover">
              <thead>
                <tr>
                  <th style={{ width: "80px" }}>Price per kilometer</th>
                  <th style={{ width: "80px" }}>Date added</th>
                  <th style={{ width: "100px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {prices.length>0?(prices.map((obj) => (
                  <tr key={obj.id}>
                    <td>{obj.pricePerKilometer.toFixed(2) + " KM"}</td>
                    <td>{obj.addingDate?.toString().substring(0, 10)}</td>
                    <td className="action-buttons">
                      {/* Edit button */}
                      <button
                        className="edit-button-disabled"
                        onClick={handleEditPrice}
                        title="Edit not available"
                      >
                        <FaEdit style={{ color: "grey" }} />
                      </button>

                      {/* Delete button */}
                      <button
                        className="delete-button"
                        onClick={() => handleDeletePrice(obj.id)}
                        title="Delete price"
                      >
                        <FaTrash style={{ color: "red" }} />
                      </button>
                    </td>
                  </tr>
                ))):( <tr>
      <td colSpan="7" className="no-results-message">
        There are no available prices.
      </td>
    </tr>)}
              </tbody>
            </table>
          </div>
        )}

        {/* Delete Confirmation Dialog */}
        {showDialog && (
          <ConfirmDialog
            title="Delete Confirmation"
            message="Are you sure you want to delete this item?"
            onConfirm={confirmDelete}
            onCancel={() => setShowDialog(false)}
          />
        )}

        {/* Add Price Modal */}
        {showModal && (
          <div className="modal-overlay">
            <div className="modal-content">
              <div className="modal-header">
                <h4>Add New Price</h4>
                <button className="modal-close" onClick={handleCloseModal}>
                  &times;
                </button>
              </div>
              <div className="modal-body">
                <div className="form-field">
                  <label htmlFor="newPrice">Price per kilometer (KM):</label>
                  <input
                    type="number"
                    id="newPrice"
                    value={newPrice}
                    onChange={(e) => {
                      setNewPrice(e.target.value);
                      validatePrice(e.target.value);
                    }}
                    min="1"
                    max="10"
                    step="0.01"
                    placeholder="Enter price per kilometer"
                    style={{color:"black"}}
                  />
                  {priceError && (
                    <div className="error-message">{priceError}</div>
                  )}
                </div>
              </div>
              <div className="modal-footer">
                <button className="btn-cancel" onClick={handleCloseModal}>
                  <FaArrowLeft className="go-back-icon" />
                  Go Back
                </button>
                <button className="btn-save" onClick={handleSavePrice}>
                  <FaSave className="save-icon" />
                  Save
                </button>
                {showAddModal && (
                  <ConfirmDialog
                    title="Add Price Confirmation"
                    message="Are you sure you want to add new price?"
                    onConfirm={confirmAddPrice}
                    onCancel={() => setShowDialog(false)}
                  />
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    </MasterPage>
  );
};
export default CompanyPricePage;
