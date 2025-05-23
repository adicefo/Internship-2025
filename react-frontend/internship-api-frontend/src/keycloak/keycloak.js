import Keycloak from 'keycloak-js';

const keycloakConfig = {
  url: 'http://localhost:8080',
  realm: 'Internship_API',
  clientId: 'internship-rest-api'
};

// Make sure client is public and doesn't need a client secret
const keycloak = new Keycloak(keycloakConfig);

const originalInit = keycloak.init;
let initialized = false;
keycloak.init = function(...args) {
  if (initialized) {
    console.log("Keycloak already initialized, skipping redundant init");
    return Promise.resolve(true);
  }
  initialized = false; // Reset to allow re-initialization
  return originalInit.apply(this, args)
    .then(auth => {
      console.log("Keycloak initialized successfully", auth);
      initialized = true;
      return auth;
    })
    .catch(error => {
      console.error("Failed to initialize Keycloak", error);
      initialized = false; // Allow retry if it failed
      throw error;
    });
};

export default keycloak; 