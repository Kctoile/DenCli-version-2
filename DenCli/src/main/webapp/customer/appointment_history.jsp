<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch hẹn của tôi | DenCli Dental Clinic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Figtree:wght@400;500;600;700&family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=7">
</head>

<body class="d-flex flex-column min-vh-100" style="background-color: #F8FAFC;">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

            <main class="col-12 col-md-9 col-lg-10 p-4 p-lg-5">
                <!-- Page Header -->
                <div class="page-header-box mb-4">
                    <div>
                        <h4 class="page-header-title d-flex align-items-center gap-2">
                            <span>🗓️</span> Lịch Hẹn Của Tôi
                        </h4>
                        <p class="page-header-subtitle">
                            Theo dõi trạng thái các cuộc hẹn khám, kiểm tra chi tiết hoặc hủy lịch khi có thay đổi kế hoạch.
                        </p>
                    </div>
                    <div class="d-flex align-items-center gap-2">
                        <a href="${pageContext.request.contextPath}/customer/medical-records" class="btn btn-outline-primary btn-sm rounded-pill px-3 py-2 fw-semibold">
                            📋 Hồ sơ bệnh án
                        </a>
                        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-cta-primary btn-sm rounded-pill px-3 py-2">
                            <span>📅</span><span>Đặt lịch mới</span>
                        </a>
                    </div>
                </div>

                <!-- Policy Alert Banner -->
                <div class="alert alert-info border-0 rounded-4 p-3 mb-4 shadow-sm d-flex align-items-center gap-3" style="background-color: #EFF6FF; border-left: 4px solid #2563EB !important;">
                    <span class="fs-4">ℹ️</span>
                    <div class="small">
                        <strong class="text-primary">Quy định hủy lịch hẹn trực tuyến:</strong>
                        Quý khách có thể tự hủy các lịch hẹn ở trạng thái <em>"Chờ xác nhận"</em> hoặc <em>"Đã xác nhận"</em>. 
                        Sau khi hủy, khung giờ sẽ lập tức được giải phóng cho bệnh nhân khác. Đối với ca khám đã <em>"Check-in"</em> tại phòng khám, vui lòng liên hệ trực tiếp nhân viên Lễ tân.
                    </div>
                </div>

                <c:if test="${empty appointments}">
                    <div class="dencli-card p-5 text-center bg-white shadow-sm rounded-4 border-0 my-4">
                        <div class="empty-state-icon mb-3" style="font-size: 3.5rem;">🗓️</div>
                        <h5 class="fw-bold mb-2 text-dark">Bạn chưa có lịch hẹn nào</h5>
                        <p class="text-muted small mb-4 mx-auto" style="max-width: 480px;">
                            Hãy chủ động đặt lịch khám định kỳ để được đội ngũ Bác sĩ chuyên khoa tại DenCli thăm khám và tư vấn tận tâm nhất.
                        </p>
                        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-cta-primary">
                            <span>📅</span><span>Đặt Lịch Ngay</span>
                        </a>
                    </div>
                </c:if>

                <c:if test="${not empty appointments}">
                    <div class="dencli-card bg-white shadow-sm rounded-4 border-0 p-3 mb-4 overflow-hidden">
                        <div class="table-responsive border-0">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th style="width: 8%;">Mã</th>
                                        <th style="width: 16%;">Ngày hẹn</th>
                                        <th style="width: 14%;">Khung giờ</th>
                                        <th style="width: 22%;">Bác sĩ phụ trách</th>
                                        <th style="width: 14%;">Phòng khám</th>
                                        <th style="width: 14%;">Trạng thái</th>
                                        <th style="width: 12%;" class="text-end">Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${appointments}" var="a">
                                        <tr id="row-app-${a.appointmentId}">
                                            <td class="fw-bold text-muted">#${a.appointmentId}</td>
                                            <td class="fw-semibold text-dark">
                                                <fmt:formatDate value="${a.appointmentDate}" pattern="dd/MM/yyyy" />
                                            </td>
                                            <td>
                                                <span class="badge bg-light text-dark border px-2 py-1 fw-medium">
                                                    ⏰ ${a.appointmentTime}
                                                </span>
                                            </td>
                                            <td>
                                                <div class="d-flex align-items-center gap-2">
                                                    <span class="rounded-circle bg-primary-subtle text-primary fw-bold d-inline-flex align-items-center justify-content-center" style="width: 28px; height: 28px; font-size: 0.75rem;">BS</span>
                                                    <span class="fw-medium text-dark">${a.doctorName}</span>
                                                </div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty a.room}"><span class="fw-medium text-secondary">🚪 ${a.room}</span></c:when>
                                                    <c:otherwise><span class="text-muted small fst-italic">Chờ phân phòng</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td id="status-cell-${a.appointmentId}">
                                                <c:choose>
                                                    <c:when test="${a.status == 'Pending'}"><span class="status-badge status-pending">Chờ xác nhận</span></c:when>
                                                    <c:when test="${a.status == 'Confirmed'}"><span class="status-badge status-confirmed">Đã xác nhận</span></c:when>
                                                    <c:when test="${a.status == 'Checked In'}"><span class="status-badge status-checkedin">Đã check-in</span></c:when>
                                                    <c:when test="${a.status == 'Completed'}"><span class="status-badge status-completed">Hoàn thành</span></c:when>
                                                    <c:when test="${a.status == 'Cancelled'}"><span class="status-badge status-cancelled">Đã hủy</span></c:when>
                                                    <c:otherwise><span class="status-badge bg-secondary text-white">${a.status}</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-end" id="action-cell-${a.appointmentId}">
                                                <c:choose>
                                                    <c:when test="${a.status == 'Pending' || a.status == 'Confirmed'}">
                                                        <button type="button" 
                                                                class="btn btn-sm btn-outline-danger rounded-pill px-3 py-1 btn-open-cancel"
                                                                data-id="${a.appointmentId}"
                                                                data-doctor="${a.doctorName}"
                                                                data-date="<fmt:formatDate value='${a.appointmentDate}' pattern='dd/MM/yyyy' />"
                                                                data-time="${a.appointmentTime}"
                                                                data-status="${a.status == 'Pending' ? 'Chờ xác nhận' : 'Đã xác nhận'}">
                                                            Hủy lịch
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted small">—</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Phân trang -->
                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Điều hướng trang" class="mt-4">
                            <ul class="pagination justify-content-center">
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${i == page ? 'active' : ''}">
                                        <a class="page-link rounded-circle mx-1" href="?page=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </c:if>
                </c:if>
            </main>
        </div>
    </div>

    <!-- Modal Hủy Lịch Hẹn Khám (Modern European Modal) -->
    <div class="modal fade" id="cancelAppointmentModal" tabindex="-1" aria-labelledby="cancelModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                <div class="modal-header border-bottom-0 pb-0 pt-4 px-4">
                    <div class="d-flex align-items-center gap-3">
                        <div class="rounded-circle d-flex align-items-center justify-content-center" style="width: 44px; height: 44px; background: #FEE2E2; color: #DC2626;">
                            <span class="fs-5">✕</span>
                        </div>
                        <div>
                            <h5 class="modal-title fw-bold text-dark mb-0" id="cancelModalLabel">Xác nhận Hủy Lịch Hẹn</h5>
                            <span class="text-muted small">Cuộc hẹn <strong id="modalAppId" class="text-primary">#---</strong></span>
                        </div>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                </div>
                <div class="modal-body px-4 py-3">
                    <!-- Tóm tắt ca hẹn -->
                    <div class="p-3 rounded-3 mb-3" style="background: #FFF1F2; border: 1px solid #FFE4E6;">
                        <div class="small text-danger fw-bold mb-2">Thông tin lịch khám:</div>
                        <div class="d-flex justify-content-between text-secondary small py-1 border-bottom border-danger-subtle">
                            <span>Bác sĩ điều trị:</span>
                            <strong class="text-dark" id="modalDoctorName">---</strong>
                        </div>
                        <div class="d-flex justify-content-between text-secondary small py-1 border-bottom border-danger-subtle">
                            <span>Thời gian hẹn:</span>
                            <strong class="text-dark"><span id="modalAppDate">---</span> lúc <span id="modalAppTime">---</span></strong>
                        </div>
                        <div class="d-flex justify-content-between text-secondary small py-1">
                            <span>Trạng thái:</span>
                            <span id="modalAppStatus" class="badge bg-light text-dark border">---</span>
                        </div>
                    </div>

                    <!-- Lý do hủy -->
                    <div class="mb-3">
                        <label for="cancelReasonSelect" class="form-label small fw-bold text-dark">Lý do hủy hẹn <span class="text-danger">*</span></label>
                        <select id="cancelReasonSelect" class="form-select form-select-sm rounded-3">
                            <option value="Bận việc đột xuất / Thay đổi kế hoạch">Bận việc đột xuất / Thay đổi kế hoạch</option>
                            <option value="Muốn dời sang ngày khám hoặc bác sĩ khác">Muốn dời sang ngày khám hoặc bác sĩ khác</option>
                            <option value="Tình trạng răng miệng đã tự thuyên giảm">Tình trạng răng miệng đã tự thuyên giảm</option>
                            <option value="Chọn nhầm giờ hoặc dịch vụ khám">Chọn nhầm giờ hoặc dịch vụ khám</option>
                            <option value="Lý do khác">Lý do khác...</option>
                        </select>
                    </div>

                    <!-- Ghi chú bổ sung -->
                    <div class="mb-3">
                        <label for="cancelNoteInput" class="form-label small fw-bold text-dark">Ghi chú thêm cho phòng khám (tùy chọn)</label>
                        <textarea id="cancelNoteInput" class="form-control form-control-sm rounded-3" rows="2" placeholder="Nhập thêm lý do chi tiết nếu muốn..."></textarea>
                    </div>

                    <div class="alert alert-warning py-2 px-3 small mb-0 d-flex align-items-center gap-2 rounded-3 border-0" style="background: #FEF3C7; color: #92400E;">
                        <span>💡</span>
                        <span>Khung giờ khám này sau khi hủy sẽ lập tức được mở lại cho bệnh nhân khác.</span>
                    </div>
                    <div id="cancelErrorMessage" class="alert alert-danger py-2 px-3 small mt-2 d-none rounded-3"></div>
                </div>
                <div class="modal-footer border-top-0 pt-0 px-4 pb-4">
                    <button type="button" class="btn btn-light rounded-pill px-4 py-2 fw-semibold" data-bs-dismiss="modal">Giữ Lịch</button>
                    <button type="button" id="btnExecuteCancel" class="btn btn-danger rounded-pill px-4 py-2 fw-semibold shadow-sm d-flex align-items-center gap-2">
                        <span id="cancelSpinner" class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                        <span id="cancelButtonText">Xác Nhận Hủy</span>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Toast Notification Container -->
    <div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 1080;">
        <div id="liveToast" class="toast align-items-center border-0 text-white rounded-4 shadow-lg" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body d-flex align-items-center gap-2 fw-medium" id="toastMessage">
                    <!-- Dynamic message -->
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- Script điều phối Hủy Lịch Hẹn Khám (Feature 7) -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            let selectedAppointmentId = null;
            const cancelModalEl = document.getElementById('cancelAppointmentModal');
            const cancelModal = new bootstrap.Modal(cancelModalEl);

            const modalAppId = document.getElementById('modalAppId');
            const modalDoctorName = document.getElementById('modalDoctorName');
            const modalAppDate = document.getElementById('modalAppDate');
            const modalAppTime = document.getElementById('modalAppTime');
            const modalAppStatus = document.getElementById('modalAppStatus');
            const cancelReasonSelect = document.getElementById('cancelReasonSelect');
            const cancelNoteInput = document.getElementById('cancelNoteInput');
            const cancelErrorMessage = document.getElementById('cancelErrorMessage');

            const btnExecuteCancel = document.getElementById('btnExecuteCancel');
            const cancelSpinner = document.getElementById('cancelSpinner');
            const cancelButtonText = document.getElementById('cancelButtonText');

            const liveToastEl = document.getElementById('liveToast');
            const liveToast = new bootstrap.Toast(liveToastEl, { delay: 4000 });
            const toastMessage = document.getElementById('toastMessage');

            function showToast(type, msg) {
                liveToastEl.classList.remove('bg-success', 'bg-danger', 'bg-info');
                if (type === 'success') liveToastEl.classList.add('bg-success');
                else if (type === 'danger') liveToastEl.classList.add('bg-danger');
                else liveToastEl.classList.add('bg-info');
                toastMessage.innerHTML = msg;
                liveToast.show();
            }

            // Gắn sự kiện mở Modal cho tất cả các nút Hủy lịch
            document.querySelectorAll('.btn-open-cancel').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    selectedAppointmentId = this.getAttribute('data-id');
                    modalAppId.textContent = '#' + selectedAppointmentId;
                    modalDoctorName.textContent = this.getAttribute('data-doctor') || 'Nha sĩ DenCli';
                    modalAppDate.textContent = this.getAttribute('data-date') || '—';
                    modalAppTime.textContent = this.getAttribute('data-time') || '—';
                    modalAppStatus.textContent = this.getAttribute('data-status') || 'Đang xử lý';
                    cancelReasonSelect.selectedIndex = 0;
                    cancelNoteInput.value = '';
                    cancelErrorMessage.classList.add('d-none');
                    cancelErrorMessage.textContent = '';
                    cancelModal.show();
                });
            });

            // Thực hiện gọi API hủy lịch hẹn
            btnExecuteCancel.addEventListener('click', function() {
                if (!selectedAppointmentId) return;

                btnExecuteCancel.disabled = true;
                cancelSpinner.classList.remove('d-none');
                cancelButtonText.textContent = 'Đang xử lý...';
                cancelErrorMessage.classList.add('d-none');

                const csrfToken = document.querySelector('meta[name="csrf-token"]')?.getAttribute('content') || '';
                const reason = encodeURIComponent(cancelReasonSelect.value);
                const note = encodeURIComponent(cancelNoteInput.value);

                const bodyData = 'action=cancel&appointment_id=' + encodeURIComponent(selectedAppointmentId) 
                               + '&reason=' + reason 
                               + '&notes=' + note 
                               + '&_csrf=' + encodeURIComponent(csrfToken);

                fetch('${pageContext.request.contextPath}/api/appointments/action', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'X-CSRF-TOKEN': csrfToken
                    },
                    body: bodyData
                })
                .then(function(res) {
                    return res.json().then(function(data) {
                        return { ok: res.ok, status: res.status, data: data };
                    });
                })
                .then(function(result) {
                    btnExecuteCancel.disabled = false;
                    cancelSpinner.classList.add('d-none');
                    cancelButtonText.textContent = 'Xác Nhận Hủy';

                    if (result.ok && result.data.success) {
                        cancelModal.hide();

                        // Cập nhật DOM thời gian thực: Status cell -> Đã hủy
                        const statusCell = document.getElementById('status-cell-' + selectedAppointmentId);
                        if (statusCell) {
                            statusCell.innerHTML = '<span class="status-badge status-cancelled">Đã hủy</span>';
                        }

                        // Cập nhật Action cell -> Dấu gạch ngang (không còn nút hủy)
                        const actionCell = document.getElementById('action-cell-' + selectedAppointmentId);
                        if (actionCell) {
                            actionCell.innerHTML = '<span class="text-muted small">—</span>';
                        }

                        showToast('success', '✓ Đã hủy lịch hẹn #' + selectedAppointmentId + ' thành công! Khung giờ đã được giải phóng.');
                    } else {
                        cancelErrorMessage.textContent = result.data.message || 'Hủy lịch thất bại. Vui lòng thử lại sau.';
                        cancelErrorMessage.classList.remove('d-none');
                    }
                })
                .catch(function(err) {
                    btnExecuteCancel.disabled = false;
                    cancelSpinner.classList.add('d-none');
                    cancelButtonText.textContent = 'Xác Nhận Hủy';
                    cancelErrorMessage.textContent = 'Lỗi kết nối máy chủ. Vui lòng kiểm tra lại mạng.';
                    cancelErrorMessage.classList.remove('d-none');
                });
            });
        });
    </script>
</body>

</html>
