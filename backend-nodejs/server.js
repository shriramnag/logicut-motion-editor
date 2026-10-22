/**
 * LogiCut Studio Pro v1.0.005 — Node.js Express & Media Asset Server
 * Serves the hybrid React/Canvas Studio UI and provides REST endpoints for recipes & exports.
 */
const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = process.env.PORT || 3000;

const server = http.createServer((req, res) => {
  if (req.url === '/api/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({
      status: 'ok',
      app: 'LogiCut Studio Pro',
      version: '1.0.005',
      engines: ['React 18 JSX', 'Python 3 Motion Engine', 'Node.js HTTP Server', 'Android Java WebView Bridge']
    }));
    return;
  }

  const htmlPath = path.join(__dirname, '..', 'logicut_studio_pro.html');
  if (fs.existsSync(htmlPath)) {
    res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
    fs.createReadStream(htmlPath).pipe(res);
  } else {
    res.writeHead(404);
    res.end('logicut_studio_pro.html not found');
  }
});

if (require.main === module) {
  server.listen(PORT, () => {
    console.log(`LogiCut Studio Pro v1.0.005 Node.js Server listening on http://localhost:${PORT}`);
  });
}

module.exports = server;
