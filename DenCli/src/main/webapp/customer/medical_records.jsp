<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Hồ sơ bệnh án | DenCli</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=3">
            </head>

            <body class="d-flex flex-column min-vh-100">
                <jsp:include page="/WEB-INF/views/common/header.jsp" />
                <div class="container-fluid flex-grow-1">
                    <div class="row min-vh-100">
                        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
                        <main class="col-12 col-md-9 col-lg-10 p-4">
                            <h4 class="fw-bold mb-4">📋 Hồ sơ bệnh án & lịch sử điều trị</h4>
                            <c:if test="${empty records}">
                                <div class="alert alert-info">Chưa có lần khám nào được ghi nhận.</div>
                            </c:if>
                            <c:forEach items="${records}" var="rec">
                                <div class="card mb-3 shadow-sm">
                                    <div class="card-header d-flex justify-content-between align-items-center">
                                        <strong>Khám ngày <fmt:formatDate value="${rec.appointment.appointmentDate}" pattern="dd/MM/yyyy"/> — #${rec.appointment.appointmentId}</strong>
                                        <span class="badge bg-success">Hoàn thành</span>
                                    </div>
                                    <div class="card-body">
                                        <p class="mb-2">
                                            <strong>Bác sĩ:</strong> ${rec.appointment.doctorName} &nbsp;|&nbsp;
                                            <strong>Phòng:</strong> ${rec.appointment.room} &nbsp;|&nbsp;
                                            <strong>Giờ hẹn:</strong> ${rec.appointment.appointmentTime}
                                        </p>
                                        <p class="mb-2"><strong>Chẩn đoán:</strong> ${rec.diagnosis}</p>

                                        <p class="mb-2"><strong>Tổng chi phí:</strong>
                                            <fmt:formatNumber value="${rec.invoice.grandTotal}" type="number" /> đ
                                        </p>

                                        <c:if test="${not empty rec.services}">
                                            <p class="mb-1"><strong>Dịch vụ đã thực hiện:</strong></p>
                                            <ul class="mb-2">
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
                                            <p class="mb-1"><strong>Đơn thuốc:</strong>
                                                <c:out value="${rec.prescription.instructions}" />
                                            </p>
                                            <ul class="mb-0">
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
            </body>

            </html>