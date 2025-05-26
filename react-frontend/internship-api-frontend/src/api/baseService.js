import apiClient from './apiClient';

/**
 * Base service class that provides common CRUD operations
 * for all entity services to extend
 */
class BaseService {
  /**
   * @param {string} endpoint - The base API endpoint for this entity
   * @param {Object} customEndpoints - Custom endpoints for specific operations
   */
  constructor(endpoint, customEndpoints = {}) {
    this.endpoint = endpoint;
    // Default endpoints that can be overridden
    this.customEndpoints = {
      getAll: '/get',
      getById: '/{id}',
      create: '/save',
      update: '/update/{id}',
      delete: '/delete/{id}',
      ...customEndpoints
    };
  }

  /**
   * Get all items
   * @param {Object} params - Query parameters
   * @returns {Promise} - API response
   */
  getAll(params = {}) {
    // Filter out any null, undefined, or empty string values
    const filteredParams = {};
    for (const key in params) {
      if (params[key] !== null && 
          params[key] !== undefined && 
          params[key] !== '') {
        filteredParams[key] = params[key];
      }
    }

    const url = `${this.endpoint}${this.customEndpoints.getAll}`;
    console.log('API Request URL:', url, 'with params:', filteredParams);
    
    return apiClient.get(url, { params: filteredParams });
  }

  /**
   * Get a single item by ID
   * @param {string|number} id - Item ID
   * @returns {Promise} - API response
   */
  getById(id) {
    const url = this.customEndpoints.getById.replace('{id}', id);
    return apiClient.get(`${this.endpoint}${url}`);
  }

  /**
   * Create a new item
   * @param {Object} data - Item data
   * @returns {Promise} - API response
   */
  create(data) {
    const url = `${this.endpoint}${this.customEndpoints.create}`;
    return apiClient.post(url, data);
  }

  /**
   * Update an existing item
   * @param {string|number} id - Item ID
   * @param {Object} data - Updated item data
   * @returns {Promise} - API response
   */
  update(id, data) {
    const url = this.customEndpoints.update.replace('{id}', id);
    return apiClient.put(`${this.endpoint}${url}`, data);
  }

  /**
   * Delete an item
   * @param {string|number} id - Item ID
   * @returns {Promise} - API response
   */
  delete(id) {
    const url = this.customEndpoints.delete.replace('{id}', id);
    return apiClient.delete(`${this.endpoint}${url}`);
  }
}

export default BaseService; 