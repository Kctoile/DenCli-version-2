<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Footer dùng chung cho toàn bộ hệ thống DenCli (Bootstrap 5 & Impeccable Design - High Contrast WCAG AAA) -->
<footer class="dencli-footer pt-5 pb-4 mt-auto">
    <style>
        .dencli-footer {
            background: #0B132B !important;
            color: #E2E8F0 !important;
            border-top: 1px solid rgba(56, 189, 248, 0.25) !important;
        }
        .dencli-footer .footer-heading {
            color: #38BDF8 !important;
            font-size: 0.9rem;
            letter-spacing: 0.06em;
            font-weight: 700;
        }
        .dencli-footer .footer-text {
            color: #CBD5E1 !important; /* Slate-300: Đạt chuẩn tương phản 11.5:1 trên nền tối */
            font-size: 0.875rem;
            line-height: 1.65;
        }
        .dencli-footer .footer-subtext {
            color: #94A3B8 !important; /* Slate-400: Tương phản 7.2:1 */
            font-size: 0.8125rem;
        }
        .dencli-footer .footer-link {
            color: #CBD5E1 !important;
            text-decoration: none !important;
            font-size: 0.875rem;
            transition: color 0.2s ease, transform 0.2s ease;
            display: inline-block;
        }
        .dencli-footer .footer-link:hover {
            color: #38BDF8 !important; /* Cyan phát sáng khi hover */
            transform: translateX(3px);
        }
        .dencli-footer .footer-bottom-link {
            color: #94A3B8 !important;
            text-decoration: none !important;
            transition: color 0.2s ease;
        }
        .dencli-footer .footer-bottom-link:hover {
            color: #38BDF8 !important;
        }
        .dencli-footer hr {
            border-color: rgba(148, 163, 184, 0.25) !important;
            opacity: 1 !important;
        }
        .dencli-footer .btn-cta-footer {
            border: 1.5px solid #38BDF8 !important;
            color: #38BDF8 !important;
            background: transparent;
            font-weight: 600;
            transition: all 0.25s ease;
        }
        .dencli-footer .btn-cta-footer:hover {
            background: #38BDF8 !important;
            color: #0B132B !important;
            box-shadow: 0 4px 14px rgba(56, 189, 248, 0.35);
        }
    </style>

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
                <p class="footer-text mb-3">
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
                <h6 class="text-uppercase footer-heading mb-3">Dịch Vụ Nổi Bật</h6>
                <ul class="list-unstyled mb-0 d-flex flex-column gap-2">
                    <li><a href="${pageContext.request.contextPath}/index.jsp#services" class="footer-link">🦷 Niềng Răng Chỉnh Nha Mắc Cài & Invisalign</a></li>
                    <li><a href="${pageContext.request.contextPath}/index.jsp#services" class="footer-link">✨ Tẩy Trắng Răng Công Nghệ Laser Whitening</a></li>
                    <li><a href="${pageContext.request.contextPath}/index.jsp#services" class="footer-link">💎 Bọc Răng Sứ Thẩm Mỹ Cercon HT</a></li>
                    <li><a href="${pageContext.request.contextPath}/index.jsp#services" class="footer-link">🔩 Cấy Ghép Trồng Răng Implant Châu Âu</a></li>
                    <li><a href="${pageContext.request.contextPath}/index.jsp#services" class="footer-link">🩺 Khám Nha Khoa Tổng Quát & Lấy Cao Răng</a></li>
                </ul>
            </div>

            <!-- Cột 3: Giờ làm việc & Đặt hẹn -->
            <div class="col-lg-2 col-md-6">
                <h6 class="text-uppercase footer-heading mb-3">Giờ Làm Việc</h6>
                <div class="mb-3">
                    <p class="mb-1 fw-semibold text-white">Thứ Hai - Chủ Nhật:</p>
                    <p class="mb-2 footer-text">08:00 - 18:00</p>
                    <p class="footer-subtext fst-italic mb-0">* Khám liên tục không nghỉ trưa, tiếp đón tận tâm.</p>
                </div>
                <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-sm btn-cta-footer rounded-pill px-3 py-1">
                    Đặt lịch khám ngay →
                </a>
            </div>

            <!-- Cột 4: Thông tin liên hệ -->
            <div class="col-lg-3 col-md-6">
                <h6 class="text-uppercase footer-heading mb-3">Liên Hệ Phòng Khám</h6>
                <ul class="list-unstyled mb-0 d-flex flex-column gap-2 footer-text">
                    <li class="d-flex align-items-start gap-2">
                        <span style="color: #38BDF8;">📍</span>
                        <span class="footer-text">45 Phố Huế, P. Nguyễn Du, Q. Hai Bà Trưng, Hà Nội</span>
                    </li>
                    <li class="d-flex align-items-center gap-2">
                        <span style="color: #38BDF8;">📞</span>
                        <span class="footer-text">Hotline 24/7: <strong style="color: #FBBF24; font-size: 1.05rem;">1900 6868</strong></span>
                    </li>
                    <li class="d-flex align-items-center gap-2">
                        <span style="color: #38BDF8;">✉️</span>
                        <span class="footer-text">Email: info@dencli.com</span>
                    </li>
                </ul>
            </div>
        </div>

        <hr class="my-4">

        <div class="d-flex flex-column flex-sm-row justify-content-between align-items-center gap-2 footer-subtext">
            <div>
                © 2026 <strong class="text-white">Phòng Khám Nha Khoa Quốc Tế DenCli</strong>. Bảo lưu mọi quyền.
            </div>
            <div class="d-flex gap-3">
                <a href="${pageContext.request.contextPath}/index.jsp#faq" class="footer-bottom-link">Chính sách bảo mật</a>
                <span style="color: #94A3B8;">•</span>
                <a href="${pageContext.request.contextPath}/index.jsp#faq" class="footer-bottom-link">Điều khoản dịch vụ</a>
                <span style="color: #94A3B8;">•</span>
                <a href="${pageContext.request.contextPath}/index.jsp#services" class="footer-bottom-link">Biểu phí niêm yết</a>
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
