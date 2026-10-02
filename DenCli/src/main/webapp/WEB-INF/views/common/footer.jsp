<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Footer dùng chung cho toàn bộ hệ thống DenCli (Bootstrap 5 & Impeccable Design) -->
<footer class="bg-dark text-white pt-5 pb-4 mt-auto border-top border-secondary-subtle" style="background: #111827 !important;">
    <div class="container">
        <div class="row g-4 mb-4">
            <!-- Cột 1: Thông tin phòng khám -->
            <div class="col-lg-4 col-md-6">
                <div class="d-flex align-items-center gap-2 mb-3">
                    <span class="d-inline-flex align-items-center justify-content-center bg-primary text-white rounded-circle shadow-sm" style="width: 36px; height: 36px; font-size: 1.2rem;">
                        🦷
                    </span>
                    <h5 class="fw-bold mb-0 text-white">DenCli Dental Clinic</h5>
                </div>
                <p class="text-secondary small mb-3 leading-relaxed">
                    Hệ thống Nha khoa Công nghệ cao DenCli tự hào mang đến các dịch vụ chăm sóc răng miệng tiêu chuẩn Châu Âu. Đồng hành kiến tạo nụ cười tự tin và rạng rỡ cho hàng vạn gia đình Việt Nam.
                </p>
                <div class="d-flex flex-wrap gap-2">
                    <span class="badge border px-3 py-2 rounded-pill small" style="background-color: rgba(14, 165, 233, 0.18); color: #E0F2FE; border-color: rgba(56, 189, 248, 0.45) !important; font-weight: 600;">
                        🛡️ Chuẩn ISO 9001:2015
                    </span>
                    <span class="badge border px-3 py-2 rounded-pill small" style="background-color: rgba(14, 165, 233, 0.18); color: #E0F2FE; border-color: rgba(56, 189, 248, 0.45) !important; font-weight: 600;">
                        ✨ 100% Thiết Bị Châu Âu
                    </span>
                </div>
            </div>

            <!-- Cột 2: Dịch vụ nha khoa nổi bật -->
            <div class="col-lg-3 col-md-6">
                <h6 class="text-uppercase fw-bold text-info mb-3 tracking-wide">Dịch Vụ Nổi Bật</h6>
                <ul class="list-unstyled text-secondary small mb-0 d-flex flex-column gap-2">
                    <li><a href="${pageContext.request.contextPath}/index.jsp#services" class="text-secondary text-decoration-none hover-text-white">🦷 Niềng Răng Chỉnh Nha Mắc Cài & Invisalign</a></li>
                    <li><a href="${pageContext.request.contextPath}/index.jsp#services" class="text-secondary text-decoration-none hover-text-white">✨ Tẩy Trắng Răng Công Nghệ Laser Whitening</a></li>
                    <li><a href="${pageContext.request.contextPath}/index.jsp#services" class="text-secondary text-decoration-none hover-text-white">💎 Bọc Răng Sứ Thẩm Mỹ Cercon HT</a></li>
                    <li><a href="${pageContext.request.contextPath}/index.jsp#services" class="text-secondary text-decoration-none hover-text-white">🔩 Cấy Ghép Trồng Răng Implant Châu Âu</a></li>
                    <li><a href="${pageContext.request.contextPath}/index.jsp#services" class="text-secondary text-decoration-none hover-text-white">🩺 Khám Nha Khoa Tổng Quát & Lấy Cao Răng</a></li>
                </ul>
            </div>

            <!-- Cột 3: Giờ làm việc & Đặt hẹn -->
            <div class="col-lg-2 col-md-6">
                <h6 class="text-uppercase fw-bold text-info mb-3 tracking-wide">Giờ Làm Việc</h6>
                <div class="text-secondary small mb-3">
                    <p class="mb-1 fw-semibold text-white">Thứ Hai - Chủ Nhật:</p>
                    <p class="mb-2">08:00 - 18:00</p>
                    <p class="text-muted fst-italic mb-0">* Khám liên tục không nghỉ trưa, tiếp đón tận tâm.</p>
                </div>
                <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-sm btn-outline-info rounded-pill px-3 py-1">
                    Đặt lịch khám ngay →
                </a>
            </div>

            <!-- Cột 4: Thông tin liên hệ -->
            <div class="col-lg-3 col-md-6">
                <h6 class="text-uppercase fw-bold text-info mb-3 tracking-wide">Liên Hệ Phòng Khám</h6>
                <ul class="list-unstyled text-secondary small mb-0 d-flex flex-column gap-2">
                    <li class="d-flex align-items-start gap-2">
                        <span class="text-info">📍</span>
                        <span>45 Phố Huế, P. Nguyễn Du, Q. Hai Bà Trưng, Hà Nội</span>
                    </li>
                    <li class="d-flex align-items-center gap-2">
                        <span class="text-info">📞</span>
                        <span>Hotline 24/7: <strong class="text-warning">1900 6868</strong></span>
                    </li>
                    <li class="d-flex align-items-center gap-2">
                        <span class="text-info">✉️</span>
                        <span>Email: info@dencli.com</span>
                    </li>
                </ul>
            </div>
        </div>

        <hr class="border-secondary my-4">

        <div class="d-flex flex-column flex-sm-row justify-content-between align-items-center gap-2 text-secondary small">
            <div>
                © 2026 <strong>Phòng Khám Nha Khoa Quốc Tế DenCli</strong>. Bảo lưu mọi quyền.
            </div>
            <div class="d-flex gap-3">
                <a href="${pageContext.request.contextPath}/index.jsp#faq" class="text-secondary text-decoration-none">Chính sách bảo mật</a>
                <span>•</span>
                <a href="${pageContext.request.contextPath}/index.jsp#faq" class="text-secondary text-decoration-none">Điều khoản dịch vụ</a>
                <span>•</span>
                <a href="${pageContext.request.contextPath}/index.jsp#services" class="text-secondary text-decoration-none">Biểu phí niêm yết</a>
            </div>
        </div>
    </div>
</footer>

<!-- Vùng hiển thị thông báo Toast thông minh (AJAX Notifications) -->
<div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 1090;">
    <div id="liveToast" class="toast align-items-center text-white border-0 shadow-lg rounded-3" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex align-items-center p-2">
            <div id="toastIcon" class="fs-4 me-2 ms-1"></div>
            <div id="toastBody" class="toast-body fw-semibold py-1"></div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle CDN -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

<!-- Helper JavaScript hiển thị thông báo Toast chuyên nghiệp -->
<script>
    function showToast(type, message) {
        var toastEl = document.getElementById('liveToast');
        var toastBody = document.getElementById('toastBody');
        var toastIcon = document.getElementById('toastIcon');
        if (!toastEl || !toastBody) return;

        var isSuccess = (type === 'success');
        toastEl.className = 'toast align-items-center text-white border-0 shadow-lg rounded-3 ' + (isSuccess ? 'bg-success' : 'bg-danger');
        if (toastIcon) {
            toastIcon.textContent = isSuccess ? '✅' : '⚠️';
        }
        toastBody.textContent = message;

        var toast = new bootstrap.Toast(toastEl, { delay: 4500 });
        toast.show();
    }
</script>
