import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { driverVehicleService, vehicleService } from "../../api";
import {
  FaSearch,
  FaTrash,
  FaEdit,
  FaChevronLeft,
  FaChevronRight,
} from "react-icons/fa";
import ConfirmDialog from "../../utils/ConfirmDialog";
import { toast } from "react-hot-toast";

const DriverVehicle = () => {
  const [vehicleIdFilter, setVehicleIdFilter] = useState("");
  const [datePickFilter, setDatePickFilter] = useState("");
  const [driverVehicles, setDriverVehicles] = useState([]);
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Pagination
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 3;
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);

  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);

  const fetchVehicles = async () => {
    try {
      const response = await vehicleService.getAll();
      setVehicles(response.data.items || []);
    } catch (err) {
      console.error("Error loading vehicles", err);
    }
  };

  const fetchDriverVehicles = async (filter = {}) => {
    try {
      setLoading(true);
      const response = await driverVehicleService.getAll(filter);
      const items = response.data.items || [];
      setDriverVehicles(items);
      setTotalItems(items.length);
      const calculatedTotalPages = Math.ceil(items.length / pageSize);
      setTotalPages(calculatedTotalPages);
      if (currentPage >= calculatedTotalPages && calculatedTotalPages > 0) {
        setCurrentPage(calculatedTotalPages - 1);
      }
      setError(null);
    } catch (err) {
      console.error("Error fetching driver-vehicle data:", err);
      setError("Failed to load data.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchVehicles();
 fetchDriverVehicles({
  vehicleId: vehicleIdFilter || undefined,
  datePick: datePickFilter ? `${datePickFilter}T00:00:00` : undefined,
});
    
  }, [currentPage]);

  const handleFilter = () => {
    setCurrentPage(0);
    fetchDriverVehicles({
  vehicleId: vehicleIdFilter || undefined,
  datePick: datePickFilter ? `${datePickFilter}T00:00:00` : undefined,
});
  };

  const handleDelete = (id) => {
    setShowDialog(true);
    setDeleteId(id);
  };

  const confirmDelete = async () => {
    try {
      await driverVehicleService.delete(deleteId);
      toast.success("Record deleted");
      fetchDriverVehicles();
      setShowDialog(false);
    } catch (err) {
      toast.error("Delete failed");
      console.error("Delete error:", err);
    }
  };

  const handleOpenAddModal=()=>{
    
  }

  const paginatedList = driverVehicles.slice(
    currentPage * pageSize,
    currentPage * pageSize + pageSize
  );



  return (
    <MasterPage currentRoute="DriverVehicle">
      <div className="driver-vehicle-section">
        <div className="section-header">
          <h3>Driver-Vehicle Assignments</h3>
          <button className="add-button" onClick={handleOpenAddModal}>
            Add New DriverVehicle
          </button>
        </div>

        {/* Filter */}
        <div className="filter-container">
          <div className="filter-row">
            <div className="filter-group">
              <label htmlFor="vehicleFilter">Vehicle:</label>
              <select
                id="vehicleFilter"
                value={vehicleIdFilter}
                onChange={(e) => setVehicleIdFilter(e.target.value)}
              >
                <option value="">-- All Vehicles --</option>
                {vehicles.map((v) => (
                  <option key={v.id} value={v.id}>
                    {v.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="filter-group">
              <label htmlFor="datePickFilter">Date:</label>
              <input
                type="date"
                id="datePickFilter"
                value={datePickFilter}
                onChange={(e) => setDatePickFilter(e.target.value)}
              />
            </div>

            <button className="filter-button" onClick={handleFilter}>
              <FaSearch className="filter-icon" /> Filter
            </button>
          </div>
        </div>

        {/* Data Table */}
        {loading ? (
          <div className="loading-indicator">Loading data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="driver-vehicle-table-container">
            <table className="table table-hover">
              <thead>
                <tr>
                  <th style={{ width: "120px" }}>Driver</th>
                  <th style={{ width: "120px" }}>Vehicle</th>
                  <th style={{ width: "120px" }}>Date Pick</th>
                  <th style={{ width: "100px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {paginatedList.map((item) => (
                  <tr key={item.id}>
                    <td>
                      {item.driver?.user?.name} {item.driver?.user?.surname}
                    </td>
                    <td>{item.vehicle?.name}</td>
                    <td>{item.datePick?.substring(0, 10)}</td>
                    <td className="action-buttons">
                      <button
                        className="edit-button-disabled"
                        onClick={() =>
                          toast.error("Editing is not available.")
                        }
                        title="Edit disabled"
                      >
                        <FaEdit style={{ color: "grey" }} />
                      </button>
                      <button
                        className="delete-button"
                        onClick={() => handleDelete(item.id)}
                        title="Delete"
                      >
                        <FaTrash style={{ color: "red" }} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {/* Pagination */}
            {totalItems > 0 && (
              <div className="pagination-container">
                <div className="pagination-info">
                  Page {currentPage + 1} of {totalPages} ({totalItems} total)
                </div>
                <div className="pagination-controls">
                  <button
                    onClick={() => setCurrentPage(currentPage - 1)}
                    disabled={currentPage === 0}
                  >
                    <FaChevronLeft />
                  </button>
                  {Array.from({ length: totalPages }, (_, i) => (
                    <button
                      key={i}
                      className={`pagination-button ${
                        currentPage === i ? "active" : ""
                      }`}
                      onClick={() => setCurrentPage(i)}
                    >
                      {i + 1}
                    </button>
                  ))}
                  <button
                    onClick={() => setCurrentPage(currentPage + 1)}
                    disabled={currentPage >= totalPages - 1}
                  >
                    <FaChevronRight />
                  </button>
                </div>
              </div>
            )}
          </div>
        )}

        {/* Confirm Dialog */}
        {showDialog && (
          <ConfirmDialog
            title="Confirm Delete"
            message="Are you sure you want to delete this assignment?"
            onConfirm={confirmDelete}
            onCancel={() => setShowDialog(false)}
          />
        )}
      </div>
    </MasterPage>
  );
};

export default DriverVehicle;
