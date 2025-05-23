import { Routes, Route } from 'react-router-dom'
import { useKeycloak } from '@react-keycloak/web'
import { useState, useEffect } from 'react'
import './App.css'
import LoginPage from './components/LoginPage'
import Dashboard from './components/Dashboard'
import PrivateRoute from './components/PrivateRoute'

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
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route element={<PrivateRoute />}>
        <Route path="/dashboard" element={<Dashboard />} />
      </Route>
    </Routes>
  );
}

export default App
