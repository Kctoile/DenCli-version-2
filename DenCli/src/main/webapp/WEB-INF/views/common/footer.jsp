<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Footer dùng chung cho toàn bộ hệ thống DenCli (Bootstrap 5) -->
<footer class="bg-white border-top py-3 mt-auto text-center text-muted" style="font-size: 0.8125rem;">
    <div class="container-fluid">
        <span>© 2026 <strong>Phòng Khám Nha Khoa DenCli</strong>. Hệ thống quản lý khám chữa bệnh tiêu chuẩn.</span>
    </div>
</footer>

<!-- Vùng hiển thị thông báo Toast thông minh (AJAX Notifications) -->
<div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 1090;">
    <div id="liveToast" class="toast align-items-center text-white border-0 shadow" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div id="toastBody" class="toast-body fw-semibold"></div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle CDN -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<!-- Chart.js CDN -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.2/dist/chart.umd.min.js"></script>

<!-- Helper JavaScript hiển thị thông báo Toast chuyên nghiệp -->
<script>
    function showToast(type, message) {
        var toastEl = document.getElementById('liveToast');
        var toastBody = document.getElementById('toastBody');
        if (!toastEl || !toastBody) return;

        toastEl.className = 'toast align-items-center text-white border-0 shadow ' + (type === 'success' ? 'bg-success' : 'bg-danger');
        toastBody.textContent = message;

        var toast = new bootstrap.Toast(toastEl, { delay: 4000 });
        toast.show();
    }
</script>
