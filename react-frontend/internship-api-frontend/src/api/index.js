
import driverService from './services/driverService';
import vehicleService from './services/vehicleService';
import clientService from './services/clientService';
import routeService from './services/routeService';
import reviewService from './services/reviewService';
import notificationService from './services/notificationService';
import rentService from './services/rentService';
import adminService from './services/adminService'; 
import statisticsService from './services/statisticsService';
import companyPriceService from './services/companyPriceService';
import driverVehicleService from './services/driverVehicleService';
import userService from './services/userService';

export {
  driverService,
  vehicleService,
  clientService,
  routeService,
  reviewService,
  notificationService,
  rentService,
  adminService,
  statisticsService,
  companyPriceService,
  driverVehicleService,
  userService
};


export { default as apiClient } from './apiClient'; 