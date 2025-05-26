import axios from 'axios';
import keycloak from '../keycloak/keycloak';

const API_BASE_URL = 'http://localhost:8081/v2';


const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for adding auth token
apiClient.interceptors.request.use(
  (config) => {
    // Add authorization header if user is authenticated
    if (keycloak.authenticated && keycloak.token) {
      config.headers.Authorization = `Bearer ${keycloak.token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor for handling common errors
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    
    // Handle 401 Unauthorized errors (token expired)
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        // Try to refresh the token
        const refreshed = await keycloak.updateToken(30);
        
        if (refreshed) {
          // Update the authorization header with new token
          originalRequest.headers.Authorization = `Bearer ${keycloak.token}`;
          return apiClient(originalRequest);
        }
      } catch (refreshError) {
        // If token refresh fails, redirect to login
        console.error('Token refresh failed:', refreshError);
        keycloak.logout();
        return Promise.reject(refreshError);
      }
    }
    
    return Promise.reject(error);
  }
);

export default apiClient; 