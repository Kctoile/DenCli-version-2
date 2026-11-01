<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bàn Tiếp Đón & Điều Phối Phòng Khám | DenCli</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Google Fonts: Plus Jakarta Sans & Figtree -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Figtree:wght@400;500;600;700&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=7">

    <style>
        /* Reception Dashboard Specific UX Styles */
        .kpi-stat-card {
            background: #FFFFFF;
            border-radius: 12px;
            padding: 1rem 1.25rem;
            border: 1px solid #E2E8F0;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
            display: flex;
            align-items: center;
            justify-content: space-between;
            transition: all 0.2s ease;
        }
        .kpi-stat-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
        }
        .kpi-icon {
            width: 44px;
            height: 44px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.25rem;
        }
        .table-reception th {
            font-size: 0.8125rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            color: #64748B;
            font-weight: 700;
            background-color: #F8FAFC;
            padding: 0.85rem 1rem;
            border-bottom: 2px solid #E2E8F0;
        }
        .table-reception td {
            padding: 1rem 1rem;
            vertical-align: middle;
            font-size: 0.875rem;
        }
        .patient-avatar {
            width: 38px;
            height: 38px;
            border-radius: 50%;
            background: #E0F2FE;
            color: #0369A1;
            font-weight: 700;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 0.95rem;
        }
    </style>
</head>

