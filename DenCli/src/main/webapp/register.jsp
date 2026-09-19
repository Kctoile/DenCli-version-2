<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%-- Trang Đăng ký tài khoản Bệnh nhân (UC-03: Dang ki tai khoan) --%>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="description" content="Đăng ký tài khoản bệnh nhân - Hệ thống Quản lý Phòng khám Nha khoa DenCli">
                <title>Đăng ký tài khoản | DenCli</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
            </head>

            <body>
                <div class="page-wrapper">
                    <div class="card">
                        <div class="logo-section">
                            <div class="logo-icon">🦷</div>
                            <h1>Tạo tài khoản mới</h1>
                            <p>Đăng ký để đặt lịch khám nha khoa trực tuyến</p>
                        </div>

                        <!-- Thông báo lỗi từ server (JSTL, 0 scriptlet) -->
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-error" style="display: block;">
                                <c:out value="${errorMessage}" />
                            </div>
                        </c:if>

                        <!-- Thông báo kết quả qua AJAX -->
                        <div id="alertSuccess" class="alert alert-success"></div>
                        <div id="alertError" class="alert alert-error"></div>

                        <form id="registerForm" onsubmit="return handleRegister(event)">
                            <div class="form-group">
                                <label for="fullName">Họ và tên <span class="required">*</span></label>
                                <input type="text" id="fullName" name="full_name" class="form-control" placeholder="Nguyễn Văn A" required>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="email">Email <span class="required">*</span></label>
                                    <input type="email" id="email" name="email" class="form-control" placeholder="email@gmail.com" required>
                                </div>
                                <div class="form-group">
                                    <label for="phone">Số điện thoại <span class="required">*</span></label>
                                    <input type="tel" id="phone" name="phone" class="form-control" placeholder="0905 123 456" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="password">Mật khẩu <span class="required">*</span></label>
                                <div class="password-field">
                                    <input type="password" id="password" name="password" class="form-control" placeholder="Nhập mật khẩu (tối thiểu 6 ký tự)" minlength="6" required>
                                    <button type="button" class="password-toggle" aria-label="Hiển thị mật khẩu" aria-pressed="false" onclick="togglePasswordVisibility('password', this)">
                            <svg class="icon-eye" aria-hidden="true" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M2 12s3.5-7 10-7 10 7 10 7-3.5 7-10 7S2 12 2 12Z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                            <svg class="icon-eye-off" aria-hidden="true" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="m3 3 18 18"></path>
                                <path d="M10.6 10.6a2 2 0 0 0 2.8 2.8"></path>
                                <path d="M9.9 4.2A10.7 10.7 0 0 1 12 4c6.5 0 10 8 10 8a18.5 18.5 0 0 1-3.1 4.3"></path>
                                <path d="M6.6 6.6C3.7 8.5 2 12 2 12s3.5 8 10 8a10.8 10.8 0 0 0 3.2-.5"></path>
                            </svg>
                            <span class="visually-hidden">Bật hoặc tắt hiển thị mật khẩu</span>
                        </button>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="gender">Giới tính</label>
                                    <select id="gender" name="gender" class="form-control">
                            <option value="">-- Chọn --</option>
                            <option value="Nam">Nam</option>
                            <option value="Nữ">Nữ</option>
                        </select>
                                </div>
                                <div class="form-group">
                                    <label for="dob">Ngày sinh</label>
                                    <input type="date" id="dob" name="dob" class="form-control">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="address">Địa chỉ</label>
                                <input type="text" id="address" name="address" class="form-control" placeholder="123 Nguyễn Trãi, Đà Nẵng">
                            </div>

                            <button type="submit" id="btnSubmit" class="btn btn-primary">Đăng ký tài khoản</button>
                        </form>

                        <div class="form-footer">
                            Đã có tài khoản? <a href="${pageContext.request.contextPath}/login.jsp">Đăng nhập ngay</a>
                        </div>
                    </div>
                </div>

                <script>
                    // Hàm xử lý sự kiện submit form đăng ký, gửi dữ liệu JSON tới Servlet /register
                    function handleRegister(event) {
                        event.preventDefault();

                        var alertSuccess = document.getElementById('alertSuccess');
                        var alertError = document.getElementById('alertError');
                        alertSuccess.style.display = 'none';
                        alertError.style.display = 'none';

                        var btn = document.getElementById('btnSubmit');
                        btn.disabled = true;
                        btn.textContent = 'Đang xử lý...';

                        var payload = {
                            full_name: document.getElementById('fullName').value.trim(),
                            email: document.getElementById('email').value.trim(),
                            phone: document.getElementById('phone').value.trim(),
                            password: document.getElementById('password').value,
                            gender: document.getElementById('gender').value,
                            dob: document.getElementById('dob').value,
                            address: document.getElementById('address').value.trim()
                        };

                        fetch('${pageContext.request.contextPath}/register', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Accept': 'application/json'
                                },
                                body: JSON.stringify(payload)
                            })
                            .then(function(response) {
                                return response.json();
                            })
                            .then(function(data) {
                                btn.disabled = false;
                                btn.textContent = 'Đăng ký tài khoản';

                                if (data.success) {
                                    alertSuccess.textContent = data.message;
                                    alertSuccess.style.display = 'block';
                                    document.getElementById('registerForm').reset();

                                    setTimeout(function() {
                                        window.location.href = '${pageContext.request.contextPath}/login.jsp';
                                    }, 1500);
                                } else {
                                    alertError.textContent = data.message;
                                    alertError.style.display = 'block';
                                }
                            })
                            .catch(function(err) {
                                btn.disabled = false;
                                btn.textContent = 'Đăng ký tài khoản';
                                alertError.textContent = 'Lỗi kết nối đến máy chủ. Vui lòng thử lại sau.';
                                alertError.style.display = 'block';
                            });

                        return false;
                    }

                    function togglePasswordVisibility(inputId, toggleButton) {
                        var passwordInput = document.getElementById(inputId);
                        var isVisible = passwordInput.type === 'text';
                        passwordInput.type = isVisible ? 'password' : 'text';
                        toggleButton.classList.toggle('is-visible', !isVisible);
                        toggleButton.setAttribute('aria-pressed', String(!isVisible));
                        toggleButton.setAttribute('aria-label', isVisible ? 'Hiển thị mật khẩu' : 'Ẩn mật khẩu');
                    }
                </script>
            </body>

            </html>