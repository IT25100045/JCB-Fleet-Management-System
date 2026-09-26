document.addEventListener("DOMContentLoaded", () => {
    const path = window.location.pathname;
    const isPage = (name) => path.includes(name) ? 'active' : '';

    const user = API.getUser();
    const isCustomer = user && user.role === 'CUSTOMER';

    let navLinks = '';
    
    if (isCustomer) {
        navLinks = `
            <a href="customer-home.html" class="${isPage('customer-home')}">Rent a JCB</a>
            <a href="customer-bookings.html" class="${isPage('customer-bookings')}">My Bookings</a>
            <a href="customer-invoices.html" class="${isPage('customer-invoices')}">My Invoices</a>
        `;
    } else {
        navLinks = `
            <a href="booking.html" class="${isPage('booking')}">Booking & Scheduling</a>
            <a href="fleet.html" class="${isPage('fleet')}">Fleet Management</a>
            <a href="driver.html" class="${isPage('driver')}">Driver Allocation</a>
            <a href="maintenance.html" class="${isPage('maintenance')}">Maintenance & Fuel</a>
            <a href="invoices.html" class="${isPage('invoices') || isPage('invoice')}">Billing & Invoicing</a>
            <a href="incident.html" class="${isPage('incident')}">Incident & Feedback</a>
        `;
    }

    const headerHTML = `
        <header class="header">
            <div class="header-left">
                <div class="logo">🚜</div>
                <div>
                    <h1>JCB Fleet System</h1>
                    <p>${isCustomer ? 'Customer Portal' : 'Heavy Machinery Management'}</p>
                </div>
            </div>
            <div class="header-links">
                ${navLinks}
                <a href="login.html" onclick="localStorage.clear()" class="logout-btn">Logout</a>
            </div>
        </header>
    `;
    document.body.insertAdjacentHTML('afterbegin', headerHTML);
});
