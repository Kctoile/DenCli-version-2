<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Lịch hẹn của tôi | DenCli</title>
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
                                    <h4 class="page-header-title">🗓️ Lịch hẹn của tôi</h4>
                                    <p class="page-header-subtitle">Theo dõi trạng thái các cuộc hẹn khám và điều trị nha khoa của bạn.</p>
                                </div>
                                <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-cta-primary btn-sm rounded-pill px-3">
                                    <span>📅</span><span>Đặt lịch mới</span>
                                </a>
                            </div>

                            <c:if test="${empty appointments}">
                                <div class="dencli-card p-5 text-center">
                                    <div class="empty-state-icon">🗓️</div>
                                    <h5 class="fw-bold mb-2">Bạn chưa có lịch hẹn nào</h5>
                                    <p class="text-muted small mb-4">Đặt lịch khám ngay để được tư vấn và chăm sóc nụ cười cùng chuyên gia.</p>
                                    <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-cta-primary">
                                        <span>📅</span><span>Đặt Lịch Ngay</span>
                                    </a>
                                </div>
                            </c:if>

                            <c:if test="${not empty appointments}">
                                <div class="dencli-card p-3">
                                    <div class="table-responsive border-0">
                                        <table class="table table-hover align-middle mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th style="width: 8%;">#</th>
                                                    <th style="width: 18%;">Ngày hẹn</th>
                                                    <th style="width: 14%;">Khung giờ</th>
                                                    <th style="width: 22%;">Bác sĩ</th>
                                                    <th style="width: 14%;">Phòng</th>
                                                    <th style="width: 14%;">Trạng thái</th>
                                                    <th style="width: 10%;" class="text-end">Thao tác</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${appointments}" var="a">
                                                    <tr>
                                                        <td class="fw-bold text-muted">#${a.appointmentId}</td>
                                                        <td class="fw-semibold">
                                                            <fmt:formatDate value="${a.appointmentDate}" pattern="dd/MM/yyyy" />
                                                        </td>
                                                        <td><span class="badge bg-light text-dark border">${a.appointmentTime}</span></td>
                                                        <td>${a.doctorName}</td>
                                                        <td>${a.room}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${a.status == 'Pending'}"><span class="status-badge status-pending">Chờ xác nhận</span></c:when>
                                                                <c:when test="${a.status == 'Confirmed'}"><span class="status-badge status-confirmed">Đã xác nhận</span></c:when>
                                                                <c:when test="${a.status == 'Checked In'}"><span class="status-badge status-checkedin">Đã check-in</span></c:when>
                                                                <c:when test="${a.status == 'Completed'}"><span class="status-badge status-completed">Hoàn thành</span></c:when>
                                                                <c:when test="${a.status == 'Cancelled'}"><span class="status-badge status-cancelled">Đã hủy</span></c:when>
                                                                <c:otherwise><span class="status-badge bg-secondary text-white">${a.status}</span></c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class="text-end">
                                                            <c:if test="${a.status == 'Pending'}">
                                                                <button class="btn btn-sm btn-outline-danger rounded-pill px-3" onclick="cancelAppointment(${a.appointmentId})">Hủy lịch</button>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
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