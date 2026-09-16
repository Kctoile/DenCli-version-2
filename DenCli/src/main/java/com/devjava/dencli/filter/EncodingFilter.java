/**
 * File: EncodingFilter.java
 * Package: com.devjava.dencli.filter
 * Mục đích: Đảm bảo 100% Request và Response trên toàn hệ thống sử dụng bảng mã UTF-8,
 *           ngăn chặn triệt để tình trạng lỗi font chữ tiếng Việt có dấu.
 */
package com.devjava.dencli.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})
public class EncodingFilter implements Filter {

    private static final String ENCODING_UTF8 = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Phương thức khởi tạo cấu hình cho EncodingFilter (nếu cần)
    }

    /**
     * Chặn mọi request đi vào và gán mã hóa ký tự UTF-8 cho cả request và response.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Thiết lập bảng mã ký tự UTF-8 cho dữ liệu đầu vào của request
        request.setCharacterEncoding(ENCODING_UTF8);

        // Thiết lập bảng mã ký tự UTF-8 cho dữ liệu đầu ra của response
        response.setCharacterEncoding(ENCODING_UTF8);

        // Chuyển tiếp yêu cầu tới Filter hoặc Servlet tiếp theo trong chuỗi FilterChain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Giải phóng tài nguyên khi Filter bị dỡ bỏ khỏi Servlet Container
    }
}
