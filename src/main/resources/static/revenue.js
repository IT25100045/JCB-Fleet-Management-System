/* revenue.js — client-side aggregation of invoices + payments
   into a revenue report (the backend has no dedicated reporting
   endpoint, so we derive everything from /api/invoices and
   /api/payments here). */

requireAuth();
wireSidebar();

const COLORS = {
    yellow: '#FFC72C',
    slate: '#1F242C',
    green: '#1E8E5A',
    red: '#C0392B',
    amber: '#B4780D',
    grey: '#B7BEC7'
};

async function loadReport() {
    const alertBox = document.getElementById('alertBox');
    try {
        const [invoices, payments] = await Promise.all([API.getInvoices(), API.getPayments()]);
        renderStats(invoices);
        renderMonthChart(payments);
        renderStatusChart(invoices);
        renderMethodChart(payments);
        renderTopCustomers(invoices);
    } catch (err) {
        alertBox.innerHTML = `<div class="alert alert-error">${err.message}</div>`;
    }
}

function renderStats(invoices) {
    const collected = invoices.reduce((s, i) => s + Number(i.amountPaid || 0), 0);
    const totalBilled = invoices.reduce((s, i) => s + Number(i.totalAmount || 0), 0);
    const outstanding = totalBilled - collected;
    const overdueCount = invoices.filter(i => i.status === 'OVERDUE').length;
    const avg = invoices.length ? totalBilled / invoices.length : 0;

    document.getElementById('statCollected').textContent = fmt.money(collected);
    document.getElementById('statOutstanding').textContent = fmt.money(outstanding);
    document.getElementById('statOverdue').textContent = overdueCount;
    document.getElementById('statAvg').textContent = fmt.money(avg);
}

function renderMonthChart(payments) {
    const byMonth = {};
    payments.forEach(p => {
        if (!p.paidAt) return;
        const d = new Date(p.paidAt);
        const key = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`;
        byMonth[key] = (byMonth[key] || 0) + Number(p.amount || 0);
    });
    const keys = Object.keys(byMonth).sort();
    const labels = keys.map(k => {
        const [y, m] = k.split('-');
        return new Date(y, m - 1, 1).toLocaleDateString('en-GB', { month: 'short', year: '2-digit' });
    });

    new Chart(document.getElementById('monthChart'), {
        type: 'bar',
        data: {
            labels: labels.length ? labels : ['No data'],
            datasets: [{
                label: 'Revenue collected',
                data: keys.length ? keys.map(k => byMonth[k]) : [0],
                backgroundColor: COLORS.yellow,
                borderRadius: 4,
                maxBarThickness: 46
            }]
        },
        options: {
            plugins: { legend: { display: false } },
            scales: {
                y: { beginAtZero: true, ticks: { callback: v => 'Rs. ' + v.toLocaleString() } }
            }
        }
    });
}

function renderStatusChart(invoices) {
    const statuses = ['UNPAID', 'PARTIALLY_PAID', 'PAID', 'OVERDUE', 'CANCELLED'];
    const counts = statuses.map(s => invoices.filter(i => i.status === s).length);
    const colors = [COLORS.red, COLORS.amber, COLORS.green, '#7A2E24', COLORS.grey];

    new Chart(document.getElementById('statusChart'), {
        type: 'doughnut',
        data: {
            labels: statuses.map(s => s.replace('_', ' ')),
            datasets: [{ data: counts, backgroundColor: colors, borderWidth: 0 }]
        },
        options: { plugins: { legend: { position: 'bottom', labels: { boxWidth: 12, font: { size: 12 } } } } }
    });
}

function renderMethodChart(payments) {
    const methods = ['CASH', 'CARD', 'BANK_TRANSFER', 'ONLINE'];
    const totals = methods.map(m => payments.filter(p => p.method === m).reduce((s, p) => s + Number(p.amount || 0), 0));

    new Chart(document.getElementById('methodChart'), {
        type: 'pie',
        data: {
            labels: methods.map(m => m.replace('_', ' ')),
            datasets: [{ data: totals, backgroundColor: [COLORS.slate, COLORS.yellow, COLORS.green, COLORS.amber], borderWidth: 0 }]
        },
        options: { plugins: { legend: { position: 'bottom', labels: { boxWidth: 12, font: { size: 12 } } } } }
    });
}

function renderTopCustomers(invoices) {
    const byCustomer = {};
    invoices.forEach(i => {
        const name = i.username || `User #${i.userId}`;
        byCustomer[name] = (byCustomer[name] || 0) + Number(i.totalAmount || 0);
    });
    const rows = Object.entries(byCustomer).sort((a, b) => b[1] - a[1]).slice(0, 8);
    const wrap = document.getElementById('topCustomersWrap');

    if (rows.length === 0) {
        wrap.innerHTML = `<div class="empty-state"><div class="empty-icon">👤</div><div>No invoice data yet.</div></div>`;
        return;
    }

    wrap.innerHTML = `
    <table class="data">
      <thead><tr><th>Customer</th><th>Total billed</th></tr></thead>
      <tbody>
        ${rows.map(([name, total]) => `<tr><td>${name}</td><td>${fmt.money(total)}</td></tr>`).join('')}
      </tbody>
    </table>`;
}

loadReport();
