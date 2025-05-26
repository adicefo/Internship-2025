
import BaseService from "../baseService";
class StatisticsService extends BaseService {
    constructor() {
      // Define custom endpoints for driver entity
      const customEndpoints = {
        getAll: '/get',
        getById: '/getById/{id}',
        create: '/save',
        update: '/update/{id}',
        delete: '/delete/{id}'
      };
      super('/statistics', customEndpoints);
    }
    
    
  }
  
  // Create a singleton instance
  const statisticsService = new StatisticsService();
  
  export default statisticsService; 