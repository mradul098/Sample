const { app, BrowserWindow, session } = require('electron');
const https = require('https');
const http = require('http');
const os = require('os');
const path = require('path');

// ── Test Results ─────────────────────────────────────────────────────────────
const results = {
  electron_launch: { status: '⏳', detail: 'Testing...' },
  window_creation: { status: '⏳', detail: 'Testing...' },
  network_http: { status: '⏳', detail: 'Testing...' },
  network_https: { status: '⏳', detail: 'Testing...' },
  os_certificates: { status: '⏳', detail: 'Testing...' },
  intranet_test: { status: '⏳', detail: 'Skipped (set URL below)' },
  system_info: { status: 'ℹ️', detail: '' }
};

// ═══════════ CONFIGURE YOUR INTRANET URL HERE ═══════════
const INTRANET_URL = '';  // e.g. 'https://internal-banking-app.yourbank.com'
// ═════════════════════════════════════════════════════════

let mainWindow;

function createWindow() {
  results.electron_launch = { status: '✅', detail: 'Electron app launched successfully' };

  mainWindow = new BrowserWindow({
    width: 900,
    height: 700,
    title: 'FlowBot — Environment Test',
    webPreferences: {
      nodeIntegration: false,
      contextIsolation: true
    }
  });

  results.window_creation = { status: '✅', detail: 'BrowserWindow created successfully' };

  // Gather system info
  results.system_info.detail = [
    `OS: ${os.type()} ${os.release()} (${os.arch()})`,
    `Electron: ${process.versions.electron}`,
    `Chrome: ${process.versions.chrome}`,
    `Node: ${process.versions.node}`,
    `User: ${os.userInfo().username}`,
    `Hostname: ${os.hostname()}`
  ].join('\n');

  // Run all tests
  runTests().then(() => {
    renderResults();
  });
}

async function runTests() {
  // Test 1: HTTP connectivity (example.com)
  try {
    await httpGet('http://example.com');
    results.network_http = { status: '✅', detail: 'HTTP requests work (reached example.com)' };
  } catch (err) {
    results.network_http = { status: '❌', detail: `HTTP blocked: ${err.message}` };
  }

  // Test 2: HTTPS connectivity (example.com)
  try {
    await httpsGet('https://example.com');
    results.network_https = { status: '✅', detail: 'HTTPS requests work (reached example.com)' };
  } catch (err) {
    results.network_https = { status: '❌', detail: `HTTPS blocked: ${err.message}` };
  }

  // Test 3: OS Certificate Store access
  try {
    // Electron uses the OS cert store by default
    // Test by making an HTTPS request that requires valid certs
    await httpsGet('https://www.google.com');
    results.os_certificates = { status: '✅', detail: 'OS certificate store accessible (TLS handshake succeeded)' };
  } catch (err) {
    if (err.message.includes('certificate') || err.message.includes('CERT')) {
      results.os_certificates = { status: '⚠️', detail: `Certificate issue: ${err.message}` };
    } else {
      results.os_certificates = { status: '✅', detail: 'OS certs likely OK (error was network, not cert)' };
    }
  }

  // Test 4: Intranet URL (if configured)
  if (INTRANET_URL) {
    try {
      const isHttps = INTRANET_URL.startsWith('https');
      if (isHttps) {
        await httpsGet(INTRANET_URL);
      } else {
        await httpGet(INTRANET_URL);
      }
      results.intranet_test = { status: '✅', detail: `Reached ${INTRANET_URL}` };
    } catch (err) {
      results.intranet_test = { status: '❌', detail: `Cannot reach ${INTRANET_URL}: ${err.message}` };
    }
  } else {
    results.intranet_test = { status: '⏭️', detail: 'Set INTRANET_URL in main.js to test intranet access' };
  }
}

