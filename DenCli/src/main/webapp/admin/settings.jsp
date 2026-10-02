<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cấu hình Hệ thống | DenCli Admin</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=3">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: #f8fafc; }
        .card-custom { border: none; border-radius: 0.75rem; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

            <main class="col-12 col-md-9 col-lg-10 p-4">
                <div class="card card-custom bg-white p-4 p-md-5 mx-auto" style="max-width: 780px;">
                    <div class="d-flex align-items-center gap-3 border-bottom pb-3 mb-4">
                        <span class="fs-2">⚙️</span>
                        <div>
                            <h4 class="fw-bold text-dark mb-0">Cấu hình Hệ thống Phòng khám</h4>
                            <p class="text-muted small mb-0">Thiết lập thông tin thương hiệu, hotline, quy tắc lịch hẹn và email.</p>
                        </div>
                    </div>

                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
                            <strong>✅ Thành công:</strong> <c:out value="${successMessage}" />
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/admin/settings" method="POST">
                        <input type="hidden" name="_csrf" value="${csrfToken}">

                        <h6 class="fw-bold text-primary mb-3">1. Thông tin Phòng khám</h6>
                        <div class="row g-3 mb-4">
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Tên phòng khám</label>
                                <input type="text" name="clinic_name" class="form-control" value="<c:out value='${settings.clinic_name}' />" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Hotline liên hệ</label>
                                <input type="text" name="hotline" class="form-control" value="<c:out value='${settings.hotline}' />" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Email hỗ trợ</label>
                                <input type="email" name="email" class="form-control" value="<c:out value='${settings.email}' />" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Thời gian làm việc</label>
                                <input type="text" name="open_hours" class="form-control" value="<c:out value='${settings.open_hours}' />" required>
                            </div>
                            <div class="col-12">
                                <label class="form-label fw-semibold">Địa chỉ trụ sở</label>
                                <input type="text" name="address" class="form-control" value="<c:out value='${settings.address}' />" required>
                            </div>
                        </div>

                        <h6 class="fw-bold text-primary mb-3">2. Quy tắc Vận hành Tự động</h6>
                        <div class="row g-3 mb-4">
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Thời gian tự động hủy lịch Pending (giờ)</label>
                                <input type="number" name="auto_cancel_hours" class="form-control" value="<c:out value='${settings.auto_cancel_hours}' />" min="1" max="168" required>
                                <div class="form-text">Lịch hẹn chờ xác nhận quá số giờ này sẽ tự động hủy bởi hệ thống.</div>
                            </div>
                            <div class="col-md-6 d-flex align-items-center">
                                <div class="form-check form-switch mt-3">
                                    <input class="form-check-input" type="checkbox" name="notify_email" id="notifyEmailSwitch" ${settings.notify_email == 'true' ? 'checked' : ''}>
                                    <label class="form-check-label fw-semibold" for="notifyEmailSwitch">Bật thông báo OTP & Email tự động</label>
                                </div>
                            </div>
                        </div>

                        <div class="text-end border-top pt-3">
                            <button type="submit" class="btn btn-primary px-4 fw-semibold">
                                💾 Lưu Cấu hình
                            </button>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
