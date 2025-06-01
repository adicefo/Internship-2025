import BaseService from '../baseService';
import apiClient from '../apiClient';

class CompanyPriceService extends BaseService {
    constructor() {
      // Define custom endpoints for driver entity
      const customEndpoints = {
        getAll: '/get',
        getById: '/getById/{id}',
        create: '/save',
        update: '/update/{id}',
        delete: '/delete/{id}'
      };
      super('/companyPrice', customEndpoints);
    }
    async getCurrentPrice(){
      const url=`${this.endpoint}/getCurrent`;
      return apiClient.get(url);
    }
    
  }
  
  // Create a singleton instance
  const companyPriceService = new CompanyPriceService();
  
  export default companyPriceService; 