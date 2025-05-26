import BaseService from '../baseService';
import apiClient from '../apiClient';

class DriverService extends BaseService {
  constructor() {
    // Define custom endpoints for driver entity
    const customEndpoints = {
      getAll: '/get',
      getById: '/getById/{id}',
      create: '/save',
      update: '/update/{id}',
      delete: '/delete/{id}'
    };
    super('/driver', customEndpoints);
  }
  
  
}

// Create a singleton instance
const driverService = new DriverService();

export default driverService; 