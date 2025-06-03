import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  FaTrash,
  FaEye,
  FaSearch,
  FaEdit,
  FaChevronLeft,
  FaChevronRight,
} from "react-icons/fa";
import { clientService } from "../../api";
import toast from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";

const ClientPage = () => {
  const [clients, setClients] = useState([]);
  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);
  const [nameFilter, setNameFilter] = useState("");
  const [surnameFilter, setSurnameFilter] = useState("");
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Pagination state
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const pageSize = 3; // 3 items per page as requested

  const fetchClients = async (filter = {}) => {
    try {
      setLoading(true);
      const response = await clientService.getAll(filter);
      const items = response.data.items || [];
      setClients(items);
      setTotalItems(items.length);
      const calculatedTotalPages = Math.ceil(items.length / pageSize);
      setTotalPages(calculatedTotalPages);
      if (currentPage >= calculatedTotalPages && calculatedTotalPages > 0) {
        setCurrentPage(calculatedTotalPages - 1);
      }
      setError(null);
    } catch (err) {
      console.error("Error fetching rents:", err);
      setError("Failed to load rents. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    var filter={
      name:nameFilter,
      surname:surnameFilter
    };
    fetchClients(filter);
  }, [currentPage]); // Re-fetch when page changes

  const handleEditClient = (client) => {
    navigate("/client/edit", { state: { client } });
  };

  const handleAddClient = () => {
    navigate("/client/add");
  };
  const confirmDelete = async () => {
    setShowDialog(false);
    try {
      await clientService.delete(deleteId);
      toast.success("Client deleted successfully");
      fetchClients();
    } catch {
      toast.error("Error deleting client");
    }
  };
  const handleDeleteClient = (id) => {
    setShowDialog(true);
    setDeleteId(id);
  };

  const handleFilter = () => {
    // Reset to first page when filtering
    setCurrentPage(0);
    var filter = {
      name: nameFilter,
      surname: surnameFilter,
    };
    fetchClients(filter);
  };

  const handlePageChange = (newPage) => {
    // Simple validation to prevent going to invalid pages
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
  };
    // Paginated reviews for current page
    const paginatedClients = clients.slice(
      currentPage * pageSize,
      currentPage * pageSize + pageSize
    );

  return (
    <MasterPage currentRoute="Clients">
      <div className="clients-section">
        <div className="section-header">
          <h3>Clients Management</h3>
          <button className="add-button" onClick={handleAddClient}>
            Add New Client
          </button>
        </div>

        <div className="filter-container">
          <div className="filter-row">
            <div className="filter-group">
              <label htmlFor="nameFilter">Name:</label>
              <input
                type="text"
                id="nameFilter"
                value={nameFilter}
                onChange={(e) => setNameFilter(e.target.value)}
                placeholder="Filter by name"
              />
            </div>

            <div className="filter-group">
              <label htmlFor="surnameFilter">Surname:</label>
              <input
                type="text"
                id="surnameFilter"
                value={surnameFilter}
                onChange={(e) => setSurnameFilter(e.target.value)}
                placeholder="Filter by surname"
              />
            </div>

            <button className="filter-button" onClick={handleFilter}>
              <FaSearch className="filter-icon" /> Filter
            </button>
          </div>
        </div>

        {loading ? (
          <div className="loading-indicator">Loading clients data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="clients-table-container">
            <table className="clients-table table table-hover">
              <thead>
                <tr>
                  <th style={{ width: "80px" }}>Name</th>
                  <th style={{ width: "80px" }}>Surname</th>
                  <th style={{ width: "100px" }}>Username</th>
                  <th style={{ width: "150px" }}>Email</th>
                  <th style={{ width: "120px" }}>Phone</th>
                  <th style={{ width: "100px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {paginatedClients.map((client) => (
                  <tr key={client.id}>
                    <td>{client.user.name}</td>
                    <td>{client.user.surname}</td>
                    <td>{client.user.username}</td>
                    <td>{client.user.email}</td>
                    <td>{client.user.telephoneNumber}</td>
                    <td className="action-buttons">
                      <button
                        className="edit-button"
                        onClick={() => handleEditClient(client)}
                        title="Edit driver"
                      >
                        <FaEdit />
                      </button>
                      <button
                        className="delete-button"
                        onClick={() => handleDeleteClient(client.id)}
                        title="Delete driver"
                      >
                        <FaTrash />
                      </button>
                      {showDialog && (
                        <ConfirmDialog
                          title="Delete Confirmation"
                          message="Are you sure you want to delete this item?"
                          onConfirm={() => confirmDelete()}
                          onCancel={() => setShowDialog(false)}
                        />
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {/* Pagination controls */}
            {clients.length > 0 && (
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
      </div>
    </MasterPage>
  );
};

export default ClientPage;