<body class="d-flex flex-column min-vh-100" style="background-color: #F8FAFC;">

    <!-- Header dùng chung -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <!-- Sidebar điều hướng theo Role -->
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

            <!-- Nội dung chính -->
            <main class="col-12 col-md-9 col-lg-10 p-4 p-lg-5">
                
                <!-- Toast & Alert Notifications -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show mb-4 rounded-3 border-0 shadow-sm" role="alert">
                        <strong>⚠️ Thông báo lỗi:</strong>
                        <c:out value="${errorMessage}" />
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="alert alert-success alert-dismissible fade show mb-4 rounded-3 border-0 shadow-sm" role="alert">
                        <strong>✅ Thành công:</strong>
                        <c:out value="${sessionScope.successMessage}" />
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <c:remove var="successMessage" scope="session" />
                </c:if>

                <!-- Page Header Bar -->
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4 pb-3 border-bottom">
                    <div>
                        <div class="d-flex align-items-center gap-2 mb-1">
                            <span class="fs-4">💁</span>
                            <h3 class="fw-bold text-dark mb-0">Bàn Tiếp Đón & Điều Phối Phòng Khám</h3>
                        </div>
                        <p class="text-muted small mb-0">
                            Tiếp nhận bệnh nhân, xác nhận hẹn trước, check-in phân phòng chuyên khoa và điều phối thanh toán viện phí.
                        </p>
                    </div>
                    <div class="d-flex align-items-center gap-2">
                        <button type="button" class="btn btn-outline-secondary rounded-pill px-3 py-2 fw-semibold btn-sm" onclick="window.location.reload()">
                            🔄 Làm mới bảng
                        </button>
                        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-primary rounded-pill px-3 py-2 fw-semibold btn-sm shadow-sm" style="background: #0891B2; border-color: #0891B2;">
                            + Tạo lịch Walk-in tại quầy
                        </a>
                    </div>
                </div>

                <!-- Main Content Card with Filters -->
                <div class="dencli-card bg-white shadow-sm rounded-4 border-0 p-4">
                    
                    <!-- Search and Filters Bar -->
                    <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4 pb-3 border-bottom">
                        <div class="d-flex align-items-center gap-2">
                            <input type="text" id="patientSearchInput" class="form-control form-control-sm rounded-pill px-3" placeholder="🔍 Tìm theo tên hoặc SĐT bệnh nhân..." style="width: 280px;">
                        </div>

                        <!-- Form Lọc Ngày và Trạng thái -->
                        <form action="${pageContext.request.contextPath}/staff/reception" method="GET" class="d-flex flex-wrap gap-2 align-items-center">
                            <label for="statusFilter" class="visually-hidden">Trạng thái</label>
                            <select id="statusFilter" name="status" class="form-select form-select-sm rounded-pill" style="width: auto;">
                                <option value="" ${empty selectedStatus ? 'selected' : ''}>-- Tất cả trạng thái --</option>
                                <option value="Pending" ${selectedStatus == 'Pending' ? 'selected' : ''}>⏳ Chờ xác nhận</option>
                                <option value="Confirmed" ${selectedStatus == 'Confirmed' ? 'selected' : ''}>📅 Đã xác nhận</option>
                                <option value="Checked In" ${selectedStatus == 'Checked In' ? 'selected' : ''}>🩺 Đã tiếp đón (Checked-in)</option>
                                <option value="Completed" ${selectedStatus == 'Completed' ? 'selected' : ''}>✅ Đã hoàn thành</option>
                                <option value="Cancelled" ${selectedStatus == 'Cancelled' ? 'selected' : ''}>❌ Đã hủy</option>
                            </select>

                            <label for="dateFilter" class="visually-hidden">Ngày hẹn</label>
                            <input type="date" id="dateFilter" name="date" class="form-control form-control-sm rounded-pill" value="${selectedDate}" style="width: auto;">
                            
                            <button type="submit" class="btn btn-sm btn-primary rounded-pill px-3" style="background: #0891B2; border-color: #0891B2;">Lọc</button>
                            <a href="${pageContext.request.contextPath}/staff/reception" class="btn btn-sm btn-outline-secondary rounded-pill px-3">Bỏ lọc</a>
                        </form>
                    </div>

                    <!-- Appointments Table -->
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0 table-reception" id="receptionTable">
                            <thead>
                                <tr>
                                    <th scope="col">Mã Hẹn</th>
                                    <th scope="col">Bệnh Nhân</th>
                                    <th scope="col">Ngày & Giờ</th>
                                    <th scope="col">Bác Sĩ Phụ Trách</th>
                                    <th scope="col">Phòng Khám</th>
                                    <th scope="col">Ghi Chú</th>
                                    <th scope="col">Trạng Thái</th>
                                    <th scope="col" class="text-center">Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty appointments}">
                                        <tr>
                                            <td colspan="8" class="text-center py-5">
                                                <div class="empty-state-box">
                                                    <div class="fs-1 text-muted mb-2">💁</div>
                                                    <h6 class="fw-bold text-dark mb-1">Không tìm thấy ca khám nào</h6>
                                                    <p class="text-muted small mb-0">Không có lịch hẹn nào phù hợp với bộ lọc ngày hoặc trạng thái hiện tại.</p>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${appointments}" var="app">
                                            <tr class="appointment-row" data-patient="<c:out value='${app.patientName}' />" data-phone="<c:out value='${app.patientPhone}' />">
                                                <!-- Mã hẹn -->
                                                <td class="fw-bold text-primary">
                                                    #<c:out value="${app.appointmentId}" />
                                                </td>

                                                <!-- Bệnh nhân (Tên + Phone) -->
                                                <td>
                                                    <div class="d-flex align-items-center gap-2">
                                                        <div class="patient-avatar">
                                                            ${not empty app.patientName ? app.patientName.substring(0, 1).toUpperCase() : 'B'}
                                                        </div>
                                                        <div>
                                                            <strong class="text-dark d-block">
                                                                <c:out value="${not empty app.patientName ? app.patientName : 'Khách vãng lai'}" />
                                                            </strong>
                                                            <small class="text-muted">
                                                                <c:out value="${not empty app.patientPhone ? app.patientPhone : 'BN-#' += app.patientId}" />
                                                            </small>
                                                        </div>
                                                    </div>
                                                </td>

                                                <!-- Ngày & Giờ -->
                                                <td>
                                                    <div>
                                                        <span class="badge bg-light text-dark border px-2 py-1 fw-bold">
                                                            <c:out value="${app.appointmentTime}" />
                                                        </span>
                                                        <small class="d-block text-muted mt-1">
                                                            <c:out value="${app.appointmentDate}" />
                                                        </small>
                                                    </div>
                                                </td>

                                                <!-- Bác sĩ phụ trách -->
                                                <td>
                                                    <div class="d-flex align-items-center gap-1">
                                                        <span>🩺</span>
                                                        <strong class="text-dark small">
                                                            <c:out value="${not empty app.doctorName ? 'BS. ' += app.doctorName : 'BS-#' += app.doctorId}" />
                                                        </strong>
                                                    </div>
                                                </td>

                                                <!-- Phòng khám -->
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty app.room}">
                                                            <span class="badge px-2 py-1 rounded" style="background: #EFF6FF; color: #1E40AF; border: 1px solid #BFDBFE;">
                                                                <c:out value="${app.room}" />
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise><span class="text-muted small">Chưa xếp phòng</span></c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <!-- Ghi chú -->
                                                <td class="small text-muted" style="max-width: 180px;">
                                                    <c:choose>
                                                        <c:when test="${not empty app.notes}">
                                                            <span class="d-inline-block text-truncate" style="max-width: 170px;" title="<c:out value='${app.notes}' />">
                                                                <c:out value="${app.notes}" />
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>—</c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <!-- Trạng thái (Pills) -->
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${app.status == 'Pending'}">
                                                            <span class="badge rounded-pill px-3 py-2 fw-semibold" style="background: #FEF3C7; color: #92400E;">⏳ Chờ xác nhận</span>
                                                        </c:when>
                                                        <c:when test="${app.status == 'Confirmed'}">
                                                            <span class="badge rounded-pill px-3 py-2 fw-semibold" style="background: #E0F2FE; color: #0369A1;">📅 Đã xác nhận</span>
                                                        </c:when>
                                                        <c:when test="${app.status == 'Checked In'}">
                                                            <span class="badge rounded-pill px-3 py-2 fw-semibold" style="background: #CCFBF1; color: #0F766E;">🩺 Đã check-in</span>
                                                        </c:when>
                                                        <c:when test="${app.status == 'Completed'}">
                                                            <span class="badge rounded-pill px-3 py-2 fw-semibold" style="background: #DCFCE7; color: #166534;">✅ Đã khám xong</span>
                                                        </c:when>
                                                        <c:when test="${app.status == 'Cancelled'}">
                                                            <span class="badge rounded-pill px-3 py-2 fw-semibold" style="background: #FEE2E2; color: #991B1B;">❌ Đã hủy</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary text-white rounded-pill px-3 py-2"><c:out value="${app.status}" /></span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <!-- Thao tác nhanh -->
                                                <td class="text-center">
                                                    <div class="btn-group btn-group-sm">
                                                        <c:choose>
                                                            <c:when test="${app.status == 'Pending'}">
                                                                <button type="button" class="btn btn-outline-success rounded-start-pill px-3" onclick="quickAction('confirm', <c:out value='${app.appointmentId}' />)" title="Gọi xác nhận lịch hẹn">
                                                                    ✓ Xác nhận
                                                                </button>
                                                                <button type="button" class="btn btn-outline-danger rounded-end-pill px-2" onclick="quickAction('cancel', <c:out value='${app.appointmentId}' />)" title="Hủy lịch hẹn">
                                                                    ✕
                                                                </button>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Confirmed'}">
                                                                <button type="button" class="btn btn-primary fw-semibold rounded-pill px-3 shadow-sm" style="background: #0891B2; border-color: #0891B2;" data-app-id="<c:out value='${app.appointmentId}' />" data-room="<c:out value='${app.room}' />" data-patient="<c:out value='${app.patientName}' />" data-doctor="<c:out value='${app.doctorName}' />" onclick="openCheckinModalFromBtn(this)" title="Bệnh nhân đã đến phòng khám">
                                                                    💁 Check-in
                                                                </button>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Checked In'}">
                                                                <a href="${pageContext.request.contextPath}/staff/invoice?appointment_id=${app.appointmentId}" class="btn btn-success fw-semibold rounded-pill px-3 shadow-sm">
                                                                    🧾 Thu ngân
                                                                </a>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Completed'}">
                                                                <a href="${pageContext.request.contextPath}/staff/invoice?appointment_id=${app.appointmentId}" class="btn btn-outline-secondary rounded-pill px-3">
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

            </main>
        </div>
    </div>

    <!-- Modal Check-in Phân Phòng Hiện Đại -->
    <div class="modal fade" id="checkinModal" tabindex="-1" aria-labelledby="checkinModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                <div class="modal-header text-white px-4 py-3" style="background: linear-gradient(135deg, #075985 0%, #0891B2 100%);">
                    <h5 class="modal-title fw-bold" id="checkinModalLabel">💁 Tiếp Đón & Phân Phòng Khám</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="p-3 bg-light rounded-3 mb-3 border">
                        <div class="d-flex justify-content-between mb-1">
                            <span class="text-muted small">Bệnh nhân:</span>
                            <strong id="modalPatientName" class="text-dark">---</strong>
                        </div>
                        <div class="d-flex justify-content-between">
                            <span class="text-muted small">Bác sĩ phụ trách:</span>
                            <strong id="modalDoctorName" class="text-dark">---</strong>
                        </div>
                    </div>

                    <input type="hidden" id="checkinAppointmentId" value="">

                    <div class="mb-3">
                        <label for="checkinRoom" class="form-label fw-bold text-dark">Chỉ định phòng khám lâm sàng</label>
                        <select id="checkinRoom" class="form-select form-select-lg rounded-3">
                            <option value="Phòng 101 - Khám Tổng Quát & Nha Chu">Phòng 101 - Khám Tổng Quát & Nha Chu</option>
                            <option value="Phòng 102 - Chỉnh Nha & Niềng Răng">Phòng 102 - Chỉnh Nha & Niềng Răng</option>
                            <option value="Phòng 103 - Cấy Ghép Implant Châu Âu">Phòng 103 - Cấy Ghép Implant Châu Âu</option>
                            <option value="Phòng 104 - Tẩy Trắng & Thẩm Mỹ Răng Sứ">Phòng 104 - Tẩy Trắng & Thẩm Mỹ Răng Sứ</option>
                            <option value="Phòng 105 - Điều Trị Tủy & Răng Khôn">Phòng 105 - Điều Trị Tủy & Răng Khôn</option>
                        </select>
                        <div class="form-text text-muted">Hệ thống sẽ đồng bộ phòng khám tới màn hình Dashboard của Bác sĩ tương ứng.</div>
                    </div>
                </div>
                <div class="modal-footer px-4 py-3 bg-light border-0">
                    <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-bs-dismiss="modal">Hủy bỏ</button>
                    <button type="button" class="btn btn-primary rounded-pill px-4 fw-bold shadow-sm" style="background: #0891B2; border-color: #0891B2;" onclick="submitCheckin()">
                        Xác Nhận Check-in
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- Script tương tác nhanh -->
    <script>
        // 1. Tìm kiếm nhanh trên bảng bệnh nhân
        var searchInput = document.getElementById('patientSearchInput');
        if (searchInput) {
            searchInput.addEventListener('input', function() {
                var query = this.value.toLowerCase().trim();
                var rows = document.querySelectorAll('.appointment-row');
                rows.forEach(function(row) {
                    var patient = (row.getAttribute('data-patient') || '').toLowerCase();
                    var phone = (row.getAttribute('data-phone') || '').toLowerCase();
                    if (patient.includes(query) || phone.includes(query)) {
                        row.style.display = '';
                    } else {
                        row.style.display = 'none';
                    }
                });
            });
        }

                function openCheckinModalFromBtn(btn) {
            openCheckinModal(btn.dataset.appId, btn.dataset.room, btn.dataset.patient, btn.dataset.doctor);
        }

        // 2. Mở Modal Check-in
        function openCheckinModal(appId, currentRoom, patientName, doctorName) {
            document.getElementById('checkinAppointmentId').value = appId;
            document.getElementById('modalPatientName').textContent = patientName || 'Khách vãng lai';
            document.getElementById('modalDoctorName').textContent = doctorName ? ('BS. ' + doctorName) : 'Theo phân bổ';

            if (currentRoom && currentRoom.trim() !== '') {
                document.getElementById('checkinRoom').value = currentRoom;
            }
            var modal = new bootstrap.Modal(document.getElementById('checkinModal'));
            modal.show();
        }

        // 3. Submit Check-in qua Fetch API
        function submitCheckin() {
            var appId = document.getElementById('checkinAppointmentId').value;
            var room = document.getElementById('checkinRoom').value;

            fetch('${pageContext.request.contextPath}/staff/reception', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    'X-CSRF-TOKEN': window.CSRF_TOKEN || ''
                },
                body: JSON.stringify({
                    action: 'checkin',
                    appointment_id: parseInt(appId),
                    room: room
                })
            })
            .then(function(res) { return res.json(); })
            .then(function(data) {
                if (data.success) {
                    if (typeof window.showToast === 'function') {
                        window.showToast('success', data.message || 'Check-in bệnh nhân thành công!');
                    }
                    setTimeout(function() { window.location.reload(); }, 900);
                } else {
                    if (typeof window.showToast === 'function') {
                        window.showToast('danger', data.message || 'Check-in thất bại.');
                    }
                }
            })
            .catch(function(err) {
                if (typeof window.showToast === 'function') {
                    window.showToast('danger', 'Lỗi kết nối tới máy chủ.');
                }
            });
        }

        // 4. Quick Action (Xác nhận hoặc Hủy lịch)
        function quickAction(actionType, appId) {
            var actionName = actionType === 'confirm' ? 'XÁC NHẬN' : 'HỦY';
            if (!confirm('Bạn có chắc chắn muốn ' + actionName + ' cuộc hẹn #' + appId + ' không?')) {
                return;
            }

            fetch('${pageContext.request.contextPath}/staff/reception', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    'X-CSRF-TOKEN': window.CSRF_TOKEN || ''
                },
                body: JSON.stringify({
                    action: actionType,
                    appointment_id: appId
                })
            })
            .then(function(res) { return res.json(); })
            .then(function(data) {
                if (data.success) {
                    if (typeof window.showToast === 'function') {
                        window.showToast('success', data.message || 'Thao tác thành công!');
                    }
                    setTimeout(function() { window.location.reload(); }, 900);
                } else {
                    if (typeof window.showToast === 'function') {
                        window.showToast('danger', data.message || 'Thao tác thất bại.');
                    }
                }
            })
            .catch(function(err) {
                if (typeof window.showToast === 'function') {
                    window.showToast('danger', 'Lỗi kết nối máy chủ.');
                }
            });
        }
    </script>
    <script>
        window.APP_CONTEXT_PATH = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/appointment-realtime.js"></script>
</body>

</html>
