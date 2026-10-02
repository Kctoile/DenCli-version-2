<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="dashboardUrl" value="${pageContext.request.contextPath}/index.jsp" />
<c:if test="${not empty sessionScope.user}">
    <c:choose>
        <c:when test="${sessionScope.user.roleId == 1}"><c:set var="dashboardUrl" value="${pageContext.request.contextPath}/admin/dashboard" /></c:when>
        <c:when test="${sessionScope.user.roleId == 2}"><c:set var="dashboardUrl" value="${pageContext.request.contextPath}/doctor/examination" /></c:when>
        <c:when test="${sessionScope.user.roleId == 4}"><c:set var="dashboardUrl" value="${pageContext.request.contextPath}/staff/reception" /></c:when>
        <c:when test="${sessionScope.user.roleId == 5}"><c:set var="dashboardUrl" value="${pageContext.request.contextPath}/customer/book" /></c:when>
    </c:choose>
</c:if>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Không tìm thấy trang | DenCli</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=3">
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
            box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.05);
            max-width: 540px;
            width: 100%;
            padding: 2.5rem 2rem;
            text-align: center;
        }
        .error-code {
            font-size: 5rem;
            font-weight: 800;
            line-height: 1;
            color: #0284c7;
            letter-spacing: -0.05em;
            margin-bottom: 0.5rem;
        }
        .icon-circle {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background-color: #e0f2fe;
            color: #0284c7;
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
            🔍
        </div>
        <div class="error-code">404</div>
        <h2 class="h4 fw-bold text-dark mb-2">Trang không tồn tại hoặc đã bị di chuyển!</h2>
        <p class="text-muted mb-4" style="font-size: 0.9375rem; line-height: 1.6;">
            Đường dẫn bạn yêu cầu không tìm thấy trên hệ thống Nha khoa DenCli. Vui lòng kiểm tra lại liên kết hoặc quay về bàn làm việc.
        </p>

        <div class="d-flex flex-wrap justify-content-center gap-2">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <a href="${dashboardUrl}" class="btn btn-custom-primary">
                        🏠 Bàn làm việc của tôi
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
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
