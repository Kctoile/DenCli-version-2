<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ Sơ Bệnh Án Điện Tử | DenCli Dental Clinic</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Google Fonts: Plus Jakarta Sans & Figtree -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=7">

    <style>
        .record-timeline {
            position: relative;
            padding-left: 2rem;
        }
        .record-timeline::before {
            content: '';
            position: absolute;
            top: 20px;
            bottom: 20px;
            left: 11px;
            width: 3px;
            background: #CBD5E1;
            border-radius: 2px;
        }
        .timeline-node {
            position: absolute;
            left: -2rem;
            top: 22px;
            width: 26px;
            height: 26px;
            border-radius: 50%;
            background: #0891B2;
            border: 4px solid #F0FDFA;
            box-shadow: 0 0 0 2px #0891B2;
            z-index: 2;
        }
        .diagnosis-box {
            background: #F0FDFA;
            border: 1px solid #99F6E4;
            border-radius: var(--den-radius-md, 12px);
            padding: 1rem 1.25rem;
        }
        .prescription-badge {
            background: #EFF6FF;
            border: 1px solid #BFDBFE;
            color: #1E40AF;
            border-radius: 8px;
            padding: 0.5rem 0.85rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
            font-size: 0.875rem;
            font-weight: 600;
        }
        .treatment-tag {
            background: #F1F5F9;
            border: 1px solid #CBD5E1;
            color: #1E293B;
            padding: 0.4rem 0.85rem;
            border-radius: 8px;
            font-size: 0.85rem;
            font-weight: 500;
        }

        /* Medical Record Print Template Styles */
        @media print {
            body * {
                visibility: hidden;
            }
            #printableMedicalRecord, #printableMedicalRecord * {
                visibility: visible;
            }
            #printableMedicalRecord {
                position: absolute;
                left: 0;
                top: 0;
                width: 100%;
                margin: 0;
                padding: 20px;
                background: #FFFFFF !important;
            }
            .no-print, header, nav, .btn, footer, .sidebar, .toast-container, .modal-footer, .btn-close {
                display: none !important;
            }
        }
    </style>
</head>

