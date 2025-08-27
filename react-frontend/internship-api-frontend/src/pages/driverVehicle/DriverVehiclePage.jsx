import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { driverService, driverVehicleService, vehicleService } from "../../api";
import {
  FaSearch,
  FaTrash,
  FaEdit,
  FaChevronLeft,
  FaChevronRight,
  FaArrowLeft,
  FaSave,
  FaSyncAlt
} from "react-icons/fa";
import ConfirmDialog from "../../utils/ConfirmDialog";
import { toast } from "react-hot-toast";
import "./DriverVehiclePage.css";

const DriverVehicle = () => {
  const [vehicleIdFilter, setVehicleIdFilter] = useState("");
  const [datePickFilter, setDatePickFilter] = useState("");
  const [driverVehicles, setDriverVehicles] = useState([]);
  const [vehicles, setVehicles] = useState([]);
  const [drivers,setDrivers]=useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAddModal, setShowAddModal] = useState(false);
   const [selectedDriver, setSelectedDriver] = useState("");
    const [selectedVehicle, setSelectedVehicle] = useState("");
     const [driverError, setDriverError] = useState("");
   const [vehicleError,setVehicleError]=useState("");
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
    const fetchDrivers = async () => {
    try {
      const response = await driverService.getAll();
      setDrivers(response.data.items || []);
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
    fetchDrivers();
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
  const validateDriver = () => {
    if (!selectedDriver) {
      setDriverError("Please select a driver");
      return false;
    }
    setDriverError("");
    return true;
  };
    const validateVehicle = () => {
    if (!selectedVehicle) {
      setVehicleError("Please select a vehicle");
      return false;
    }
    setVehicleError("");
    return true;
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
        setShowAddModal(true);
    setSelectedDriver("");
    setSelectedVehicle("");
    setDriverError("");
    setVehicleError("");

  }
  const handleSaveDriverVehicle= async()=>{
      if (validateDriver()&&validateVehicle()) {
      try {
        await driverVehicleService.create({
          driver_id: parseInt(selectedDriver),
          vehicle_id:parseInt(selectedVehicle)
        });
        toast.success("New driverVehicle added successfully");
        setShowAddModal(false);
        fetchDriverVehicles();
      } catch (err) {
        console.error("Error adding driverVehicle:", err);
        toast.error("Failed to add driverVehicle. Please try again.");
      }
    }
  }
const handleRefresh = () => {
  setVehicleIdFilter("");
  setDatePickFilter("");
  setCurrentPage(0); 
  fetchDrivers();
  fetchVehicles();
  fetchDriverVehicles({});
};

  const handleCloseAddModal=()=>{
    setShowAddModal(false);
  }

  const paginatedList = driverVehicles.slice(
    currentPage * pageSize,
    currentPage * pageSize + pageSize
  );



  return (
    <MasterPage currentRoute="Driver Vehicle">
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
                <option value="">All Vehicles</option>
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
<FaSyncAlt className="refresh-button" onClick={handleRefresh} />

            <button className="filter-buttondv" onClick={handleFilter}>
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
                {paginatedList.length>0?(paginatedList.map((item) => (
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
                ))):( <tr>
      <td colSpan="7" className="no-results-message">
        There are no matching elements.
        
      </td>
    </tr>)}
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
         {showAddModal && (
                  <div className="modal-overlay">
                    <div className="modal-content">
                      <div className="modal-header">
                        <h4>Add New DriverVehicle</h4>
                        <button className="modal-close" onClick={handleCloseAddModal}>
                          &times;
                        </button>
                      </div>
                      <div className="modal-body">
                        <form
                          onSubmit={(e) => {
                            e.preventDefault();
                            handleSaveDriverVehicle();
                          }}
                        >
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
               <div className="modal-body">
                <div className="form-field">
                  <label htmlFor="vehicleSelect">Select Vehicle:</label>
                  <select
                    id="vehicleSelect"
                    value={selectedVehicle}
                    onChange={(e) => {
                      setSelectedVehicle(e.target.value);
                      if (e.target.value) setVehicleError("");
                    }}
                  >
                    <option value="">-- Select a vehicle --</option>
                    {vehicles.map((v) => (
                      <option key={v.id} value={v.id}>
                        {v?.name}
                      </option>
                    ))}
                  </select>
                  {vehicleError && (
                    <div className="error-message">{vehicleError}</div>
                  )}
                </div>
              </div>
        
                          <div className="modal-footer">
                            <button
                              type="button"
                              className="btn-cancel"
                              onClick={handleCloseAddModal}
                            >
                              <FaArrowLeft className="go-back-icon" />
                              Close
                            </button>
                            <button type="submit" className="btn-save">
                              <FaSave className="save-icon" />
                              Save
                            </button>
                          </div>
                        </form>
                      </div>
                    </div>
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
