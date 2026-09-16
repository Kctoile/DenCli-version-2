<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lịch khám nha khoa | DenCli</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: #f8fafc; }
        .card-custom { border: none; border-radius: 0.75rem; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- Header dùng chung -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <!-- Sidebar điều hướng theo Role -->
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

            <!-- Nội dung chính -->
            <main class="col-12 col-md-9 col-lg-10 p-4">
                <div class="card card-custom bg-white p-4 mx-auto" style="max-width: 860px;">
                    <div class="border-bottom pb-3 mb-4">
                        <h3 class="fw-bold text-primary mb-1">📅 Đăng ký Đặt lịch khám Bệnh</h3>
                        <p class="text-muted mb-0">Chọn bác sĩ phụ trách, thời gian và dịch vụ nha khoa mong muốn.</p>
                    </div>

                    <!-- Thông báo lỗi hoặc thành công -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <strong>⚠️ Có lỗi xảy ra:</strong> <c:out value="${errorMessage}" />
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.successMessage}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <strong>✅ Thành công:</strong> <c:out value="${sessionScope.successMessage}" />
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <c:remove var="successMessage" scope="session" />
                    </c:if>

                    <form id="bookForm" action="${pageContext.request.contextPath}/customer/book" method="POST">
                        <div class="mb-3">
                            <label for="doctorId" class="form-label fw-semibold">Bác sĩ điều trị <span class="text-danger">*</span></label>
                            <select id="doctorId" name="doctor_id" class="form-select" required>
                                <option value="">-- Chọn Bác sĩ phụ trách khám --</option>
                                <c:forEach items="${doctors}" var="doc">
                                    <option value="${doc.userId}">BS. <c:out value="${doc.fullName}" /></option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label for="appointmentDate" class="form-label fw-semibold">Ngày hẹn khám <span class="text-danger">*</span></label>
                                <input type="date" id="appointmentDate" name="appointment_date" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label for="appointmentTime" class="form-label fw-semibold">Khung giờ hẹn <span class="text-danger">*</span></label>
                                <select id="appointmentTime" name="appointment_time" class="form-select" required>
                                    <option value="">-- Chọn khung giờ --</option>
                                    <option value="08:00">08:00 - 09:00 (Sáng)</option>
                                    <option value="09:00">09:00 - 10:00 (Sáng)</option>
                                    <option value="10:00">10:00 - 11:00 (Sáng)</option>
                                    <option value="11:00">11:00 - 12:00 (Sáng)</option>
                                    <option value="14:00">14:00 - 15:00 (Chiều)</option>
                                    <option value="15:00">15:00 - 16:00 (Chiều)</option>
                                    <option value="16:00">16:00 - 17:00 (Chiều)</option>
                                    <option value="17:00">17:00 - 18:00 (Chiều)</option>
                                </select>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Dịch vụ nha khoa mong muốn</label>
                            <div class="border rounded p-3 bg-light" style="max-height: 200px; overflow-y: auto;">
                                <div class="row g-2">
                                    <c:forEach items="${services}" var="svc">
                                        <div class="col-md-6">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="service_ids" value="${svc.serviceId}" id="svc_${svc.serviceId}">
                                                <label class="form-check-label d-flex justify-content-between pe-3" for="svc_${svc.serviceId}">
                                                    <span><c:out value="${svc.serviceName}" /></span>
                                                    <span class="text-primary fw-semibold">
                                                        <fmt:formatNumber value="${svc.price}" pattern="#,##0" /> VNĐ
                                                    </span>
                                                </label>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="form-text">Bạn có thể chọn một hoặc nhiều dịch vụ trước, bác sĩ sẽ tư vấn thêm khi khám.</div>
                        </div>

                        <div class="mb-4">
                            <label for="notes" class="form-label fw-semibold">Ghi chú triệu chứng hoặc yêu cầu đặc biệt</label>
                            <textarea id="notes" name="notes" class="form-control" rows="3" placeholder="Ví dụ: Đau răng hàm dưới bên trái 2 ngày nay, ê buốt khi uống nước lạnh..."></textarea>
                        </div>

                        <div class="d-flex justify-content-end gap-2">
                            <a href="${pageContext.request.contextPath}/customer/profile" class="btn btn-outline-secondary px-4">Xem lịch sử</a>
                            <button type="submit" id="btnSubmitBook" class="btn btn-primary px-5 fw-semibold">Xác nhận Đặt lịch</button>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </div>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <script>
        // Thiết lập ngày hẹn tối thiểu là ngày hôm nay
        document.addEventListener('DOMContentLoaded', function() {
            var today = new Date().toISOString().split('T')[0];
            var dateInput = document.getElementById('appointmentDate');
            if (dateInput) {
                dateInput.min = today;
                if (!dateInput.value) {
                    dateInput.value = today;
                }
            }
        });
    </script>
</body>
</html>
