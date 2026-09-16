/**
 * File: AuthorizationFilter.java
 * Package: com.devjava.dencli.filter
 * Mục đích: Thực thi kiểm soát truy cập dựa trên vai trò người dùng (Role-Based Access Control - RBAC)
 *           đối với 4 phân hệ chức năng chính: Quản trị viên (/admin/*), Bác sĩ (/doctor/*),
 *           Nhân viên lễ tân (/staff/*), và Bệnh nhân (/customer/*).
 */
package com.devjava.dencli.filter;

import com.devjava.dencli.model.User;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo AuthorizationFilter
    }

    /**
     * Kiểm tra quyền hạn (Role) của người dùng đối với phân hệ URL mà họ đang cố gắng truy cập.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        if (path.isEmpty()) {
            path = "/";
        }

        // 1. Xác định xem đường dẫn có thuộc phân hệ yêu cầu phân quyền Role hay không
        boolean isRoleProtected = false;
        int requiredRoleId = 0;

        if (path.startsWith("/admin/") || path.equals("/admin") || path.equals("/admin.jsp")) {
            isRoleProtected = true;
            requiredRoleId = Constants.ROLE_ADMIN_ID;
        } else if (path.startsWith("/doctor/") || path.equals("/doctor") || path.equals("/doctor.jsp")) {
            isRoleProtected = true;
            requiredRoleId = Constants.ROLE_DOCTOR_ID;
        } else if (path.startsWith("/staff/") || path.equals("/staff") || path.equals("/staff.jsp")) {
            isRoleProtected = true;
            requiredRoleId = Constants.ROLE_STAFF_ID;
        } else if (path.startsWith("/customer/") || path.equals("/customer") || path.equals("/book.jsp")) {
            isRoleProtected = true;
            requiredRoleId = Constants.ROLE_CUSTOMER_ID;
        }

        // Nếu không thuộc phân hệ bảo vệ theo vai trò, cho phép đi tiếp
        if (!isRoleProtected) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Lấy thông tin người dùng từ phiên làm việc hiện tại
        HttpSession session = httpRequest.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        // Nếu người dùng chưa đăng nhập, chuyển hướng sang trang đăng nhập
        if (currentUser == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }

        int currentRoleId = currentUser.getRoleId();

        // 3. Kiểm tra tính hợp lệ của quyền hạn:
        // Quản trị viên (ADMIN_ID = 1) có quyền quản trị tối cao (có thể truy cập điều phối các phân hệ)
        // hoặc vai trò của người dùng phải trùng khớp với vai trò được yêu cầu
        boolean hasPermission = (currentRoleId == requiredRoleId)
                || (currentRoleId == Constants.ROLE_ADMIN_ID);

        if (hasPermission) {
            // Người dùng có đủ thẩm quyền, tiếp tục thực hiện yêu cầu
            chain.doFilter(request, response);
            return;
        }

        // 4. Xử lý khi VI PHẠM PHÂN QUYỀN (Unauthorized / 403 Forbidden)
        String acceptHeader = httpRequest.getHeader("Accept");
        String xRequestedWith = httpRequest.getHeader("X-Requested-With");
        boolean isApiOrJson = (acceptHeader != null && acceptHeader.contains(Constants.CONTENT_TYPE_JSON))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)
                || path.startsWith("/api/");

        if (isApiOrJson) {
            // Trả về JSON lỗi 403 Forbidden cho yêu cầu API/Fetch
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
            httpResponse.getWriter().print("{\"success\":false,\"message\":\"Bạn không có quyền truy cập chức năng này.\",\"error_code\":\"ERR_FORBIDDEN\"}");
            return;
        }

        // Đối với yêu cầu trang web thông thường: Thiết lập HTTP 403 và chuyển tiếp sang trang 403.jsp
        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpRequest.getRequestDispatcher("/403.jsp").forward(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {
        // Giải phóng tài nguyên của Filter
    }
}
