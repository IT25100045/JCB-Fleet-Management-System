/* home.js — cross-module dashboard snapshot */

requireAuth();
wireSidebar();

async function loadDashboard() {
    const alertBox = document.getElementById('alertBox');
    try {
        const [vehicles, bookings, drivers, invoices, incidents] = await Promise.all([
            API.getVehicles(),
            API.getAllBookings(),
            API.getDrivers(),
            API.getInvoices(),
            API.getIncidents()
        ]);

        document.getElementById('statVehicles').textContent = vehicles.length;
        document.getElementById('statAvailable').textContent = vehicles.filter(v => v.status === 'AVAILABLE').length;
        document.getElementById('statMaintenance').textContent = vehicles.filter(v => v.status === 'IN_MAINTENANCE').length;

        document.getElementById('statBookings').textContent =
            bookings.filter(b => b.status === 'PENDING' || b.status === 'CONFIRMED').length;
        document.getElementById('statDrivers').textContent = drivers.length;

        const totalBilled = invoices.reduce((s, i) => s + Number(i.totalAmount || 0), 0);
        const collected = invoices.reduce((s, i) => s + Number(i.amountPaid || 0), 0);
        document.getElementById('statOutstanding').textContent = fmt.money(totalBilled - collected);
        document.getElementById('statOverdue').textContent = invoices.filter(i => i.status === 'OVERDUE').length;

        document.getElementById('statIncidents').textContent =
            incidents.filter(i => i.status === 'REPORTED' || i.status === 'UNDER_REVIEW').length;

    } catch (err) {
        alertBox.innerHTML = `<div class="alert alert-error">${err.message}</div>`;
    }
}

loadDashboard();
