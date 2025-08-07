import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { adminService } from "../../api";
import {
  FaSearch,
  FaEdit,
  FaTrash,
  FaChevronLeft,
  FaChevronRight,
  FaSave,
  FaArrowLeft,
} from "react-icons/fa";
import { toast } from "react-hot-toast";
import "./AdminPage.css";
const AdminPage = () => {
  const [adminNameFilter, setAdminNameFilter] = useState("");
  const [admins, setAdmins] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [showAddModal, setShowAddModal] = useState(false);

  // Pagination
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 3;
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);

  // New Admin form state
const [formData, setFormData] = useState({
  name: "",
  surname: "",
  gender: "",           // added here
  username: "",
  email: "",
  password: "",
  passwordConfirm: "",
  telephoneNumber: "",
  isActive:true
});

  const [formErrors, setFormErrors] = useState({});

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

  const handleFilter = () => {
    setCurrentPage(0);
    fetchAdmins({ name: adminNameFilter });
  };

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
  };

  const handleEditAdmin = () => {
    toast.error(
      "Editing is restricted. ONLY SUPER ADMIN/DEVELOPER have access to this operation. Thank you."
    );
  };

  // Add Modal handlers
  const handleOpenAddModal = () => {
    setFormData({
      name: "",
      surname: "",
      username: "",
      email: "",
      password: "",
      passwordConfirm: "",
      telephoneNumber: "",
      gender:"",
       isActive:true
    });
    setFormErrors({});
    setShowAddModal(true);
  };

  const handleCloseAddModal = () => {
    setShowAddModal(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // Clear error on change
    setFormErrors((prev) => ({ ...prev, [name]: null }));
  };

  const validateForm = () => {
    const errors = {};
    if (!formData.name.trim()) errors.name = "Name is required";
    if (!formData.surname.trim()) errors.surname = "Surname is required";
    if (!formData.username.trim()) errors.username = "Username is required";
    if (!formData.gender) errors.gender = "Please select gender";
    if (!formData.email.trim()) {
      errors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      errors.email = "Invalid email format";
    }
    if (!formData.password) errors.password = "Password is required";
    if (!formData.passwordConfirm)
      errors.passwordConfirm = "Please confirm password";
    if (formData.password !== formData.passwordConfirm)
      errors.passwordConfirm = "Passwords do not match";
    if (!formData.telephoneNumber.trim())
      errors.telephoneNumber = "Phone number is required";

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSaveAdmin = async () => {
    if (!validateForm()) return;

    try {
     
      const postData = {
        name: formData.name,
        surname: formData.surname,
        username: formData.username,
        email: formData.email,
        password: formData.password,
        passwordConfirm:formData.passwordConfirm,
        telephoneNumber: formData.telephoneNumber,
        gender:formData.gender,
        isActive:formData.isActive
      };

      await adminService.create(postData);

      toast.success("Admin successfully added");
      setShowAddModal(false);
      fetchAdmins({ name: adminNameFilter });
    } catch (err) {
      console.error("Error adding admin:", err);
      toast.error("Failed to add admin. Please try again.");
    }
  };

  // Paginated admins for current page
  const paginatedAdmins = admins.slice(
    currentPage * pageSize,
    currentPage * pageSize + pageSize
  );

  return (
    <MasterPage currentRoute="Admin">
      <div className="admin-section">
        <div className="section-header">
          <h3>Admin Management</h3>
          <button className="add-button" onClick={handleOpenAddModal}>
            Add New Admin
          </button>
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

            <button className="filter-buttona" onClick={handleFilter}>
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
                        onClick={() =>
                          toast.error(
                            "Editing restricted to superadmin only."
                          )
                        }
                        title="Editing restricted to superadmin"
                      >
                        <FaEdit style={{ color: "grey" }} />
                      </button>
                      <button
                        className="delete-button-disabled"
                        onClick={() =>
                          toast.error(
                            "Deleting restricted to superadmin only."
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

        {/* Add New Admin Modal */}
        {showAddModal && (
          <div className="modal-overlay">
            <div className="modal-content">
              <div className="modal-header">
                <h4>Add New Admin</h4>
                <button className="modal-close" onClick={handleCloseAddModal}>
                  &times;
                </button>
              </div>
              <div className="modal-body">
                <form
                  onSubmit={(e) => {
                    e.preventDefault();
                    handleSaveAdmin();
                  }}
                >
                  <div className="form-field">
                    <label htmlFor="name">Name:</label>
                    <input
                      type="text"
                      id="name"
                      name="name"
                      value={formData.name}
                      onChange={handleInputChange}
                    />
                    {formErrors.name && (
                      <div className="error-message">{formErrors.name}</div>
                    )}
                  </div>

                  <div className="form-field">
                    <label htmlFor="surname">Surname:</label>
                    <input
                      type="text"
                      id="surname"
                      name="surname"
                      value={formData.surname}
                      onChange={handleInputChange}
                    />
                    {formErrors.surname && (
                      <div className="error-message">{formErrors.surname}</div>
                    )}
                  </div>

                  <div className="form-field">
                    <label htmlFor="username">Username:</label>
                    <input
                      type="text"
                      id="username"
                      name="username"
                      value={formData.username}
                      onChange={handleInputChange}
                    />
                    {formErrors.username && (
                      <div className="error-message">{formErrors.username}</div>
                    )}
                  </div>

                  <div className="form-field">
                    <label htmlFor="email">Email:</label>
                    <input
                      type="email"
                      id="email"
                      name="email"
                      value={formData.email}
                      onChange={handleInputChange}
                    />
                    {formErrors.email && (
                      <div className="error-message">{formErrors.email}</div>
                    )}
                  </div>

                  <div className="form-field">
                    <label htmlFor="password">Password:</label>
                    <input
                      type="password"
                      id="password"
                      name="password"
                      value={formData.password}
                      onChange={handleInputChange}
                    />
                    {formErrors.password && (
                      <div className="error-message">{formErrors.password}</div>
                    )}
                  </div>

                  <div className="form-field">
                    <label htmlFor="passwordConfirm">Confirm Password:</label>
                    <input
                      type="password"
                      id="passwordConfirm"
                      name="passwordConfirm"
                      value={formData.passwordConfirm}
                      onChange={handleInputChange}
                    />
                    {formErrors.passwordConfirm && (
                      <div className="error-message">
                        {formErrors.passwordConfirm}
                      </div>
                    )}
                  </div>

                  <div className="form-field">
                    <label htmlFor="telephoneNumber">Phone:</label>
                    <input
                      type="text"
                      id="telephoneNumber"
                      name="telephoneNumber"
                      value={formData.telephoneNumber}
                      onChange={handleInputChange}
                    />
                    {formErrors.telephoneNumber && (
                      <div className="error-message">
                        {formErrors.telephoneNumber}
                      </div>
                    )}
                  </div>
                  <div className="form-field">
  <label htmlFor="gender">Gender:</label>
  <select
    id="gender"
    name="gender"
    value={formData.gender}
    onChange={handleInputChange}
  >
    <option value="">-- Select Gender --</option>
    <option value="Male">Male</option>
    <option value="Female">Female</option>
  </select>
  {formErrors.gender && (
    <div className="error-message">{formErrors.gender}</div>
  )}
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
      </div>
    </MasterPage>
  );
};

export default AdminPage;
