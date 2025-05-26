import BaseService from '../baseService';
class NotificationService extends BaseService {
    constructor() {
      // Define custom endpoints for driver entity
      const customEndpoints = {
        getAll: '/get',
        getById: '/getById/{id}',
        create: '/save',
        update: '/update/{id}',
        delete: '/delete/{id}'
      };
      super('/notification', customEndpoints);
    }
    
    
  }
  
  // Create a singleton instance
  const notificationService = new NotificationService();
  
  export default notificationService; 