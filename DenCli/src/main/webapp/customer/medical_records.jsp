<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Hồ sơ bệnh án | DenCli</title>
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
                                    <h4 class="page-header-title">📋 Hồ sơ bệnh án & Lịch sử điều trị</h4>
                                    <p class="page-header-subtitle">Chi tiết kết quả chẩn đoán, thủ thuật đã làm và đơn thuốc cho từng đợt khám.</p>
                                </div>
                            </div>

                            <c:if test="${empty records}">
                                <div class="dencli-card p-5 text-center">
                                    <div class="empty-state-icon">📋</div>
                                    <h5 class="fw-bold mb-2">Chưa có hồ sơ bệnh án</h5>
                                    <p class="text-muted small mb-0">Hồ sơ bệnh án sẽ xuất hiện tự động sau khi bác sĩ hoàn thành ca khám của bạn.</p>
                                </div>
                            </c:if>

                            <c:forEach items="${records}" var="rec">
                                <div class="dencli-card mb-4 overflow-hidden">
                                    <div class="p-3 bg-light border-bottom d-flex justify-content-between align-items-center">
                                        <strong class="text-navy">
                                            🩺 Khám ngày <fmt:formatDate value="${rec.appointment.appointmentDate}" pattern="dd/MM/yyyy"/> — Lịch hẹn #${rec.appointment.appointmentId}
                                        </strong>
                                        <span class="status-badge status-completed">Đã hoàn thành</span>
                                    </div>
                                    <div class="p-4">
                                        <p class="mb-2 text-muted">
                                            <strong>Bác sĩ:</strong> ${rec.appointment.doctorName} &nbsp;|&nbsp;
                                            <strong>Phòng:</strong> ${rec.appointment.room} &nbsp;|&nbsp;
                                            <strong>Giờ hẹn:</strong> ${rec.appointment.appointmentTime}
                                        </p>
                                        <p class="mb-2"><strong>Chẩn đoán:</strong> <span class="text-dark">${rec.diagnosis}</span></p>

                                        <p class="mb-2"><strong>Tổng chi phí:</strong>
                                            <span class="fw-bold text-primary"><fmt:formatNumber value="${rec.invoice.grandTotal}" type="number" /> đ</span>
                                        </p>

                                        <c:if test="${not empty rec.services}">
                                            <p class="mb-1 fw-semibold text-navy">Dịch vụ đã thực hiện:</p>
                                            <ul class="mb-3 text-muted ps-3">
                                                <c:forEach items="${rec.services}" var="ps">
                                                    <li>
                                                        ${ps.serviceName}
                                                        <c:if test="${not empty ps.price and ps.price > 0}"> —
                                                            <fmt:formatNumber value="${ps.price}" type="number" /> đ</c:if>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </c:if>

                                        <c:if test="${not empty rec.prescription}">
                                            <p class="mb-1 fw-semibold text-navy">Đơn thuốc & Lời dặn:</p>
                                            <p class="mb-2 small text-muted fst-italic">
                                                <c:out value="${rec.prescription.instructions}" />
                                            </p>
                                            <ul class="mb-0 text-muted ps-3">
                                                <c:forEach items="${rec.prescription.details}" var="medicine">
                                                    <li>
                                                        <c:out value="${medicine.medicineName}" /> — ${medicine.prescribedQuantity} viên
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </main>
                    </div>
                </div>
                <jsp:include page="/WEB-INF/views/common/footer.jsp" />
            </body>

            </html>