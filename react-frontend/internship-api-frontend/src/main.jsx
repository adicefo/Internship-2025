import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { ReactKeycloakProvider } from '@react-keycloak/web'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import keycloak from './keycloak/keycloak.js'
import './index.css'
import App from './App.jsx'

// Create a client for React Query
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

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
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </QueryClientProvider>
  </ReactKeycloakProvider>
)
