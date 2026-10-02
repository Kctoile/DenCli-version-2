<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Đăng nhập hệ thống - Phòng Khám Nha Khoa Quốc Tế DenCli">
    <meta name="csrf-token" content="${csrfToken}">
    <title>Đăng nhập | DenCli</title>

    <!-- Google Fonts: Plus Jakarta Sans -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- DenCli Pure CSS Stylesheets -->
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/dencli-theme.css?v=3">
    
    <script>
        window.CONTEXT_PATH = "${ctx}";
    </script>
</head>
<body>
    <div class="page-wrapper">
        <div class="auth-card">
            <!-- Quay về trang chủ -->
            <div class="mb-3">
                <a href="${ctx}/" class="text-decoration-none small fw-semibold text-secondary d-inline-flex align-items-center gap-1" title="Quay về trang chủ DenCli">
                    <span style="font-size: 1.1rem; line-height: 1;">←</span> <span>Về trang chủ</span>
                </a>
            </div>

            <!-- Logo & Title Section -->
            <div class="auth-logo-section">
                <a href="${ctx}/" class="auth-logo-icon d-inline-flex text-decoration-none" title="Về trang chủ DenCli">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="#ffffff" viewBox="0 0 256 256">
                        <path d="M216,72a40,40,0,0,0-40-40,8,8,0,0,0-7,4.3L153.2,70.5a16.1,16.1,0,0,1-22.1,6.5,8,8,0,0,0-10.2,10.2,16.1,16.1,0,0,1,6.5,22.1l-34.2,15.8a8,8,0,0,0-4.3,7,40,40,0,0,0,40,40,8,8,0,0,0,7-4.3l15.8-34.2a16.1,16.1,0,0,1,22.1-6.5,8,8,0,0,0,10.2-10.2,16.1,16.1,0,0,1-6.5-22.1l34.2-15.8A8,8,0,0,0,216,72Z" opacity="0.2"></path>
                        <path d="M224,72a48.05,48.05,0,0,0-48-48,16,16,0,0,0-14,8.59l-11.85,25.68a8,8,0,0,1-11,3.25,16,16,0,0,0-20.4,20.4,8,8,0,0,1,3.25,11L96.3,118.6A16,16,0,0,0,75.9,139a8,8,0,0,1-11,3.25L39.23,116.59A16,16,0,0,0,17.2,131.2l15.85,34.33a8,8,0,0,1-3.25,11,16,16,0,0,0-20.4,20.4,8,8,0,0,1,3.25,11L38,219.41A16,16,0,0,0,52,228a16.14,16.14,0,0,0,6.9-1.57l34.33-15.85a8,8,0,0,1,11,3.25,16,16,0,0,0,20.4-20.4,8,8,0,0,1-3.25-11l25.68-11.85a16,16,0,0,0,20.4-20.4,8,8,0,0,1,11-3.25l25.68,11.85A16,16,0,0,0,210.8,172l11.85-25.68a8,8,0,0,1,11-3.25,16,16,0,0,0,20.4-20.4,8,8,0,0,1-3.25-11ZM88,208a8,8,0,0,1-11,3.25l-34.33-15.85A8,8,0,0,1,39.4,184.4,24.1,24.1,0,0,0,61,152.1a8,8,0,0,1,5.65,13.56A32.14,32.14,0,0,1,88,208Zm120-40a8,8,0,0,1-11,3.25l-25.68-11.85a24.1,24.1,0,0,0-30.6,30.6,8,8,0,0,1-3.25,11l-34.33,15.85a8,8,0,0,1-11-3.25,32.14,32.14,0,0,1,21.36-42.34,8,8,0,0,1,5.65,13.56A24.1,24.1,0,0,0,172.9,139a8,8,0,0,1,11-3.25l25.68,11.85A8,8,0,0,1,212.8,159.4,24.1,24.1,0,0,0,234.4,127.1a8,8,0,0,1,5.65,13.56A32.14,32.14,0,0,1,208,168Z"></path>
                    </svg>
                </a>
                <h1>Chào mừng trở lại</h1>
                <p class="auth-subtitle">Đăng nhập để đặt lịch và quản lý hồ sơ khám</p>
            </div>

            <!-- Server-Side Notifications (JSTL) -->
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert-custom alert-error" style="display: block;">
                    <c:out value="${sessionScope.errorMessage}" />
                </div>
                <c:remove var="errorMessage" scope="session" />
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert-custom alert-error" style="display: block;">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>

            <!-- Client-Side Dynamic Alerts -->
            <div id="alertSuccess" class="alert-custom alert-success"></div>
            <div id="alertError" class="alert-custom alert-error"></div>

            <!-- Login Form -->
            <form id="loginForm" action="${ctx}/login" method="POST" onsubmit="return handleLogin(event)">
                <input type="hidden" name="_csrf" value="${csrfToken}">
                <div class="form-group">
                    <label for="emailOrPhone">Email hoặc Số điện thoại <span style="color: #ef4444;">*</span></label>
                    <input type="text" id="emailOrPhone" name="email_or_phone" class="form-control" placeholder="nguyenvana@gmail.com hoặc 0905123456" required autocomplete="username">
                </div>

                <div class="form-group">
                    <label for="password">Mật khẩu <span style="color: #ef4444;">*</span></label>
                    <div class="password-field">
                        <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required autocomplete="current-password">
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

                <div class="d-flex justify-content-end mb-3">
                    <a href="${ctx}/forgot-password" class="small fw-semibold text-primary text-decoration-none">Quên mật khẩu?</a>
                </div>

                <button type="submit" id="btnSubmit" class="btn-cta-primary w-100 py-2" data-original-text="Đăng nhập">
                    Đăng nhập
                </button>

                <div class="text-center my-3 text-muted small fw-semibold">— HOẶC —</div>
                
                <a href="${ctx}/auth/google" class="btn-google-login">
                    <svg width="18" height="18" viewBox="0 0 24 24"><path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/><path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/><path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"/><path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"/></svg>
                    <span>Đăng nhập với Google</span>
                </a>
            </form>

            <div class="auth-footer">
                Chưa có tài khoản? <a href="${ctx}/register">Đăng ký ngay</a>
            </div>
        </div>
    </div>

    <!-- DenCli Pure Login Vanilla JS -->
    <script src="${ctx}/assets/js/login.js" charset="UTF-8"></script>
</body>
</html>