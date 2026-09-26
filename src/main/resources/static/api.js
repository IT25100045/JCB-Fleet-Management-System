/* ============================================================
   api.js — shared API client for the JCB Fleet System frontend.
   Talks to the Spring Boot backend (JWT auth, REST/JSON).
   Loaded on every page before the page's own script.
   ============================================================ */

const API = (() => {
    // Same-origin by default (this frontend is served from Spring
    // Boot's /static folder on :8080). Override if you host the
    // frontend separately.
    // api.js

    const isLocalServer = window.location.port === '63342' || window.location.port === '63343' || window.location.port === '5500' || window.location.protocol === 'file:';
    const BASE_URL = (window.location.origin.startsWith('http') && !isLocalServer)
        ? window.location.origin
        : 'http://localhost:8082';

    const TOKEN_KEY = 'jcb_token';
    const USER_KEY = 'jcb_user';

    function getToken() {
        return localStorage.getItem(TOKEN_KEY);
    }

    function getUser() {
        try {
            return JSON.parse(localStorage.getItem(USER_KEY) || 'null');
        } catch (e) {
            return null;
        }
    }

    function setSession(authResponse) {
        localStorage.setItem(TOKEN_KEY, authResponse.token);
        localStorage.setItem(USER_KEY, JSON.stringify({
            userId: authResponse.userId,
            username: authResponse.username,
            role: authResponse.role
        }));
    }

    function clearSession() {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
    }

    function isLoggedIn() {
        return !!getToken();
    }

    /**
     * Core request helper. Throws an Error with a readable message
     * on non-2xx responses (using the backend's {message} field
     * when present). Redirects to login on 401.
     */
    async function request(path, { method = 'GET', body, auth = true } = {}) {
        const headers = { 'Content-Type': 'application/json' };
        if (auth) {
            const token = getToken();
            if (token) headers['Authorization'] = `Bearer ${token}`;
        }

        let res;
        try {
            res = await fetch(`${BASE_URL}${path}`, {
                method,
                headers,
                body: body !== undefined ? JSON.stringify(body) : undefined
            });
        } catch (networkErr) {
            throw new Error('Could not reach the server. Is the backend running on ' + BASE_URL + '?');
        }

        if (res.status === 401) {
            clearSession();
            if (!location.pathname.endsWith('login.html')) {
                location.href = 'login.html';
            }
            throw new Error('Session expired. Please log in again.');
        }

        if (res.status === 204) return null;

        let data = null;
        const text = await res.text();
        if (text) {
            try { data = JSON.parse(text); } catch (e) { data = text; }
        }

        if (!res.ok) {
            const msg = (data && data.errors && Object.keys(data.errors).length > 0) 
                ? Object.values(data.errors).join(', ')
                : (data && data.message ? data.message : `Request failed (${res.status})`);
            throw new Error(msg);
        }

        return data;
    }

    return {
        BASE_URL,
        getToken, getUser, setSession, clearSession, isLoggedIn,

        // Auth
        login: (username, password) => request('/api/auth/login', { method: 'POST', body: { username, password }, auth: false }),
        register: (payload) => request('/api/auth/register', { method: 'POST', body: payload, auth: false }),

        // Invoices
        getInvoices: () => request('/api/invoices'),
        getInvoicesByUser: (userId) => request(`/api/invoices/user/${userId}`),
        getInvoice: (id) => request(`/api/invoices/${id}`),
        createInvoice: (payload) => request('/api/invoices', { method: 'POST', body: payload }),
        updateInvoice: (id, payload) => request(`/api/invoices/${id}`, { method: 'PUT', body: payload }),
        deleteInvoice: (id) => request(`/api/invoices/${id}`, { method: 'DELETE' }),

        // Payments
        getPayments: () => request('/api/payments'),
        getPaymentsByInvoice: (invoiceId) => request(`/api/payments/invoice/${invoiceId}`),
        recordPayment: (payload) => request('/api/payments', { method: 'POST', body: payload }),
        refundPayment: (id) => request(`/api/payments/${id}/refund`, { method: 'DELETE' }),

        // Bookings
        getAllBookings: () => request('/api/bookings'),
        getMyBookings: () => request('/api/bookings/my'),
        getBooking: (id) => request(`/api/bookings/${id}`),
        createBooking: (payload) => request('/api/bookings', { method: 'POST', body: payload }),
        updateBooking: (id, payload) => request(`/api/bookings/${id}`, { method: 'PUT', body: payload }),
        deleteBooking: (id) => request(`/api/bookings/${id}`, { method: 'DELETE' }),
        updateBookingStatus: (id, status) => request(`/api/bookings/${id}/status?status=${status}`, { method: 'PUT' }),
        cancelBooking: (id) => request(`/api/bookings/${id}/cancel`, { method: 'PUT' }),

        // Fleet / vehicles
        getVehicles: (availableOnly = false) => request(`/api/fleet?availableOnly=${availableOnly}`),
        getVehicle: (id) => request(`/api/fleet/${id}`),
        createVehicle: (payload) => request('/api/fleet', { method: 'POST', body: payload }),
        updateVehicle: (id, payload) => request(`/api/fleet/${id}`, { method: 'PUT', body: payload }),
        updateVehicleStatus: (id, status) => request(`/api/fleet/${id}/status?status=${status}`, { method: 'PUT' }),
        deleteVehicle: (id) => request(`/api/fleet/${id}`, { method: 'DELETE' }),

        // Drivers
        getDrivers: (availableOnly = false) => request(`/api/drivers?availableOnly=${availableOnly}`),
        getDriver: (id) => request(`/api/drivers/${id}`),
        createDriver: (payload) => request('/api/drivers', { method: 'POST', body: payload }),
        updateDriver: (id, payload) => request(`/api/drivers/${id}`, { method: 'PUT', body: payload }),
        deleteDriver: (id) => request(`/api/drivers/${id}`, { method: 'DELETE' }),

        // Shifts
        getShifts: () => request('/api/shifts'),
        getShiftsByDriver: (driverId) => request(`/api/shifts/driver/${driverId}`),
        assignShift: (payload) => request('/api/shifts', { method: 'POST', body: payload }),
        updateShiftStatus: (id, status) => request(`/api/shifts/${id}/status?status=${status}`, { method: 'PUT' }),

        // Maintenance
        getMaintenance: () => request('/api/maintenance'),
        getMaintenanceByVehicle: (vehicleId) => request(`/api/maintenance/vehicle/${vehicleId}`),
        createMaintenance: (payload) => request('/api/maintenance', { method: 'POST', body: payload }),
        updateMaintenanceStatus: (id, status) => request(`/api/maintenance/${id}/status?status=${status}`, { method: 'PUT' }),

        // Fuel
        getFuel: () => request('/api/fuel'),
        getFuelByVehicle: (vehicleId) => request(`/api/fuel/vehicle/${vehicleId}`),
        getFuelById: (id) => request(`/api/fuel/${id}`),
        createFuel: (payload) => request('/api/fuel', { method: 'POST', body: payload }),
        updateFuel: (id, payload) => request(`/api/fuel/${id}`, { method: 'PUT', body: payload }),
        deleteFuel: (id) => request(`/api/fuel/${id}`, { method: 'DELETE' }),

        // Incidents
        getIncidents: () => request('/api/incidents'),
        getIncidentsByVehicle: (vehicleId) => request(`/api/incidents/vehicle/${vehicleId}`),
        createIncident: (payload) => request('/api/incidents', { method: 'POST', body: payload }),
        updateIncidentStatus: (id, status) => request(`/api/incidents/${id}/status?status=${status}`, { method: 'PUT' }),

        // Feedback
        getFeedback: () => request('/api/feedback'),
        getFeedbackByBooking: (bookingId) => request(`/api/feedback/booking/${bookingId}`),
        createFeedback: (payload) => request('/api/feedback', { method: 'POST', body: payload })
    };
})();

