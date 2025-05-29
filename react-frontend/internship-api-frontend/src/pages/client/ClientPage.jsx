import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { FaTrash, FaEye, FaSearch ,FaEdit} from "react-icons/fa";
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

  const fetchClients = async (filter = {}) => {
    try {
      setLoading(true);
      const response = await clientService.getAll(filter);
      console.log("API response:", response);
      setClients(response.data.items || []);
      setError(null);
    } catch (err) {
      console.error("Error fetching clients:", err);
      setError("Failed to load clients. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchClients();
  }, []);

  const handleEditClient=(client)=>{

    navigate('/client/edit', {state:{client}})};
  
  const handleAddClient = () => {
    navigate("/client/add");
  };
 const confirmDelete=async()=>{
    setShowDialog(false);
    try{
        await clientService.delete(deleteId);
        toast.success('Client deleted successfully');
        fetchClients();
    }
    catch{
      toast.error('Error deleting client');
    }
 }
  const handleDeleteClient = (id) => {
    setShowDialog(true);
    setDeleteId(id);
  };

  const handleFilter = () => {
    var filter = {
      name: nameFilter,
      surname: surnameFilter,
    };
    fetchClients(filter);
  };

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
                  <th style={{ width: '80px' }}>Name</th>
                  <th style={{ width: '80px' }}>Surname</th>
                  <th style={{ width: '100px' }}>Username</th>
                  <th style={{ width: '150px' }}>Email</th>
                  <th style={{ width: '120px' }}>Phone</th>
                  <th style={{ width: '100px' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {clients.map((client) => (
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
          onConfirm={()=>confirmDelete()}
          onCancel={() => setShowDialog(false)}
        />
      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </MasterPage>
  );
};

export default ClientPage;
