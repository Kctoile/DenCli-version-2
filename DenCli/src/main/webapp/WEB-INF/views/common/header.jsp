<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!-- Header dùng chung cho toàn bộ hệ thống DenCli (Bootstrap 5 & Impeccable Design) -->
            <meta name="csrf-token" content="${csrfToken}">
            <script>
                window.CSRF_TOKEN = "${csrfToken}";
            </script>
            <!-- Navbar styles centralized in dencli-theme.css -->

            <nav class="navbar navbar-expand-lg navbar-dark dencli-navbar sticky-top py-2 py-lg-3" style="background: linear-gradient(135deg, #1E3A5F 0%, #0F2744 100%) !important; box-shadow: 0 4px 20px -2px rgba(14, 165, 233, 0.15);">
                <div class="container px-3 px-md-4">
                    <!-- Logo DenCli Dental -->
                    <a class="navbar-brand dencli-brand d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/index.jsp">
                        <span class="d-inline-flex align-items-center justify-content-center bg-primary text-white rounded-circle shadow-sm" style="width: 38px; height: 38px; font-size: 1.25rem;">
                🦷
            </span>
                        <span>Den<span class="brand-highlight">Cli</span> Dental</span>
                    </a>

                    <!-- Nút Toggler mobile -->
                    <button class="navbar-toggler border-0 shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#navbarDencliContent" aria-controls="navbarDencliContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

                    <!-- Menu điều hướng -->
                    <div class="collapse navbar-collapse" id="navbarDencliContent">
                        <ul class="navbar-nav mx-auto mb-2 mb-lg-0 gap-lg-1">
                            <li class="nav-item">
                                <c:choose>
                                    <c:when test="${sessionScope.user.roleId == 1}">
                                        <a class="nav-link dencli-nav-link" href="${pageContext.request.contextPath}/admin/dashboard">Trang chủ</a>
                                    </c:when>
                                    <c:when test="${sessionScope.user.roleId == 2}">
                                        <a class="nav-link dencli-nav-link" href="${pageContext.request.contextPath}/doctor/dashboard">Trang chủ</a>
                                    </c:when>
                                    <c:when test="${sessionScope.user.roleId == 4}">
                                        <a class="nav-link dencli-nav-link" href="${pageContext.request.contextPath}/staff/dashboard">Trang chủ</a>
                                    </c:when>
                                    <c:when test="${not empty sessionScope.user}">
                                        <a class="nav-link dencli-nav-link" href="${pageContext.request.contextPath}/customer/appointments">Trang chủ</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="nav-link dencli-nav-link" href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link dencli-nav-link" href="${pageContext.request.contextPath}/index.jsp#services">Dịch vụ</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link dencli-nav-link" href="${pageContext.request.contextPath}/index.jsp#doctors">Đội ngũ Bác sĩ</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link dencli-nav-link" href="${pageContext.request.contextPath}/index.jsp#faq">FAQ</a>
                            </li>
                        </ul>

                        <!-- Khối trạng thái đăng nhập & Nút CTA Đặt lịch -->
                        <div class="d-flex align-items-center flex-wrap gap-2 mt-3 mt-lg-0">
                            <!-- Nút CTA chính -->
                            <c:if test="${empty sessionScope.user or sessionScope.user.roleId == 5}">
                                <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-cta-nav d-inline-flex align-items-center gap-2">
                                    <span>📅</span>
                                    <span>Đặt Lịch Tư Vấn Miễn Phí</span>
                                </a>
                            </c:if>

                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <!-- Người dùng đã đăng nhập -->
                                    <div class="dropdown">
                                        <button class="btn btn-sm btn-outline-light dropdown-toggle px-3 py-2 rounded-pill d-flex align-items-center gap-2 shadow-sm" type="button" id="userMenuDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                <span>
                                    <c:choose>
                                        <c:when test="${sessionScope.user.roleId == 1}">⚙️</c:when>
                                        <c:when test="${sessionScope.user.roleId == 2}">🩺</c:when>
                                        <c:when test="${sessionScope.user.roleId == 4}">💁</c:when>
                                        <c:otherwise>👤</c:otherwise>
                                    </c:choose>
                                </span>
                                <span class="fw-semibold text-truncate" style="max-width: 140px;">
                                    <c:out value="${sessionScope.user.fullName}" />
                                </span>
                            </button>
                                        <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 rounded-3 mt-2" aria-labelledby="userMenuDropdown">
                                            <c:choose>
                                                <c:when test="${sessionScope.user.roleId == 1}">
                                                    <li><a class="dropdown-item py-2" href="${pageContext.request.contextPath}/admin/dashboard">⚙️ Bảng Điều Khiển Admin</a></li>
                                                    <li><a class="dropdown-item py-2" href="${pageContext.request.contextPath}/admin/users">👥 Quản Lý Người Dùng</a></li>
                                                </c:when>
                                                <c:when test="${sessionScope.user.roleId == 2}">
                                                    <li><a class="dropdown-item py-2" href="${pageContext.request.contextPath}/doctor/dashboard">🩺 Dashboard Bác Sĩ</a></li>
                                                    <li><a class="dropdown-item py-2" href="${pageContext.request.contextPath}/doctor/prescription">💊 Kê Đơn Thuốc</a></li>
                                                </c:when>
                                                <c:when test="${sessionScope.user.roleId == 4}">
                                                    <li><a class="dropdown-item py-2" href="${pageContext.request.contextPath}/staff/dashboard">💁 Dashboard Lễ Tân</a></li>
                                                    <li><a class="dropdown-item py-2" href="${pageContext.request.contextPath}/staff/invoice">🧾 Lập Hóa Đơn Thu Phí</a></li>
                                                </c:when>
                                                <c:otherwise>
                                                    <li><a class="dropdown-item py-2" href="${pageContext.request.contextPath}/customer/profile">📋 Hồ Sơ & Lịch Sử Khám</a></li>
                                                </c:otherwise>
                                            </c:choose>
                                            <li>
                                                <hr class="dropdown-divider">
                                            </li>
                                            <li>
                                                <a class="dropdown-item text-danger py-2" href="${pageContext.request.contextPath}/logout">
                                        🚪 Đăng xuất
                                    </a>
                                            </li>
                                        </ul>
                                    </div>

                                    <!-- Nút Đăng xuất trực tiếp trên thanh điều hướng -->
                                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-danger px-3 py-2 rounded-pill d-inline-flex align-items-center gap-1 shadow-sm" title="Đăng xuất khỏi hệ thống" style="color: #FFFFFF; background-color: rgba(239, 68, 68, 0.25); border-color: #EF4444; font-weight: 600;">
                                        <span>🚪</span>
                                        <span class="d-none d-sm-inline">Đăng xuất</span>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <!-- Khách vãng lai -->
                                    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm px-3 py-2 rounded-pill fw-medium">
                            Đăng nhập
                        </a>
                                    <a href="${pageContext.request.contextPath}/register" class="btn btn-light btn-sm text-primary px-3 py-2 rounded-pill fw-medium shadow-sm">
                            Đăng ký
                        </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </nav>

            <!-- Bootstrap 5 JS Bundle & Native Dropdown Fallback -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
            <script>
                document.addEventListener('DOMContentLoaded', function() {
                    var userBtn = document.getElementById('userMenuDropdown');
                    if (userBtn) {
                        userBtn.addEventListener('click', function(e) {
                            var menu = this.nextElementSibling;
                            if (menu && menu.classList.contains('dropdown-menu')) {
                                var isShown = menu.classList.contains('show');
                                menu.classList.toggle('show', !isShown);
                                userBtn.setAttribute('aria-expanded', String(!isShown));
                                e.stopPropagation();
                            }
                        });
                        document.addEventListener('click', function(e) {
                            var menu = document.querySelector('#userMenuDropdown + .dropdown-menu.show');
                            if (menu && !menu.contains(e.target) && e.target !== userBtn) {
                                menu.classList.remove('show');
                                userBtn.setAttribute('aria-expanded', 'false');
                            }
                        });
                    }
                });
            </script>