/**
 * DenCli Dental Clinic - Login Vanilla JavaScript
 * Pure ES6+, Zero React, Native Fetch API & Smart Role Routing
 * Safe Unicode Escaped Text to Prevent Any Encoding/Mojibake Issues
 */

function handleLogin(event) {
    event.preventDefault();

    const form = document.getElementById('loginForm');
    const alertSuccess = document.getElementById('alertSuccess');
    const alertError = document.getElementById('alertError');
    const btn = document.getElementById('btnSubmit');

    if (alertSuccess) alertSuccess.style.display = 'none';
    if (alertError) alertError.style.display = 'none';

    const emailOrPhone = document.getElementById('emailOrPhone').value.trim();
    const password = document.getElementById('password').value;

    if (!emailOrPhone || !password) {
        if (alertError) {
            alertError.textContent = 'Vui l\u00F2ng nh\u1EADp \u0111\u1EA7y \u0111\u1EE7 t\u00E0i kho\u1EA3n v\u00E0 m\u1EADt kh\u1EA9u.';
            alertError.style.display = 'block';
        }
        return false;
    }

    if (btn) {
        btn.disabled = true;
        btn.textContent = '\u0110ang x\u00E1c th\u1EF1c...';
    }

    const payload = {
        email_or_phone: emailOrPhone,
        password: password
    };

    const contextPath = window.CONTEXT_PATH || '';

    // Attach CSRF token if present
    const headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    };
    const csrfMeta = document.querySelector('meta[name="csrf-token"]');
    if (csrfMeta && csrfMeta.getAttribute('content')) {
        headers['X-CSRF-TOKEN'] = csrfMeta.getAttribute('content');
    } else if (window.CSRF_TOKEN) {
        headers['X-CSRF-TOKEN'] = window.CSRF_TOKEN;
    }

    fetch(contextPath + '/login', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(payload)
    })
    .then(async response => {
        let data;
        try {
            data = await response.json();
        } catch (e) {
            throw new Error('M\u00E1y ch\u1EE7 ph\u1EA3n h\u1ED3i kh\u00F4ng h\u1EE3p l\u1EC7.');
        }

        if (btn) {
            btn.disabled = false;
            btn.textContent = '\u0110\u0103ng nh\u1EADp';
        }

        if (response.ok && data.success) {
            if (alertSuccess) {
                alertSuccess.textContent = data.message || '\u0110\u0103ng nh\u1EADp th\u00E0nh c\u00F4ng!';
                alertSuccess.style.display = 'block';
            }

            const targetUrl = (data.data && data.data.target_url) ? data.data.target_url : '';
            const role = (data.data && data.data.role) ? data.data.role : 'CUSTOMER';

            setTimeout(() => {
                if (targetUrl && targetUrl.length > 0) {
                    window.location.href = targetUrl;
                } else if (role === 'ADMIN') {
                    window.location.href = contextPath + '/admin/dashboard';
                } else if (role === 'DOCTOR') {
                    window.location.href = contextPath + '/doctor/examination';
                } else if (role === 'STAFF') {
                    window.location.href = contextPath + '/staff/reception';
                } else {
                    window.location.href = contextPath + '/customer/book';
                }
            }, 600);
        } else {
            const message = data.message || 'Email/S\u1ED1 \u0111i\u1EC7n tho\u1EA1i ho\u1EB7c m\u1EADt kh\u1EA9u kh\u00F4ng ch\u00EDnh x\u00E1c.';
            if (alertError) {
                alertError.textContent = message;
                alertError.style.display = 'block';
            }
        }
    })
    .catch(err => {
        if (btn) {
            btn.disabled = false;
            btn.textContent = '\u0110\u0103ng nh\u1EADp';
        }
        if (alertError) {
            alertError.textContent = err.message || 'L\u1ED7i k\u1EBFt n\u1ED1i t\u1EDBi m\u00E1y ch\u1EE7. Vui l\u00F2ng th\u1EED l\u1EA1i sau.';
            alertError.style.display = 'block';
        }
    });

    return false;
}

function togglePasswordVisibility(inputId, toggleButton) {
    const passwordInput = document.getElementById(inputId);
    if (!passwordInput || !toggleButton) return;

    const isVisible = passwordInput.type === 'text';
    passwordInput.type = isVisible ? 'password' : 'text';
    toggleButton.classList.toggle('is-visible', !isVisible);
    toggleButton.setAttribute('aria-pressed', String(!isVisible));
    toggleButton.setAttribute('aria-label', isVisible ? 'Hi\u1EC3n th\u1ECB m\u1EADt kh\u1EA9u' : '\u1EA8n m\u1EADt kh\u1EA9u');
}
