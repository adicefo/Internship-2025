import { useKeycloak } from '@react-keycloak/web';
import { Navigate, Outlet } from 'react-router-dom';

const PrivateRoute = () => {
  const { keycloak, initialized } = useKeycloak();
  
  if (!initialized) {
    return <div>Loading...</div>;
  }

  if (!keycloak.authenticated) {

    return <Navigate to="/" replace />;
  }

  return <Outlet />;
};

export default PrivateRoute; 