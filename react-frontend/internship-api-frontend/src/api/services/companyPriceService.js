import BaseService from '../baseService';


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
    
    
  }
  
  // Create a singleton instance
  const companyPriceService = new CompanyPriceService();
  
  export default companyPriceService; 