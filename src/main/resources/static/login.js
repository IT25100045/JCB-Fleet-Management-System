document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const submitBtn = document.getElementById('submitBtn');
    const errorAlert = document.getElementById('errorAlert');

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;

        // Reset error box state
        errorAlert.style.display = 'none';
        errorAlert.textContent = '';
        submitBtn.disabled = true;
        submitBtn.textContent = 'Authenticating...';

        try {
            const data = await API.login(username, password);

            // Store JWT token and authentication metadata using the API helper
            API.setSession(data);
            
            if (data.role === 'ADMIN') {
                window.location.href = 'home.html';
            } else {
                window.location.href = 'customer-home.html';
            }

        } catch (err) {
            errorAlert.textContent = err.message;
            errorAlert.style.display = 'block';
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Sign In';
        }
    });
});