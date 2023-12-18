const proxyUrl = 'https://crossorigin.me/';
const apiUrl = 'https://api.carsxe.com/specs?key=rqx8xyzf5_uto6mwpym_php2r1u28&deepdata=1&vin=ZPBUA1ZL4NLA19618';
const url = proxyUrl + apiUrl;

const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

const apiProxy = createProxyMiddleware({
  target: 'https://api.carsxe.com',
  changeOrigin: true,
  pathRewrite: {
    '^/api': '', // Remove the '/api' prefix when forwarding the request
  },
});

app.use('/api', (req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  apiProxy(req, res, next);
});

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.log(`Proxy server is running on http://localhost:${PORT}`);
});
