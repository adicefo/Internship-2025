import BaseService from '../baseService';

class ReviewService extends BaseService {
    constructor() {
      // Define custom endpoints for driver entity
      const customEndpoints = {
        getAll: '/get',
        getById: '/getById/{id}',
        create: '/save',
        update: '/update/{id}',
        delete: '/delete/{id}'
      };
      super('/review', customEndpoints);
    }
    
    
  }
  
  // Create a singleton instance
  const reviewService = new ReviewService();
  
  export default reviewService; 