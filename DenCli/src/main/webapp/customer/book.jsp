<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Đặt lịch hẹn khám chữa răng trực tuyến tại Phòng khám Nha khoa DenCli. Chọn bác sĩ, dịch vụ và khung giờ nhanh chóng.">
    <title>Đăng Ký Đặt Lịch Khám | DenCli Dental Clinic</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Google Fonts: Plus Jakarta Sans & Figtree -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=7">

    <style>
        /* Scoped UI/UX Pro Max Enhancements for Booking Page */
        .booking-hero {
            background: linear-gradient(135deg, #075985 0%, #0369A1 50%, #0891B2 100%);
            color: #FFFFFF;
            padding: 3rem 1rem 3.5rem;
            position: relative;
            overflow: hidden;
        }
        .booking-hero::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 24px;
            background: #F8FAFC;
            border-radius: 24px 24px 0 0;
        }
        .booking-container {
            margin-top: -2rem;
            position: relative;
            z-index: 10;
        }
        
        /* Stepper bar */
        .booking-stepper {
            display: flex;
            justify-content: space-between;
            position: relative;
            margin-bottom: 2.5rem;
        }
        .booking-stepper::before {
            content: '';
            position: absolute;
            top: 20px;
            left: 15%;
            right: 15%;
            height: 3px;
            background: #E2E8F0;
            z-index: 1;
        }
        .step-item {
            position: relative;
            z-index: 2;
            background: #FFFFFF;
            padding: 0 10px;
            text-align: center;
            flex: 1;
        }
        .step-number {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: #F1F5F9;
            color: #64748B;
            font-weight: 700;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 0.5rem;
            border: 2px solid #E2E8F0;
            transition: all 0.25s ease;
        }
        .step-item.active .step-number {
            background: #0891B2;
            color: #FFFFFF;
            border-color: #0891B2;
            box-shadow: 0 0 0 4px rgba(8, 145, 178, 0.2);
        }
        .step-title {
            font-size: 0.85rem;
            font-weight: 600;
            color: #475569;
        }

        /* Quick Date Pills */
        .quick-date-btn {
            font-size: 0.8125rem;
            font-weight: 600;
            border: 1px solid #CBD5E1;
            background: #FFFFFF;
            color: #334155;
            padding: 0.35rem 0.85rem;
            border-radius: 20px;
            transition: all 0.2s ease;
        }
        .quick-date-btn:hover, .quick-date-btn.active {
            background: #E0F2FE;
            border-color: #0891B2;
            color: #0369A1;
        }

        /* Time Slot Grid */
        .slot-group-title {
            font-size: 0.8125rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            color: #64748B;
            margin-bottom: 0.75rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        .slot-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(130px, 1fr));
            gap: 0.75rem;
        }
        .slot-card {
            border: 1.5px solid #E2E8F0;
            background: #FFFFFF;
            border-radius: 12px;
            padding: 0.65rem 0.5rem;
            text-align: center;
            cursor: pointer;
            transition: all 0.2s cubic-bezier(0.16, 1, 0.3, 1);
            user-select: none;
            position: relative;
        }
        .slot-card:hover:not(.disabled) {
            border-color: #0891B2;
            background: #F0FDFA;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(8, 145, 178, 0.12);
        }
        .slot-card.selected {
            border-color: #0891B2 !important;
            background: linear-gradient(135deg, #0891B2 0%, #0E7490 100%) !important;
            color: #FFFFFF !important;
            box-shadow: 0 6px 16px rgba(8, 145, 178, 0.28);
            transform: translateY(-2px);
        }
        .slot-card.selected .slot-time {
            color: #FFFFFF !important;
            font-weight: 700;
        }
        .slot-card.selected .slot-status {
            color: #CCFBF1 !important;
        }
        .slot-card.disabled {
            background: #F8FAFC !important;
            border-color: #E2E8F0 !important;
            color: #94A3B8 !important;
            cursor: not-allowed;
            opacity: 0.65;
            text-decoration: none;
        }
        .slot-card.disabled .slot-time {
            text-decoration: line-through;
            color: #94A3B8;
        }
        .slot-time {
            font-size: 1rem;
            font-weight: 600;
            color: #1E293B;
            display: block;
        }
        .slot-status {
            font-size: 0.7rem;
            font-weight: 500;
            color: #16A34A;
            display: block;
            margin-top: 2px;
        }
        .slot-card.disabled .slot-status {
            color: #DC2626 !important;
        }

        /* Service Checkbox Card */
        .service-selection-card {
            border: 1.5px solid #E2E8F0;
            border-radius: 12px;
            padding: 0.85rem 1rem;
            background: #FFFFFF;
            cursor: pointer;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .service-selection-card:hover {
            border-color: #0891B2;
            background: #F0FDFA;
        }
        .service-selection-card.checked {
            border-color: #0891B2;
            background: #F0FDFA;
            box-shadow: 0 2px 8px rgba(8, 145, 178, 0.15);
        }
        .service-selection-card .form-check-input {
            width: 1.25rem;
            height: 1.25rem;
            cursor: pointer;
        }

        /* Estimate Box */
        .estimate-box {
            background: linear-gradient(135deg, #F0FDFA 0%, #E0F2FE 100%);
            border: 1px dashed #0891B2;
            border-radius: 12px;
            padding: 1rem 1.25rem;
        }
    </style>
</head>

<body class="d-flex flex-column min-vh-100" style="background-color: #F8FAFC;">

    <!-- Header dùng chung -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- Hero Header Banner -->
    <div class="booking-hero text-center">
        <div class="container">
            <span class="badge px-3 py-2 rounded-pill fw-semibold mb-2" style="background: rgba(255, 255, 255, 0.2); backdrop-filter: blur(4px); color: #E0F2FE;">
                ✨ HỆ THỐNG ĐẶT LỊCH KHÁM CHUẨN CHÂU ÂU
            </span>
            <h1 class="display-6 fw-bold mb-2">Đăng Ký Đặt Lịch Hẹn Khám Răng</h1>
            <p class="mx-auto mb-0" style="max-width: 600px; color: #E0F2FE; font-weight: 400; font-size: 1.05rem;">
                Chủ động chọn bác sĩ chuyên khoa, ngày giờ thuận tiện và dịch vụ theo yêu cầu chỉ với 3 bước đơn giản.
            </p>
        </div>
    </div>

    <!-- Booking Form Container -->
    <main class="container booking-container mb-5">
        <div class="dencli-card p-4 p-md-5 bg-white shadow-sm rounded-4 border-0">
            
            <!-- 3-Step Progress Indicator -->
            <div class="booking-stepper d-none d-md-flex">
                <div class="step-item active">
                    <div class="step-number">1</div>
                    <div class="step-title">Bác Sĩ & Thời Gian</div>
                </div>
                <div class="step-item active">
                    <div class="step-number">2</div>
                    <div class="step-title">Dịch Vụ & Nhu Cầu</div>
                </div>
                <div class="step-item">
                    <div class="step-number">3</div>
                    <div class="step-title">Xác Nhận Đặt Lịch</div>
                </div>
            </div>

            <!-- Alert cảnh báo lỗi trực tiếp trên Form -->
            <div id="bookingAlert" class="alert alert-danger d-none align-items-center gap-2 mb-4 rounded-3 shadow-sm border-0" role="alert" style="background-color: #FEF2F2; color: #991B1B;">
                <span class="fs-4">⚠️</span>
                <div id="bookingAlertText" class="fw-semibold"></div>
            </div>

            <!-- Alert thành công trên Form -->
            <div id="bookingSuccessAlert" class="alert alert-success d-none align-items-center gap-2 mb-4 rounded-3 shadow-sm border-0" role="alert" style="background-color: #F0FDF4; color: #166534;">
                <span class="fs-4">✅</span>
                <div id="bookingSuccessText" class="fw-semibold"></div>
            </div>

            <form id="appointmentBookingForm" novalidate>
                <div class="row g-4">
                    
                    <!-- 1. BÁC SĨ PHỤ TRÁCH -->
                    <div class="col-12">
                        <label for="doctorId" class="form-label fw-bold text-dark d-flex align-items-center gap-2">
                            <span>🩺 Bác sĩ phụ trách thăm khám</span>
                            <span class="text-danger">*</span>
                        </label>
                        <select id="doctorId" name="doctor_id" class="form-select form-select-lg rounded-3 border-secondary-subtle" required>
                            <option value="">-- Chọn Bác sĩ chuyên khoa --</option>
                            <c:forEach items="${doctors}" var="doc">
                                <option value="${doc.userId}">
                                    BS. <c:out value="${doc.fullName}" />
                                    <c:if test="${not empty doc.phone}"> — ĐT: <c:out value="${doc.phone}" /></c:if>
                                </option>
                            </c:forEach>
                        </select>
                        <div class="form-text text-muted mt-1">
                            Hệ thống sẽ đồng bộ lịch làm việc và các khung giờ trống của Bác sĩ được chọn theo thời gian thực.
                        </div>
                    </div>

                    <!-- 2. NGÀY HẸN KHÁM -->
                    <div class="col-lg-5 col-md-6">
                        <label for="appointmentDate" class="form-label fw-bold text-dark d-flex align-items-center gap-2">
                            <span>📅 Ngày hẹn khám</span>
                            <span class="text-danger">*</span>
                        </label>
                        <input type="date" id="appointmentDate" name="appointment_date" class="form-control form-control-lg rounded-3 border-secondary-subtle" required>
                        
                        <!-- Quick Date Selectors -->
                        <div class="d-flex flex-wrap gap-2 mt-2">
                            <button type="button" class="quick-date-btn active" data-offset="0">Hôm nay</button>
                            <button type="button" class="quick-date-btn" data-offset="1">Ngày mai</button>
                            <button type="button" class="quick-date-btn" data-offset="2">Ngày kia</button>
                        </div>
                    </div>

                    <!-- 3. KHUNG GIỜ KHÁM DẠNG LƯỚI TRỰC QUAN (TIME SLOT GRID) -->
                    <div class="col-lg-7 col-md-6">
                        <label class="form-label fw-bold text-dark d-flex align-items-center justify-content-between">
                            <span>⏰ Chọn khung giờ khám</span>
                            <span class="text-danger">*</span>
                        </label>

                        <!-- Hidden select for form submission & realtime websocket sync -->
                        <select id="appointmentTime" name="appointment_time" class="d-none" required>
                            <option value="">-- Chọn khung giờ khám --</option>
                            <option value="08:00">08:00</option>
                            <option value="09:00">09:00</option>
                            <option value="10:00">10:00</option>
                            <option value="11:00">11:00</option>
                            <option value="14:00">14:00</option>
                            <option value="15:00">15:00</option>
                            <option value="16:00">16:00</option>
                            <option value="17:00">17:00</option>
                        </select>

                        <!-- Ca Sáng -->
                        <div class="mb-3">
                            <div class="slot-group-title">
                                <span>🌅 Buổi Sáng (08:00 - 12:00)</span>
                            </div>
                            <div class="slot-grid">
                                <div class="slot-card" data-slot="08:00">
                                    <span class="slot-time">08:00 - 09:00</span>
                                    <span class="slot-status">Trống</span>
                                </div>
                                <div class="slot-card" data-slot="09:00">
                                    <span class="slot-time">09:00 - 10:00</span>
                                    <span class="slot-status">Trống</span>
                                </div>
                                <div class="slot-card" data-slot="10:00">
                                    <span class="slot-time">10:00 - 11:00</span>
                                    <span class="slot-status">Trống</span>
                                </div>
                                <div class="slot-card" data-slot="11:00">
                                    <span class="slot-time">11:00 - 12:00</span>
                                    <span class="slot-status">Trống</span>
                                </div>
                            </div>
                        </div>

                        <!-- Ca Chiều -->
                        <div>
                            <div class="slot-group-title">
                                <span>🌇 Buổi Chiều (14:00 - 18:00)</span>
                            </div>
                            <div class="slot-grid">
                                <div class="slot-card" data-slot="14:00">
                                    <span class="slot-time">14:00 - 15:00</span>
                                    <span class="slot-status">Trống</span>
                                </div>
                                <div class="slot-card" data-slot="15:00">
                                    <span class="slot-time">15:00 - 16:00</span>
                                    <span class="slot-status">Trống</span>
                                </div>
                                <div class="slot-card" data-slot="16:00">
                                    <span class="slot-time">16:00 - 17:00</span>
                                    <span class="slot-status">Trống</span>
                                </div>
                                <div class="slot-card" data-slot="17:00">
                                    <span class="slot-time">17:00 - 18:00</span>
                                    <span class="slot-status">Trống</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-text text-muted mt-2">
                            Mỗi ca khám kéo dài 45-60 phút. Các ô màu xám gạch ngang là khung giờ đã có người đặt trước.
                        </div>
                    </div>

                    <!-- 4. DỊCH VỤ NHA KHOA MONG MUỐN & TẠM TÍNH PHÍ -->
                    <div class="col-12 pt-2">
                        <div class="d-flex flex-wrap justify-content-between align-items-center mb-2">
                            <label class="form-label fw-bold text-dark mb-0">
                                🦷 Dịch vụ nha khoa mong muốn
                            </label>
                            <span class="badge px-3 py-1 rounded-pill" style="background-color: #E0F2FE; color: #0369A1; font-weight: 600;">
                                Có thể chọn nhiều dịch vụ
                            </span>
                        </div>

                        <div class="row g-2 mb-3">
                            <c:choose>
                                <c:when test="${empty services}">
                                    <div class="col-12 text-center py-4 text-muted small bg-light rounded-3">
                                        Đang cập nhật danh mục dịch vụ nha khoa...
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${services}" var="svc">
                                        <div class="col-md-6">
                                            <label class="service-selection-card w-100 mb-0" for="svc_${svc.serviceId}">
                                                <div class="d-flex align-items-center gap-2">
                                                    <input class="form-check-input mt-0 service-checkbox" type="checkbox" name="service_ids" value="${svc.serviceId}" id="svc_${svc.serviceId}" data-price="${svc.price}">
                                                    <span class="fw-semibold text-dark small"><c:out value="${svc.serviceName}" /></span>
                                                </div>
                                                <span class="fw-bold small" style="color: #0891B2;">
                                                    <fmt:formatNumber value="${svc.price}" pattern="#,##0" /> đ
                                                </span>
                                            </label>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Hộp Tạm Tính Dự Kiến -->
                        <div class="estimate-box d-flex justify-content-between align-items-center flex-wrap gap-2">
                            <div>
                                <span class="fw-bold text-dark d-block">Tạm tính chi phí dịch vụ chọn trước:</span>
                                <small class="text-muted">Chi phí thực tế có thể linh hoạt theo tư vấn trực tiếp của Bác sĩ.</small>
                            </div>
                            <div class="text-end">
                                <span id="totalEstimatePrice" class="fs-4 fw-bold" style="color: #0891B2;">0 đ</span>
                                <small class="d-block text-muted" id="selectedServicesCount">(Chưa chọn dịch vụ - Khám tổng quát)</small>
                            </div>
                        </div>
                    </div>

                    <!-- 5. MÔ TẢ TRIỆU CHỨNG -->
                    <div class="col-12">
                        <label for="notes" class="form-label fw-bold text-dark">
                            📝 Mô tả triệu chứng hoặc ghi chú cho Bác sĩ
                        </label>
                        <textarea id="notes" name="notes" class="form-control rounded-3 border-secondary-subtle" rows="3" placeholder="Ví dụ: Răng hàm dưới đau buốt khi nhai nước đá, có nhu cầu khám tư vấn niềng răng trong suốt, hoặc dị ứng thuốc gây tê..."></textarea>
                    </div>
                </div>

                <!-- CAM KẾT VÀ NÚT SUBMIT -->
                <div class="d-flex flex-wrap align-items-center justify-content-between gap-3 pt-4 border-top mt-4">
                    <div class="d-flex flex-wrap gap-2 text-muted small">
                        <span class="badge bg-light border text-dark fw-semibold px-3 py-2 rounded-pill">🛡️ Khám êm ái - Chuẩn Châu Âu</span>
                        <span class="badge bg-light border text-dark fw-semibold px-3 py-2 rounded-pill">⏱️ Tiếp đón đúng giờ</span>
                        <span class="badge bg-light border text-dark fw-semibold px-3 py-2 rounded-pill">🔒 Bảo mật hồ sơ bệnh án</span>
                    </div>

                    <div class="d-flex align-items-center gap-2 ms-auto">
                        <a href="${pageContext.request.contextPath}/customer/appointments" class="btn btn-outline-secondary rounded-pill px-4 fw-semibold">
                            Lịch Sử Cuộc Hẹn
                        </a>
                        <button type="submit" id="btnSubmitBooking" class="btn btn-lg rounded-pill px-4 fw-bold text-white d-inline-flex align-items-center gap-2 shadow-sm" style="background: linear-gradient(135deg, #0891B2 0%, #0E7490 100%); border: none;">
                            <span id="btnSubmitSpinner" class="spinner-border spinner-border-sm d-none" aria-hidden="true"></span>
                            <span id="btnSubmitText">Xác Nhận Đặt Lịch Khám</span>
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </main>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- Script xử lý AJAX Fetch API Đặt Lịch & Slot Conflict Handling -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var dateInput = document.getElementById('appointmentDate');
            var timeSelect = document.getElementById('appointmentTime');
            var slotCards = document.querySelectorAll('.slot-card');
            var quickDateBtns = document.querySelectorAll('.quick-date-btn');

            // 1. Khởi tạo ngày hôm nay làm ngày tối thiểu
            var today = new Date();
            var yyyy = today.getFullYear();
            var mm = String(today.getMonth() + 1).padStart(2, '0');
            var dd = String(today.getDate()).padStart(2, '0');
            var todayStr = yyyy + '-' + mm + '-' + dd;

            if (dateInput) {
                dateInput.min = todayStr;
                if (!dateInput.value) {
                    dateInput.value = todayStr;
                }
            }

            // Xử lý Quick Date Buttons (Hôm nay, Ngày mai, Ngày kia)
            quickDateBtns.forEach(function(btn) {
                btn.addEventListener('click', function() {
                    quickDateBtns.forEach(function(b) { b.classList.remove('active'); });
                    btn.classList.add('active');

                    var offset = parseInt(btn.getAttribute('data-offset') || '0');
                    var targetDate = new Date();
                    targetDate.setDate(today.getDate() + offset);

                    var tY = targetDate.getFullYear();
                    var tM = String(targetDate.getMonth() + 1).padStart(2, '0');
                    var tD = String(targetDate.getDate()).padStart(2, '0');
                    dateInput.value = tY + '-' + tM + '-' + tD;

                    // Kích hoạt sự kiện change để trigger sync lịch
                    dateInput.dispatchEvent(new Event('change'));
                });
            });

            // 2. Tương tác với Lưới Slot giờ (Slot Cards)
            slotCards.forEach(function(card) {
                card.addEventListener('click', function() {
                    if (card.classList.contains('disabled')) {
                        return;
                    }
                    var slotValue = card.getAttribute('data-slot');

                    slotCards.forEach(function(c) { c.classList.remove('selected'); });
                    card.classList.add('selected');

                    // Gán vào hidden select
                    if (timeSelect) {
                        timeSelect.value = slotValue;
                        timeSelect.dispatchEvent(new Event('change'));
                    }
                });
            });

            // Quan sát thay đổi từ hidden select (ví dụ khi WebSocket đánh dấu slot booked)
            var observer = new MutationObserver(function() {
                syncSlotsVisual();
            });
            if (timeSelect) {
                observer.observe(timeSelect, { childList: true, subtree: true, attributes: true });
            }

            // Đồng bộ trạng thái visual từ options của select
            function syncSlotsVisual() {
                if (!timeSelect) return;
                var bookedValues = [];
                Array.from(timeSelect.options).forEach(function(opt) {
                    if (opt.disabled && opt.value) {
                        bookedValues.push(opt.value);
                    }
                });

                slotCards.forEach(function(card) {
                    var slot = card.getAttribute('data-slot');
                    var statusSpan = card.querySelector('.slot-status');
                    var isBooked = bookedValues.some(function(b) { return b === slot || b.startsWith(slot); });

                    if (isBooked) {
                        card.classList.add('disabled');
                        card.classList.remove('selected');
                        if (statusSpan) statusSpan.textContent = 'Đã kín';
                        if (timeSelect.value === slot) {
                            timeSelect.value = '';
                        }
                    } else {
                        card.classList.remove('disabled');
                        if (statusSpan && !card.classList.contains('selected')) {
                            statusSpan.textContent = 'Trống';
                        }
                    }
                });
            }

            // Định kỳ kiểm tra sync slots visual
            setInterval(syncSlotsVisual, 1000);

            // 3. Tính toán Tạm tính chi phí dịch vụ chọn trước
            var serviceCheckboxes = document.querySelectorAll('.service-checkbox');
            var totalEstimateElem = document.getElementById('totalEstimatePrice');
            var selectedCountElem = document.getElementById('selectedServicesCount');

            function updateEstimate() {
                var total = 0;
                var count = 0;
                serviceCheckboxes.forEach(function(cb) {
                    var card = cb.closest('.service-selection-card');
                    if (cb.checked) {
                        if (card) card.classList.add('checked');
                        var price = parseFloat(cb.getAttribute('data-price') || '0');
                        total += price;
                        count++;
                    } else {
                        if (card) card.classList.remove('checked');
                    }
                });

                if (totalEstimateElem) {
                    totalEstimateElem.textContent = new Intl.NumberFormat('vi-VN').format(total) + ' đ';
                }
                if (selectedCountElem) {
                    if (count === 0) {
                        selectedCountElem.textContent = '(Chưa chọn dịch vụ - Khám tổng quát)';
                    } else {
                        selectedCountElem.textContent = '(Đã chọn ' + count + ' dịch vụ)';
                    }
                }
            }

            serviceCheckboxes.forEach(function(cb) {
                cb.addEventListener('change', updateEstimate);
            });
            updateEstimate();

            // 4. Xử lý Form Submit qua Fetch API (AJAX JSON)
            var form = document.getElementById('appointmentBookingForm');
            var btnSubmit = document.getElementById('btnSubmitBooking');
            var spinner = document.getElementById('btnSubmitSpinner');
            var btnText = document.getElementById('btnSubmitText');
            var errorAlert = document.getElementById('bookingAlert');
            var errorAlertText = document.getElementById('bookingAlertText');
            var successAlert = document.getElementById('bookingSuccessAlert');
            var successAlertText = document.getElementById('bookingSuccessText');

            form.addEventListener('submit', function(e) {
                e.preventDefault();

                errorAlert.classList.add('d-none');
                errorAlert.classList.remove('d-flex');
                successAlert.classList.add('d-none');
                successAlert.classList.remove('d-flex');

                var doctorId = document.getElementById('doctorId').value;
                var appointmentDate = document.getElementById('appointmentDate').value;
                var appointmentTime = document.getElementById('appointmentTime').value;
                var notes = document.getElementById('notes').value;

                if (!doctorId) {
                    showInlineError("Vui lòng chọn Bác sĩ phụ trách thăm khám!");
                    document.getElementById('doctorId').focus();
                    return;
                }
                if (!appointmentDate) {
                    showInlineError("Vui lòng chọn Ngày hẹn khám!");
                    document.getElementById('appointmentDate').focus();
                    return;
                }
                if (!appointmentTime) {
                    showInlineError("Vui lòng bấm chọn một Khung giờ khám còn trống!");
                    return;
                }

                var serviceIds = [];
                var checkedBoxes = document.querySelectorAll('.service-checkbox:checked');
                checkedBoxes.forEach(function(cb) {
                    var val = parseInt(cb.value);
                    if (!isNaN(val)) {
                        serviceIds.push(val);
                    }
                });

                var payload = {
                    doctorId: parseInt(doctorId),
                    appointmentDate: appointmentDate,
                    appointmentTime: appointmentTime,
                    notes: notes,
                    serviceIds: serviceIds
                };

                btnSubmit.disabled = true;
                spinner.classList.remove('d-none');
                btnText.textContent = "Đang đặt chỗ...";

                fetch('${pageContext.request.contextPath}/customer/book', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8',
                        'Accept': 'application/json',
                        'X-CSRF-TOKEN': window.CSRF_TOKEN || ''
                    },
                    body: JSON.stringify(payload)
                })
                .then(function(response) {
                    return response.json().then(function(data) {
                        return { status: response.status, data: data };
                    });
                })
                .then(function(res) {
                    btnSubmit.disabled = false;
                    spinner.classList.add('d-none');
                    btnText.textContent = "Xác Nhận Đặt Lịch Khám";

                    if (res.status === 201 && res.data.success) {
                        var successMsg = res.data.message || "Đăng ký lịch khám thành công!";
                        showInlineSuccess(successMsg + " Nhân viên tiếp đón sẽ liên hệ xác nhận cuộc hẹn với bạn trong ít phút.");
                        if (typeof window.showToast === 'function') {
                            window.showToast('success', successMsg);
                        }

                        form.reset();
                        slotCards.forEach(function(c) { c.classList.remove('selected'); });
                        updateEstimate();

                        setTimeout(function() {
                            window.location.href = '${pageContext.request.contextPath}/customer/appointments';
                        }, 2200);
                    } else if (res.status === 401) {
                        showInlineError("Bạn cần đăng nhập tài khoản để đặt lịch khám. Đang chuyển tới trang Đăng nhập...");
                        if (typeof window.showToast === 'function') {
                            window.showToast('error', "Vui lòng đăng nhập!");
                        }
                        setTimeout(function() {
                            window.location.href = '${pageContext.request.contextPath}/login?redirect=' + encodeURIComponent(window.location.pathname);
                        }, 1800);
                    } else {
                        var errMsg = res.data.message || "Bác sĩ đã có lịch hẹn trong khung giờ này, vui lòng chọn khung giờ khác!";
                        showInlineError(errMsg);
                        if (typeof window.showToast === 'function') {
                            window.showToast('error', errMsg);
                        }
                    }
                })
                .catch(function(err) {
                    btnSubmit.disabled = false;
                    spinner.classList.add('d-none');
                    btnText.textContent = "Xác Nhận Đặt Lịch Khám";
                    console.error("Booking error:", err);
                    showInlineError("Không thể kết nối tới máy chủ. Vui lòng kiểm tra lại kết nối mạng hoặc thử lại!");
                });
            });

            function showInlineError(msg) {
                errorAlertText.textContent = msg;
                errorAlert.classList.remove('d-none');
                errorAlert.classList.add('d-flex');
                errorAlert.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            }

            function showInlineSuccess(msg) {
                successAlertText.textContent = msg;
                successAlert.classList.remove('d-none');
                successAlert.classList.add('d-flex');
                successAlert.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            }
        });
    </script>
    <!-- Real-time slot synchronization via WebSocket -->
    <script>
        window.APP_CONTEXT_PATH = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/appointment-realtime.js"></script>
</body>

</html>
