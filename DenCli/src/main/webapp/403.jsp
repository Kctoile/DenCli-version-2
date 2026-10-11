<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- 
 * File: 403.jsp
 * Mục đích: Trang thông báo lỗi 403 Forbidden - Truy cập bị từ chối (Access Denied),
 *           được hiển thị khi người dùng không đủ quyền hạn (Role) để truy cập tài nguyên.
 * Giao diện: Thiết kế hiện đại, tinh gọn với Bootstrap 5, 100% JSTL.
 --%>
<c:set var="dashboardUrl" value="${pageContext.request.contextPath}/index.jsp" />
<c:set var="roleName" value="Khách vãng lai" />
<c:if test="${not empty sessionScope.user}">
    <c:choose>
        <c:when test="${sessionScope.user.roleId == 1}">
            <c:set var="dashboardUrl" value="${pageContext.request.contextPath}/admin/dashboard" />
            <c:set var="roleName" value="Quản trị viên (ADMIN)" />
        </c:when>
        <c:when test="${sessionScope.user.roleId == 2}">
            <c:set var="dashboardUrl" value="${pageContext.request.contextPath}/doctor/examination" />
            <c:set var="roleName" value="Bác sĩ (DOCTOR)" />
        </c:when>
        <c:when test="${sessionScope.user.roleId == 4}">
            <c:set var="dashboardUrl" value="${pageContext.request.contextPath}/staff/reception" />
            <c:set var="roleName" value="Nhân viên Lễ tân (STAFF)" />
        </c:when>
        <c:when test="${sessionScope.user.roleId == 5}">
            <c:set var="dashboardUrl" value="${pageContext.request.contextPath}/customer/book" />
            <c:set var="roleName" value="Bệnh nhân (CUSTOMER)" />
        </c:when>
    </c:choose>
</c:if>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>403 - Quyền truy cập bị từ chối | DenCli</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=4">
</head>
<body class="system-error-wrapper">
    <div class="system-error-card">
        <div class="system-icon-circle system-icon-danger">
            🚫
        </div>
        <div class="system-error-code system-error-code-danger">403</div>
        <h2 class="h4 fw-bold text-dark mb-2">Bạn không có quyền truy cập trang này!</h2>
        <p class="text-muted mb-4" style="font-size: 0.9375rem; line-height: 1.6;">
            Tài khoản của bạn không có đủ thẩm quyền để xem nội dung hoặc thực hiện thao tác tại phân hệ này. Vui lòng kiểm tra lại quyền truy cập hoặc liên hệ Quản trị viên phòng khám.
        </p>

        <c:if test="${not empty sessionScope.user}">
            <div class="alert alert-light border d-inline-block text-start py-2 px-3 mb-4 rounded-3" style="font-size: 0.875rem;">
                <div><strong>Tài khoản:</strong> <c:out value="${sessionScope.user.fullName}" /> (<c:out value="${not empty sessionScope.user.email ? sessionScope.user.email : sessionScope.user.phone}" />)</div>
                <div><strong>Vai trò hiện tại:</strong> <span class="badge bg-secondary"><c:out value="${roleName}" /></span></div>
            </div>
        </c:if>

        <div class="d-flex flex-wrap justify-content-center gap-2">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <a href="${dashboardUrl}" class="btn btn-den-primary rounded-pill px-4">
                        🏠 Bàn làm việc của tôi
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger rounded-pill px-4">
                        Đăng xuất
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-cta-primary px-4">
                        🔑 Đăng nhập
                    </a>
                </c:otherwise>
            </c:choose>
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-outline-secondary rounded-pill px-4">
                Về Trang chủ
            </a>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
