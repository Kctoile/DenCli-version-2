<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu | DenCli Dental</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
    <style>
        .step-hidden { display: none; }
    </style>
</head>
<body>
    <div class="page-wrapper">
        <div class="card">
            <div class="logo-section">
                <div class="logo-icon">🔑</div>
                <h1>Khôi phục mật khẩu</h1>
                <p>Nhập email để nhận mã OTP xác thực</p>
            </div>

            <div id="alertSuccess" class="alert alert-success"></div>
            <div id="alertError" class="alert alert-error"></div>

            <!-- Bước 1: Nhập Email để nhận OTP -->
            <form id="stepEmailForm" onsubmit="return handleSendOtp(event)">
                <div class="form-group">
                    <label for="fpEmail">Địa chỉ Email đã đăng ký <span class="required">*</span></label>
                    <input type="email" id="fpEmail" class="form-control" placeholder="nguyenvana@gmail.com" required>
                </div>
                <button type="submit" id="btnSendOtp" class="btn btn-primary">Gửi mã OTP</button>
            </form>

            <!-- Bước 2: Nhập OTP và Mật khẩu mới -->
            <form id="stepResetForm" class="step-hidden" onsubmit="return handleVerifyReset(event)">
                <div class="form-group">
                    <label for="fpOtp">Mã OTP (6 chữ số) <span class="required">*</span></label>
                    <input type="text" id="fpOtp" class="form-control" placeholder="123456" maxlength="6" required style="letter-spacing: 4px; font-weight: bold; font-size: 1.1rem; text-align: center;">
                </div>
                <div class="form-group">
                    <label for="fpNewPassword">Mật khẩu mới <span class="required">*</span></label>
                    <input type="password" id="fpNewPassword" class="form-control" placeholder="Tối thiểu 6 ký tự" minlength="6" required>
                </div>
                <div class="form-group">
                    <label for="fpConfirmPassword">Xác nhận mật khẩu mới <span class="required">*</span></label>
                    <input type="password" id="fpConfirmPassword" class="form-control" placeholder="Nhập lại mật khẩu mới" minlength="6" required>
                </div>
                <button type="submit" id="btnResetPass" class="btn btn-primary">Cập nhật mật khẩu</button>
                <div style="text-align: center; margin-top: 10px;">
                    <button type="button" onclick="backToStep1()" class="btn btn-outline-secondary" style="font-size: 0.85rem; padding: 4px 10px;">Quay lại nhập Email khác</button>
                </div>
            </form>

            <div class="form-footer">
                Đã nhớ mật khẩu? <a href="${pageContext.request.contextPath}/login.jsp">Đăng nhập</a>
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

            fetch('${pageContext.request.contextPath}/forgot-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ action: 'send-otp', email: email })
            })
            .then(function(res) { return res.json(); })
            .then(function(data) {
                btn.disabled = false;
                btn.textContent = 'Gửi mã OTP';
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
                btn.textContent = 'Gửi mã OTP';
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

            fetch('${pageContext.request.contextPath}/forgot-password', {
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
                btn.textContent = 'Cập nhật mật khẩu';
                if (data.success) {
                    alertSuccess.textContent = data.message;
                    alertSuccess.style.display = 'block';
                    document.getElementById('stepResetForm').classList.add('step-hidden');
                    setTimeout(function() {
                        window.location.href = '${pageContext.request.contextPath}/login.jsp';
                    }, 2000);
                } else {
                    alertError.textContent = data.message;
                    alertError.style.display = 'block';
                }
            })
            .catch(function(err) {
                btn.disabled = false;
                btn.textContent = 'Cập nhật mật khẩu';
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
