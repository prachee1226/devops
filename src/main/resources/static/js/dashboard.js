// Notification Toast Helper
function showNotification(message, type = 'info') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.style.cssText = 'position:fixed; top:20px; right:20px; z-index:9999; display:flex; flex-direction:column; gap:10px; max-width:350px;';
        document.body.appendChild(container);
    }
    const toast = document.createElement('div');
    toast.className = 'toast-alert';
    toast.style.cssText = `background:${type === 'error' ? '#ef4444' : type === 'success' ? '#16a34a' : '#0284c7'}; color:white; padding:12px 16px; border-radius:8px; box-shadow:0 4px 12px rgba(0,0,0,0.15); font-size:0.85rem; font-weight:600; display:flex; align-items:center; gap:8px; animation:fadeIn 0.3s ease;`;
    toast.innerHTML = `<i class="fa-solid ${type === 'error' ? 'fa-circle-xmark' : type === 'success' ? 'fa-circle-check' : 'fa-bell'}"></i> <span>${escapeHtml(message)}</span>`;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transition = 'opacity 0.5s ease';
        setTimeout(() => toast.remove(), 500);
    }, 4000);
}

let currentAmbulances = [];
let currentFilter = 'ALL';
let currentSearch = '';

document.addEventListener('DOMContentLoaded', () => {
    initClock();
    loadAllData();

    // Auto-refresh metrics and fleet status every 15 seconds
    setInterval(loadAllData, 15000);

    // Form submission listener
    const dispatchForm = document.getElementById('quick-dispatch-form');
    if (dispatchForm) {
        dispatchForm.addEventListener('submit', handleEmergencySubmit);
    }
});

function initClock() {
    function updateClock() {
        const now = new Date();
        const timeStr = now.toLocaleTimeString('en-US', { hour12: false });
        const clockEl = document.getElementById('current-clock');
        if (clockEl) clockEl.textContent = timeStr;
    }
    updateClock();
    setInterval(updateClock, 1000);
}

async function loadAllData() {
    await Promise.all([
        fetchMetrics(),
        fetchAlerts(),
        fetchAmbulances(),
        fetchPendingCalls()
    ]);
}

// 1. Fetch & Render Summary Metrics (Requirement 3)
async function fetchMetrics() {
    try {
        const res = await fetch('/api/dashboard/metrics');
        if (!res.ok) throw new Error('Failed to fetch metrics');
        const data = await res.json();

        document.getElementById('metric-total').textContent = data.totalAmbulances;
        document.getElementById('metric-available').textContent = data.availableUnits;
        document.getElementById('metric-dispatched').textContent = data.dispatchedUnits;
        document.getElementById('metric-active-calls').textContent = data.activeEmergencies;
        document.getElementById('metric-resp-time').textContent = data.avgResponseTimeMinutes + 'm';
        document.getElementById('metric-utilization').textContent = data.fleetUtilizationRate + '%';
    } catch (err) {
        console.error('Error loading metrics:', err);
    }
}

// 2. Fetch & Render Alerts / Exceptions (Requirement 5)
async function fetchAlerts() {
    try {
        const res = await fetch('/api/dashboard/alerts');
        if (!res.ok) throw new Error('Failed to fetch alerts');
        const alerts = await res.json();

        const countEl = document.getElementById('alert-count');
        const container = document.getElementById('alerts-list');
        countEl.textContent = alerts.length;

        if (alerts.length === 0) {
            container.innerHTML = '<div style="grid-column: 1/-1; color: #16a34a; font-size: 0.85rem; font-weight: 600;"><i class="fa-solid fa-circle-check"></i> All systems optimal. No unresolved alerts or dispatch bottlenecks.</div>';
            return;
        }

        container.innerHTML = alerts.map(a => `
            <div class="alert-item ${a.severity === 'WARNING' ? 'warning' : ''}">
                <div class="alert-text">
                    <i class="fa-solid ${a.severity === 'WARNING' ? 'fa-triangle-exclamation' : 'fa-bell'}"></i>
                    ${escapeHtml(a.message)}
                </div>
                <button class="alert-resolve-btn" onclick="resolveAlert('${a.id}')">Acknowledge</button>
            </div>
        `).join('');
    } catch (err) {
        console.error('Error loading alerts:', err);
    }
}

async function resolveAlert(alertId) {
    try {
        const res = await fetch(`/api/dashboard/alerts/${alertId}/resolve`, { method: 'POST' });
        if (res.ok) {
            fetchAlerts();
            fetchMetrics();
        }
    } catch (err) {
        alert('Could not resolve alert: ' + err.message);
    }
}

// 3. Fetch & Render Ambulances Table (Requirement 2: Searchable Dashboard)
async function fetchAmbulances() {
    try {
        const res = await fetch('/api/ambulances');
        if (!res.ok) throw new Error('Failed to fetch ambulances');
        currentAmbulances = await res.json();
        renderAmbulances();
    } catch (err) {
        console.error('Error loading ambulances:', err);
    }
}

