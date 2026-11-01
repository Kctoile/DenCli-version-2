<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Buồng Khám Bác Sĩ & Chẩn Đoán Lâm Sàng | DenCli</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Google Fonts: Plus Jakarta Sans & Figtree -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Figtree:wght@400;500;600;700&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=7">

    <style>
        .doctor-kpi-card {
            background: #FFFFFF;
            border-radius: 12px;
            padding: 1rem 1.25rem;
            border: 1px solid #E2E8F0;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .doctor-kpi-icon {
            width: 44px;
            height: 44px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.25rem;
        }
        .table-doctor th {
            font-size: 0.8125rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            color: #64748B;
            font-weight: 700;
            background-color: #F8FAFC;
            padding: 0.85rem 1rem;
            border-bottom: 2px solid #E2E8F0;
        }
        .table-doctor td {
            padding: 1rem 1rem;
            vertical-align: middle;
            font-size: 0.875rem;
        }
        .patient-avatar-doc {
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
                            <span class="fs-4">🩺</span>
                            <h3 class="fw-bold text-dark mb-0">Buồng Khám Bác Sĩ & Điều Trị</h3>
                        </div>
                        <p class="text-muted small mb-0">
                            Danh sách bệnh nhân phân công theo ngày, ghi nhận chẩn đoán bệnh lý, chỉ định thủ thuật và kê đơn thuốc.
                        </p>
                    </div>
                    <div>
                        <!-- Bộ lọc ngày -->
                        <form action="${pageContext.request.contextPath}/doctor/examination" method="GET" class="d-flex gap-2 align-items-center">
                            <label for="filterDate" class="small fw-bold text-dark text-nowrap">Lọc ngày:</label>
                            <input type="date" id="filterDate" name="date" class="form-control form-control-sm rounded-pill" value="${param.date}">
                            <button type="submit" class="btn btn-sm btn-primary rounded-pill px-3" style="background: #0891B2; border-color: #0891B2;">Lọc</button>
                            <c:if test="${not empty param.date}">
                                <a href="${pageContext.request.contextPath}/doctor/examination" class="btn btn-sm btn-outline-secondary rounded-pill px-3">Xóa lọc</a>
                            </c:if>
                        </form>
                    </div>
                </div>

                <!-- Main Content Table Card -->
                <div class="dencli-card bg-white shadow-sm rounded-4 border-0 p-4">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0 table-doctor">
                            <thead>
                                <tr>
                                    <th scope="col">Mã Hẹn</th>
                                    <th scope="col">Bệnh Nhân</th>
                                    <th scope="col">Giờ Hẹn</th>
                                    <th scope="col">Phòng Khám</th>
                                    <th scope="col">Triệu Chứng Bệnh Nhân Ghi Chú</th>
                                    <th scope="col">Trạng Thái</th>
                                    <th scope="col" class="text-center">Thao Tác Bác Sĩ</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty appointments}">
                                        <tr>
                                            <td colspan="7" class="text-center py-5">
                                                <div class="empty-state-box">
                                                    <div class="fs-1 text-muted mb-2">🩺</div>
                                                    <h6 class="fw-bold text-dark mb-1">Chưa có ca khám nào</h6>
                                                    <p class="text-muted small mb-0">Hiện chưa có ca khám nào được phân công cho bạn trong khoảng thời gian đã chọn.</p>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${appointments}" var="app">
                                            <tr>
                                                <td class="fw-bold text-primary">#<c:out value="${app.appointmentId}" /></td>

                                                <!-- Bệnh nhân info -->
                                                <td>
                                                    <div class="d-flex align-items-center gap-2">
                                                        <div class="patient-avatar-doc">
                                                            ${not empty app.patientName ? app.patientName.substring(0, 1).toUpperCase() : 'B'}
                                                        </div>
                                                        <div>
                                                            <strong class="text-dark d-block">
                                                                <c:out value="${not empty app.patientName ? app.patientName : 'BN-#' += app.patientId}" />
                                                            </strong>
                                                            <small class="text-muted">
                                                                <c:out value="${not empty app.patientPhone ? app.patientPhone : 'Mã: #' += app.patientId}" />
                                                            </small>
                                                        </div>
                                                    </div>
                                                </td>

                                                <!-- Giờ khám -->
                                                <td>
                                                    <span class="badge bg-light text-dark border px-2 py-1 fw-bold">
                                                        <c:out value="${app.appointmentTime}" />
                                                    </span>
                                                    <small class="d-block text-muted mt-1"><c:out value="${app.appointmentDate}" /></small>
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

                                                <!-- Triệu chứng -->
                                                <td class="small text-muted" style="max-width: 220px;">
                                                    <c:choose>
                                                        <c:when test="${not empty app.notes}">
                                                            <span class="d-inline-block text-truncate" style="max-width: 200px;" title="${app.notes}">
                                                                <c:out value="${app.notes}" />
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>—</c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <!-- Trạng thái -->
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${app.status == 'Pending'}">
                                                            <span class="badge rounded-pill px-3 py-2 fw-semibold" style="background: #FEF3C7; color: #92400E;">⏳ Chờ xác nhận</span>
                                                        </c:when>
                                                        <c:when test="${app.status == 'Confirmed'}">
                                                            <span class="badge rounded-pill px-3 py-2 fw-semibold" style="background: #E0F2FE; color: #0369A1;">📅 Đã xác nhận</span>
                                                        </c:when>
                                                        <c:when test="${app.status == 'Checked In'}">
                                                            <span class="badge rounded-pill px-3 py-2 fw-semibold" style="background: #CCFBF1; color: #0F766E;">🩺 Đang chờ khám</span>
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

                                                <!-- Thao tác Bác sĩ -->
                                                <td class="text-center">
                                                    <div class="d-flex align-items-center justify-content-center gap-1">
                                                        <button type="button" class="btn btn-sm btn-outline-secondary rounded-pill px-2" onclick="openPatientHistory(${app.patientId})" title="Xem lịch sử điều trị trước đây">
                                                            📋 Lịch sử
                                                        </button>

                                                        <button type="button" class="btn btn-sm btn-primary fw-semibold rounded-pill px-3 shadow-sm" style="background: #0891B2; border-color: #0891B2;" onclick="openExamModal(${app.appointmentId}, '${app.patientId}', '${app.patientName}')">
                                                            🩺 Khám bệnh
                                                        </button>

                                                        <c:choose>
                                                            <c:when test="${not empty app.examinationResult and app.examinationResult.resultId > 0}">
                                                                <a href="${pageContext.request.contextPath}/doctor/prescription?result_id=${app.examinationResult.resultId}" class="btn btn-sm btn-outline-success rounded-pill px-2" title="Kê đơn thuốc">
                                                                    💊 Kê đơn
                                                                </a>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <a href="${pageContext.request.contextPath}/doctor/prescription" class="btn btn-sm btn-outline-success rounded-pill px-2" title="Kê đơn thuốc">
                                                                    💊 Kê đơn
                                                                </a>
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

    <!-- Modal Lập Bệnh Án & Chẩn Đoán Lâm Sàng -->
    <div class="modal fade" id="examModal" tabindex="-1" aria-labelledby="examModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                <div class="modal-header text-white px-4 py-3" style="background: linear-gradient(135deg, #075985 0%, #0891B2 100%);">
                    <h5 class="modal-title fw-bold" id="examModalLabel">🩺 Ghi Nhận Chẩn Đoán & Chỉ Định Dịch Vụ</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="${pageContext.request.contextPath}/doctor/examination" method="POST">
                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <div class="modal-body p-4">
                        <div class="p-3 bg-light rounded-3 mb-3 border d-flex justify-content-between align-items-center">
                            <div>
                                <span class="text-muted small d-block">Bệnh nhân thăm khám:</span>
                                <strong id="modalPatientName" class="text-dark">---</strong>
                            </div>
                            <div class="text-end">
                                <span class="badge px-3 py-2 rounded-pill" style="background: #E0F2FE; color: #0369A1;">
                                    Lịch hẹn #<span id="modalAppIdText"></span>
                                </span>
                            </div>
                        </div>
                        <input type="hidden" id="modalAppointmentId" name="appointment_id" value="">

                        <!-- Kết luận chẩn đoán -->
                        <div class="mb-3">
                            <label for="modalDiagnosis" class="form-label fw-bold text-dark">
                                Kết luận chẩn đoán lâm sàng <span class="text-danger">*</span>
                            </label>
                            <textarea id="modalDiagnosis" name="diagnosis" class="form-control rounded-3" rows="3" required placeholder="Ví dụ: Viêm quanh cuống răng P.46, mòn cổ răng hàm dưới, chỉ định chụp phim X-quang và làm sạch tủy..."></textarea>
                        </div>

                        <!-- Lịch hẹn tái khám -->
                        <div class="row g-3 mb-3">
                            <div class="col-md-5">
                                <label for="revisitDate" class="form-label fw-bold text-dark">Ngày hẹn tái khám</label>
                                <input type="date" id="revisitDate" name="revisit_date" class="form-control rounded-3">
                            </div>
                            <div class="col-md-7">
                                <label for="revisitNote" class="form-label fw-bold text-dark">Ghi chú dặn dò tái khám</label>
                                <input type="text" id="revisitNote" name="revisit_note" class="form-control rounded-3" maxlength="1000" placeholder="Ví dụ: Kiểm tra lại tủy sau 7 ngày, kiêng ăn đồ cứng...">
                            </div>
                        </div>

                        <!-- Chỉ định thêm dịch vụ / thủ thuật -->
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark d-block">
                                Chỉ định dịch vụ / Thủ thuật thực hiện thêm
                            </label>
                            <div class="border rounded-3 p-3 bg-light" style="max-height: 220px; overflow-y: auto;">
                                <div class="row g-2">
                                    <c:forEach items="${services}" var="svc">
                                        <div class="col-md-6">
                                            <div class="form-check p-2 bg-white rounded border">
                                                <input class="form-check-input ms-0 me-2" type="checkbox" name="service_ids" value="${svc.serviceId}" id="modal_svc_${svc.serviceId}">
                                                <label class="form-check-label d-flex justify-content-between align-items-center w-100 pe-2" for="modal_svc_${svc.serviceId}">
                                                    <span class="small fw-semibold text-dark"><c:out value="${svc.serviceName}" /></span>
                                                    <span class="text-primary fw-bold small">
                                                        <fmt:formatNumber value="${svc.price}" pattern="#,##0" /> đ
                                                    </span>
                                                </label>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="form-text text-muted mt-1">Các thủ thuật/dịch vụ chỉ định thêm sẽ được tự động đồng bộ sang hóa đơn viện phí tại quầy tiếp đón.</div>
                        </div>
                    </div>
                    <div class="modal-footer px-4 py-3 bg-light border-0">
                        <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-primary rounded-pill px-4 fw-bold shadow-sm" style="background: #0891B2; border-color: #0891B2;">
                            Hoàn Tất Khám & Lưu Kết Quả
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Modal Lịch Sử Khám Bệnh -->
    <div class="modal fade" id="patientHistoryModal" tabindex="-1" aria-labelledby="patientHistoryTitle" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-scrollable modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
                <div class="modal-header text-white px-4 py-3" style="background: linear-gradient(135deg, #075985 0%, #0891B2 100%);">
                    <h5 class="modal-title fw-bold" id="patientHistoryTitle">📋 Tiền Sử & Lịch Sử Khám Bệnh</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Đóng"></button>
                </div>
                <div class="modal-body p-4" id="patientHistoryContent"></div>
                <div class="modal-footer px-4 py-2 bg-light border-0">
                    <button type="button" class="btn btn-secondary rounded-pill px-4" data-bs-dismiss="modal">Đóng</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <script>
        function openExamModal(appId, patientId, patientName) {
            document.getElementById('modalAppointmentId').value = appId;
            document.getElementById('modalAppIdText').textContent = appId;
            document.getElementById('modalPatientName').textContent = patientName ? (patientName + ' (BN-#' + patientId + ')') : ('BN-#' + patientId);
            var modal = new bootstrap.Modal(document.getElementById('examModal'));
            modal.show();
        }

        async function openPatientHistory(patientId) {
            const content = document.getElementById('patientHistoryContent');
            const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('patientHistoryModal'));
            content.replaceChildren();
            const loading = document.createElement('div');
            loading.className = 'text-center py-4 text-muted';
            loading.innerHTML = '<div class="spinner-border spinner-border-sm text-primary me-2"></div> Đang tải hồ sơ bệnh án...';
            content.append(loading);
            modal.show();

            try {
                const response = await fetch('${pageContext.request.contextPath}/doctor/patient-history?patient_id=' + encodeURIComponent(patientId));
                if (!response.ok) throw new Error('Không thể tải lịch sử khám.');
                const records = await response.json();
                content.replaceChildren();
                if (!records.length) {
                    const empty = document.createElement('div');
                    empty.className = 'text-center py-4 text-muted';
                    empty.innerHTML = '📋 Bệnh nhân chưa có lịch sử điều trị hoàn thành nào trước đây.';
                    content.append(empty);
                    return;
                }
                for (const record of records) {
                    const section = document.createElement('section');
                    section.className = 'p-3 mb-3 rounded-3 border bg-light';

                    const heading = document.createElement('div');
                    heading.className = 'd-flex justify-content-between align-items-center mb-2';
                    const dateStrong = document.createElement('strong');
                    dateStrong.className = 'text-primary';
                    dateStrong.textContent = '📅 Ngày khám: ' + (record.date || '');
                    const badge = document.createElement('span');
                    badge.className = 'badge bg-success-subtle text-success rounded-pill px-3';
                    badge.textContent = 'Hoàn thành';
                    heading.append(dateStrong, badge);
                    section.append(heading);

                    if (record.diagnosis) {
                        const diagnosis = document.createElement('div');
                        diagnosis.className = 'p-2 bg-white rounded border mb-2 small';
                        const diagStrong = document.createElement('strong');
                        diagStrong.textContent = 'Chẩn đoán: ';
                        diagnosis.append(diagStrong, document.createTextNode(record.diagnosis));
                        section.append(diagnosis);
                    }
                    if (record.services && record.services.length) {
                        const svcBox = document.createElement('div');
                        svcBox.className = 'small text-muted mb-2';
                        const svcStrong = document.createElement('strong');
                        svcStrong.textContent = 'Thủ thuật thực hiện: ';
                        let sNames = record.services.map(s => s.serviceName).join(', ');
                        svcBox.append(svcStrong, document.createTextNode(sNames));
                        section.append(svcBox);
                    }
                    if (record.prescription) {
                        const prescription = document.createElement('div');
                        prescription.className = 'small text-dark p-2 bg-white rounded border';
                        const rxStrong = document.createElement('strong');
                        rxStrong.textContent = 'Đơn thuốc: ';
                        prescription.append(rxStrong, document.createTextNode(record.prescription.instructions || 'Theo đơn'));
                        section.append(prescription);
                    }
                    content.append(section);
                }
            } catch (error) {
                content.replaceChildren();
                const message = document.createElement('div');
                message.className = 'alert alert-danger py-2 small';
                message.textContent = error.message;
                content.append(message);
            }
        }
    </script>
</body>

</html>
