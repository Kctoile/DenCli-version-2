<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Sidebar Menu - điều hướng theo vai trò. --%>
<%
    String uri = request.getRequestURI();
    String cp  = request.getContextPath();
%>
<div class="den-sidebar col-12 col-md-3 col-lg-2 bg-white border-end py-3 px-2 shadow-sm min-vh-md-100">
    <div class="list-group list-group-flush gap-1">
        <c:choose>
            <%-- MENU CHO QUẢN TRọ VIÊN (ROLE 1) --%>
            <c:when test="${sessionScope.user.roleId == 1}">
                <div class="den-sidebar-section">Quản trị hệ thống</div>
                <a href="<%= cp %>/admin/dashboard"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/admin/dashboard") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>
                    <span>Bảng điều khiển</span>
                </a>
                <a href="<%= cp %>/admin/users"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/admin/users") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75"/></svg>
                    <span>Quản lý người dùng</span>
                </a>
                <a href="<%= cp %>/admin/settings"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/admin/settings") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.65 1.65 0 00.33 1.82l.06.06a2 2 0 010 2.83 2 2 0 01-2.83 0l-.06-.06a1.65 1.65 0 00-1.82-.33 1.65 1.65 0 00-1 1.51V21a2 2 0 01-4 0v-.09A1.65 1.65 0 009 19.4a1.65 1.65 0 00-1.82.33l-.06.06a2 2 0 01-2.83-2.83l.06-.06A1.65 1.65 0 004.68 15a1.65 1.65 0 00-1.51-1H3a2 2 0 010-4h.09A1.65 1.65 0 004.6 9a1.65 1.65 0 00-.33-1.82l-.06-.06a2 2 0 012.83-2.83l.06.06A1.65 1.65 0 009 4.68a1.65 1.65 0 001-1.51V3a2 2 0 014 0v.09a1.65 1.65 0 001 1.51 1.65 1.65 0 001.82-.33l.06-.06a2 2 0 012.83 2.83l-.06.06A1.65 1.65 0 0019.4 9a1.65 1.65 0 001.51 1H21a2 2 0 010 4h-.09a1.65 1.65 0 00-1.51 1z"/></svg>
                    <span>Cấu hình hệ thống</span>
                </a>
                <div class="den-sidebar-section mt-2">Truy cập nhanh</div>
                <a href="<%= cp %>/doctor/examination" class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M9 12h6m-3-3v6M5 3h14a2 2 0 012 2v14a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2z"/></svg>
                    <span>Buồng khám BS</span>
                </a>
                <a href="<%= cp %>/staff/reception" class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/></svg>
                    <span>Bàn tiếp đón</span>
                </a>
            </c:when>

            <%-- MENU CHO BÁC SĨ (ROLE 2) --%>
            <c:when test="${sessionScope.user.roleId == 2}">
                <div class="den-sidebar-section">Nghiệp vụ Bác sĩ</div>
                <a href="<%= cp %>/doctor/examination"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/doctor/examination") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M9 12h6m-3-3v6M5 3h14a2 2 0 012 2v14a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2z"/></svg>
                    <span>Buồng khám bệnh</span>
                </a>
                <a href="<%= cp %>/doctor/prescription"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/doctor/prescription") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M19 3H5a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2V5a2 2 0 00-2-2z"/><line x1="9" y1="9" x2="15" y2="9"/><line x1="9" y1="13" x2="15" y2="13"/><line x1="9" y1="17" x2="12" y2="17"/></svg>
                    <span>Kê đơn thuốc</span>
                </a>
            </c:when>

            <%-- MENU CHO LỄ TÂN &amp; THU NGÂN (ROLE 4) --%>
            <c:when test="${sessionScope.user.roleId == 4}">
                <div class="den-sidebar-section">Bàn Tiếp đón</div>
                <a href="<%= cp %>/staff/reception"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/staff/reception") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/></svg>
                    <span>Tiếp đón &amp; Check-in</span>
                </a>
                <a href="<%= cp %>/staff/invoice"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/staff/invoice") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="9" y1="13" x2="15" y2="13"/><line x1="9" y1="17" x2="15" y2="17"/></svg>
                    <span>Xuất hóa đơn</span>
                </a>
            </c:when>

            <%-- MENU CHO BỆNH NHÂN (ROLE 5) HOẶC MẶC ĐọNH --%>
            <c:otherwise>
                <div class="den-sidebar-section">Dịch vụ Bệnh nhân</div>
                <a href="<%= cp %>/customer/book"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/customer/book") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                    <span>Đặt lịch khám</span>
                </a>
                <a href="<%= cp %>/customer/appointments"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/customer/appointments") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><rect x="3" y="4" width="18" height="18" rx="2"/><polyline points="16 2 16 6"/><polyline points="8 2 8 6"/><line x1="3" y1="10" x2="21" y2="10"/><line x1="8" y1="14" x2="8" y2="14"/></svg>
                    <span>Lịch hẹn của tôi</span>
                </a>
                <a href="<%= cp %>/customer/revisitation"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/customer/revisitation") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><polyline points="1 4 1 10 7 10"/><path d="M3.51 15a9 9 0 102.13-9.36L1 10"/></svg>
                    <span>Lịch tái khám</span>
                </a>
                <a href="<%= cp %>/customer/medical-records"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/customer/medical-records") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M9 12h6m-3-3v6"/><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/></svg>
                    <span>Hồ sơ bệnh án</span>
                </a>
                <a href="<%= cp %>/customer/profile"
                   class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/customer/profile") ? " active" : "" %>">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><circle cx="12" cy="8" r="4"/><path d="M20 21a8 8 0 10-16 0"/></svg>
                    <span>Hồ sơ &amp; Lịch sử</span>
                </a>
            </c:otherwise>
        </c:choose>

        <div class="den-sidebar-section mt-2">Tài khoản</div>
        <a href="<%= cp %>/change-password"
           class="den-sidebar-link list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2<%= uri.contains("/change-password") ? " active" : "" %>">
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><rect x="3" y="11" width="18" height="11" rx="2"/><path d="M7 11V7a5 5 0 0110 0v4"/></svg>
            <span>Đổi mật khẩu</span>
        </a>
        <a href="<%= cp %>/logout"
           class="den-sidebar-link den-sidebar-logout list-group-item list-group-item-action border-0 rounded-3 py-2 px-3 d-flex align-items-center gap-2 fw-semibold">
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
            <span>Đăng xuất</span>
        </a>
    </div>
</div>
