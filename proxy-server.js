const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const app = express();

// Enable CORS for all routes
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
  res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
  next();
});

const apiProxy = createProxyMiddleware({
  target: 'https://www.carqueryapi.com',
  changeOrigin: true,
  pathRewrite: {
    '^/carquery-api': '',
  },
});

// '/carquery-api' route for forwarding API requests
app.use('/carquery-api', apiProxy);
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Proxy server is running on http://localhost:${PORT}`);
});
