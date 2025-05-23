import { useKeycloak } from '@react-keycloak/web';
import { useState } from 'react';
import { 
  FaCar, FaUsers, FaRoute, FaCarAlt, FaBell, 
  FaStar, FaShoppingCart, FaBars, FaSignOutAlt,
  FaCog, FaChartBar, FaMoneyBillWave, FaCarSide
} from 'react-icons/fa';

const Dashboard = () => {
  const { keycloak } = useKeycloak();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [currentRoute, setCurrentRoute] = useState('Dashboard');
  
  const handleLogout = () => {
    keycloak.logout({
      redirectUri: window.location.origin
    });
  };

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };
  
  const navItems = [
    { title: 'Drivers', icon: <FaCar />, route: 'Drivers' },
    { title: 'Clients', icon: <FaUsers />, route: 'Clients' },
    { title: 'Routes', icon: <FaRoute />, route: 'Routes' },
    { title: 'Vehicles', icon: <FaCarAlt />, route: 'Vehicles' },
    { title: 'Notifications', icon: <FaBell />, route: 'Notifications' },
    { title: 'Reviews', icon: <FaStar />, route: 'Reviews' },
    { title: 'Rents', icon: <FaShoppingCart />, route: 'Rents' }
  ];

  const additionalItems = [
    { title: 'Admin', icon: <FaCog />, route: 'Admin' },
    { title: 'Statistics', icon: <FaChartBar />, route: 'Statistics' },
    { title: 'Company Prices', icon: <FaMoneyBillWave />, route: 'CompanyPrices' },
    { title: 'Driver Vehicles', icon: <FaCarSide />, route: 'DriverVehicles' }
  ];
  
  const handleNavigation = (route) => {
    setCurrentRoute(route);
  };
  
  return (
    <div className="dashboard">
      {/* Header */}
      <header className="dashboard-header">
        <button className="menu-toggle" onClick={toggleSidebar}>
          <FaBars />
        </button>
        <h1>eCar Management Dashboard</h1>
        <button onClick={handleLogout} className="logout-button">
          Logout
        </button>
      </header>
      
      {/* Sidebar Navigation */}
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
              onClick={() => handleNavigation(item.route)}
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
                  onClick={() => handleNavigation(item.route)}
                >
                  <div className="nav-icon">{item.icon}</div>
                  <div className="nav-title">{item.title}</div>
                </div>
              ))}
            </div>
          </div>
          
          <div className="nav-item logout" onClick={handleLogout}>
            <div className="nav-icon"><FaSignOutAlt /></div>
            <div className="nav-title">Log out</div>
          </div>
        </div>
      </div>
      
      {/* Main Content */}
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
          
          {currentRoute === 'Dashboard' && (
            <div className="dashboard-overview">
              <div className="stats-container">
                <div className="stat-card">
                  <div className="stat-icon drivers">
                    <FaCar />
                  </div>
                  <div className="stat-details">
                    <h3>Drivers</h3>
                    <p className="stat-value">24</p>
                    <p className="stat-label">Total Active</p>
                  </div>
                </div>
                
                <div className="stat-card">
                  <div className="stat-icon vehicles">
                    <FaCarAlt />
                  </div>
                  <div className="stat-details">
                    <h3>Vehicles</h3>
                    <p className="stat-value">42</p>
                    <p className="stat-label">Available</p>
                  </div>
                </div>
                
                <div className="stat-card">
                  <div className="stat-icon clients">
                    <FaUsers />
                  </div>
                  <div className="stat-details">
                    <h3>Clients</h3>
                    <p className="stat-value">156</p>
                    <p className="stat-label">Registered</p>
                  </div>
                </div>
                
                <div className="stat-card">
                  <div className="stat-icon rents">
                    <FaShoppingCart />
                  </div>
                  <div className="stat-details">
                    <h3>Rents</h3>
                    <p className="stat-value">18</p>
                    <p className="stat-label">Active Rentals</p>
                  </div>
                </div>
              </div>
              
    </div>
            
          )}
          
          {currentRoute !== 'Dashboard' && (
            <div className="content-placeholder">
              <div className="section-icon">
                {navItems.find(item => item.route === currentRoute)?.icon || 
                 additionalItems.find(item => item.route === currentRoute)?.icon}
              </div>
              <h3>{currentRoute} Section</h3>
              <p><b>This section is under development. Please check back later.</b></p>
              <button className="return-button" onClick={() => setCurrentRoute('Dashboard')}>
                Return to Dashboard
              </button>
            </div>
          )}
        </div>
      </div>
      
      {/* Overlay for mobile */}
      {sidebarOpen && (
        <div className="sidebar-overlay" onClick={toggleSidebar}></div>
      )}
    </div>
  );
};

export default Dashboard; 