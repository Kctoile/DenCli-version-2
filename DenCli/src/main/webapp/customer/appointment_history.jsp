<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Lịch hẹn của tôi | DenCli</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=3">
            </head>

            <body class="d-flex flex-column min-vh-100">
                <jsp:include page="/WEB-INF/views/common/header.jsp" />
                <div class="container-fluid flex-grow-1">
                    <div class="row min-vh-100">
                        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
                        <main class="col-12 col-md-9 col-lg-10 p-4">
                            <h4 class="fw-bold mb-4">🗓️ Lịch hẹn của tôi</h4>
                            <c:if test="${empty appointments}">
                                <div class="alert alert-info">Bạn chưa có lịch hẹn nào. <a href="${pageContext.request.contextPath}/customer/book">Đặt lịch ngay</a></div>
                            </c:if>
                            <c:if test="${not empty appointments}">
                                <div class="table-responsive card shadow-sm p-3">
                                    <table class="table table-hover align-middle">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Ngày hẹn</th>
                                                <th>Khung giờ</th>
                                                <th>Bác sĩ</th>
                                                <th>Phòng</th>
                                                <th>Trạng thái</th>
                                                <th></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${appointments}" var="a">
                                                <tr>
                                                    <td>${a.appointmentId}</td>
                                                    <td>
                                                        <fmt:formatDate value="${a.appointmentDate}" pattern="dd/MM/yyyy" />
                                                    </td>
                                                    <td>${a.appointmentTime}</td>
                                                    <td>${a.doctorName}</td>
                                                    <td>${a.room}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${a.status == 'Pending'}"><span class="badge bg-warning text-dark">Chờ xác nhận</span></c:when>
                                                            <c:when test="${a.status == 'Confirmed'}"><span class="badge bg-info text-dark">Đã xác nhận</span></c:when>
                                                            <c:when test="${a.status == 'Checked In'}"><span class="badge bg-primary">Đã check-in</span></c:when>
                                                            <c:when test="${a.status == 'Completed'}"><span class="badge bg-success">Hoàn thành</span></c:when>
                                                            <c:when test="${a.status == 'Cancelled'}"><span class="badge bg-secondary">Đã hủy</span></c:when>
                                                            <c:otherwise><span class="badge bg-light text-dark">${a.status}</span></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:if test="${a.status == 'Pending'}">
                                                            <button class="btn btn-sm btn-outline-danger" onclick="cancelAppointment(${a.appointmentId})">Hủy lịch</button>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <!-- Phân trang đơn giản -->
                                <c:if test="${totalPages > 1}">
                                    <nav>
                                        <ul class="pagination mt-3">
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${i == page ? 'active' : ''}">
                                                    <a class="page-link" href="?page=${i}">${i}</a>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </nav>
                                </c:if>
                            </c:if>
                        </main>
                    </div>
                </div>
                <script>
                    function cancelAppointment(id) {
                        if (!confirm('Bạn chắc chắn muốn hủy lịch hẹn #' + id + '?')) return;
                        fetch('${pageContext.request.contextPath}/api/appointments/action', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                    'X-CSRF-Token': document.querySelector('meta[name="csrf-token"]') ? .content || ''
                                },
                                body: 'action=cancel&appointment_id=' + id
                            })
                            .then(r => r.json())
                            .then(d => {
                                alert(d.message || 'Đã xử lý');
                                location.reload();
                            })
                            .catch(() => location.reload());
                    }
                </script>
            </body>

            </html>