const API_BASE = '/api';

// Fetch and load all invoices
async function loadInvoices() {
    try {
        const invoices = await API.getInvoices();
        renderTable(invoices);
        calculateKPIs(invoices);
    } catch (err) {
        document.getElementById('invoiceTableBody').innerHTML =
            `<tr><td colspan="7" style="text-align: center; color: red;">Error: ${err.message}. Please login.</td></tr>`;
    }
}

function renderTable(invoices) {
    const tbody = document.getElementById('invoiceTableBody');
    if (!invoices || invoices.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align: center;">No invoices found.</td></tr>`;
        return;
    }

    tbody.innerHTML = invoices.map(inv => `
    <tr>
      <td><strong>#INV-${inv.id}</strong></td>
      <td>Booking #${inv.bookingId || inv.booking?.id || 'N/A'}</td>
      <td>LKR ${Number(inv.subtotal || 0).toLocaleString()}</td>
      <td>LKR ${Number(inv.tax || 0).toLocaleString()}</td>
      <td><strong>LKR ${Number(inv.totalAmount || 0).toLocaleString()}</strong></td>
      <td><span class="badge badge-${(inv.status || 'UNPAID').toLowerCase()}">${inv.status || 'UNPAID'}</span></td>
      <td style="display:flex; gap:10px;">
        <button class="btn btn-outline btn-sm" onclick="window.location.href='invoice-detail.html?id=${inv.id}'">View</button>
        ${inv.status !== 'PAID' ?
            `<button class="btn btn-primary btn-sm" onclick="openPaymentModal(${inv.id}, ${inv.totalAmount})">Pay</button>` :
            `<span style="color: green; font-size: 0.85rem; align-self:center;">Settled</span>`
        }
        ${inv.amountPaid > 0 ? `<button class="btn btn-outline btn-sm" style="border-color:#fb7185; color:#fb7185;" onclick="window.location.href='invoice-detail.html?id=${inv.id}'">Refund</button>` : ''}
      </td>
    </tr>
  `).join('');
}

function printInvoice(inv) {
    const printWindow = window.open('', '_blank');
    printWindow.document.write(`
        <html>
        <head>
            <title>Invoice #INV-${inv.id}</title>
            <style>
                body { font-family: Arial, sans-serif; padding: 40px; color: #333; }
                .header { text-align: center; margin-bottom: 40px; }
                .invoice-details { margin-bottom: 30px; }
                table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
                th { background-color: #f4f4f4; }
                .total { text-align: right; font-weight: bold; font-size: 1.2em; margin-top: 20px; }
            </style>
        </head>
        <body>
            <div class="header">
                <h2>JCB Fleet Management</h2>
                <h1>INVOICE</h1>
            </div>
            <div class="invoice-details">
                <p><strong>Invoice Number:</strong> #INV-${inv.id}</p>
                <p><strong>Date:</strong> ${new Date().toLocaleDateString()}</p>
                <p><strong>Status:</strong> ${inv.status}</p>
                <p><strong>Booking Reference:</strong> #${inv.bookingId || inv.booking?.id || 'N/A'}</p>
            </div>
            <table>
                <tr>
                    <th>Description</th>
                    <th>Amount (LKR)</th>
                </tr>
                <tr>
                    <td>Subtotal for Booking</td>
                    <td>${Number(inv.subtotal || 0).toLocaleString()}</td>
                </tr>
                <tr>
                    <td>Tax</td>
                    <td>${Number(inv.tax || 0).toLocaleString()}</td>
                </tr>
            </table>
            <div class="total">
                <p>Total Amount: LKR ${Number(inv.totalAmount || 0).toLocaleString()}</p>
            </div>
        </body>
        </html>
    `);
    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
}

// Update KPI stats dynamically
function calculateKPIs(invoices) {
    let total = 0, paid = 0, unpaid = 0;
    if (invoices) {
        invoices.forEach(inv => {
            const amount = Number(inv.totalAmount || 0);
            total += amount;
            if (inv.status === 'PAID') paid += amount;
            else unpaid += amount;
        });
    }

    document.getElementById('kpiTotalInvoiced').innerText = `LKR ${total.toLocaleString()}`;
    document.getElementById('kpiTotalPaid').innerText = `LKR ${paid.toLocaleString()}`;
    document.getElementById('kpiTotalUnpaid').innerText = `LKR ${unpaid.toLocaleString()}`;
}

async function loadBookingsForDropdown() {
    const select = document.getElementById('invoiceBookingId');
    try {
        const bookings = await API.getAllBookings();
        if (bookings && bookings.length > 0) {
            bookings.forEach(b => {
                const option = document.createElement('option');
                option.value = b.id;
                option.textContent = `#B-${b.id} - ${b.vehicleName} (${b.username})`;
                select.appendChild(option);
            });
        } else {
            select.innerHTML = '<option value="" disabled selected>No bookings available</option>';
        }
    } catch (err) {
        console.error('Failed to load bookings for dropdown', err);
    }
}

// Invoice Modal handlers
function openInvoiceModal() { 
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('invoiceDueDate').setAttribute('min', today);
    document.getElementById('invoiceModal').classList.add('active'); 
}
function closeInvoiceModal() {
    document.getElementById('invoiceModal').classList.remove('active');
    document.getElementById('invoiceForm').reset();
}

// Payment Modal handlers
function openPaymentModal(invoiceId, defaultAmount) {
    document.getElementById('payInvoiceId').value = invoiceId;
    document.getElementById('payAmount').value = defaultAmount;
    document.getElementById('paymentModal').classList.add('active');
}
function closePaymentModal() {
    document.getElementById('paymentModal').classList.remove('active');
    document.getElementById('paymentForm').reset();
}

// Submit Create Invoice
document.getElementById('invoiceForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        bookingId: Number(document.getElementById('invoiceBookingId').value),
        dueDate: document.getElementById('invoiceDueDate').value,
        subtotal: parseFloat(document.getElementById('invoiceSubtotal').value),
        tax: parseFloat(document.getElementById('invoiceTax').value),
        userId: API.getUser()?.userId || 1
    };

    try {
        await API.createInvoice(payload);
        closeInvoiceModal();
        loadInvoices();
    } catch (err) {
        alert(err.message);
    }
});

// Submit Payment
document.getElementById('paymentForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        invoiceId: Number(document.getElementById('payInvoiceId').value),
        amount: parseFloat(document.getElementById('payAmount').value),
        method: document.getElementById('payMethod').value
    };

    try {
        await API.recordPayment(payload);
        closePaymentModal();
        loadInvoices();
    } catch (err) {
        alert(err.message);
    }
});

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    requireAuth();
    wireSidebar();
    loadInvoices();
    loadBookingsForDropdown();
});