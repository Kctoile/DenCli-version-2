<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!-- Header dùng chung cho toàn bộ hệ thống DenCli (Bootstrap 5 & Impeccable Design) -->
            <meta name="csrf-token" content="${csrfToken}">
            <script>
                window.CSRF_TOKEN = "${csrfToken}";
            </script>
            <!-- Navbar styles centralized in dencli-theme.css -->

            <nav class="navbar navbar-expand-lg navbar-dark dencli-navbar sticky-top py-2 py-lg-3">
                <div class="container px-3 px-md-4">
                    <!-- Logo DenCli Dental — SVG tooth icon (crisp at all resolutions) -->
                    <a class="navbar-brand dencli-brand d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/index.jsp">
                        <span class="den-logo-icon d-inline-flex align-items-center justify-content-center rounded-circle shadow-sm" aria-hidden="true">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M12 2C9.5 2 7.5 3.5 6.5 5.5C5.5 4.5 4 4 3 5C1.5 6.5 2 9 3 11C4 13 5 15 5.5 17.5C6 20 7 22 8.5 22C10 22 10.5 20 11 18.5C11.3 17.5 11.7 17 12 17C12.3 17 12.7 17.5 13 18.5C13.5 20 14 22 15.5 22C17 22 18 20 18.5 17.5C19 15 20 13 21 11C22 9 22.5 6.5 21 5C20 4 18.5 4.5 17.5 5.5C16.5 3.5 14.5 2 12 2Z" fill="white" opacity="0.95"/>
                            </svg>
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
                                <span class="den-role-icon" aria-hidden="true">
                                    <c:choose>
                                        <c:when test="${sessionScope.user.roleId == 1}">
                                            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2a5 5 0 015 5v1a5 5 0 01-10 0V7a5 5 0 015-5z"/><path d="M3 21a9 9 0 0118 0"/></svg>
                                        </c:when>
                                        <c:when test="${sessionScope.user.roleId == 2}">
                                            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 12h6m-3-3v6M5 3h14a2 2 0 012 2v14a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2z"/></svg>
                                        </c:when>
                                        <c:when test="${sessionScope.user.roleId == 4}">
                                            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75"/></svg>
                                        </c:when>
                                        <c:otherwise>
                                            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2a5 5 0 015 5v1a5 5 0 01-10 0V7a5 5 0 015-5z"/><path d="M3 21a9 9 0 0118 0"/></svg>
                                        </c:otherwise>
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
                                    <a href="${pageContext.request.contextPath}/logout"
                                       class="btn btn-sm btn-nav-logout px-3 py-2 rounded-pill d-inline-flex align-items-center gap-1 shadow-sm"
                                       title="Đăng xuất khỏi hệ thống">
                                        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" aria-hidden="true"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
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
                    // Phase 2: Page fade-in on every navigation
                    document.body.classList.add('den-page-enter');
                });
            </script>