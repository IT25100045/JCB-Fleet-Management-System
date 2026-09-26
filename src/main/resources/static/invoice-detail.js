/* invoice-detail.js — single invoice view + payment recording */

requireAuth();
wireSidebar();

const params = new URLSearchParams(location.search);
const invoiceId = params.get('id');

const loadingMsg = document.getElementById('loadingMsg');
const content = document.getElementById('content');
const alertBox = document.getElementById('alertBox');

let currentInvoice = null;

function showError(msg) {
    alertBox.innerHTML = `<div class="alert alert-error">${msg}</div>`;
}

async function loadInvoice() {
    if (!invoiceId) {
        loadingMsg.textContent = '';
        showError('No invoice specified.');
        return;
    }
    try {
        currentInvoice = await API.getInvoice(invoiceId);
        renderInvoice(currentInvoice);
        loadingMsg.style.display = 'none';
        content.style.display = 'block';
        loadPayments();
    } catch (err) {
        loadingMsg.textContent = '';
        showError(err.message);
    }
}

function renderInvoice(inv) {
    document.title = "Invoice " + inv.invoiceNumber + " - JCB Fleet";
    document.getElementById('invNumber').textContent = inv.invoiceNumber;
    document.getElementById('invMeta').textContent = `Created ${fmt.datetime(inv.createdAt)}`;

    const badge = document.getElementById('invStatusBadge');
    badge.textContent = fmt.statusLabel(inv.status);
    badge.className = `badge badge-${inv.status}`;

    document.getElementById('invCustomer').textContent = inv.username || '—';
    document.getElementById('invUserId').textContent = `User ID: ${inv.userId}`;
    document.getElementById('invBooking').textContent = `Booking #${inv.bookingId}`;
    document.getElementById('invIssueDate').textContent = fmt.date(inv.issueDate);
    document.getElementById('invDueDate').textContent = fmt.date(inv.dueDate);

    document.getElementById('rowSubtotal').textContent = fmt.money(inv.subtotal);
    document.getElementById('rowTax').textContent = fmt.money(inv.tax);
    document.getElementById('rowTotal').textContent = fmt.money(inv.totalAmount);
    document.getElementById('rowPaid').textContent = fmt.money(inv.amountPaid);

    const balance = Number(inv.totalAmount || 0) - Number(inv.amountPaid || 0);
    document.getElementById('rowBalance').textContent = fmt.money(balance);

    const recordBtn = document.getElementById('recordPaymentBtn');
    if (inv.status === 'PAID' || inv.status === 'CANCELLED' || balance <= 0) {
        recordBtn.disabled = true;
        recordBtn.textContent = inv.status === 'PAID' ? 'Fully paid' : 'No balance due';
    }
}

async function loadPayments() {
    const wrap = document.getElementById('paymentsWrap');
    try {
        const payments = await API.getPaymentsByInvoice(invoiceId);
        if (payments.length === 0) {
            wrap.innerHTML = `<div class="empty-state"><div class="empty-icon">💳</div><div>No payments recorded yet.</div></div>`;
            return;
        }
        wrap.innerHTML = `
      <table class="data">
        <thead><tr><th>Date</th><th>Amount</th><th>Method</th><th>Reference</th><th>Actions</th></tr></thead>
        <tbody>
          ${payments.map(p => `
            <tr>
              <td>${fmt.datetime(p.paidAt)}</td>
              <td>${fmt.money(p.amount)}</td>
              <td>${fmt.statusLabel(p.method)}</td>
              <td>${p.reference || '—'}</td>
              <td><button class="btn-outline" style="color: #fb7185; border-color: #fb7185; font-size: 12px; padding: 4px 8px;" onclick="refundPayment(${p.id})">Refund</button></td>
            </tr>
          `).join('')}
        </tbody>
      </table>`;
    } catch (err) {
        wrap.innerHTML = `<div class="alert alert-error">${err.message}</div>`;
    }
}

window.refundPayment = async function(paymentId) {
    if (!confirm('Are you sure you want to refund/delete this payment? This will restore the invoice balance.')) return;
    try {
        await API.refundPayment(paymentId);
        await loadInvoice(); // reload everything
    } catch (err) {
        showError(err.message);
    }
};

/* ---------- Record payment modal ---------- */
const paymentModal = document.getElementById('paymentModal');
const recordPaymentBtn = document.getElementById('recordPaymentBtn');
const closePaymentModalBtn = document.getElementById('closePaymentModalBtn');
const paymentForm = document.getElementById('paymentForm');
const paymentModalAlert = document.getElementById('paymentModalAlert');
const paymentAmount = document.getElementById('paymentAmount');
const paymentSubmitBtn = document.getElementById('paymentSubmitBtn');

function openPaymentModal() {
    if (!currentInvoice) return;
    const balance = Number(currentInvoice.totalAmount || 0) - Number(currentInvoice.amountPaid || 0);
    document.getElementById('remainingBalanceDisplay').textContent = fmt.money(balance);
    paymentAmount.max = balance.toFixed(2);
    paymentAmount.value = balance.toFixed(2);
    paymentModal.classList.add('active');
}
function closePaymentModal() {
    paymentModal.classList.remove('active');
    paymentForm.reset();
    paymentModalAlert.innerHTML = '';
}

recordPaymentBtn.addEventListener('click', openPaymentModal);
closePaymentModalBtn.addEventListener('click', closePaymentModal);
paymentModal.addEventListener('click', (e) => { if (e.target === paymentModal) closePaymentModal(); });

paymentForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    paymentModalAlert.innerHTML = '';

    const payload = {
        invoiceId: Number(invoiceId),
        amount: Number(paymentAmount.value),
        method: document.getElementById('paymentMethod').value,
        reference: document.getElementById('paymentReference').value.trim() || null
    };

    paymentSubmitBtn.disabled = true;
    paymentSubmitBtn.textContent = 'Recording…';

    try {
        await API.recordPayment(payload);
        closePaymentModal();
        await loadInvoice();
    } catch (err) {
        paymentModalAlert.innerHTML = `<div class="alert alert-error">${err.message}</div>`;
    } finally {
        paymentSubmitBtn.disabled = false;
        paymentSubmitBtn.textContent = 'Record payment';
    }
});

/* ---------- Print ---------- */
document.getElementById('printBtn').addEventListener('click', () => window.print());

loadInvoice();
