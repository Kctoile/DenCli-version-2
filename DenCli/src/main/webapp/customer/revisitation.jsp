<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch tái khám | DenCli</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=3">
</head>
<body class="d-flex flex-column min-vh-100">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
            <main class="col-12 col-md-9 col-lg-10 p-4">
                <h4 class="fw-bold mb-4">🔁 Lịch tái khám bác sĩ chỉ định</h4>
                <c:if test="${empty revisits}">
                    <div class="alert alert-info">Bạn chưa có lịch tái khám nào sắp tới.</div>
                </c:if>
                <c:forEach items="${revisits}" var="a">
                    <div class="card mb-3 shadow-sm p-3">
                        <div class="d-flex justify-content-between">
                            <div>
                                <strong>Ngày tái khám:</strong> <fmt:formatDate value="${a.revisitDate}" pattern="dd/MM/yyyy"/> — khám gốc #${a.appointmentId}
                            </div>
                            <span class="badge bg-info text-dark align-self-center">${a.status}</span>
                        </div>
                        <c:if test="${not empty a.revisitNote}">
                            <p class="mb-0 mt-2 text-muted">📝 <c:out value="${a.revisitNote}" /></p>
                        </c:if>
                    </div>
                </c:forEach>
            </main>
        </div>
    </div>
</body>
</html>
