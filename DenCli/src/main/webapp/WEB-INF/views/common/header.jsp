<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!-- Header dùng chung cho toàn bộ hệ thống DenCli (Bootstrap 5) -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm sticky-top">
    <div class="container-fluid px-3 px-md-4">
        <a class="navbar-brand fw-bold d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/index.jsp">
            <span class="fs-4">🦷</span>
            <span>DenCli</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
                </li>
            </ul>
            <div class="d-flex align-items-center gap-2">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <span class="badge bg-light text-primary px-3 py-2 fw-semibold d-flex align-items-center gap-1 shadow-sm">
                            <c:choose>
                                <c:when test="${sessionScope.user.roleId == 1}">⚙️ Admin:</c:when>
                                <c:when test="${sessionScope.user.roleId == 2}">🩺 BS:</c:when>
                                <c:when test="${sessionScope.user.roleId == 4}">💁 Lễ tân:</c:when>
                                <c:otherwise>👤</c:otherwise>
                            </c:choose>
                            <c:out value="${sessionScope.user.fullName}" />
                        </span>
                        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm px-3">Đăng xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-light btn-sm text-primary fw-semibold px-3">Đăng nhập</a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-light btn-sm px-3">Đăng ký</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
