import BaseService from '../baseService';
import apiClient from '../apiClient';

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
    
    async checkAvailability(id,request){
      const url=`${this.endpoint}/checkAvailability/${id}`;
      console.log(url);
      return apiClient.post(url,request);
    }

    async updateActive(id){
      const url=`${this.endpoint}/updateActive/${id}`;
      return apiClient.put(url);
    }
    async updateFinish(id){
      const url=`${this.endpoint}/updateFinish/${id}`;
      return apiClient.put(url);
    }
  }
  
  // Create a singleton instance
  const rentService = new RentService();
  
  export default rentService; 