import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { statisticsService, driverService } from "../../api";
import { FaSearch, FaEdit, FaTrash,FaSave,FaArrowLeft } from "react-icons/fa";
import { toast } from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";
import "./StatisticsPage.css";

const StatisticsPage = () => {
  const [driverNameFilter, setDriverNameFilter] = useState("");
  const [statistics, setStatistics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [drivers, setDrivers] = useState([]);
  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [selectedDriver, setSelectedDriver] = useState("");
  const [driverError, setDriverError] = useState("");

  const fetchStatistics = async (filter) => {
    try {
      setLoading(true);
      const response = await statisticsService.getAll(filter);
      setStatistics(response.data.items);
      setError(null);
    } catch (err) {
      console.error("Error fetching statistics:", err);
      setError("Failed to load statistics. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const fetchDrivers = async () => {
    try {
      const response = await driverService.getAll();
      setDrivers(response.data.items);
    } catch (err) {
      console.error("Error fetching drivers:", err);
      setError("Failed to load drivers. Please try again.");
    }
  };

  useEffect(() => {
    fetchStatistics();
    fetchDrivers();
  }, []);

  const handleEditStatistics = () => {
    toast.error(
      "Edit is not available for statistics entity. You can either delete or add new statistics."
    );
  };

  const confirmDelete = async () => {
    try {
      await statisticsService.delete(deleteId);
      toast.success("Statistics deleted successfully");
      fetchStatistics();
      setShowDialog(false);
    } catch (err) {
      console.error("Error deleting statistics:", err);
      toast.error("Failed to delete statistics. Please try again.");
    }
  };

  const handleDeleteStatistics = (id) => {
    setShowDialog(true);
    setDeleteId(id);
  };

  const handleAddStatistics = () => {
    setShowModal(true);
    setSelectedDriver("");
    setDriverError("");
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const validateDriver = () => {
    if (!selectedDriver) {
      setDriverError("Please select a driver");
      return false;
    }
    setDriverError("");
    return true;
  };

  const handleSaveStatistics = async () => {
    if (validateDriver()) {
      try {
        await statisticsService.create({
          driver_id: parseInt(selectedDriver),
        });
        toast.success("New statistics added successfully");
        setShowModal(false);
        fetchStatistics();
      } catch (err) {
        console.error("Error adding statistics:", err);
        toast.error("Failed to add statistics. Please try again.");
      }
    }
  };

  const handleFilter = () => {
    var filter = {
      driverName: driverNameFilter,
    };
    fetchStatistics(filter);
  };

  return (
    <MasterPage currentRoute="Statistics">
      <div className="review-section">
        <div className="section-header">
          <h3>Statistics Management</h3>
          <button className="add-button" onClick={handleAddStatistics}>
            Add New Statistics
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
          <div className="loading-indicator">Loading statistics data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="statistics-table-container">
            <table className="statistics-table table table-hover">
              <thead>
                <tr>
                  <th style={{ width: "80px" }}>Driver name</th>
                  <th style={{ width: "80px" }}>Driver surname</th>
                  <th style={{ width: "100px" }}>Work begin</th>
                  <th style={{ width: "150px" }}>Work end</th>
                  <th style={{ width: "150px" }}>Hours</th>
                  <th style={{ width: "150px" }}>Clients</th>
                  <th style={{ width: "100px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {statistics.map((obj) => (
                  <tr key={obj.id}>
                    <td>{obj.driver?.user?.name}</td>
                    <td>{obj.driver?.user?.surname}</td>
                    <td>{obj.beginningOfWork?.toString().substring(0, 16)}</td>
                    <td>{obj.endOfWork?.toString().substring(0, 16)}</td>
                    <td>{obj.numberOfHours}</td>
                    <td>{obj.numberOfClients}</td>
                    <td className="action-buttons">
                      {/* Edit button */}
                      <button
                        className="edit-button-disabled"
                        onClick={handleEditStatistics}
                        title="Edit not available"
                      >
                        <FaEdit style={{ color: "grey" }} />
                      </button>

                      {/* Delete button */}
                      <button
                        className="delete-button"
                        onClick={() => handleDeleteStatistics(obj.id)}
                        title="Delete statistics"
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

        {/* Delete Confirmation Dialog */}
        {showDialog && (
          <ConfirmDialog
            title="Delete Confirmation"
            message="Are you sure you want to delete this item?"
            onConfirm={confirmDelete}
            onCancel={() => setShowDialog(false)}
          />
        )}

        {/* Add Statistics Modal */}
        {showModal && (
          <div className="modal-overlay">
            <div className="modal-content">
              <div className="modal-header">
                <h4>Add New Statistics</h4>
                <button className="modal-close" onClick={handleCloseModal}>
                  &times;
                </button>
              </div>
              <div className="modal-body">
                <div className="form-field">
                  <label htmlFor="driverSelect">Select Driver:</label>
                  <select
                    id="driverSelect"
                    value={selectedDriver}
                    onChange={(e) => {
                      setSelectedDriver(e.target.value);
                      if (e.target.value) setDriverError("");
                    }}
                  >
                    <option value="">-- Select a driver --</option>
                    {drivers.map((driver) => (
                      <option key={driver.id} value={driver.id}>
                        {driver.user?.name} {driver.user?.surname}
                      </option>
                    ))}
                  </select>
                  {driverError && (
                    <div className="error-message">{driverError}</div>
                  )}
                </div>
              </div>
              <div className="modal-footer">
                <button className="btn-cancel" onClick={handleCloseModal}>
                  <FaArrowLeft className="go-back-icon" />
                  Go Back
                </button>
                <button className="btn-save" onClick={handleSaveStatistics}>
                  <FaSave className="save-icon" />
                  Save
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </MasterPage>
  );
};
export default StatisticsPage;
