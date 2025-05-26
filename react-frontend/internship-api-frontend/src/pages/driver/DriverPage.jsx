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

const DriverPage = () => {
  const { keycloak } = useKeycloak();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [currentRoute, setCurrentRoute] = useState('Drivers');
  const [drivers, setDrivers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
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
  
  const handleLogout = () => {
    keycloak.logout({
      redirectUri: window.location.origin
    });
  };

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };
  
  const navItems = [
    { title: 'Drivers', icon: <FaCar />, route: 'Drivers', path: '/drivers' },
    { title: 'Clients', icon: <FaUsers />, route: 'Clients', path: '/clients' },
    { title: 'Routes', icon: <FaRoute />, route: 'Routes', path: '/routes' },
    { title: 'Vehicles', icon: <FaCarAlt />, route: 'Vehicles', path: '/vehicles' },
    { title: 'Notifications', icon: <FaBell />, route: 'Notifications', path: '/notifications' },
    { title: 'Reviews', icon: <FaStar />, route: 'Reviews', path: '/reviews' },
    { title: 'Rents', icon: <FaShoppingCart />, route: 'Rents', path: '/rents' },
    
  ];

  const additionalItems = [
    { title: 'Admin', icon: <FaCog />, route: 'Admin', path: '/admin' },
    { title: 'Statistics', icon: <FaChartBar />, route: 'Statistics', path: '/statistics' },
    { title: 'Company Prices', icon: <FaMoneyBillWave />, route: 'CompanyPrices', path: '/prices' },
    { title: 'Driver Vehicles', icon: <FaCarSide />, route: 'DriverVehicles', path: '/driver-vehicles' }
  ];
  
  const handleNavigation = (route, path) => {
    setCurrentRoute(route);
    if (path) {
      navigate(path);
    } else {
      navigate('/dashboard');
    }
  };
  

  
  const handleEditDriver = (id) => {
    console.log('Edit driver:', id);
    // In a real app, navigate to driver edit page
    // navigate(`/drivers/${id}/edit`);
  };
  
  const handleDeleteDriver = (id) => {
    console.log('Delete driver:', id);
    // In a real app, show confirmation and delete
  };
  

  const handleFilter = () => {
    var filter={
      "name":nameFilter,
      "surname":surnameFilter
    }
    

    fetchDrivers(filter);
  };

  const handleDashboard=()=>{
    handleNavigation("Dashboard","/dashboard");
  }

  return (
    <div className="dashboard">
      {/* Header - Same as Dashboard */}
      <header className="dashboard-header">
        <button className="menu-toggle" onClick={toggleSidebar}>
          <FaBars />
        </button>
        <h1>eCar Management Dashboard</h1>
        <button onClick={handleLogout} className="logout-button">
          Logout
        </button>
      </header>
      
      {/* Sidebar Navigation - Same as Dashboard */}
      <div className={`sidebar ${sidebarOpen ? 'open' : ''}`}>
        <div className="sidebar-header">
          <div className="sidebar-logo">
            <span>@eCar</span>
          </div>
          <div className="user-avatar">
            <div className="avatar-circle">
              <FaCarAlt />
            </div>
          </div>
          <div className="user-name">
            {keycloak.tokenParsed?.preferred_username || keycloak.tokenParsed?.name || 'User'}
          </div>
        </div>
        
        <div className="nav-items">
          {navItems.map((item) => (
            <div 
              key={item.route}
              className={`nav-item ${currentRoute === item.route ? 'active' : ''}`}
              onClick={() => handleNavigation(item.route, item.path)}
            >
              <div className="nav-icon">{item.icon}</div>
              <div className="nav-title">{item.title}</div>
            </div>
          ))}
          
          <div className="nav-divider"></div>
          
          <div className="additional-menu">
            <div className="additional-header">
              <div className="additional-icon"><FaCog /></div>
              <div className="additional-title">Additional</div>
            </div>
            
            <div className="additional-items">
              {additionalItems.map((item) => (
                <div 
                  key={item.route}
                  className={`nav-item ${currentRoute === item.route ? 'active' : ''}`}
                  onClick={() => handleNavigation(item.route, item.path)}
                >
                  <div className="nav-icon">{item.icon}</div>
                  <div className="nav-title">{item.title}</div>
                </div>
              ))}
            </div>
          </div>
          
        <div className="nav-items dashboard-items">
        <div className="nav-item dashboard" onClick={handleDashboard} style={{minHeight: '40px'}}>
            <div className="nav-icon"><FaHome /></div>
            <div className="nav-title">Dashboard</div>
          </div>
          <div className="nav-item logout" onClick={handleLogout}>
            <div className="nav-icon"><FaSignOutAlt /></div>
            <div className="nav-title">Log out</div>
          </div>
        </div>
        </div>
      </div>
      
      {/* Main Content - Drivers Table */}
      <div className={`main-content ${sidebarOpen ? 'shifted' : ''}`}>
        <div className="dashboard-content">
          <div className="welcome-banner">
            <div className="welcome-text">
              <h2>Welcome, {keycloak.tokenParsed?.preferred_username || keycloak.tokenParsed?.name || 'User'}</h2>
              <p>Current section: <span className="highlight">{currentRoute}</span></p>
            </div>
            <div className="date-display">
              {new Date().toLocaleDateString('en-US', { 
                weekday: 'long', 
                year: 'numeric', 
                month: 'long', 
                day: 'numeric' 
              })}
            </div>
          </div>
          
          <div className="drivers-section">
            <div className="section-header">
              <h3>Drivers Management</h3>
              <button className="add-button">Add New Driver</button>
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
                <table className="drivers-table" class="table table-hover">
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
                        <td>{driver.user.telephoneNumber
                        }</td>
                        <td>{driver.numberOfHoursAmount}</td>
                        <td>{driver.numberOfClientsAmount}</td>
                        <td className="action-buttons">
                          
                          <button 
                            className="edit-button" 
                            onClick={() => handleEditDriver(driver.id)}
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
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      </div>
      
      {/* Overlay for mobile */}
      {sidebarOpen && (
        <div className="sidebar-overlay" onClick={toggleSidebar}></div>
      )}
    </div>
  );
};

export default DriverPage; 