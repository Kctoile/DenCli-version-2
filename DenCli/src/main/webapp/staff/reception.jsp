<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bàn Tiếp đón & Điều phối Bệnh nhân | DenCli</title>
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

                    <div class="card card-custom bg-white p-4">
                        <div class="d-flex flex-wrap justify-content-between align-items-center border-bottom pb-3 mb-3 gap-2">
                            <div>
                                <h4 class="fw-bold text-primary mb-1">💁 Tiếp đón & Phân phối Lịch hẹn</h4>
                                <p class="text-muted small mb-0">Quản lý tiếp nhận bệnh nhân, check-in phòng khám và điều phối thanh toán.</p>
                            </div>

                            <!-- Bộ lọc Trạng thái và Ngày hẹn -->
                            <form action="${pageContext.request.contextPath}/staff/reception" method="GET" class="d-flex flex-wrap gap-2 align-items-center">
                                <select name="status" class="form-select form-select-sm" style="width: auto;">
                                    <option value="" ${empty selectedStatus ? 'selected' : ''}>-- Tất cả trạng thái --</option>
                                    <option value="Pending" ${selectedStatus == 'Pending' ? 'selected' : ''}>Chờ xác nhận</option>
                                    <option value="Confirmed" ${selectedStatus == 'Confirmed' ? 'selected' : ''}>Đã xác nhận</option>
                                    <option value="Checked In" ${selectedStatus == 'Checked In' ? 'selected' : ''}>Đã tiếp đón (Checked-in)</option>
                                    <option value="Completed" ${selectedStatus == 'Completed' ? 'selected' : ''}>Đã hoàn thành</option>
                                    <option value="Cancelled" ${selectedStatus == 'Cancelled' ? 'selected' : ''}>Đã hủy</option>
                                </select>
                                <input type="date" name="date" class="form-control form-control-sm" value="${selectedDate}" style="width: auto;">
                                <button type="submit" class="btn btn-sm btn-primary">Lọc</button>
                                <a href="${pageContext.request.contextPath}/staff/reception" class="btn btn-sm btn-outline-secondary">Xóa lọc</a>
                            </form>
                        </div>

                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Mã hẹn</th>
                                        <th scope="col">Ngày khám</th>
                                        <th scope="col">Giờ khám</th>
                                        <th scope="col">Mã BN</th>
                                        <th scope="col">BS phụ trách</th>
                                        <th scope="col">Phòng</th>
                                        <th scope="col">Ghi chú</th>
                                        <th scope="col">Trạng thái</th>
                                        <th scope="col" class="text-center">Thao tác nhanh</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty appointments}">
                                            <tr>
                                                <td colspan="9" class="text-center py-4 text-muted">
                                                    Không tìm thấy lịch hẹn nào theo điều kiện tìm kiếm.
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
                                                    <td>BS-#<c:out value="${app.doctorId}" /></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty app.room}"><c:out value="${app.room}" /></c:when>
                                                            <c:otherwise><span class="text-muted">—</span></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="small text-muted" style="max-width: 180px;">
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
                                                                <span class="badge bg-primary">Đã check-in</span>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Completed'}">
                                                                <span class="badge bg-success">Đã khám xong</span>
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
                                                        <div class="btn-group btn-group-sm" role="group">
                                                            <c:choose>
                                                                <c:when test="${app.status == 'Pending'}">
                                                                    <button type="button" class="btn btn-outline-success" 
                                                                            onclick="quickAction('confirm', ${app.appointmentId})" title="Xác nhận lịch hẹn">
                                                                        ✓ Xác nhận
                                                                    </button>
                                                                    <button type="button" class="btn btn-outline-danger" 
                                                                            onclick="quickAction('cancel', ${app.appointmentId})" title="Hủy lịch hẹn">
                                                                        ✕ Hủy
                                                                    </button>
                                                                </c:when>
                                                                <c:when test="${app.status == 'Confirmed'}">
                                                                    <button type="button" class="btn btn-primary fw-semibold" 
                                                                            onclick="openCheckinModal(${app.appointmentId}, '${app.room}')" title="Tiếp đón bệnh nhân">
                                                                        💁 Check-in
                                                                    </button>
                                                                    <button type="button" class="btn btn-outline-danger" 
                                                                            onclick="quickAction('cancel', ${app.appointmentId})" title="Hủy lịch hẹn">
                                                                        ✕
                                                                    </button>
                                                                </c:when>
                                                                <c:when test="${app.status == 'Checked In'}">
                                                                    <a href="${pageContext.request.contextPath}/staff/invoice?appointment_id=${app.appointmentId}" 
                                                                       class="btn btn-success fw-semibold">
                                                                        🧾 Thu ngân
                                                                    </a>
                                                                </c:when>
                                                                <c:when test="${app.status == 'Completed'}">
                                                                    <a href="${pageContext.request.contextPath}/staff/invoice?appointment_id=${app.appointmentId}" 
                                                                       class="btn btn-outline-secondary">
                                                                        Xem hóa đơn
                                                                    </a>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="text-muted small">—</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
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

    <!-- Modal Check-in phân phòng -->
    <div class="modal fade" id="checkinModal" tabindex="-1" aria-labelledby="checkinModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content border-0 shadow">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title fw-bold" id="checkinModalLabel">💁 Tiếp đón bệnh nhân & Phân phòng khám</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body p-4">
                    <p class="mb-3 text-muted">Xác nhận bệnh nhân đã có mặt tại sảnh tiếp đón cho <strong>Cuộc hẹn #<span id="checkinAppIdText"></span></strong>.</p>
                    <input type="hidden" id="checkinAppointmentId" value="">

                    <div class="mb-3">
                        <label for="checkinRoom" class="form-label fw-semibold">Phân phòng khám chuyên khoa</label>
                        <select id="checkinRoom" class="form-select">
                            <option value="Phòng khám 101 - Răng hàm mặt">Phòng khám 101 - Răng hàm mặt</option>
                            <option value="Phòng khám 102 - Chỉnh nha & Niềng">Phòng khám 102 - Chỉnh nha & Niềng</option>
                            <option value="Phòng khám 103 - Cấy ghép Implant">Phòng khám 103 - Cấy ghép Implant</option>
                            <option value="Phòng khám 104 - Tẩy trắng & Thẩm mỹ">Phòng khám 104 - Tẩy trắng & Thẩm mỹ</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-primary fw-semibold px-4" onclick="submitCheckin()">Xác nhận Check-in</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- AJAX Fetch Actions: Check-in, Xác nhận, Hủy hẹn -->
    <script>
        function openCheckinModal(appId, currentRoom) {
            document.getElementById('checkinAppointmentId').value = appId;
            document.getElementById('checkinAppIdText').textContent = appId;
            if (currentRoom && currentRoom.trim() !== '') {
                document.getElementById('checkinRoom').value = currentRoom;
            }
            var modal = new bootstrap.Modal(document.getElementById('checkinModal'));
            modal.show();
        }

        function submitCheckin() {
            var appId = document.getElementById('checkinAppointmentId').value;
            var room = document.getElementById('checkinRoom').value;

            fetch('${pageContext.request.contextPath}/staff/reception', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
                body: JSON.stringify({ action: 'checkin', appointment_id: parseInt(appId), room: room })
            })
            .then(function(res) { return res.json(); })
            .then(function(data) {
                if (data.success) {
                    showToast('success', data.message || 'Check-in thành công!');
                    setTimeout(function() { window.location.reload(); }, 1000);
                } else {
                    showToast('danger', data.message || 'Check-in thất bại.');
                }
            })
            .catch(function(err) {
                showToast('danger', 'Lỗi kết nối máy chủ.');
            });
        }

        function quickAction(actionType, appId) {
            var actionName = actionType === 'confirm' ? 'xác nhận' : 'hủy';
            if (!confirm('Bạn có chắc chắn muốn ' + actionName + ' lịch hẹn #' + appId + ' không?')) {
                return;
            }

            fetch('${pageContext.request.contextPath}/staff/reception', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
                body: JSON.stringify({ action: actionType, appointment_id: appId })
            })
            .then(function(res) { return res.json(); })
            .then(function(data) {
                if (data.success) {
                    showToast('success', data.message || 'Thao tác thành công!');
                    setTimeout(function() { window.location.reload(); }, 1000);
                } else {
                    showToast('danger', data.message || 'Thao tác thất bại.');
                }
            })
            .catch(function(err) {
                showToast('danger', 'Lỗi kết nối máy chủ.');
            });
        }
    </script>
</body>
</html>
