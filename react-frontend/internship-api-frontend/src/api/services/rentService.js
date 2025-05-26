import BaseService from '../baseService';

class RentService extends BaseService {
    constructor() {
      // Define custom endpoints for driver entity
      const customEndpoints = {
        getAll: '/get',
        getById: '/getById/{id}',
        create: '/save',
        update: '/update/{id}',
        delete: '/delete/{id}'
      };
      super('/rent', customEndpoints);
    }
    
    
  }
  
  // Create a singleton instance
  const rentService = new RentService();
  
  export default rentService; 