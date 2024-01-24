// config.js
const dev = {
    apiUrl: 'http://localhost:8080',
};

const prod = {
    apiUrl: '', // Assuming the API is hosted at the same URL as the frontend in production
};

const config = process.env.NODE_ENV === 'development' ? dev : prod;

export default config;
