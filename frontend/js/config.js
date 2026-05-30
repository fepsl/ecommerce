window.APP_CONFIG = {
  apiBase: window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
    ? 'http://localhost:8080'
    : 'https://ecommerce-production-4adf.up.railway.app'
};
