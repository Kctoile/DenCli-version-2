<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Buồng khám Bác sĩ | DenCli</title>
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
                <div class="container-fluid p-0">

                    <!-- Thông báo phản hồi -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
                            <strong>⚠️ Lỗi:</strong> <c:out value="${errorMessage}" />
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.successMessage}">
                        <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
                            <strong>✅ Thành công:</strong> <c:out value="${sessionScope.successMessage}" />
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <c:remove var="successMessage" scope="session" />
                    </c:if>

                    <!-- Thẻ danh sách ca khám -->
                    <div class="card card-custom bg-white p-4 mb-4">
                        <div class="d-flex flex-wrap justify-content-between align-items-center border-bottom pb-3 mb-3 gap-2">
                            <div>
                                <h4 class="fw-bold text-primary mb-1">🩺 Buồng khám Bác sĩ</h4>
                                <p class="text-muted small mb-0">Danh sách các ca khám được phân công cho bạn.</p>
                            </div>
                            <!-- Bộ lọc ngày -->
                            <form action="${pageContext.request.contextPath}/doctor/examination" method="GET" class="d-flex gap-2 align-items-center">
                                <label for="filterDate" class="small fw-semibold text-muted text-nowrap">Lọc ngày:</label>
                                <input type="date" id="filterDate" name="date" class="form-control form-control-sm" value="${param.date}">
                                <button type="submit" class="btn btn-sm btn-outline-primary">Lọc</button>
                                <c:if test="${not empty param.date}">
                                    <a href="${pageContext.request.contextPath}/doctor/examination" class="btn btn-sm btn-outline-secondary">Xóa lọc</a>
                                </c:if>
                            </form>
                        </div>

                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Mã hẹn</th>
                                        <th scope="col">Ngày khám</th>
                                        <th scope="col">Giờ hẹn</th>
                                        <th scope="col">Mã BN</th>
                                        <th scope="col">Phòng</th>
                                        <th scope="col">Triệu chứng</th>
                                        <th scope="col">Trạng thái</th>
                                        <th scope="col" class="text-center">Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty appointments}">
                                            <tr>
                                                <td colspan="8" class="text-center py-4 text-muted">
                                                    Hiện chưa có ca khám nào được phân công cho bạn theo thời gian đã chọn.
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach items="${appointments}" var="app">
                                                <tr>
                                                    <td class="fw-bold text-primary">#<c:out value="${app.appointmentId}" /></td>
                                                    <td><c:out value="${app.appointmentDate}" /></td>
                                                    <td><span class="badge bg-light text-dark border"><c:out value="${app.appointmentTime}" /></span></td>
                                                    <td>BN-#<c:out value="${app.patientId}" /></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty app.room}"><c:out value="${app.room}" /></c:when>
                                                            <c:otherwise><span class="text-muted">—</span></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="small text-muted" style="max-width: 200px;">
                                                        <c:choose>
                                                            <c:when test="${not empty app.notes}"><c:out value="${app.notes}" /></c:when>
                                                            <c:otherwise>—</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${app.status == 'Pending'}">
                                                                <span class="badge bg-warning text-dark">Chờ xác nhận</span>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Confirmed'}">
                                                                <span class="badge bg-info text-dark">Đã xác nhận</span>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Checked In'}">
                                                                <span class="badge bg-primary">Đang chờ khám</span>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Completed'}">
                                                                <span class="badge bg-success">Đã hoàn thành</span>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Cancelled'}">
                                                                <span class="badge bg-danger">Đã hủy</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary"><c:out value="${app.status}" /></span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="text-center">
                                                        <button type="button" class="btn btn-sm btn-primary fw-semibold" 
                                                                onclick="openExamModal(${app.appointmentId}, '${app.patientId}')">
                                                            🩺 Khám & Chẩn đoán
                                                        </button>
                                                        <a href="${pageContext.request.contextPath}/doctor/prescription" class="btn btn-sm btn-outline-success ms-1">
                                                            💊 Kê đơn
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
            </main>
        </div>
    </div>

    <!-- Modal Lập Bệnh án & Chẩn đoán lâm sàng -->
    <div class="modal fade" id="examModal" tabindex="-1" aria-labelledby="examModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content border-0 shadow">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title fw-bold" id="examModalLabel">🩺 Ghi nhận Chẩn đoán & Chỉ định Dịch vụ</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="${pageContext.request.contextPath}/doctor/examination" method="POST">
                    <div class="modal-body p-4">
                        <div class="alert alert-info py-2 mb-3 small">
                            Đang xử lý kết quả ca khám cho: <strong>Lịch hẹn #<span id="modalAppIdText"></span></strong> (Mã BN: #<span id="modalPatientIdText"></span>)
                        </div>
                        <input type="hidden" id="modalAppointmentId" name="appointment_id" value="">

                        <div class="mb-3">
                            <label for="modalDiagnosis" class="form-label fw-semibold">Kết luận chẩn đoán lâm sàng <span class="text-danger">*</span></label>
                            <textarea id="modalDiagnosis" name="diagnosis" class="form-control" rows="3" required placeholder="Ví dụ: Viêm tủy răng hàm dưới P.36 giai đoạn cấp, có lỗ sâu mặt nhai..."></textarea>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Chỉ định thêm dịch vụ / Thủ thuật điều trị</label>
                            <div class="border rounded p-3 bg-light" style="max-height: 200px; overflow-y: auto;">
                                <div class="row g-2">
                                    <c:forEach items="${services}" var="svc">
                                        <div class="col-md-6">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="service_ids" value="${svc.serviceId}" id="modal_svc_${svc.serviceId}">
                                                <label class="form-check-label d-flex justify-content-between pe-2" for="modal_svc_${svc.serviceId}">
                                                    <span class="small"><c:out value="${svc.serviceName}" /></span>
                                                    <span class="text-primary fw-semibold small">
                                                        <fmt:formatNumber value="${svc.price}" pattern="#,##0" />đ
                                                    </span>
                                                </label>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="form-text">Các thủ thuật/dịch vụ chỉ định thêm sẽ được tự động cộng vào hóa đơn viện phí.</div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-primary fw-semibold px-4">Lưu Kết Quả Khám</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <script>
        function openExamModal(appId, patientId) {
            document.getElementById('modalAppointmentId').value = appId;
            document.getElementById('modalAppIdText').textContent = appId;
            document.getElementById('modalPatientIdText').textContent = patientId;
            var modal = new bootstrap.Modal(document.getElementById('examModal'));
            modal.show();
        }
    </script>
</body>
</html>
