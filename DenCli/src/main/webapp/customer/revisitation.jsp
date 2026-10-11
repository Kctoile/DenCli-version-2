<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch tái khám | DenCli</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=4">
</head>
<body class="d-flex flex-column min-vh-100">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
            <main class="col-12 col-md-9 col-lg-10 p-4">
                <div class="page-header-box">
                    <div>
                        <h4 class="page-header-title">🔁 Lịch tái khám bác sĩ chỉ định</h4>
                        <p class="page-header-subtitle">Danh sách các cuộc hẹn tái khám theo phác đồ điều trị của bác sĩ chuyên khoa.</p>
                    </div>
                </div>

                <c:if test="${empty revisits}">
                    <div class="dencli-card p-5 text-center">
                        <div class="empty-state-icon">🔁</div>
                        <h5 class="fw-bold mb-2">Chưa có lịch tái khám</h5>
                        <p class="text-muted small mb-0">Bạn hiện không có chỉ định tái khám nào cần xử lý.</p>
                    </div>
                </c:if>

                <c:forEach items="${revisits}" var="a">
                    <div class="dencli-card mb-3 p-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="fw-bold text-navy mb-1">
                                    🗓️ Ngày tái khám: <span class="text-primary"><fmt:formatDate value="${a.revisitDate}" pattern="dd/MM/yyyy"/></span>
                                </h6>
                                <span class="text-muted small">Liên kết hồ sơ ca khám gốc #${a.appointmentId}</span>
                            </div>
                            <span class="status-badge status-confirmed">${a.status}</span>
                        </div>
                        <c:if test="${not empty a.revisitNote}">
                            <div class="mt-3 pt-2 border-top">
                                <p class="mb-0 text-muted small"><strong>📝 Ghi chú từ Bác sĩ:</strong> <c:out value="${a.revisitNote}" /></p>
                            </div>
                        </c:if>
                    </div>
                </c:forEach>
            </main>
        </div>
    </div>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
