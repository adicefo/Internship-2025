import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { adminService } from "../../api"; // make sure this exists
import {
  FaSearch,
  FaEdit,
  FaTrash,
  FaChevronLeft,
  FaChevronRight,
} from "react-icons/fa";
import { toast } from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";


const AdminPage = () => {
  const [adminNameFilter, setAdminNameFilter] = useState("");
  const [admins, setAdmins] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [showDialog, setShowDialog] = useState(false);
  const [deleteId, setDeleteId] = useState(0);

  // Pagination
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 3;
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);

  const fetchAdmins = async (filter = {}) => {
    try {
      setLoading(true);
      const response = await adminService.getAll(filter);
      const items = response.data.items || [];
      setAdmins(items);
      setTotalItems(items.length);
      const calculatedTotalPages = Math.ceil(items.length / pageSize);
      setTotalPages(calculatedTotalPages);
      if (currentPage >= calculatedTotalPages && calculatedTotalPages > 0) {
        setCurrentPage(calculatedTotalPages - 1);
      }
      setError(null);
    } catch (err) {
      console.error("Error fetching admins:", err);
      setError("Failed to load admins. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAdmins({ name: adminNameFilter });
    // eslint-disable-next-line
  }, [currentPage]);

  const handleDeleteAdmin = (id) => {
    setShowDialog(true);
    setDeleteId(id);
  };

  const confirmDelete = async () => {
    // Disabled for normal admins - only superadmin can delete
    toast.error(
      "Due to application restrictions ONLY SUPER ADMIN/DEVELOPER have access to this operation. Thank you."
    );
    setShowDialog(false);
    // If you want to enable real delete for superadmin, implement here
  };

  const handleEditAdmin = () => {
    toast.error(
      "Editing is restricted. ONLY SUPER ADMIN/DEVELOPER have access to this operation. Thank you."
    );
  };

  const handleFilter = () => {
    setCurrentPage(0);
    fetchAdmins({ name: adminNameFilter });
  };

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
  };

  // Paginated admins for current page
  const paginatedAdmins = admins.slice(
    currentPage * pageSize,
    currentPage * pageSize + pageSize
  );

  return (
    <MasterPage currentRoute="Admin Management">
      <div className="admin-section">
        <div className="section-header">
          <h3>Admin Management</h3>
        </div>

        {/* Filter controls */}
        <div className="filter-container">
          <div className="filter-row">
            <div className="filter-group">
              <label htmlFor="adminNameFilter">Admin name:</label>
              <input
                type="text"
                id="adminNameFilter"
                value={adminNameFilter}
                onChange={(e) => setAdminNameFilter(e.target.value)}
                placeholder="Filter by admin name"
              />
            </div>

            <button className="filter-button" onClick={handleFilter}>
              <FaSearch className="filter-icon" /> Filter
            </button>
          </div>
        </div>

        {loading ? (
          <div className="loading-indicator">Loading admin data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="admin-table-container">
            <table className="admin-table table table-hover">
              <thead>
                <tr>
                  <th style={{ width: "100px" }}>Name</th>
                  <th style={{ width: "100px" }}>Surname</th>
                  <th style={{ width: "120px" }}>Username</th>
                  <th style={{ width: "180px" }}>Email</th>
                  <th style={{ width: "120px" }}>Phone</th>
                  <th style={{ width: "120px" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {paginatedAdmins.map((admin) => (
                  <tr key={admin.id}>
                    <td>{admin.user?.name}</td>
                    <td>{admin.user?.surname}</td>
                    <td>{admin.user?.userName}</td>
                    <td>{admin.user?.email}</td>
                    <td>{admin.user?.telephoneNumber}</td>
                    <td className="action-buttons">
                      <button
                        className="edit-button-disabled"
                        onClick={handleEditAdmin}
                        title="Editing restricted to superadmin"
                      >
                        <FaEdit style={{ color: "grey" }} />
                      </button>
                      <button
                        className="delete-button-disabled"
                        onClick={() =>
                          toast.error(
                            "Only superadmin can delete administrator."
                          )
                        }
                        title="Delete restricted to superadmin"
                      >
                        <FaTrash style={{ color: "grey" }} />
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
                  Page {currentPage + 1} of {totalPages} ({totalItems} total
                  items)
                </div>
                <div className="pagination-controls">
                  <button
                    className="pagination-button pagination-arrow"
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 0}
                    title="Previous page"
                  >
                    <FaChevronLeft size={16} />
                  </button>

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

export default AdminPage;
