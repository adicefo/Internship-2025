import BaseService from '../baseService';
import apiClient from '../apiClient';

class VehicleService extends BaseService {
  constructor() {
    // Define custom endpoints for vehicle entity
    const customEndpoints = {
      getAll: '/get',
      getById: '/getById/{id}',
      create: '/save',
      update: '/update/{id}',
      delete: '/delete/{id}'
    };
    
   
    super('/vehicle', customEndpoints);
  }


}

// Create a singleton instance
const vehicleService = new VehicleService();

export default vehicleService; 