<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Khôi phục mật khẩu | DenCli Dental</title>

    <!-- Google Fonts: Plus Jakarta Sans -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- DenCli Pure CSS Stylesheets -->
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/dencli-theme.css?v=3">
    <style>
        .step-hidden { display: none !important; }
    </style>
</head>
<body>
    <div class="page-wrapper">
        <div class="auth-card" style="max-width: 440px;">
            <!-- Quay về trang chủ -->
            <div class="mb-3">
                <a href="${ctx}/" class="text-decoration-none small fw-semibold text-secondary d-inline-flex align-items-center gap-1" title="Quay về trang chủ DenCli">
                    <span style="font-size: 1.1rem; line-height: 1;">←</span> <span>Về trang chủ</span>
                </a>
            </div>

            <div class="auth-logo-section">
                <a href="${ctx}/" class="auth-logo-icon d-inline-flex text-decoration-none" title="Về trang chủ DenCli">
                    <span style="font-size: 1.5rem;">🔑</span>
                </a>
                <h1>Khôi phục mật khẩu</h1>
                <p class="auth-subtitle">Nhập email đăng ký để nhận mã OTP xác thực</p>
            </div>

            <div id="alertSuccess" class="alert-custom alert-success"></div>
            <div id="alertError" class="alert-custom alert-error"></div>

            <!-- Bước 1: Nhập Email để nhận OTP -->
            <form id="stepEmailForm" onsubmit="return handleSendOtp(event)">
                <div class="form-group">
                    <label for="fpEmail">Địa chỉ Email đã đăng ký <span style="color: #ef4444;">*</span></label>
                    <input type="email" id="fpEmail" class="form-control" placeholder="nguyenvana@gmail.com" required autocomplete="email">
                </div>
                <button type="submit" id="btnSendOtp" class="btn-cta-primary" style="width: 100%; border-radius: var(--dencli-radius-sm); padding: 0.8rem 1rem;">
                    Gửi mã OTP xác thực
                </button>
            </form>

            <!-- Bước 2: Nhập OTP và Mật khẩu mới -->
            <form id="stepResetForm" class="step-hidden" onsubmit="return handleVerifyReset(event)">
                <div class="form-group">
                    <label for="fpOtp">Mã OTP (6 chữ số) <span style="color: #ef4444;">*</span></label>
                    <input type="text" id="fpOtp" class="form-control" placeholder="123456" maxlength="6" required style="letter-spacing: 6px; font-weight: 800; font-size: 1.25rem; text-align: center;">
                </div>
                <div class="form-group">
                    <label for="fpNewPassword">Mật khẩu mới <span style="color: #ef4444;">*</span></label>
                    <input type="password" id="fpNewPassword" class="form-control" placeholder="Tối thiểu 6 ký tự" minlength="6" required autocomplete="new-password">
                </div>
                <div class="form-group">
                    <label for="fpConfirmPassword">Xác nhận mật khẩu mới <span style="color: #ef4444;">*</span></label>
                    <input type="password" id="fpConfirmPassword" class="form-control" placeholder="Nhập lại mật khẩu mới" minlength="6" required autocomplete="new-password">
                </div>
                <button type="submit" id="btnResetPass" class="btn-cta-primary" style="width: 100%; border-radius: var(--dencli-radius-sm); padding: 0.8rem 1rem;">
                    Cập nhật mật khẩu mới
                </button>
                <div style="text-align: center; margin-top: 12px;">
                    <button type="button" onclick="backToStep1()" class="btn btn-sm btn-outline-secondary" style="font-size: 0.85rem; border-radius: 9999px; padding: 4px 14px;">
                        ← Quay lại nhập Email khác
                    </button>
                </div>
            </form>

            <div class="auth-footer">
                Đã nhớ mật khẩu? <a href="${ctx}/login">Đăng nhập ngay</a>
            </div>
        </div>
    </div>

    <script>
        var currentEmail = "";

        function handleSendOtp(e) {
            e.preventDefault();
            var email = document.getElementById('fpEmail').value.trim();
            var btn = document.getElementById('btnSendOtp');
            var alertSuccess = document.getElementById('alertSuccess');
            var alertError = document.getElementById('alertError');

            alertSuccess.style.display = 'none';
            alertError.style.display = 'none';
            btn.disabled = true;
            btn.textContent = 'Đang gửi mã OTP...';

            fetch('${ctx}/forgot-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ action: 'send-otp', email: email })
            })
            .then(function(res) { return res.json(); })
            .then(function(data) {
                btn.disabled = false;
                btn.textContent = 'Gửi mã OTP xác thực';
                if (data.success) {
                    currentEmail = email;
                    alertSuccess.textContent = data.message;
                    alertSuccess.style.display = 'block';
                    document.getElementById('stepEmailForm').classList.add('step-hidden');
                    document.getElementById('stepResetForm').classList.remove('step-hidden');
                } else {
                    alertError.textContent = data.message;
                    alertError.style.display = 'block';
                }
            })
            .catch(function(err) {
                btn.disabled = false;
                btn.textContent = 'Gửi mã OTP xác thực';
                alertError.textContent = 'Lỗi kết nối máy chủ. Vui lòng thử lại sau.';
                alertError.style.display = 'block';
            });
            return false;
        }

        function handleVerifyReset(e) {
            e.preventDefault();
            var otp = document.getElementById('fpOtp').value.trim();
            var newPass = document.getElementById('fpNewPassword').value;
            var confirmPass = document.getElementById('fpConfirmPassword').value;
            var btn = document.getElementById('btnResetPass');
            var alertSuccess = document.getElementById('alertSuccess');
            var alertError = document.getElementById('alertError');

            alertSuccess.style.display = 'none';
            alertError.style.display = 'none';

            if (newPass !== confirmPass) {
                alertError.textContent = 'Mật khẩu xác nhận không khớp.';
                alertError.style.display = 'block';
                return false;
            }

            btn.disabled = true;
            btn.textContent = 'Đang cập nhật...';

            fetch('${ctx}/forgot-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    action: 'verify-reset',
                    email: currentEmail,
                    otp: otp,
                    new_password: newPass
                })
            })
            .then(function(res) { return res.json(); })
            .then(function(data) {
                btn.disabled = false;
                btn.textContent = 'Cập nhật mật khẩu mới';
                if (data.success) {
                    alertSuccess.textContent = data.message;
                    alertSuccess.style.display = 'block';
                    document.getElementById('stepResetForm').classList.add('step-hidden');
                    setTimeout(function() {
                        window.location.href = '${ctx}/login';
                    }, 1500);
                } else {
                    alertError.textContent = data.message;
                    alertError.style.display = 'block';
                }
            })
            .catch(function(err) {
                btn.disabled = false;
                btn.textContent = 'Cập nhật mật khẩu mới';
                alertError.textContent = 'Lỗi kết nối máy chủ. Vui lòng thử lại.';
                alertError.style.display = 'block';
            });
            return false;
        }

        function backToStep1() {
            document.getElementById('stepResetForm').classList.add('step-hidden');
            document.getElementById('stepEmailForm').classList.remove('step-hidden');
            document.getElementById('alertSuccess').style.display = 'none';
            document.getElementById('alertError').style.display = 'none';
        }
    </script>
</body>
</html>
