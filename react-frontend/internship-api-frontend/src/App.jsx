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
import RentPage from './pages/rent/RentPage'
import RentDetailsPage from './pages/rent/RentDetailsPage'
import RentActivePage from './pages/rent/RentActivePage'
import NotificationPage from './pages/notification/NotificationPage'
import NotificationDetailsPage from './pages/notification/NotificationDetailsPage'
import ReviewPage from './pages/review/ReviewPage'
import ReviewDetailsPage from './pages/review/ReviewDetailsPage'
import StatisticsPage from './pages/statistics/StatisticsPage'
import CompanyPricePage from './pages/companyPrice/CompanyPricePage'
import ReportsPage from './components/report/ReportsPage';
import AdminPage from './pages/admin/AdminPage';
import DriverVehiclePage from './pages/driverVehicle/DriverVehiclePage';
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
          <Route path="/notifications" element={<NotificationPage/>}/>
          <Route path="/notification/add" element={<NotificationDetailsPage/>}/>
          <Route path="/notification/edit" element={<NotificationDetailsPage/>}/>
          <Route path="/reviews" element={<ReviewPage/>}/>
          <Route path="/review/add" element={<ReviewDetailsPage/>}/>
          <Route path="/review/edit" element={<ReviewDetailsPage/>}/>
          <Route path="/rents" element={<RentPage />} />
          <Route path="/rent/add" element={<RentDetailsPage />} />
          <Route path="/rent/activate" element={<RentActivePage />} />
          <Route path="/rent/edit" element={<RentDetailsPage />} />
          <Route path="/statistics" element={<StatisticsPage/>}/>
          <Route path="/prices" element={<CompanyPricePage/>}/>
          <Route path="/report" element={<ReportsPage/>}></Route>
          <Route path="/admin" element={<AdminPage/>}/>
          <Route path="/driverVehicle" element={<DriverVehiclePage/>}/>

        </Route>
      </Routes>
    </>
    
  );
}

export default App