function renderAmbulances() {
    const tbody = document.getElementById('ambulance-table-body');
    const filtered = currentAmbulances.filter(amb => {
        const matchesStatus = (currentFilter === 'ALL' || amb.status === currentFilter);
        if (!matchesStatus) return false;
        if (!currentSearch) return true;
        const q = currentSearch.toLowerCase();
        return (
            amb.id.toLowerCase().includes(q) ||
            amb.vehicleNumber.toLowerCase().includes(q) ||
            amb.driverName.toLowerCase().includes(q) ||
            amb.currentLocation.toLowerCase().includes(q) ||
            amb.assignedHospital.toLowerCase().includes(q)
        );
    });

    if (filtered.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" style="text-align:center; padding: 2rem; color: #64748b;">No ambulance units matching criteria "${escapeHtml(currentSearch)}" (${currentFilter}).</td></tr>`;
        return;
    }

    tbody.innerHTML = filtered.map(amb => `
        <tr>
            <td><strong>${amb.id}</strong></td>
            <td><span style="font-family: monospace; font-weight:700;">${amb.vehicleNumber}</span></td>
            <td>
                <div style="font-weight:600;">${escapeHtml(amb.driverName)}</div>
                <div style="font-size:0.75rem; color:#64748b;">${amb.driverPhone}</div>
            </td>
            <td>${escapeHtml(amb.currentLocation)}</td>
            <td><span class="status-badge ${amb.status}">${amb.status.replace(/_/g, ' ')}</span></td>
            <td>
                <div class="progress-container">
                    <div style="font-size:0.7rem; display:flex; justify-content:space-between;">
                        <span>O2: ${amb.oxygenPercent}%</span>
                    </div>
                    <div class="mini-bar">
                        <div class="mini-fill ${amb.oxygenPercent < 20 ? 'danger' : amb.oxygenPercent < 50 ? 'warning' : 'healthy'}" style="width: ${amb.oxygenPercent}%;"></div>
                    </div>
                    <div style="font-size:0.7rem; display:flex; justify-content:space-between; margin-top:2px;">
                        <span>Fuel: ${amb.fuelPercent}%</span>
                    </div>
                    <div class="mini-bar">
                        <div class="mini-fill ${amb.fuelPercent < 20 ? 'danger' : amb.fuelPercent < 50 ? 'warning' : 'healthy'}" style="width: ${amb.fuelPercent}%;"></div>
                    </div>
                </div>
            </td>
            <td><small>${escapeHtml(amb.assignedHospital)}</small></td>
            <td>
                <div style="display: flex; gap: 0.35rem;">
                    <button class="btn btn-secondary btn-sm" title="View Journey Drill-Down" onclick="viewTimeline('${amb.id}')">
                        <i class="fa-solid fa-route"></i> Timeline
                    </button>
                    <button class="btn btn-primary btn-sm" title="Change Status" onclick="openStatusModal('${amb.id}', '${amb.status}')">
                        <i class="fa-solid fa-sliders"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function handleSearch() {
    currentSearch = document.getElementById('search-input').value.trim();
    renderAmbulances();
}

function setStatusFilter(status) {
    currentFilter = status;
    document.querySelectorAll('.filter-pills .pill-btn').forEach(btn => {
        btn.classList.toggle('active', btn.getAttribute('data-status') === status);
    });
    renderAmbulances();
}

// 4. Pending Calls List & Dispatch
async function fetchPendingCalls() {
    try {
        const res = await fetch('/api/calls');
        if (!res.ok) return;
        const calls = await res.json();
        const pending = calls.filter(c => c.status === 'PENDING');
        const container = document.getElementById('pending-calls-container');

        if (pending.length === 0) {
            container.innerHTML = '<p style="font-size: 0.8rem; color: #64748b;">No pending emergency requests.</p>';
            return;
        }

        container.innerHTML = pending.map(c => `
            <div class="pending-call-card">
                <div>
                    <div class="pending-call-title">${c.id}: ${escapeHtml(c.emergencyType)} [${c.priority}]</div>
                    <div class="pending-call-sub"><i class="fa-solid fa-location-dot"></i> ${escapeHtml(c.locationAddress)}</div>
                    <div class="pending-call-sub"><i class="fa-solid fa-user"></i> ${escapeHtml(c.callerName)} (${c.callerPhone})</div>
                </div>
                <div>
                    <button class="btn btn-primary btn-sm" onclick="quickAssignAmbulance('${c.id}')">
                        Dispatch
                    </button>
                </div>
            </div>
        `).join('');
    } catch (e) {
        console.error(e);
    }
}

// 5. Data / Event Entry: Handle New Emergency Call (Requirement 1)
async function handleEmergencySubmit(e) {
    e.preventDefault();
    const btn = document.getElementById('submit-call-btn');
    btn.disabled = true;
    btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Dispatching...';

    const payload = {
        callerName: document.getElementById('callerName').value,
        callerPhone: document.getElementById('callerPhone').value,
        emergencyType: document.getElementById('emergencyType').value,
        priority: document.getElementById('priority').value,
        locationAddress: document.getElementById('locationAddress').value,
        notes: document.getElementById('notes').value
    };

    try {
        const res = await fetch('/api/calls', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            const created = await res.json();
            showNotification(`Call ${created.id} registered successfully! Finding available unit...`, 'success');
            document.getElementById('quick-dispatch-form').reset();
            
            // Auto-assign first available ambulance
            const availableUnit = currentAmbulances.find(a => a.status === 'AVAILABLE');
            if (availableUnit) {
                await fetch(`/api/calls/${created.id}/dispatch`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ ambulanceId: availableUnit.id })
                });
                showNotification(`Unit ${availableUnit.id} (${availableUnit.vehicleNumber}) has been immediately dispatched!`, 'success');
            }
            loadAllData();
        } else {
            showNotification('Failed to register emergency call.', 'error');
        }
    } catch (err) {
        showNotification('Error: ' + err.message, 'error');
    } finally {
        btn.disabled = false;
        btn.innerHTML = '<i class="fa-solid fa-paper-plane"></i> Register & Dispatch Call';
    }
}

async function quickAssignAmbulance(callId) {
    const availableUnit = currentAmbulances.find(a => a.status === 'AVAILABLE');
    if (!availableUnit) {
        showNotification('All ambulances are currently busy or out of service!', 'error');
        return;
    }

    try {
        const res = await fetch(`/api/calls/${callId}/dispatch`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ ambulanceId: availableUnit.id })
        });
        if (res.ok) {
            showNotification(`Ambulance ${availableUnit.id} assigned to call ${callId}!`, 'success');
            loadAllData();
        }
    } catch (e) {
        showNotification('Error assigning ambulance: ' + e.message, 'error');
    }
}

// 6. Status Drill-down Timeline Modal (Requirement 4)
async function viewTimeline(ambulanceId) {
    const amb = currentAmbulances.find(a => a.id === ambulanceId);
    if (!amb) return;

    const summaryEl = document.getElementById('drilldown-unit-summary');
    summaryEl.innerHTML = `
        <div><strong>Ambulance ID:</strong> ${amb.id}</div>
        <div><strong>Reg Number:</strong> ${amb.vehicleNumber}</div>
        <div><strong>Driver:</strong> ${escapeHtml(amb.driverName)}</div>
        <div><strong>Current Status:</strong> <span class="status-badge ${amb.status}">${amb.status}</span></div>
        <div><strong>Assigned Depot:</strong> ${escapeHtml(amb.currentLocation)}</div>
        <div><strong>Hospital Target:</strong> ${escapeHtml(amb.assignedHospital)}</div>
    `;

    try {
        const res = await fetch(`/api/ambulances/${ambulanceId}/events`);
        const events = await res.json();
        const timelineContainer = document.getElementById('timeline-events');

        if (events.length === 0) {
            timelineContainer.innerHTML = '<p style="color: #64748b; font-size: 0.85rem;">No historical journey dispatch logs recorded for this unit yet.</p>';
        } else {
            timelineContainer.innerHTML = events.map(evt => `
                <div class="timeline-item">
                    <span class="timeline-dot"></span>
                    <div class="timeline-time">${new Date(evt.timestamp).toLocaleString()}</div>
                    <div class="timeline-event-title">${evt.eventType.replace(/_/g, ' ')} ${evt.callId ? `(Ref: ${evt.callId})` : ''}</div>
                    <div class="timeline-remarks">${escapeHtml(evt.remarks || 'Standard automated dispatch update')}</div>
                </div>
            `).join('');
        }

        openModal('drilldown-modal');
    } catch (err) {
        alert('Could not retrieve timeline: ' + err.message);
    }
}

// 7. Status Update Modal
function openStatusModal(ambulanceId, currentStatus) {
    document.getElementById('status-amb-id').value = ambulanceId;
    document.getElementById('new-status-select').value = currentStatus;
    openModal('status-modal');
}

async function confirmStatusUpdate() {
    const ambId = document.getElementById('status-amb-id').value;
    const newStatus = document.getElementById('new-status-select').value;

    try {
        const res = await fetch(`/api/ambulances/${ambId}/status`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: newStatus })
        });
        if (res.ok) {
            closeModal('status-modal');
            loadAllData();
        } else {
            alert('Failed to update status.');
        }
    } catch (err) {
        alert('Error: ' + err.message);
    }
}

// Modal helper utilities
function openModal(modalId) {
    const m = document.getElementById(modalId);
    if (m) m.classList.remove('hidden');
}

function closeModal(modalId) {
    const m = document.getElementById(modalId);
    if (m) m.classList.add('hidden');
}

function escapeHtml(text) {
    if (!text) return '';
    return text.replace(/[&<>"']/g, function(m) {
        return {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        }[m];
    });
}