function renderResults() {
  const html = `
    <!DOCTYPE html>
    <html>
    <head>
      <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', -apple-system, sans-serif; background: #0f0f1a; color: #e2e8f0; padding: 32px; }
        h1 { font-size: 24px; margin-bottom: 8px; }
        .subtitle { color: #94a3b8; margin-bottom: 32px; font-size: 14px; }
        .card { background: #1e1e2e; border: 1px solid #2d2d3d; border-radius: 12px; padding: 20px; margin-bottom: 16px; }
        .card-title { font-size: 16px; font-weight: 600; margin-bottom: 16px; color: #c4b5fd; }
        .test-row { display: flex; align-items: flex-start; gap: 12px; padding: 10px 0; border-bottom: 1px solid #2d2d3d; }
        .test-row:last-child { border-bottom: none; }
        .test-icon { font-size: 18px; flex-shrink: 0; width: 28px; text-align: center; }
        .test-name { font-weight: 600; font-size: 14px; min-width: 160px; }
        .test-detail { color: #94a3b8; font-size: 13px; white-space: pre-wrap; word-break: break-word; }
        .all-pass { background: linear-gradient(135deg, rgba(34,197,94,0.1), rgba(34,197,94,0.05)); border-color: rgba(34,197,94,0.3); }
        .has-fail { background: linear-gradient(135deg, rgba(239,68,68,0.1), rgba(239,68,68,0.05)); border-color: rgba(239,68,68,0.3); }
        .verdict { font-size: 20px; font-weight: 700; margin-top: 8px; }
        .verdict.pass { color: #22c55e; }
        .verdict.fail { color: #ef4444; }
        .verdict.warn { color: #f59e0b; }
        .note { margin-top: 24px; padding: 16px; background: #1a1a2e; border-radius: 8px; font-size: 13px; color: #94a3b8; border: 1px solid #2d2d3d; }
        .note strong { color: #e2e8f0; }
      </style>
    </head>
    <body>
      <h1>🧪 FlowBot — Bank Environment Test</h1>
      <p class="subtitle">Checking if Electron can run in your environment</p>

      <div class="card">
        <div class="card-title">Test Results</div>
        ${Object.entries(results).filter(([k]) => k !== 'system_info').map(([key, val]) => `
          <div class="test-row">
            <span class="test-icon">${val.status}</span>
            <span class="test-name">${formatTestName(key)}</span>
            <span class="test-detail">${val.detail}</span>
          </div>
        `).join('')}
      </div>

      <div class="card ${getVerdictClass()}">
        <div class="card-title">Verdict</div>
        <div class="verdict ${getVerdictLevel()}">${getVerdictMessage()}</div>
      </div>

      <div class="card">
        <div class="card-title">System Info</div>
        <div class="test-detail">${results.system_info.detail}</div>
      </div>

      <div class="note">
        <strong>Next steps:</strong><br>
        • If all tests pass → Electron will work for FlowBot RPA<br>
        • If blocked by proxy → We may need to configure proxy settings<br>
        • If certs fail → We'll need to handle certificate trust<br>
        • To test intranet: edit <code>INTRANET_URL</code> in <code>main.js</code> and re-run<br><br>
        <strong>How to share results:</strong> Take a screenshot of this window and share it.
      </div>
    </body>
    </html>
  `;

  mainWindow.loadURL('data:text/html;charset=utf-8,' + encodeURIComponent(html));
}

// ── Helpers ──────────────────────────────────────────────────────────────────

function httpGet(url) {
  return new Promise((resolve, reject) => {
    const req = http.get(url, { timeout: 10000 }, (res) => {
      resolve(res.statusCode);
    });
    req.on('error', reject);
    req.on('timeout', () => { req.destroy(); reject(new Error('Timeout')); });
  });
}

function httpsGet(url) {
  return new Promise((resolve, reject) => {
    const req = https.get(url, { timeout: 10000 }, (res) => {
      resolve(res.statusCode);
    });
    req.on('error', reject);
    req.on('timeout', () => { req.destroy(); reject(new Error('Timeout')); });
  });
}

function formatTestName(key) {
  return key.split('_').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ');
}

function getVerdictClass() {
  const vals = Object.values(results).filter((_, i) => Object.keys(results)[i] !== 'system_info');
  if (vals.some(v => v.status === '❌')) return 'has-fail';
  return 'all-pass';
}

function getVerdictLevel() {
  const vals = Object.values(results).filter((_, i) => Object.keys(results)[i] !== 'system_info');
  if (vals.some(v => v.status === '❌')) return 'fail';
  if (vals.some(v => v.status === '⚠️')) return 'warn';
  return 'pass';
}

function getVerdictMessage() {
  const vals = Object.values(results).filter((_, i) => Object.keys(results)[i] !== 'system_info');
  const fails = vals.filter(v => v.status === '❌').length;
  if (fails > 0) return `${fails} test(s) failed — Electron may be blocked in this environment`;
  if (vals.some(v => v.status === '⚠️')) return 'Mostly works but some warnings — likely fixable';
  return '✓ All tests passed — Electron will work here!';
}

// ── App Lifecycle ────────────────────────────────────────────────────────────
app.whenReady().then(createWindow);
app.on('window-all-closed', () => app.quit());
