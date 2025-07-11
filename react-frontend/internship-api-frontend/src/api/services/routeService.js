import BaseService from '../baseService';
import apiClient from '../apiClient';
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
    async updateFinish(id){
      const url = `${this.endpoint}/updateFinish/${id}`;
      console.log(url);
      return apiClient.put(url);
    }

    async getForReport(filter){
      const url = `${this.endpoint}/getAmountForReport`;
      return apiClient.post(url,filter);
    }

    async getTopClients()
    {
      const url = `${this.endpoint}/getTopClients`;
      return apiClient.get(url);

    }
    
  }
  
  // Create a singleton instance
  const routeService = new RouteService();
  
  export default routeService; 