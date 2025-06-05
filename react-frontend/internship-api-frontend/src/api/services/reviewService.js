import apiClient from '../apiClient';
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
    
    async getAverageReview(){
      const url = `${this.endpoint}/getAverageReview`;
      return apiClient.get(url);
    }
    
  }
  
  // Create a singleton instance
  const reviewService = new ReviewService();
  
  export default reviewService; 