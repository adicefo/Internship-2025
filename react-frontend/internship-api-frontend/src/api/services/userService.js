import BaseService from '../baseService';
import apiClient from '../apiClient';

class UserService extends BaseService {
  constructor() {
    // Define custom endpoints for driver entity
    const customEndpoints = {
      getAll: '/get',
      getById: '/getById/{id}',
      create: '/register',
      update: '/update/{id}',
      delete: '/delete/{id}'
    };
    super('/users', customEndpoints);
  }
  
  async updatePassword(id,request){
    const url = `${this.endpoint}/updatePassword/${id}`;
    console.log(url);
    return apiClient.put(url,request);
  }
  
}

// Create a singleton instance
const userService = new UserService();

export default userService; 