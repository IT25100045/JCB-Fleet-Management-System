document.addEventListener('DOMContentLoaded', () => {
    requireAuth();
    loadBookings();
    loadVehiclesForDropdown();
    
    document.getElementById('bookingForm').addEventListener('submit', handleCreateBooking);
});

async function loadBookings() {
    const tbody = document.getElementById('bookingTableBody');
    try {
        const bookings = await API.getAllBookings();
        
        let pendingCount = 0;
        let confirmedCount = 0;
        
        if (!bookings || bookings.length === 0) {
            tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: #94a3b8;">No bookings found.</td></tr>`;
            return;
        }

        tbody.innerHTML = bookings.map(b => {
            if (b.status === 'PENDING') pendingCount++;
            if (b.status === 'CONFIRMED') confirmedCount++;
            
            const badgeClass = b.status === 'PENDING' ? 'badge-pending' : 
                               (b.status === 'CONFIRMED' ? 'badge-confirmed' : 
                               (b.status === 'COMPLETED' ? 'badge-completed' : 'badge-cancelled'));

            return `
                <tr>
                    <td>#BK-${b.id}</td>
                    <td>${b.userId || '—'}</td>
                    <td>${b.vehicleId || '—'}</td>
                    <td>${fmt.date(b.startDate)}</td>
                    <td>${fmt.date(b.endDate)}</td>
                    <td><span class="badge ${badgeClass}">${fmt.statusLabel(b.status)}</span></td>
                    <td>
                        ${b.status === 'PENDING' ? `
                            <button class="btn-sm btn-primary" style="margin-right: 5px;" onclick="updateStatus(${b.id}, 'CONFIRMED')">Approve</button>
                            <button class="btn-sm btn-danger" onclick="cancelBooking(${b.id})">Cancel</button>
                        ` : ''}
                    </td>
                </tr>
            `;
        }).join('');

        document.getElementById('kpiPending').textContent = pendingCount;
        document.getElementById('kpiConfirmed').textContent = confirmedCount;

    } catch (err) {
        console.error(err);
        tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: #fb7185;">Error loading bookings: ${err.message}</td></tr>`;
    }
}

async function handleCreateBooking(e) {
    e.preventDefault();
    const payload = {
        vehicleId: parseInt(document.getElementById('bookingVehicleId').value, 10),
        startDate: document.getElementById('bookingStartDate').value,
        endDate: document.getElementById('bookingEndDate').value,
        userId: API.getUser()?.userId || 1
    };

    try {
        await API.createBooking(payload);
        closeBookingModal();
        document.getElementById('bookingForm').reset();
        loadBookings();
        alert('Booking created successfully!');
    } catch (err) {
        alert('Failed to create booking: ' + err.message);
    }
}

async function updateStatus(id, newStatus) {
    if (!confirm(`Are you sure you want to mark this booking as ${newStatus}?`)) return;
    try {
        await API.updateBookingStatus(id, newStatus);
        loadBookings();
    } catch (err) {
        alert('Failed to update status: ' + err.message);
    }
}

async function cancelBooking(id) {
    if (!confirm('Are you sure you want to cancel this booking?')) return;
    try {
        await API.cancelBooking(id);
        loadBookings();
    } catch (err) {
        alert('Failed to cancel booking: ' + err.message);
    }
}

function openBookingModal() {
    document.getElementById('bookingModal').classList.add('active');
}

function closeBookingModal() {
    document.getElementById('bookingModal').classList.remove('active');
    document.getElementById('bookingForm').reset();
}

async function loadVehiclesForDropdown() {
    const select = document.getElementById('bookingVehicleId');
    try {
        const vehicles = await API.getVehicles(true); // Assuming true fetches available only, or false for all
        if (vehicles && vehicles.length > 0) {
            vehicles.forEach(v => {
                const option = document.createElement('option');
                option.value = v.id;
                option.textContent = `#V-${v.id} - ${v.name} (${v.registrationNumber || 'No Plate'})`;
                select.appendChild(option);
            });
        } else {
            select.innerHTML = '<option value="" disabled selected>No vehicles available</option>';
        }
    } catch (err) {
        console.error('Failed to load vehicles for dropdown', err);
    }
}
