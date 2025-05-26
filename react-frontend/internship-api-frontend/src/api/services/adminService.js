import BaseService from '../baseService';

class AdminService extends BaseService {
    constructor() {
      // Define custom endpoints for driver entity
      const customEndpoints = {
        getAll: '/get',
        getById: '/getById/{id}',
        create: '/save',
        update: '/update/{id}',
        delete: '/delete/{id}'
      };
      super('/admin', customEndpoints);
    }
    
    
  }
  
  // Create a singleton instance
  const adminService = new AdminService();
  
  export default adminService; 