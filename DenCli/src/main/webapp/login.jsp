<%-- 
 * Purpose: Login page for patients and clinic staff (UC-04).
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Đăng nhập hệ thống - Hệ thống Quản lý Phòng khám Nha khoa DenCli">
    <title>Đăng nhập | DenCli</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page-wrapper">
        <div class="card">
            <div class="logo-section">
                <!-- SVG Icon thay thế cho Emoji giúp giao diện đồng bộ chuyên nghiệp -->
                <div class="logo-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="#ffffff" viewBox="0 0 256 256">
                        <path d="M216,72a40,40,0,0,0-40-40,8,8,0,0,0-7,4.3L153.2,70.5a16.1,16.1,0,0,1-22.1,6.5,8,8,0,0,0-10.2,10.2,16.1,16.1,0,0,1,6.5,22.1l-34.2,15.8a8,8,0,0,0-4.3,7,40,40,0,0,0,40,40,8,8,0,0,0,7-4.3l15.8-34.2a16.1,16.1,0,0,1,22.1-6.5,8,8,0,0,0,10.2-10.2,16.1,16.1,0,0,1-6.5-22.1l34.2-15.8A8,8,0,0,0,216,72Z" opacity="0.2"></path>
                        <path d="M224,72a48.05,48.05,0,0,0-48-48,16,16,0,0,0-14,8.59l-11.85,25.68a8,8,0,0,1-11,3.25,16,16,0,0,0-20.4,20.4,8,8,0,0,1,3.25,11L96.3,118.6A16,16,0,0,0,75.9,139a8,8,0,0,1-11,3.25L39.23,116.59A16,16,0,0,0,17.2,131.2l15.85,34.33a8,8,0,0,1-3.25,11,16,16,0,0,0-20.4,20.4,8,8,0,0,1,3.25,11L38,219.41A16,16,0,0,0,52,228a16.14,16.14,0,0,0,6.9-1.57l34.33-15.85a8,8,0,0,1,11,3.25,16,16,0,0,0,20.4-20.4,8,8,0,0,1-3.25-11l25.68-11.85a16,16,0,0,0,20.4-20.4,8,8,0,0,1,11-3.25l25.68,11.85A16,16,0,0,0,210.8,172l11.85-25.68a8,8,0,0,1,11-3.25,16,16,0,0,0,20.4-20.4,8,8,0,0,1-3.25-11ZM88,208a8,8,0,0,1-11,3.25l-34.33-15.85A8,8,0,0,1,39.4,184.4,24.1,24.1,0,0,0,61,152.1a8,8,0,0,1,5.65,13.56A32.14,32.14,0,0,1,88,208Zm120-40a8,8,0,0,1-11,3.25l-25.68-11.85a24.1,24.1,0,0,0-30.6,30.6,8,8,0,0,1-3.25,11l-34.33,15.85a8,8,0,0,1-11-3.25,32.14,32.14,0,0,1,21.36-42.34,8,8,0,0,1,5.65,13.56A24.1,24.1,0,0,0,172.9,139a8,8,0,0,1,11-3.25l25.68,11.85A8,8,0,0,1,212.8,159.4,24.1,24.1,0,0,0,234.4,127.1a8,8,0,0,1,5.65,13.56A32.14,32.14,0,0,1,208,168Z"></path>
                    </svg>
                </div>
                <h1>Chào mừng trở lại</h1>
                <p>Đăng nhập để đặt lịch và quản lý hồ sơ khám</p>
            </div>

            <!-- Thông báo lỗi hoặc thành công -->
            <div id="alertSuccess" class="alert alert-success"></div>
            <div id="alertError" class="alert alert-error"></div>

            <form id="loginForm" onsubmit="return handleLogin(event)">
                <div class="form-group">
                    <label>Email hoặc Số điện thoại <span class="required">*</span></label>
                    <input type="text" id="emailOrPhone" name="email_or_phone" class="form-control" placeholder="nguyenvana@gmail.com hoặc 0905123456" required>
                </div>

                <div class="form-group">
                    <label>Mật khẩu <span class="required">*</span></label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
                </div>

                <button type="submit" id="btnSubmit" class="btn btn-primary">Đăng nhập</button>
            </form>

            <div class="form-footer">
                Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register.jsp">Đăng ký ngay</a>
            </div>
        </div>
    </div>

    <script>
        // Hàm gửi dữ liệu đăng nhập không đồng bộ qua API /login
        function handleLogin(event) {
            event.preventDefault(); // Chặn hành vi load lại trang mặc định của form

            var alertSuccess = document.getElementById('alertSuccess');
            var alertError = document.getElementById('alertError');
            alertSuccess.style.display = 'none';
            alertError.style.display = 'none';

            var btn = document.getElementById('btnSubmit');
            btn.disabled = true;
            btn.textContent = 'Đang xác thực...';

            // Tạo payload JSON gửi đi
            var payload = {
                email_or_phone: document.getElementById('emailOrPhone').value.trim(),
                password: document.getElementById('password').value
            };

            // Gọi API bằng fetch gửi yêu cầu POST dạng JSON
            fetch('${pageContext.request.contextPath}/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload) // Convert dữ liệu thành chuỗi JSON
            })
            .then(function(response) {
                return response.json(); // Phân tích kết quả JSON trả về
            })
            .then(function(data) {
                btn.disabled = false;
                btn.textContent = 'Đăng nhập';

                if (data.success) {
                    alertSuccess.textContent = data.message;
                    alertSuccess.style.display = 'block';

                    // Chuyển hướng sang trang đặt lịch khám (book.jsp) sau 1.5 giây
                    setTimeout(function() {
                        window.location.href = '${pageContext.request.contextPath}/book.jsp';
                    }, 1500);

                } else {
                    alertError.textContent = data.message;
                    alertError.style.display = 'block';
                }
            })
            .catch(function(err) {
                btn.disabled = false;
                btn.textContent = 'Đăng nhập';
                alertError.textContent = 'Lỗi kết nối hoặc hệ thống gặp sự cố.';
                alertError.style.display = 'block';
            });

            return false;
        }
    </script>
</body>
</html>
