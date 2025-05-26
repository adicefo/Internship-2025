import { useKeycloak } from '@react-keycloak/web';
import { use, useState,useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  FaCar, FaUsers, FaRoute, FaCarAlt, FaBell, 
  FaStar, FaShoppingCart, FaBars, FaSignOutAlt,
  FaCog, FaChartBar, FaMoneyBillWave, FaCarSide,FaHome
} from 'react-icons/fa';
import './Dashboard.css';
import { driverService,vehicleService,clientService,routeService,notificationService,
    reviewService,rentService,adminService,statisticsService,companyPriceService,driverVehicleService
 } from '../../api';

const Dashboard = () => {
  const { keycloak } = useKeycloak();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [currentRoute, setCurrentRoute] = useState('Dashboard');
  const [error, setError] = useState(null);
  const [driverCount,setDriverCount]=useState(0);
  const [vehicleCount,setVehicleCount]=useState(0);
  const [clientCount,setClientCount]=useState(0);
  const [rentCount,setRentCount]=useState(0);
  const [routeCount,setRouteCount]=useState(0);
  const [notificationCount,setNotificationCount]=useState(0);
  const [reviewCount,setReviewCount]=useState(0);
  const [adminCount,setAdminCount]=useState(0);
  const [statisticsCount,setStatisticsCount]=useState(0);
  const [companyPricesCount,setCompanyPricesCount]=useState(0);
  const [driverVehiclesCount,setDriverVehiclesCount]=useState(0);


  const navigate = useNavigate();

  useEffect(()=>{
     const fetchData=async()=>{
        try {
            const responseDriver = await driverService.getAll();
            const responseVehicle = await vehicleService.getAll();
            const responseClient = await clientService.getAll();
            const responseRent = await rentService.getAll();
            const responseRoute = await routeService.getAll();
            const responseNotification = await notificationService.getAll();
            const responseReview = await reviewService.getAll();
            const responseAdmin = await adminService.getAll();
            const responseStatistics = await statisticsService.getAll();
            const responseCompanyPrices = await companyPriceService.getAll();
            const responseDriverVehicles = await driverVehicleService.getAll();
            
            setDriverCount(responseDriver.data.count || []);
            setVehicleCount(responseVehicle.data.count || []);
            setClientCount(responseClient.data.count || []);
            setRentCount(responseRent.data.count || []);
            setRouteCount(responseRoute.data.count || []);
            setNotificationCount(responseNotification.data.count || []);
            setReviewCount(responseReview.data.count || []);
            setAdminCount(responseAdmin.data.count || []);
            setStatisticsCount(responseStatistics.data.count || []);
            setCompanyPricesCount(responseCompanyPrices.data.count || []);
            setDriverVehiclesCount(responseDriverVehicles.data.count || []);
            setError(null);
          } catch (err) {
            console.error('Error fetching data:', err);
            setError('Failed to load data. Please try again.');
          } finally {
            
          }
     }
     fetchData();
  })
  
  const handleLogout = () => {
    keycloak.logout({
      redirectUri: window.location.origin
    });
  };

  const handleDashboard=()=>{
    handleNavigation("Dashboard","/dashboard");
  }

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
    { title: 'Rents', icon: <FaShoppingCart />, route: 'Rents', path: '/rents' }
  ];

  const additionalItems = [
    { title: 'Admin', icon: <FaCog />, route: 'Admin', path: '/admin' },
    { title: 'Statistics', icon: <FaChartBar />, route: 'Statistics', path: '/statistics' },
    { title: 'Company Prices', icon: <FaMoneyBillWave />, route: 'CompanyPrices', path: '/prices' },
    { title: 'Driver Vehicles', icon: <FaCarSide />, route: 'DriverVehicles', path: '/driver-vehicles' }
  ];
  
  // Combine navItems and additionalItems for the cards
  const allItems = [...navItems, ...additionalItems];
  
  const handleNavigation = (route, path) => {
    setCurrentRoute(route);
    if (path) {
      navigate(path);
    }
  };
  
  // Generate random statistics for demonstration
  const getRandomStats = (item) => {
    let statValue = Math.floor(Math.random() * 100) + 10;
    let statLabel = 'Total';
    
    if (item.title === 'Drivers') return { value: driverCount, label: 'Total Active' };
    if (item.title === 'Vehicles') return { value: vehicleCount, label: 'Available' };
    if (item.title === 'Clients') return { value: clientCount, label: 'Registered' };
    if (item.title === 'Rents') return { value: rentCount, label: 'Active Rentals' };
    
    switch (item.title) {
      case 'Routes':
        statLabel = 'Active Routes';
        statValue = routeCount;
        break;
      case 'Notifications':
        statLabel = 'Unread';
        statValue = notificationCount;
        break;
      case 'Reviews':
        statLabel = 'New Reviews';
        statValue = reviewCount;
        break;
      case 'Admin':
        statLabel = 'Actions Required';
        statValue = adminCount;
        break;
      case 'Statistics':
        statLabel = 'New Reports';
        statValue = statisticsCount;
        break;
      case 'Company Prices':
        statLabel = 'Price Updates';
        statValue = companyPricesCount;
        break;
      case 'Driver Vehicles':
        statLabel = 'Assignments';
        statValue = driverVehiclesCount;
        break;
      default:
        statLabel = 'Total';
        
    }
    
    return { value: statValue, label: statLabel };
  };
  
  // Get appropriate CSS class for item icon
  const getIconClass = (item) => {
    const classes = {
      'Drivers': 'drivers',
      'Vehicles': 'vehicles',
      'Clients': 'clients',
      'Rents': 'rents',
      'Routes': 'routes',
      'Notifications': 'notifications',
      'Reviews': 'reviews',
      'Admin': 'admin',
      'Statistics': 'statistics',
      'CompanyPrices': 'prices',
      'DriverVehicles': 'driver-vehicles'
    };
    
    return classes[item.route] || 'default';
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
                {allItems.map(item => {
                  const stats = getRandomStats(item);
                  return (
                    <div 
                      key={item.route} 
                      className="stat-card"
                      onClick={() => handleNavigation(item.route, item.path)}
                    >
                      <div className={`stat-icon ${getIconClass(item)}`}>
                        {item.icon}
                      </div>
                      <div className="stat-details">
                        <h3>{item.title}</h3>
                        <p className="stat-value">{stats.value}</p>
                        <p className="stat-label">{stats.label}</p>
                      </div>
                    </div>
                  );
                })}
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