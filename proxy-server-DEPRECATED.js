const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const path = require('path');

const app = express();

const apiProxy = createProxyMiddleware({
  target: 'https://api.carsxe.com',
  changeOrigin: true,
  pathRewrite: {
    '^/api': '', // Remove the '/api' prefix when forwarding the request
  },
});

const publicPath = 'public';
app.use(express.static(publicPath));

// Serve the 'index.html' file for the root URL ("/")
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, publicPath, 'index.html'));
});

// Use the '/api' route for forwarding API requests
app.use('/api', apiProxy);

const PORT = process.env.PORT || 3001;

app.listen(PORT, () => {
  console.log(`Proxy server is running on http://localhost:${PORT}`);
});

