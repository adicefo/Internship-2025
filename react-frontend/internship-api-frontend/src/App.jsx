import { Routes, Route } from 'react-router-dom'
import { useKeycloak } from '@react-keycloak/web'
import { useState, useEffect } from 'react'
import { Toaster } from 'react-hot-toast';
import './App.css'
import LoginPage from './components/LoginPage'
import Dashboard from './components/dashboard/Dashboard'
import DriverPage from './pages/driver/DriverPage'
import DriverDetailsPage from './pages/driver/DriverDetailsPage'
import PrivateRoute from './components/PrivateRoute'
import VehiclePage from './pages/vehicle/VehiclePage'
import VehicleDetailsPage from'./pages/vehicle/VehicleDetailsPage'
import ClientPage from './pages/client/ClientPage'
import ClientDetailsPage from './pages/client/ClientDetailsPage'
import RoutePage from './pages/route/RoutePage'
import RouteDetailsPage from './pages/route/RouteDetailsPage'
function App() {
  const { initialized, keycloak } = useKeycloak();
  const [initializationTimeout, setInitializationTimeout] = useState(false);
  
  useEffect(() => {
    
    const timeoutId = setTimeout(() => {
      if (!initialized) {
        setInitializationTimeout(true);
      }
    }, 10000); 
    
    return () => clearTimeout(timeoutId);
  }, [initialized]);
  
  const handleRetry = () => {
  
    setInitializationTimeout(false);
  
    window.location.reload();
  };

  if (!initialized) {
    if (initializationTimeout) {
      return (
        <div className="loading-container">
          <div className="loading-box">
            <h2>Connection Issue</h2>
            <p>There was a problem connecting to the authentication service.</p>
            <p>Please make sure Keycloak is running at http://localhost:8080.</p>
            <button onClick={handleRetry} className="retry-button">
              Retry Login
            </button>
          </div>
        </div>
      );
    }
    
    return (
      <div className="loading-container">
        <div className="loading-box">
          <h2>Loading...</h2>
          <p>Please wait while we connect to the authentication service.</p>
        </div>
      </div>
    );
  }

  return (
    <>
      <Toaster position="top-right" reverseOrder={false} />
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route element={<PrivateRoute />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/drivers" element={<DriverPage />} />
          <Route path="/driver/add" element={<DriverDetailsPage />} />
          <Route path="/driver/edit" element={<DriverDetailsPage />} />
          <Route path="/clients" element={<ClientPage />} />
          <Route path="/client/add" element={<ClientDetailsPage />} />
          <Route path="/client/edit" element={<ClientDetailsPage />} />
          <Route path="/routes" element={<RoutePage />} />
          <Route path="/route/add" element={<RouteDetailsPage />} />
          <Route path="/route/edit" element={<RouteDetailsPage />} />
          <Route path="/vehicles" element={<VehiclePage />} />   
          <Route path="/vehicle/add" element={<VehicleDetailsPage />} />
          <Route path="/vehicle/edit" element={<VehicleDetailsPage />} />
          <Route path="/vehicle/delete" element={<VehicleDetailsPage />} />
        </Route>
      </Routes>
    </>
    
  );
}

export default App
