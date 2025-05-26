import { useKeycloak } from '@react-keycloak/web';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
  const { keycloak, initialized } = useKeycloak();
  const navigate = useNavigate();
  const [loginError, setLoginError] = useState(false);
  
  useEffect(() => {
    if (initialized && keycloak.authenticated) {
      console.log("Access Token:", keycloak.token);
      navigate('/dashboard');
    } 
    else if (initialized && !keycloak.authenticated) {
      console.log("Not authenticated, redirecting to login");
      handleLogin();
    }
  }, [initialized, keycloak.authenticated, navigate]);

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error')) {
      setLoginError(true);
    }
  }, []);

  const handleLogin = () => {
    setLoginError(false);
    
    try {
      
      keycloak.login({
        redirectUri: window.location.origin + '/dashboard',
      }).catch((error) => {
        console.error("Login failed:", error);
        setLoginError(true);
      });
    } catch (error) {
      console.error("Exception during login:", error);
      setLoginError(true);
    }
  };

  
  return (
    <div className="login-container">
      <div className="login-box">
        <div className="login-logo">
          <h1>eCar Management</h1>
        </div>
        <p>Please log in to access the system</p>
        
        {loginError && (
          <div className="error-message">
            <p>There was a problem logging in. Please try again.</p>
          </div>
        )}
        
        <button 
          onClick={handleLogin} 
          className="login-button"
        >
          Login with Keycloak
        </button>
      </div>
    </div>
  );
};

export default LoginPage; 