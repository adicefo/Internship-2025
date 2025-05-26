import BaseService from '../baseService';


class ClientService extends BaseService {
    constructor() {
      // Define custom endpoints for driver entity
      const customEndpoints = {
        getAll: '/get',
        getById: '/getById/{id}',
        create: '/save',
        update: '/update/{id}',
        delete: '/delete/{id}'
      };
      super('/client', customEndpoints);
    }
    
    
  }
  
  // Create a singleton instance
  const clientService = new ClientService();
  
  export default clientService; 