/* ---------- Formatting helpers ---------- */
const fmt = {
    money(n) {
        const v = Number(n || 0);
        return 'Rs. ' + v.toLocaleString('en-LK', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
    date(d) {
        if (!d) return '—';
        const dt = new Date(d);
        if (isNaN(dt)) return d;
        return dt.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
    },
    datetime(d) {
        if (!d) return '—';
        const dt = new Date(d);
        if (isNaN(dt)) return d;
        return dt.toLocaleString('en-GB', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' });
    },
    statusLabel(s) {
        return String(s || '').replace('_', ' ');
    }
};

function requireAuth() {
    if (!API.isLoggedIn()) {
        location.href = 'login.html';
    }
}

function requireRole(allowedRoles) {
    if (!API.isLoggedIn()) {
        location.href = 'login.html';
        return;
    }
    const user = API.getUser();
    if (user && !allowedRoles.includes(user.role)) {
        if (user.role === 'ADMIN') location.href = 'home.html';
        else location.href = 'customer-home.html';
    }
}

/* ---------- Sidebar user info + logout wiring (called on load) ---------- */
function wireSidebar() {
    const user = API.getUser();
    const nameEl = document.getElementById('sidebarUserName');
    const roleEl = document.getElementById('sidebarUserRole');
    if (user && nameEl) nameEl.textContent = user.username;
    if (user && roleEl) roleEl.textContent = user.role;

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            API.clearSession();
            location.href = 'login.html';
        });
    }
}
