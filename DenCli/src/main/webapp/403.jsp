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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: #f8fafc;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
            padding: 1.5rem;
        }
        .error-card {
            background: #ffffff;
            border-radius: 1rem;
            border: 1px solid #e2e8f0;
            box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.05), 0 8px 10px -6px rgba(0, 0, 0, 0.01);
            max-width: 540px;
            width: 100%;
            padding: 2.5rem 2rem;
            text-align: center;
        }
        .error-code {
            font-size: 5rem;
            font-weight: 800;
            line-height: 1;
            color: #ef4444;
            letter-spacing: -0.05em;
            margin-bottom: 0.5rem;
        }
        .icon-circle {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background-color: #fee2e2;
            color: #dc2626;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 2.5rem;
            margin-bottom: 1.5rem;
        }
        .btn-custom-primary {
            background-color: #0284c7;
            color: #ffffff;
            font-weight: 600;
            border: none;
            padding: 0.625rem 1.25rem;
            border-radius: 0.5rem;
            transition: all 0.2s ease-in-out;
            text-decoration: none;
        }
        .btn-custom-primary:hover {
            background-color: #0369a1;
            color: #ffffff;
            transform: translateY(-1px);
        }
    </style>
</head>
<body>
    <div class="error-card">
        <div class="icon-circle">
            🚫
        </div>
        <div class="error-code">403</div>
        <h2 class="h4 fw-bold text-dark mb-2">Bạn không có quyền truy cập trang này!</h2>
        <p class="text-muted mb-4" style="font-size: 0.9375rem; line-height: 1.6;">
            Tài khoản của bạn không có đủ thẩm quyền để xem nội dung hoặc thực hiện thao tác tại phân hệ này. Vui lòng kiểm tra lại quyền truy cập hoặc liên hệ Quản trị viên phòng khám.
        </p>

        <c:if test="${not empty sessionScope.user}">
            <div class="alert alert-light border d-inline-block text-start py-2 px-3 mb-4" style="font-size: 0.875rem;">
                <div><strong>Tài khoản:</strong> <c:out value="${sessionScope.user.fullName}" /> (<c:out value="${not empty sessionScope.user.email ? sessionScope.user.email : sessionScope.user.phone}" />)</div>
                <div><strong>Vai trò hiện tại:</strong> <span class="badge bg-secondary"><c:out value="${roleName}" /></span></div>
            </div>
        </c:if>

        <div class="d-flex flex-wrap justify-content-center gap-2">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <a href="${dashboardUrl}" class="btn btn-custom-primary">
                        🏠 Bàn làm việc của tôi
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger">
                        Đăng xuất
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-custom-primary">
                        🔑 Đăng nhập
                    </a>
                </c:otherwise>
            </c:choose>
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-outline-secondary">
                Về Trang chủ
            </a>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
