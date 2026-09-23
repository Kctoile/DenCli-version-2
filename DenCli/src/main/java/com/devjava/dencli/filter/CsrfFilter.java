package com.devjava.dencli.filter;

import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.CsrfUtil;
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
import java.util.Set;

/**
 * Filter bảo vệ ứng dụng trước các tấn công Cross-Site Request Forgery (CSRF).
 */
@WebFilter(filterName = "CsrfFilter", urlPatterns = {"/*"})
public class CsrfFilter implements Filter {

    // ponytail: miễn trừ các endpoint xác thực ban đầu để tránh gãy luồng đăng nhập/đăng ký
    private static final Set<String> EXEMPT_PATHS = Set.of(
            "/login", "/register", "/logout", "/forgot-password"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Luôn đảm bảo CSRF token tồn tại trong session và đưa vào request attribute
        HttpSession session = httpRequest.getSession(true);
        String token = CsrfUtil.getToken(session);
        httpRequest.setAttribute(CsrfUtil.CSRF_SESSION_ATTR, token);

        String method = httpRequest.getMethod();
        // Safe HTTP methods
        if ("GET".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method) || "OPTIONS".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        String contextPath = httpRequest.getContextPath();
        String path = httpRequest.getRequestURI().substring(contextPath.length());
        if (path.isEmpty()) {
            path = "/";
        }

        // Bỏ qua tài nguyên tĩnh hoặc endpoint được miễn trừ
        if (isExempt(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Kiểm tra hợp lệ CSRF token
        if (!CsrfUtil.isValid(httpRequest)) {
            String acceptHeader = httpRequest.getHeader("Accept");
            String xRequestedWith = httpRequest.getHeader("X-Requested-With");
            boolean isJson = (acceptHeader != null && acceptHeader.contains(Constants.CONTENT_TYPE_JSON))
                    || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)
                    || path.startsWith("/api/");

            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            if (isJson) {
                httpResponse.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
                httpResponse.getWriter().print("{\"success\":false,\"message\":\"Yêu cầu bị từ chối do thiếu hoặc sai CSRF Token.\",\"error_code\":\"ERR_CSRF_INVALID\"}");
            } else {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Yêu cầu bị từ chối: CSRF Token không hợp lệ.");
            }
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isExempt(String path) {
        if (EXEMPT_PATHS.contains(path)) {
            return true;
        }
        return path.startsWith("/assets/") || path.startsWith("/css/")
                || path.startsWith("/js/") || path.startsWith("/images/")
                || path.startsWith("/vendor/");
    }

    @Override
    public void destroy() {
    }
}
