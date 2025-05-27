import { useKeycloak } from '@react-keycloak/web';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  FaCar, FaUsers, FaRoute, FaCarAlt, FaBell, 
  FaStar, FaShoppingCart, FaBars, FaSignOutAlt,
  FaCog, FaChartBar, FaMoneyBillWave, FaCarSide,
  FaEdit, FaTrash, FaEye, FaSearch,
  FaHome
} from 'react-icons/fa';
import { driverService } from '../../api';
import MasterPage from '../../components/layout/MasterPage';
import './DriverPage.css';
import ConfirmDialog from '../../utils/ConfirmDialog';
import toast from 'react-hot-toast';


const DriverPage = () => {
  const { keycloak } = useKeycloak();
  const [drivers, setDrivers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showDialog, setShowDialog] = useState(false);
  const navigate = useNavigate();
  
  // Filter state
  const [nameFilter, setNameFilter] = useState('');
  const [surnameFilter, setSurnameFilter] = useState('');
  
  const fetchDrivers = async (filter = {}) => {
    try {
      setLoading(true);
      const response = await driverService.getAll(filter);
      console.log('API response:', response);
      setDrivers(response.data.items || []);
      setError(null);
    } catch (err) {
      console.error('Error fetching drivers:', err);
      setError('Failed to load drivers. Please try again.');
    } finally {
      setLoading(false);
    }
  };
  
  useEffect(() => {
    fetchDrivers();
  }, []);

  const confirmDelete=async(id)=>{
    setShowDialog(false);
    try{
        await driverService.delete(id);
        toast.success('Driver deleted successfully');
        fetchDrivers();
    }
    catch{
      toast.error('Error deleting driver');
    }
  }
  const handleEditDriver = (driver) => {
   
    navigate('/driver/edit', { state: { driver } });
  };
  
  const handleDeleteDriver = (id) => {

    setShowDialog(true);
  };
  
  const handleAddDriver = () => {
    navigate('/driver/add');
  };

  const handleFilter = () => {
    var filter={
      "name":nameFilter,
      "surname":surnameFilter
    }
    fetchDrivers(filter);
  };

  return (
    <MasterPage currentRoute="Drivers">
      <div className="drivers-section">
        <div className="section-header">
          <h3>Drivers Management</h3>
          <button className="add-button" onClick={handleAddDriver}>Add New Driver</button>
        </div>
        {/* Filter controls */}
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
          <div className="loading-indicator">Loading drivers data...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : (
          <div className="drivers-table-container">
            <table className="drivers-table table table-hover">
              <thead>
                <tr>
                  <th style={{ width: '80px' }}>Name</th>
                  <th style={{ width: '80px' }}>Surname</th>
                  <th style={{ width: '100px' }}>Username</th>
                  <th style={{ width: '150px' }}>Email</th>
                  <th style={{ width: '120px' }}>Phone</th>
                  <th style={{ width: '80px' }} title="Number of driving hours">Hours</th>
                  <th style={{ width: '80px' }} title="Number of clients served">Clients</th>
                  <th style={{ width: '100px' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {drivers.map((driver) => (
                  <tr key={driver.id}>
                    <td>{driver.user.name}</td>
                    <td>{driver.user.surname}</td>
                    <td>{driver.user.username}</td>
                    <td>{driver.user.email}</td>
                    <td>{driver.user.telephoneNumber}</td>
                    <td>{driver.numberOfHoursAmount}</td>
                    <td>{driver.numberOfClientsAmount}</td>
                    <td className="action-buttons">
                      <button 
                        className="edit-button" 
                        onClick={() => handleEditDriver(driver)}
                        title="Edit driver"
                      >
                        <FaEdit />
                      </button>
                      <button 
                        className="delete-button" 
                        onClick={() => handleDeleteDriver(driver.id)}
                        title="Delete driver"
                      >
                        <FaTrash />
                      </button>
                      {showDialog && (
        <ConfirmDialog
          title="Delete Confirmation"
          message="Are you sure you want to delete this item?"
          onConfirm={()=>confirmDelete(driver.id)}
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

export default DriverPage; 