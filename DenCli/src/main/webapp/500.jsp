<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
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
    <title>500 - Lỗi hệ thống nội bộ | DenCli</title>
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
            ⚠️
        </div>
        <div class="system-error-code system-error-code-danger">500</div>
        <h2 class="h4 fw-bold text-dark mb-2">Đã xảy ra sự cố nội bộ máy chủ!</h2>
        <p class="text-muted mb-4" style="font-size: 0.9375rem; line-height: 1.6;">
            Hệ thống tạm thời gặp trục trặc khi xử lý yêu cầu của bạn. Kỹ thuật viên đã được thông báo. Vui lòng thử lại sau ít phút.
        </p>

        <div class="d-flex flex-wrap justify-content-center gap-2">
            <button onclick="window.location.reload()" class="btn btn-den-primary rounded-pill px-4">
                🔄 Thử lại
            </button>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <a href="${dashboardUrl}" class="btn btn-outline-secondary rounded-pill px-4">
                        🏠 Bàn làm việc
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-outline-secondary rounded-pill px-4">
                        Về Trang chủ
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
