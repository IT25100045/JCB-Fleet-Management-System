document.addEventListener('DOMContentLoaded', () => {
    requireRole(['CUSTOMER']);
    loadMyBookings();
});

async function loadMyBookings() {
    const tbody = document.getElementById('bookingsBody');
    const errorMsg = document.getElementById('errorMsg');

    try {
        const bookings = await API.getMyBookings();

        if (!bookings || bookings.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align:center; color:var(--text-secondary);">You have no bookings yet.</td></tr>';
            return;
        }

        tbody.innerHTML = bookings.map(b => `
            <tr>
                <td>#${b.id}</td>
                <td><strong>${b.vehicleName}</strong></td>
                <td>${fmt.date(b.startDate)}</td>
                <td>${fmt.date(b.endDate)}</td>
                <td><span class="badge badge-${getBadgeType(b.status)}">${fmt.statusLabel(b.status)}</span></td>
                <td>${fmt.money(b.totalPrice)}</td>
                <td>
                    ${b.status === 'PENDING' ? `<button class="btn-outline" style="padding:4px 8px; font-size:12px; color:var(--danger); border-color:var(--danger);" onclick="cancelBooking(${b.id})">Cancel</button>` : ''}
                    ${b.status === 'COMPLETED' ? `<button class="btn-primary" style="padding:4px 8px; font-size:12px;" onclick="openFeedbackModal(${b.id})">Leave Feedback</button>` : ''}
                </td>
            </tr>
        `).join('');
    } catch (err) {
        errorMsg.textContent = 'Failed to load bookings: ' + err.message;
        errorMsg.style.display = 'block';
    }
}

function getBadgeType(status) {
    if (status === 'COMPLETED') return 'success';
    if (status === 'IN_PROGRESS' || status === 'CONFIRMED') return 'info';
    if (status === 'CANCELLED') return 'danger';
    return 'warning';
}

async function cancelBooking(id) {
    if (!confirm('Are you sure you want to cancel this booking?')) return;
    
    try {
        await API.cancelBooking(id);
        alert('Booking cancelled successfully.');
        loadMyBookings();
    } catch (err) {
        alert(err.message);
    }
}

// Feedback Modal Logic
function openFeedbackModal(bookingId) {
    document.getElementById('feedbackBookingId').value = bookingId;
    document.getElementById('rating').value = 5;
    document.getElementById('comments').value = '';
    document.getElementById('modalError').style.display = 'none';
    
    document.getElementById('feedbackModal').classList.add('active');
}

function closeFeedbackModal() {
    document.getElementById('feedbackModal').classList.remove('active');
}

document.getElementById('feedbackForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const btn = document.getElementById('submitFeedbackBtn');
    const errorBox = document.getElementById('modalError');
    
    const payload = {
        bookingId: document.getElementById('feedbackBookingId').value,
        rating: document.getElementById('rating').value,
        comments: document.getElementById('comments').value
    };

    try {
        btn.disabled = true;
        btn.textContent = 'Submitting...';
        errorBox.style.display = 'none';
        
        await API.createFeedback(payload);
        
        alert('Thank you for your feedback!');
        closeFeedbackModal();
        
    } catch (err) {
        errorBox.textContent = err.message;
        errorBox.style.display = 'block';
    } finally {
        btn.disabled = false;
        btn.textContent = 'Submit';
    }
});
