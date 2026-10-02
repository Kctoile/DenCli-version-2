<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đổi mật khẩu | DenCli Dental</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=3">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: #f8fafc; }
        .card-custom { border: none; border-radius: 0.75rem; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

            <main class="col-12 col-md-9 col-lg-10 p-4">
                <div class="card card-custom bg-white p-4 p-md-5 mx-auto" style="max-width: 580px;">
                    <div class="text-center mb-4">
                        <div class="fs-1 text-primary mb-2">🔒</div>
                        <h4 class="fw-bold text-dark">Thay đổi mật khẩu tài khoản</h4>
                        <p class="text-muted small">Cập nhật mật khẩu định kỳ để nâng cao tính an toàn và bảo mật.</p>
                    </div>

                    <div id="alertSuccess" class="alert alert-success" style="display: none;"></div>
                    <div id="alertError" class="alert alert-danger" style="display: none;"></div>

                    <form id="changePasswordForm" onsubmit="return handleChangePassword(event)">
                        <div class="mb-3">
                            <label for="oldPassword" class="form-label fw-semibold">Mật khẩu hiện tại <span class="text-danger">*</span></label>
                            <input type="password" id="oldPassword" class="form-control" placeholder="••••••••" required>
                        </div>
                        <div class="mb-3">
                            <label for="newPassword" class="form-label fw-semibold">Mật khẩu mới <span class="text-danger">*</span></label>
                            <input type="password" id="newPassword" class="form-control" placeholder="Tối thiểu 6 ký tự" minlength="6" required>
                        </div>
                        <div class="mb-4">
                            <label for="confirmPassword" class="form-label fw-semibold">Xác nhận mật khẩu mới <span class="text-danger">*</span></label>
                            <input type="password" id="confirmPassword" class="form-control" placeholder="Nhập lại mật khẩu mới" minlength="6" required>
                        </div>
                        <button type="submit" id="btnSubmit" class="btn btn-primary w-100 py-2 fw-semibold">
                            Cập nhật mật khẩu
                        </button>
                    </form>
                </div>
            </main>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <script>
        function handleChangePassword(e) {
            e.preventDefault();
            var alertSuccess = document.getElementById('alertSuccess');
            var alertError = document.getElementById('alertError');
            var btn = document.getElementById('btnSubmit');

            var oldPass = document.getElementById('oldPassword').value;
            var newPass = document.getElementById('newPassword').value;
            var confirmPass = document.getElementById('confirmPassword').value;

            alertSuccess.style.display = 'none';
            alertError.style.display = 'none';

            if (newPass !== confirmPass) {
                alertError.textContent = 'Mật khẩu xác nhận không khớp.';
                alertError.style.display = 'block';
                return false;
            }

            btn.disabled = true;
            btn.textContent = 'Đang xử lý...';

            fetch('${pageContext.request.contextPath}/change-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': window.CSRF_TOKEN || ''
                },
                body: JSON.stringify({
                    old_password: oldPass,
                    new_password: newPass,
                    confirm_password: confirmPass
                })
            })
            .then(function(res) { return res.json(); })
            .then(function(data) {
                btn.disabled = false;
                btn.textContent = 'Cập nhật mật khẩu';
                if (data.success) {
                    alertSuccess.textContent = data.message;
                    alertSuccess.style.display = 'block';
                    document.getElementById('changePasswordForm').reset();
                } else {
                    alertError.textContent = data.message;
                    alertError.style.display = 'block';
                }
            })
            .catch(function(err) {
                btn.disabled = false;
                btn.textContent = 'Cập nhật mật khẩu';
                alertError.textContent = 'Lỗi kết nối máy chủ. Vui lòng thử lại sau.';
                alertError.style.display = 'block';
            });
            return false;
        }
    </script>
</body>
</html>
