/**
 * File: AuthenticationFilter.java
 * Package: com.devjava.dencli.filter
 * Mục đích: Chặn các yêu cầu chưa đăng nhập cố tình truy cập vào các đường dẫn bảo mật,
 *           chỉ cho phép truy cập nếu phiên làm việc (Session) đã được xác thực hợp lệ.
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

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Phương thức khởi tạo cấu hình cho AuthenticationFilter
    }

    /**
     * Kiểm tra trạng thái đăng nhập của người dùng qua Session trước khi cho phép đi tiếp.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Trích xuất đường dẫn tương đối sau contextPath
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        if (path.isEmpty()) {
            path = "/";
        }

        // 1. Kiểm tra nếu yêu cầu thuộc danh sách URL công khai (Bypass/Public URLs)
        if (isPublicResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Kiểm tra phiên làm việc (HttpSession) có lưu thông tin người dùng hợp lệ không
        HttpSession session = httpRequest.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        if (currentUser != null) {
            // Người dùng đã đăng nhập thành công, cho phép tiếp tục chuỗi FilterChain
            chain.doFilter(request, response);
            return;
        }

        // 3. Xử lý trường hợp CHƯA ĐĂNG NHẬP (Unauthenticated)
        // Kiểm tra xem yêu cầu gửi lên là API/JSON hay yêu cầu tải trang HTML
        String acceptHeader = httpRequest.getHeader("Accept");
        String xRequestedWith = httpRequest.getHeader("X-Requested-With");
        boolean isApiOrJson = (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)
                || path.startsWith("/api/");

        if (isApiOrJson) {
            // Nếu là gọi API ngầm qua Fetch/AJAX: Trả về mã lỗi HTTP 401 Unauthorized kèm JSON
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().print("{\"success\":false,\"message\":\"Yêu cầu đăng nhập trước khi thực hiện.\",\"error_code\":\"ERR_UNAUTHORIZED\"}");
            return;
        }

        // Nếu là yêu cầu duyệt trang thông thường:
        // Lưu URL mục tiêu vào Session để hỗ trợ tự động chuyển hướng lại sau khi đăng nhập thành công
        HttpSession newSession = httpRequest.getSession(true);
        String targetUrl = httpRequest.getRequestURI();
        if (httpRequest.getQueryString() != null) {
            targetUrl += "?" + httpRequest.getQueryString();
        }
        newSession.setAttribute("targetUrl", targetUrl);
        newSession.setAttribute(Constants.SESSION_ERROR_MESSAGE, "Vui lòng đăng nhập để tiếp tục truy cập.");

        // Chuyển hướng người dùng về trang đăng nhập
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
    }

    /**
     * Phương thức kiểm tra tài nguyên công khai được phép truy cập tự do mà không cần đăng nhập.
     * @param path Đường dẫn tương đối của yêu cầu
     * @return true nếu là tài nguyên công khai, false nếu là đường dẫn cần bảo vệ
     */
    private boolean isPublicResource(String path) {
        // Trang chủ và các trang công khai cơ bản
        if (path.equals("/") || path.equals("/index.jsp") || path.equals("/index.html")
                || path.equals("/403.jsp") || path.equals("/error.jsp") || path.equals("/favicon.ico")) {
            return true;
        }

        // Các đường dẫn phục vụ đăng nhập, đăng ký và đăng xuất
        if (path.equals("/login") || path.equals("/login.jsp")
                || path.equals("/register") || path.equals("/register.jsp")
                || path.equals("/logout") || path.equals("/logout.jsp")) {
            return true;
        }

        // Các tài nguyên tĩnh (CSS, JS, Hình ảnh, Fonts, Thư viện bên ngoài) và phân hệ xác thực công khai
        if (path.startsWith("/assets/") || path.startsWith("/css/")
                || path.startsWith("/js/") || path.startsWith("/images/")
                || path.startsWith("/vendor/") || path.startsWith("/auth/")) {
            return true;
        }

        return false;
    }

    @Override
    public void destroy() {
        // Giải phóng tài nguyên khi Filter bị dỡ bỏ
    }
}
