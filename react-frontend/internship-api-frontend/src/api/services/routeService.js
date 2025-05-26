import BaseService from '../baseService';

class RouteService extends BaseService {
    constructor() {
      // Define custom endpoints for driver entity
      const customEndpoints = {
        getAll: '/get',
        getById: '/getById/{id}',
        create: '/save',
        update: '/update/{id}',
        delete: '/delete/{id}'
      };
      super('/route', customEndpoints);
    }
    
    
  }
  
  // Create a singleton instance
  const routeService = new RouteService();
  
  export default routeService; 