import { useKeycloak } from '@react-keycloak/web';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  FaCar, FaUsers, FaRoute, FaCarAlt, FaBell, 
  FaStar, FaShoppingCart, FaCog, FaChartBar, 
  FaMoneyBillWave, FaCarSide
} from 'react-icons/fa';
import './Dashboard.css';
import { 
  driverService, vehicleService, clientService, routeService, notificationService,
  reviewService, rentService, adminService, statisticsService, companyPriceService, driverVehicleService
} from '../../api';
import MasterPage from '../layout/MasterPage';

const Dashboard = () => {
  const { keycloak } = useKeycloak();
  const navigate = useNavigate();
  
  const [error, setError] = useState(null);
  const [driverCount, setDriverCount] = useState(0);
  const [vehicleCount, setVehicleCount] = useState(0);
  const [clientCount, setClientCount] = useState(0);
  const [rentCount, setRentCount] = useState(0);
  const [routeCount, setRouteCount] = useState(0);
  const [notificationCount, setNotificationCount] = useState(0);
  const [reviewCount, setReviewCount] = useState(0);
  const [adminCount, setAdminCount] = useState(0);
  const [statisticsCount, setStatisticsCount] = useState(0);
  const [companyPricesCount, setCompanyPricesCount] = useState(0);
  const [driverVehiclesCount, setDriverVehiclesCount] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
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
        
        setDriverCount(responseDriver.data.count || 0);
        setVehicleCount(responseVehicle.data.count || 0);
        setClientCount(responseClient.data.count || 0);
        setRentCount(responseRent.data.count || 0);
        setRouteCount(responseRoute.data.count || 0);
        setNotificationCount(responseNotification.data.count || 0);
        setReviewCount(responseReview.data.count || 0);
        setAdminCount(responseAdmin.data.count || 0);
        setStatisticsCount(responseStatistics.data.count || 0);
        setCompanyPricesCount(responseCompanyPrices.data.count || 0);
        setDriverVehiclesCount(responseDriverVehicles.data.count || 0);
        setError(null);
      } catch (err) {
        console.error('Error fetching data:', err);
        setError('Failed to load data. Please try again.');
      }
    };
    fetchData();
  }, []);
  
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
  
  // Generate statistics for cards
  const getStats = (item) => {
    if (item.title === 'Drivers') return { value: driverCount, label: 'Total Active' };
    if (item.title === 'Vehicles') return { value: vehicleCount, label: 'Available' };
    if (item.title === 'Clients') return { value: clientCount, label: 'Registered' };
    if (item.title === 'Rents') return { value: rentCount, label: 'Active Rentals' };
    
    switch (item.title) {
      case 'Routes':
        return { value: routeCount, label: 'Active Routes' };
      case 'Notifications':
        return { value: notificationCount, label: 'Unread' };
      case 'Reviews':
        return { value: reviewCount, label: 'New Reviews' };
      case 'Admin':
        return { value: adminCount, label: 'Actions Required' };
      case 'Statistics':
        return { value: statisticsCount, label: 'New Reports' };
      case 'Company Prices':
        return { value: companyPricesCount, label: 'Price Updates' };
      case 'Driver Vehicles':
        return { value: driverVehiclesCount, label: 'Assignments' };
      default:
        return { value: 0, label: 'Total' };
    }
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
  
  const handleCardClick = (path) => {
    navigate(path);
  };
  
  return (
    <MasterPage currentRoute="Dashboard">
      <div className="dashboard-overview">
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}
        <div className="stats-container">
          {allItems.map(item => {
            const stats = getStats(item);
            return (
              <div 
                key={item.route} 
                className="stat-card"
                onClick={() => handleCardClick(item.path)}
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
      
     
    </MasterPage>
  );
};

export default Dashboard; 