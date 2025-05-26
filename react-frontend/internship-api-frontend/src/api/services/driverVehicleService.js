import BaseService from '../baseService';

class DriverVehicleService extends BaseService {
    constructor() {
      // Define custom endpoints for driver entity
      const customEndpoints = {
        getAll: '/get',
        getById: '/getById/{id}',
        create: '/save',
        update: '/update/{id}',
        delete: '/delete/{id}'
      };
      super('/driverVehicle', customEndpoints);
    }
    
    
  }
  
  // Create a singleton instance
  const driverVehicleService = new DriverVehicleService();
  
  export default driverVehicleService; 