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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=3">

    <style>
        :root {
            --dencli-primary: #0EA5E9;
            --dencli-navy: #1E3A5F;
            --dencli-coral: #FF6B6B;
            --dencli-coral-hover: #fa5252;
            --dencli-bg: #F8FAFC;
        }

        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: var(--dencli-bg);
            color: #0F172A;
        }

        .booking-hero {
            background: linear-gradient(135deg, #1E3A5F 0%, #0369A1 100%);
            color: #FFFFFF;
            padding: 3rem 0 4rem;
            margin-bottom: -2rem;
        }

        .booking-container {
            max-width: 980px;
            margin: 0 auto;
            position: relative;
            z-index: 10;
        }

        .booking-card {
            background: #FFFFFF;
            border-radius: 1.5rem;
            border: 1px solid #E2E8F0;
            box-shadow: 0 20px 40px -15px rgba(30, 58, 95, 0.12);
            padding: 2.5rem;
        }

        .form-label {
            font-weight: 600;
            color: var(--dencli-navy);
            margin-bottom: 0.5rem;
            font-size: 0.95rem;
        }

        .form-control, .form-select {
            border-radius: 0.75rem;
            padding: 0.75rem 1rem;
            border-color: #CBD5E1;
            font-size: 0.95rem;
            transition: all 0.2s ease;
        }

        .form-control:focus, .form-select:focus {
            border-color: var(--dencli-primary);
            box-shadow: 0 0 0 3px rgba(14, 165, 233, 0.15);
        }

        .services-selection-box {
            max-height: 240px;
            overflow-y: auto;
            border-radius: 0.75rem;
            background: #F8FAFC;
            border: 1px solid #E2E8F0;
            padding: 1rem;
        }

        .service-checkbox-card {
            background: #FFFFFF;
            border: 1px solid #E2E8F0;
            border-radius: 0.5rem;
            padding: 0.75rem 1rem;
            transition: all 0.2s ease;
            cursor: pointer;
        }

        .service-checkbox-card:hover {
            border-color: var(--dencli-primary);
            background-color: #F0F9FF;
        }

        .btn-submit-booking {
            background-color: var(--dencli-coral);
            color: #FFFFFF;
            font-weight: 700;
            font-size: 1.05rem;
            padding: 0.875rem 2.5rem;
            border-radius: 9999px;
            border: none;
            box-shadow: 0 10px 25px -3px rgba(255, 107, 107, 0.4);
            transition: all 0.25s ease;
        }

        .btn-submit-booking:hover:not(:disabled) {
            background-color: var(--dencli-coral-hover);
            transform: translateY(-2px);
            box-shadow: 0 15px 30px -3px rgba(255, 107, 107, 0.5);
            color: #FFFFFF;
        }

        .btn-submit-booking:disabled {
            opacity: 0.75;
            cursor: not-allowed;
        }

        .guarantee-badge {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.875rem;
            color: #334155;
            font-weight: 500;
        }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- Header dùng chung -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- Hero Header Banner -->
    <div class="booking-hero text-center">
        <div class="container">
            <span class="badge bg-info-subtle text-info-emphasis px-3 py-2 rounded-pill fw-semibold mb-2">
                HỆ THỐNG ĐẶT LỊCH KHÁM THÔNG MINH
            </span>
            <h1 class="display-6 fw-bold mb-2">Đăng Ký Đặt Lịch Hẹn Khám Răng</h1>
            <p class="mx-auto mb-0" style="max-width: 600px; color: #F0F9FF; font-weight: 500;">
                Chủ động chọn bác sĩ chuyên khoa, thời gian phù hợp và dịch vụ mong muốn chỉ với vài thao tác đơn giản.
            </p>
        </div>
    </div>

    <!-- Booking Form Container -->
    <main class="container booking-container mb-5">
        <div class="booking-card">
            <!-- Alert cảnh báo lỗi trực tiếp trên Form (Slot Conflict hoặc thiếu dữ liệu) -->
            <div id="bookingAlert" class="alert alert-danger d-none align-items-center gap-2 mb-4 rounded-3 shadow-sm" role="alert">
                <span class="fs-4">⚠️</span>
                <div id="bookingAlertText" class="fw-semibold"></div>
            </div>

            <!-- Alert thành công trên Form -->
            <div id="bookingSuccessAlert" class="alert alert-success d-none align-items-center gap-2 mb-4 rounded-3 shadow-sm" role="alert">
                <span class="fs-4">✅</span>
                <div id="bookingSuccessText" class="fw-semibold"></div>
            </div>

            <form id="appointmentBookingForm">
                <div class="row g-4">
                    <!-- Bác sĩ điều trị -->
                    <div class="col-12">
                        <label for="doctorId" class="form-label">
                            Bác sĩ phụ trách khám <span class="text-danger">*</span>
                        </label>
                        <select id="doctorId" name="doctor_id" class="form-select form-select-lg" required>
                            <option value="">-- Chọn Bác sĩ chuyên khoa --</option>
                            <c:forEach items="${doctors}" var="doc">
                                <option value="${doc.userId}">
                                    🩺 BS. <c:out value="${doc.fullName}" />
                                    <c:if test="${not empty doc.phone}"> - ĐT: <c:out value="${doc.phone}" /></c:if>
                                </option>
                            </c:forEach>
                        </select>
                        <div class="form-text">Bạn có thể chọn bác sĩ chuyên khoa mong muốn hoặc bác sĩ đã từng điều trị trước đó.</div>
                    </div>

                    <!-- Ngày khám -->
                    <div class="col-md-6">
                        <label for="appointmentDate" class="form-label">
                            Ngày hẹn khám <span class="text-danger">*</span>
                        </label>
                        <input type="date" id="appointmentDate" name="appointment_date" class="form-control" required>
                        <div class="form-text">Phòng khám tiếp nhận đặt lịch từ ngày hôm nay trở đi.</div>
                    </div>

                    <!-- Khung giờ khám -->
                    <div class="col-md-6">
                        <label for="appointmentTime" class="form-label">
                            Khung giờ khám bệnh <span class="text-danger">*</span>
                        </label>
                        <select id="appointmentTime" name="appointment_time" class="form-select" required>
                            <option value="">-- Chọn khung giờ khám --</option>
                            <optgroup label="Buổi Sáng (08:00 - 12:00)">
                                <option value="08:00">08:00 - 09:00 (Sáng sớm)</option>
                                <option value="09:00">09:00 - 10:00 (Sáng)</option>
                                <option value="10:00">10:00 - 11:00 (Sáng)</option>
                                <option value="11:00">11:00 - 12:00 (Trưa)</option>
                            </optgroup>
                            <optgroup label="Buổi Chiều (14:00 - 18:00)">
                                <option value="14:00">14:00 - 15:00 (Đầu giờ chiều)</option>
                                <option value="15:00">15:00 - 16:00 (Chiều)</option>
                                <option value="16:00">16:00 - 17:00 (Chiều muộn)</option>
                                <option value="17:00">17:00 - 18:00 (Cuối ngày)</option>
                            </optgroup>
                        </select>
                        <div class="form-text">Mỗi ca khám lâm sàng tiêu chuẩn kéo dài 45-60 phút.</div>
                    </div>

                    <!-- Dịch vụ nha khoa mong muốn -->
                    <div class="col-12">
                        <label class="form-label d-flex justify-content-between align-items-center">
                            <span>Dịch vụ nha khoa mong muốn</span>
                            <span class="badge bg-light border fw-semibold" style="color: #334155 !important;">Có thể chọn nhiều dịch vụ</span>
                        </label>
                        <div class="services-selection-box">
                            <div class="row g-2">
                                <c:choose>
                                    <c:when test="${empty services}">
                                        <div class="col-12 text-center py-3 text-muted small">
                                            Đang cập nhật danh mục dịch vụ nha khoa...
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${services}" var="svc">
                                            <div class="col-md-6">
                                                <label class="service-checkbox-card d-flex align-items-center justify-content-between w-100 mb-0" for="svc_${svc.serviceId}">
                                                    <div class="d-flex align-items-center gap-2">
                                                        <input class="form-check-input mt-0 service-checkbox" type="checkbox" name="service_ids" value="${svc.serviceId}" id="svc_${svc.serviceId}">
                                                        <span class="fw-semibold text-dark small"><c:out value="${svc.serviceName}" /></span>
                                                    </div>
                                                    <span class="text-primary fw-bold small">
                                                        <fmt:formatNumber value="${svc.price}" pattern="#,##0" /> đ
                                                    </span>
                                                </label>
                                            </div>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="form-text">Bác sĩ sẽ kiểm tra trực tiếp và tư vấn chính xác phác đồ phù hợp nhất với tình trạng răng của bạn.</div>
                    </div>

                    <!-- Ghi chú triệu chứng -->
                    <div class="col-12">
                        <label for="notes" class="form-label">
                            Mô tả triệu chứng hoặc yêu cầu đặc biệt
                        </label>
                        <textarea id="notes" name="notes" class="form-control" rows="3" placeholder="Ví dụ: Răng hàm dưới đau buốt khi nhai, muốn chụp X-quang kiểm tra răng khôn, hoặc yêu cầu gây tê đặc biệt..."></textarea>
                    </div>
                </div>

                <!-- Cam kết dịch vụ -->
                <div class="d-flex flex-wrap align-items-center justify-content-between gap-3 pt-4 border-top mt-4">
                    <div class="d-flex flex-wrap gap-3">
                        <span class="guarantee-badge">🛡️ Khám không đau</span>
                        <span class="guarantee-badge">⏱️ Không phải xếp hàng chờ</span>
                        <span class="guarantee-badge">🔒 Bảo mật hồ sơ bệnh án</span>
                    </div>

                    <div class="d-flex align-items-center gap-2 ms-auto">
                        <a href="${pageContext.request.contextPath}/customer/profile" class="btn btn-outline-secondary rounded-pill px-4">
                            Lịch Sử Khám
                        </a>
                        <button type="submit" id="btnSubmitBooking" class="btn btn-submit-booking d-inline-flex align-items-center gap-2">
                            <span id="btnSubmitSpinner" class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                            <span id="btnSubmitText">Xác Nhận Đặt Lịch</span>
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
            // 1. Ràng buộc ngày tối thiểu là ngày hôm nay
            var today = new Date().toISOString().split('T')[0];
            var dateInput = document.getElementById('appointmentDate');
            if (dateInput) {
                dateInput.min = today;
                if (!dateInput.value) {
                    dateInput.value = today;
                }
            }

            // 2. Xử lý Form Submit qua Fetch API (AJAX JSON)
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

                // Ẩn các thông báo cũ
                errorAlert.classList.add('d-none');
                errorAlert.classList.remove('d-flex');
                successAlert.classList.add('d-none');
                successAlert.classList.remove('d-flex');

                var doctorId = document.getElementById('doctorId').value;
                var appointmentDate = document.getElementById('appointmentDate').value;
                var appointmentTime = document.getElementById('appointmentTime').value;
                var notes = document.getElementById('notes').value;

                if (!doctorId || !appointmentDate || !appointmentTime) {
                    showInlineError("Vui lòng chọn đầy đủ Bác sĩ phụ trách, Ngày khám và Khung giờ!");
                    return;
                }

                // Thu thập danh sách dịch vụ đã chọn
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

                // Trạng thái đang gửi yêu cầu
                btnSubmit.disabled = true;
                spinner.classList.remove('d-none');
                btnText.textContent = "Đang xử lý...";

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
                    btnText.textContent = "Xác Nhận Đặt Lịch";

                    if (res.status === 201 && res.data.success) {
                        // Thành công
                        var successMsg = res.data.message || "Đặt lịch khám thành công!";
                        showInlineSuccess(successMsg + " Nhân viên tiếp đón sẽ liên hệ xác nhận lịch hẹn của bạn.");
                        showToast('success', successMsg);

                        // Reset form
                        form.reset();
                        dateInput.value = today;

                        // Chuyển hướng nhẹ sang trang hồ sơ sau 2.5 giây
                        setTimeout(function() {
                            window.location.href = '${pageContext.request.contextPath}/customer/profile';
                        }, 2500);
                    } else if (res.status === 401) {
                        // Chưa đăng nhập
                        showInlineError("Bạn cần đăng nhập tài khoản để thực hiện đặt lịch khám. Đang chuyển tới trang Đăng nhập...");
                        showToast('error', "Vui lòng đăng nhập!");
                        setTimeout(function() {
                            window.location.href = '${pageContext.request.contextPath}/login?redirect=' + encodeURIComponent(window.location.pathname);
                        }, 1800);
                    } else {
                        // Trùng lịch (Slot Conflict) hoặc lỗi khác
                        var errMsg = res.data.message || "Bác sĩ đã có lịch hẹn trong khung giờ này, vui lòng chọn khung giờ khác!";
                        showInlineError(errMsg);
                        showToast('error', errMsg);
                    }
                })
                .catch(function(err) {
                    btnSubmit.disabled = false;
                    spinner.classList.add('d-none');
                    btnText.textContent = "Xác Nhận Đặt Lịch";
                    console.error("Booking error:", err);
                    showInlineError("Không thể kết nối tới máy chủ. Vui lòng kiểm tra lại kết nối mạng hoặc thử lại!");
                    showToast('error', "Lỗi kết nối máy chủ");
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
        // Expose context path for appointment-realtime.js
        window.APP_CONTEXT_PATH = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/appointment-realtime.js"></script>
</body>
</html>
