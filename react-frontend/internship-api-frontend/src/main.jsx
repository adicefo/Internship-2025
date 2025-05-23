import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { ReactKeycloakProvider } from '@react-keycloak/web'
import keycloak from './keycloak/keycloak.js'
import './index.css'
import App from './App.jsx'

const initOptions = {
  onLoad: 'login-required',
  silentCheckSsoFallbackRedirectUri: window.location.origin + '/',
  checkLoginIframe: false,
  enableLogging: true,
  promiseType: 'native',
  pkceMethod: 'S256',
  flow: 'standard'
}

const eventLogger = (event, error) => {
  if (error) {
    console.error('Keycloak event error:', event, error);
  } else {
    console.log('Keycloak event:', event);
  }
}

const tokenLogger = (tokens) => {
  console.log('Keycloak tokens updated');
}

const handleKeycloakError = (error) => {
  console.error('Failed to initialize Keycloak', error);
  window.location.href = '/';
}

createRoot(document.getElementById('root')).render(
  <ReactKeycloakProvider
    authClient={keycloak}
    initOptions={initOptions}
    onEvent={eventLogger}
    onTokens={tokenLogger}
    onError={handleKeycloakError}
    LoadingComponent={<div>Loading Keycloak...</div>}
  >
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </ReactKeycloakProvider>
)
