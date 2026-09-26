document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('registerForm');
    const alertBox = document.getElementById('alertBox');
    const submitBtn = document.getElementById('submitBtn');

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        alertBox.style.display = 'none';
        alertBox.className = 'alert';
        submitBtn.disabled = true;
        submitBtn.textContent = 'Registering...';

        const payload = {
            fullName: document.getElementById('fullName').value.trim(),
            username: document.getElementById('username').value.trim(),
            email: document.getElementById('email').value.trim(),
            phone: document.getElementById('phone').value.trim(),
            password: document.getElementById('password').value
        };

        try {
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const data = await response.json().catch(() => ({}));

            if (!response.ok) {
                throw new Error(data.message || 'Registration failed. Please check your inputs.');
            }

            alertBox.textContent = 'Account created successfully! Redirecting to login...';
            alertBox.classList.add('alert-success');
            alertBox.style.display = 'block';

            setTimeout(() => {
                window.location.href = '/login.html';
            }, 1500);

        } catch (err) {
            alertBox.textContent = err.message;
            alertBox.classList.add('alert-danger');
            alertBox.style.display = 'block';
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register Account';
        }
    });
});