<body class="d-flex flex-column min-vh-100" style="background-color: #F8FAFC;">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

            <main class="col-12 col-md-9 col-lg-10 p-4 p-lg-5">
                
                <!-- Page Header -->
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4 pb-3 border-bottom">
                    <div>
                        <div class="d-flex align-items-center gap-2 mb-1">
                            <span class="fs-4">📋</span>
                            <h3 class="fw-bold text-dark mb-0">Hồ Sơ Bệnh Án Điện Tử (EMR)</h3>
                        </div>
                        <p class="text-muted small mb-0">
                            Tra cứu chi tiết kết quả chẩn đoán lâm sàng, dịch vụ nha khoa đã thực hiện, đơn thuốc điều trị và lịch hẹn tái khám.
                        </p>
                    </div>
                    <div class="d-flex gap-2">
                        <a href="${pageContext.request.contextPath}/customer/revisitation" class="btn btn-sm btn-outline-info rounded-pill px-3 py-2 fw-semibold">
                            🗓️ Lịch tái khám
                        </a>
                        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-cta-primary btn-sm rounded-pill px-3 py-2">
                            <span>📅</span><span>Đặt lịch mới</span>
                        </a>
                    </div>
                </div>

                <!-- Instant Search & Quick Filter Toolbar -->
                <c:if test="${not empty records}">
                    <div class="dencli-card bg-white p-3 rounded-4 shadow-sm border-0 mb-4">
                        <div class="row g-2 align-items-center">
                            <div class="col-12 col-md-6 col-lg-7">
                                <div class="input-group">
                                    <span class="input-group-text bg-light border-end-0 text-muted">🔍</span>
                                    <input type="text" id="recordSearchInput" class="form-control border-start-0 bg-light" 
                                           placeholder="Tìm theo Bác sĩ, chẩn đoán, dịch vụ hoặc tên thuốc...">
                                </div>
                            </div>
                            <div class="col-6 col-md-3 col-lg-3">
                                <select id="recordFilterType" class="form-select bg-light">
                                    <option value="all">Tất cả ca khám</option>
                                    <option value="prescription">Có đơn thuốc</option>
                                    <option value="revisit">Có hẹn tái khám</option>
                                </select>
                            </div>
                            <div class="col-6 col-md-3 col-lg-2 text-end">
                                <span class="badge bg-light text-dark border px-3 py-2 fw-semibold" id="recordCounter">
                                    ${records.size()} ca khám
                                </span>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Empty State -->
                <c:if test="${empty records}">
                    <div class="dencli-card p-5 text-center bg-white shadow-sm rounded-4 border-0 my-5">
                        <div class="display-4 text-muted mb-3">🩺</div>
                        <h5 class="fw-bold text-dark mb-2">Chưa có dữ liệu hồ sơ bệnh án</h5>
                        <p class="text-muted small mx-auto mb-4" style="max-width: 500px;">
                            Hồ sơ bệnh án điện tử sẽ tự động cập nhật ngay sau khi Bác sĩ hoàn thành phiên khám và ghi nhận kết quả điều trị.
                        </p>
                        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-primary rounded-pill px-4 fw-semibold shadow-sm" style="background: #0891B2; border-color: #0891B2;">
                            Đặt Lịch Khám Ngay
                        </a>
                    </div>
                </c:if>

                <!-- Medical Records Timeline List -->
                <div class="record-timeline" id="recordsTimelineList">
                    <c:forEach items="${records}" var="rec" varStatus="loop">
                        <div class="position-relative mb-5 record-item-card" 
                             data-doctor="${rec.appointment.doctorName}"
                             data-diagnosis="${rec.diagnosis}"
                             data-has-rx="${not empty rec.prescription ? 'true' : 'false'}"
                             data-has-revisit="${not empty rec.appointment.revisitDate ? 'true' : 'false'}"
                             data-services="<c:forEach items='${rec.services}' var='s'>${s.serviceName} </c:forEach>"
                             data-medicines="<c:forEach items='${rec.prescription.details}' var='m'>${m.medicineName} </c:forEach>">
                            
                            <!-- Timeline Dot Indicator -->
                            <div class="timeline-node"></div>

                            <!-- Card Hồ sơ -->
                            <div class="dencli-card bg-white shadow-sm rounded-4 border-0 overflow-hidden">
                                
                                <!-- Card Header -->
                                <div class="px-4 py-3 bg-light border-bottom d-flex flex-wrap justify-content-between align-items-center gap-2">
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="badge px-3 py-2 rounded-pill fw-bold" style="background: #E0F2FE; color: #0369A1; font-size: 0.85rem;">
                                            📅 Ngày khám: <fmt:formatDate value="${rec.appointment.appointmentDate}" pattern="dd/MM/yyyy"/>
                                        </span>
                                        <span class="text-muted small">Khung giờ: <strong>${rec.appointment.appointmentTime}</strong></span>
                                        <span class="text-muted small">| Phòng: <strong>${rec.appointment.room}</strong></span>
                                    </div>
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="badge bg-success-subtle text-success px-3 py-1 rounded-pill fw-semibold">
                                            ✓ Đã hoàn thành
                                        </span>
                                        <span class="text-muted small fw-medium">Mã khám: #${rec.appointment.appointmentId}</span>
                                        <!-- Nút In Bệnh Án / Đơn Thuốc -->
                                        <button type="button" class="btn btn-sm btn-outline-secondary rounded-pill px-3 py-1 fw-semibold btn-trigger-print"
                                                data-index="${loop.index}">
                                            🖨️ In Bệnh Án & Đơn Thuốc
                                        </button>
                                    </div>
                                </div>

                                <!-- Card Body -->
                                <div class="p-4">
                                    <div class="row g-4">
                                        
                                        <!-- Cột Trái: Bác sĩ & Chẩn đoán & Dịch vụ -->
                                        <div class="col-lg-7">
                                            <div class="d-flex align-items-center gap-2 mb-3">
                                                <div class="rounded-circle d-flex align-items-center justify-content-center text-white fw-bold" style="width: 36px; height: 36px; background: #0891B2;">
                                                    BS
                                                </div>
                                                <div>
                                                    <span class="text-muted small d-block">Bác sĩ phụ trách:</span>
                                                    <strong class="text-dark">BS. <c:out value="${rec.appointment.doctorName}" /></strong>
                                                </div>
                                            </div>

                                            <!-- Khối Chẩn đoán lâm sàng -->
                                            <div class="diagnosis-box mb-3">
                                                <div class="fw-bold text-dark small mb-1 d-flex align-items-center gap-2">
                                                    <span>🔍 Kết quả chẩn đoán lâm sàng:</span>
                                                </div>
                                                <p class="mb-0 text-dark small leading-relaxed fw-medium">
                                                    <c:out value="${rec.diagnosis}" />
                                                </p>
                                            </div>

                                            <!-- Dịch vụ đã thực hiện -->
                                            <c:if test="${not empty rec.services}">
                                                <div class="mb-3">
                                                    <span class="fw-bold text-dark small d-block mb-2">🛠️ Dịch vụ nha khoa đã thực hiện:</span>
                                                    <div class="d-flex flex-wrap gap-2">
                                                        <c:forEach items="${rec.services}" var="ps">
                                                            <div class="treatment-tag d-flex align-items-center gap-2">
                                                                <span><c:out value="${ps.serviceName}" /></span>
                                                                <c:if test="${not empty ps.price and ps.price > 0}">
                                                                    <strong class="text-primary">
                                                                        (<fmt:formatNumber value="${ps.price}" pattern="#,##0" /> đ)
                                                                    </strong>
                                                                </c:if>
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </c:if>

                                            <!-- Khối Nhắc nhở Lịch hẹn tái khám (nếu có) -->
                                            <c:if test="${not empty rec.appointment.revisitDate}">
                                                <div class="p-3 rounded-3 mt-3 d-flex flex-wrap justify-content-between align-items-center gap-2" 
                                                     style="background-color: #EFF6FF; border: 1px dashed #3B82F6;">
                                                    <div class="d-flex align-items-center gap-2">
                                                        <span class="fs-4">🗓️</span>
                                                        <div>
                                                            <span class="badge bg-primary text-white px-2 py-0 small mb-1" style="font-size: 0.75rem;">LỊCH HẸN TÁI KHÁM</span>
                                                            <div class="text-dark fw-bold small">
                                                                Ngày tái khám: <span class="text-primary"><fmt:formatDate value="${rec.appointment.revisitDate}" pattern="dd/MM/yyyy" /></span>
                                                                <c:if test="${not empty rec.appointment.revisitNote}">
                                                                    <span class="text-muted fw-normal"> — <c:out value="${rec.appointment.revisitNote}" /></span>
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <a href="${pageContext.request.contextPath}/customer/revisitation" class="btn btn-sm btn-primary rounded-pill px-3 py-1 fw-semibold">
                                                        Xem lịch hẹn →
                                                    </a>
                                                </div>
                                            </c:if>
                                        </div>

                                        <!-- Cột Phải: Đơn thuốc & Viện phí -->
                                        <div class="col-lg-5">
                                            <!-- Đơn thuốc điện tử -->
                                            <div class="p-3 bg-light rounded-3 mb-3 border">
                                                <div class="d-flex justify-content-between align-items-center mb-2">
                                                    <span class="fw-bold text-dark small d-flex align-items-center gap-1">
                                                        <span>💊 Đơn thuốc đã kê:</span>
                                                    </span>
                                                    <c:if test="${empty rec.prescription}">
                                                        <span class="badge bg-secondary-subtle text-secondary small">Không có đơn thuốc</span>
                                                    </c:if>
                                                </div>

                                                <c:if test="${not empty rec.prescription}">
                                                    <c:if test="${not empty rec.prescription.instructions}">
                                                        <p class="small text-muted mb-2 fst-italic">
                                                            " <c:out value="${rec.prescription.instructions}" /> "
                                                        </p>
                                                    </c:if>
                                                    <div class="d-flex flex-column gap-2">
                                                        <c:forEach items="${rec.prescription.details}" var="medicine">
                                                            <div class="prescription-badge">
                                                                <span><c:out value="${medicine.medicineName}" /></span>
                                                                <span class="badge bg-primary text-white rounded-pill px-2 py-1">${medicine.prescribedQuantity} viên</span>
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </c:if>
                                            </div>

                                            <!-- Tóm tắt chi phí -->
                                            <div class="p-3 rounded-3 d-flex justify-content-between align-items-center" style="background: #F0FDFA; border: 1px solid #CCFBF1;">
                                                <div>
                                                    <span class="small text-muted d-block">Tổng chi phí điều trị:</span>
                                                    <span class="fw-bold fs-5 text-dark">
                                                        <fmt:formatNumber value="${rec.invoice.grandTotal}" pattern="#,##0" /> đ
                                                    </span>
                                                </div>
                                                <div>
                                                    <a href="${pageContext.request.contextPath}/customer/invoice?appointmentId=${rec.appointment.appointmentId}" class="btn btn-sm btn-outline-primary rounded-pill px-3 py-1 fw-semibold">
                                                        Xem hóa đơn →
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Ẩn mẫu In Bệnh Án / Đơn Thuốc cho từng ca khám -->
                            <div class="d-none print-template" id="print-source-${loop.index}">
                                <div class="p-4 p-md-5 bg-white text-dark" style="font-family: 'Times New Roman', Times, serif;">
                                    <!-- Tiêu đề Phòng khám -->
                                    <div class="d-flex justify-content-between align-items-start border-bottom pb-3 mb-4">
                                        <div>
                                            <h4 class="fw-bold mb-1" style="color: #0F172A;">PHÒNG KHÁM NHA KHOA QUỐC TẾ DENCLI</h4>
                                            <p class="mb-0 small text-secondary">Địa chỉ: 123 Đường Nguyễn Tri Phương, Quận 10, TP.HCM</p>
                                            <p class="mb-0 small text-secondary">Hotline: 1900 6868 | Email: contact@dencli.com | Web: www.dencli.com</p>
                                        </div>
                                        <div class="text-end">
                                            <div class="fw-bold fs-5 text-primary">PHIẾU KHÁM & ĐƠN THUỐC</div>
                                            <div class="small text-muted">Mã hồ sơ: <strong>#${rec.appointment.appointmentId}</strong></div>
                                            <div class="small text-muted">Ngày in: <span class="current-print-date"></span></div>
                                        </div>
                                    </div>

                                    <!-- Thông tin hành chính bệnh nhân -->
                                    <div class="mb-4">
                                        <h6 class="fw-bold text-uppercase border-bottom pb-1 mb-2" style="color: #0369A1;">I. THÔNG TIN HÀNH CHÍNH</h6>
                                        <div class="row g-2 small">
                                            <div class="col-6"><strong>Họ và tên bệnh nhân:</strong> <c:out value="${rec.appointment.patientName}" /></div>
                                            <div class="col-6"><strong>Số điện thoại:</strong> <c:out value="${rec.appointment.patientPhone}" /></div>
                                            <div class="col-6"><strong>Bác sĩ phụ trách:</strong> BS. <c:out value="${rec.appointment.doctorName}" /></div>
                                            <div class="col-6"><strong>Phòng điều trị:</strong> <c:out value="${rec.appointment.room}" /></div>
                                            <div class="col-6">
                                                <strong>Ngày khám:</strong> <fmt:formatDate value="${rec.appointment.appointmentDate}" pattern="dd/MM/yyyy" /> (${rec.appointment.appointmentTime})
                                            </div>
                                            <div class="col-6"><strong>Trạng thái:</strong> Hoàn thành</div>
                                        </div>
                                    </div>

                                    <!-- Chẩn đoán lâm sàng -->
                                    <div class="mb-4">
                                        <h6 class="fw-bold text-uppercase border-bottom pb-1 mb-2" style="color: #0369A1;">II. CHẨN ĐOÁN LÂM SÀNG</h6>
                                        <p class="small mb-0 p-2 rounded bg-light border">
                                            <c:out value="${rec.diagnosis}" />
                                        </p>
                                    </div>

                                    <!-- Dịch vụ đã thực hiện -->
                                    <div class="mb-4">
                                        <h6 class="fw-bold text-uppercase border-bottom pb-1 mb-2" style="color: #0369A1;">III. DỊCH VỤ NHA KHOA ĐÃ THỰC HIỆN</h6>
                                        <table class="table table-sm table-bordered small mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th style="width: 10%;">STT</th>
                                                    <th style="width: 60%;">Tên dịch vụ</th>
                                                    <th style="width: 30%;" class="text-end">Chi phí</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${rec.services}" var="ps" varStatus="sLoop">
                                                    <tr>
                                                        <td class="text-center">${sLoop.count}</td>
                                                        <td><c:out value="${ps.serviceName}" /></td>
                                                        <td class="text-end">
                                                            <fmt:formatNumber value="${ps.price}" pattern="#,##0" /> đ
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>

                                    <!-- Đơn thuốc điều trị -->
                                    <div class="mb-4">
                                        <h6 class="fw-bold text-uppercase border-bottom pb-1 mb-2" style="color: #0369A1;">IV. ĐƠN THUỐC ĐIỀU TRỊ</h6>
                                        <c:choose>
                                            <c:when test="${not empty rec.prescription and not empty rec.prescription.details}">
                                                <table class="table table-sm table-bordered small mb-2">
                                                    <thead class="table-light">
                                                        <tr>
                                                            <th style="width: 8%;">STT</th>
                                                            <th style="width: 50%;">Tên thuốc / Hàm lượng</th>
                                                            <th style="width: 20%;" class="text-center">Số lượng</th>
                                                            <th style="width: 22%;" class="text-end">Đơn giá</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${rec.prescription.details}" var="item" varStatus="mLoop">
                                                            <tr>
                                                                <td class="text-center">${mLoop.count}</td>
                                                                <td class="fw-semibold"><c:out value="${item.medicineName}" /></td>
                                                                <td class="text-center">${item.prescribedQuantity} viên/gói</td>
                                                                <td class="text-end">
                                                                    <c:if test="${not empty item.unitPrice}">
                                                                        <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0" /> đ
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                                <c:if test="${not empty rec.prescription.instructions}">
                                                    <div class="small p-2 rounded bg-light border">
                                                        <strong>Lời dặn & Hướng dẫn sử dụng:</strong> <c:out value="${rec.prescription.instructions}" />
                                                    </div>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="small text-muted fst-italic mb-0">Ca khám không có kê đơn thuốc kèm theo.</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <!-- Hẹn tái khám -->
                                    <c:if test="${not empty rec.appointment.revisitDate}">
                                        <div class="mb-4">
                                            <h6 class="fw-bold text-uppercase border-bottom pb-1 mb-2" style="color: #0369A1;">V. LỊCH HẸN TÁI KHÁM</h6>
                                            <p class="small mb-0">
                                                - Ngày hẹn tái khám: <strong><fmt:formatDate value="${rec.appointment.revisitDate}" pattern="dd/MM/yyyy" /></strong>
                                                <c:if test="${not empty rec.appointment.revisitNote}">
                                                    <br>- Nội dung theo dõi: <c:out value="${rec.appointment.revisitNote}" />
                                                </c:if>
                                            </p>
                                        </div>
                                    </c:if>

                                    <!-- Chữ ký xác nhận -->
                                    <div class="row pt-4 mt-4 text-center small">
                                        <div class="col-6">
                                            <strong>BỆNH NHÂN / ĐẠI DIỆN GIA ĐÌNH</strong><br>
                                            <span class="text-muted fst-italic">(Ký và ghi rõ họ tên)</span>
                                            <div style="height: 60px;"></div>
                                            <strong><c:out value="${rec.appointment.patientName}" /></strong>
                                        </div>
                                        <div class="col-6">
                                            <strong>BÁC SĨ ĐIỀU TRỊ</strong><br>
                                            <span class="text-muted fst-italic">(Ký và ghi rõ họ tên)</span>
                                            <div style="height: 60px;"></div>
                                            <strong>BS. <c:out value="${rec.appointment.doctorName}" /></strong>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </main>
        </div>
    </div>

    <!-- Modal Xem Trước & In Bệnh Án Điện Tử -->
    <div class="modal fade" id="printPreviewModal" tabindex="-1" aria-labelledby="printModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-scrollable">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header border-bottom px-4 py-3">
                    <div class="d-flex align-items-center gap-2">
                        <span class="fs-5">🖨️</span>
                        <h5 class="modal-title fw-bold text-dark mb-0" id="printModalLabel">In Bệnh Án & Đơn Thuốc Điện Tử</h5>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                </div>
                <div class="modal-body p-0" id="printModalContent">
                    <!-- Dynamic Clone of print template -->
                </div>
                <div class="modal-footer border-top px-4 py-3 bg-light">
                    <button type="button" class="btn btn-light rounded-pill px-4 py-2 fw-semibold" data-bs-dismiss="modal">Đóng</button>
                    <button type="button" class="btn btn-primary rounded-pill px-4 py-2 fw-semibold d-flex align-items-center gap-2" onclick="executePrint()">
                        <span>🖨️</span><span>In Bản Ghi (Print)</span>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Vùng in ẩn độc quyền cho window.print() -->
    <div id="printableMedicalRecord" class="d-none"></div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- Script Tìm kiếm thời gian thực & In Bệnh Án (Feature 8) -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Định dạng ngày hiện tại vào mẫu in
            const today = new Date();
            const todayStr = String(today.getDate()).padStart(2, '0') + '/' + String(today.getMonth() + 1).padStart(2, '0') + '/' + today.getFullYear();
            document.querySelectorAll('.current-print-date').forEach(el => el.textContent = todayStr);

            // Xử lý Tìm kiếm và Bộ lọc
            const searchInput = document.getElementById('recordSearchInput');
            const filterType = document.getElementById('recordFilterType');
            const recordCounter = document.getElementById('recordCounter');
            const recordCards = document.querySelectorAll('.record-item-card');

            function filterRecords() {
                if (!recordCards.length) return;
                const query = (searchInput ? searchInput.value : '').toLowerCase().trim();
                const type = filterType ? filterType.value : 'all';

                let visibleCount = 0;

                recordCards.forEach(function(card) {
                    const doctor = (card.getAttribute('data-doctor') || '').toLowerCase();
                    const diagnosis = (card.getAttribute('data-diagnosis') || '').toLowerCase();
                    const services = (card.getAttribute('data-services') || '').toLowerCase();
                    const medicines = (card.getAttribute('data-medicines') || '').toLowerCase();
                    const hasRx = card.getAttribute('data-has-rx') === 'true';
                    const hasRevisit = card.getAttribute('data-has-revisit') === 'true';

                    const matchesQuery = !query || doctor.includes(query) || diagnosis.includes(query) || services.includes(query) || medicines.includes(query);
                    let matchesType = true;
                    if (type === 'prescription') matchesType = hasRx;
                    else if (type === 'revisit') matchesType = hasRevisit;

                    if (matchesQuery && matchesType) {
                        card.classList.remove('d-none');
                        visibleCount++;
                    } else {
                        card.classList.add('d-none');
                    }
                });

                if (recordCounter) {
                    recordCounter.textContent = visibleCount + ' ca khám';
                }
            }

            if (searchInput) searchInput.addEventListener('input', filterRecords);
            if (filterType) filterType.addEventListener('change', filterRecords);

            // Xử lý mở Modal xem trước và in
            const printModalEl = document.getElementById('printPreviewModal');
            const printModal = new bootstrap.Modal(printModalEl);
            const printModalContent = document.getElementById('printModalContent');
            const printableRecordContainer = document.getElementById('printableMedicalRecord');

            document.querySelectorAll('.btn-trigger-print').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const index = this.getAttribute('data-index');
                    const sourceEl = document.getElementById('print-source-' + index);
                    if (sourceEl) {
                        printModalContent.innerHTML = sourceEl.innerHTML;
                        printableRecordContainer.innerHTML = sourceEl.innerHTML;
                        printModal.show();
                    }
                });
            });

            window.executePrint = function() {
                printableRecordContainer.classList.remove('d-none');
                window.print();
                printableRecordContainer.classList.add('d-none');
            };
        });
    </script>
</body>

</html>
