<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Sidebar Menu - điều hướng theo vai trò. Dùng scriptlet để tránh lỗi EL ternary trong attribute --%>
<%
    String uri = request.getRequestURI();
    String cp  = request.getContextPath();
%>
<div class="col-12 col-md-3 col-lg-2 bg-white border-end py-3 px-2 shadow-sm min-vh-md-100">
    <div class="list-group list-group-flush">
        <c:choose>
            <%-- MENU CHO QUẢN TRỊ VIÊN (ROLE 1) --%>
            <c:when test="${sessionScope.user.roleId == 1}">
                <div class="text-uppercase text-muted px-3 pb-2 fw-bold" style="font-size: 0.75rem;">Quản trị hệ thống</div>
                <a href="<%= cp %>/admin/dashboard"
                   class="list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 mb-1 d-flex align-items-center gap-2<%= uri.contains("/admin/dashboard") ? " active bg-primary text-white" : " text-dark" %>">
                    <span>📊</span><span>Bảng điều khiển</span>
                </a>
                <a href="<%= cp %>/admin/users"
                   class="list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 mb-1 d-flex align-items-center gap-2<%= uri.contains("/admin/users") ? " active bg-primary text-white" : " text-dark" %>">
                    <span>👥</span><span>Quản lý người dùng</span>
                </a>
                <div class="text-uppercase text-muted px-3 pt-3 pb-2 fw-bold" style="font-size: 0.75rem;">Truy cập nhanh</div>
                <a href="<%= cp %>/doctor/examination" class="list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 mb-1 text-secondary">
                    <span>🩺</span> Buồng khám BS
                </a>
                <a href="<%= cp %>/staff/reception" class="list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 mb-1 text-secondary">
                    <span>💁</span> Bàn tiếp đón
                </a>
            </c:when>

            <%-- MENU CHO BÁC SĨ (ROLE 2) --%>
            <c:when test="${sessionScope.user.roleId == 2}">
                <div class="text-uppercase text-muted px-3 pb-2 fw-bold" style="font-size: 0.75rem;">Nghiệp vụ Bác sĩ</div>
                <a href="<%= cp %>/doctor/examination"
                   class="list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 mb-1 d-flex align-items-center gap-2<%= uri.contains("/doctor/examination") ? " active bg-primary text-white" : " text-dark" %>">
                    <span>🩺</span><span>Buồng khám bệnh</span>
                </a>
                <a href="<%= cp %>/doctor/prescription"
                   class="list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 mb-1 d-flex align-items-center gap-2<%= uri.contains("/doctor/prescription") ? " active bg-primary text-white" : " text-dark" %>">
                    <span>💊</span><span>Kê đơn thuốc</span>
                </a>
            </c:when>

            <%-- MENU CHO LỄ TÂN & THU NGÂN (ROLE 4) --%>
            <c:when test="${sessionScope.user.roleId == 4}">
                <div class="text-uppercase text-muted px-3 pb-2 fw-bold" style="font-size: 0.75rem;">Bàn Tiếp đón</div>
                <a href="<%= cp %>/staff/reception"
                   class="list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 mb-1 d-flex align-items-center gap-2<%= uri.contains("/staff/reception") ? " active bg-primary text-white" : " text-dark" %>">
                    <span>💁</span><span>Tiếp đón &amp; Check-in</span>
                </a>
                <a href="<%= cp %>/staff/invoice"
                   class="list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 mb-1 d-flex align-items-center gap-2<%= uri.contains("/staff/invoice") ? " active bg-primary text-white" : " text-dark" %>">
                    <span>🧾</span><span>Xuất hóa đơn</span>
                </a>
            </c:when>

            <%-- MENU CHO BỆNH NHÂN (ROLE 5) HOẶC MẶC ĐỊNH --%>
            <c:otherwise>
                <div class="text-uppercase text-muted px-3 pb-2 fw-bold" style="font-size: 0.75rem;">Dịch vụ Bệnh nhân</div>
                <a href="<%= cp %>/customer/book"
                   class="list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 mb-1 d-flex align-items-center gap-2<%= uri.contains("/customer/book") ? " active bg-primary text-white" : " text-dark" %>">
                    <span>📅</span><span>Đặt lịch khám</span>
                </a>
                <a href="<%= cp %>/customer/profile"
                   class="list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 mb-1 d-flex align-items-center gap-2<%= uri.contains("/customer/profile") ? " active bg-primary text-white" : " text-dark" %>">
                    <span>📋</span><span>Hồ sơ &amp; Lịch sử</span>
                </a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
