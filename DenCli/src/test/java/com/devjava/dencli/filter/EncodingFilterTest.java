/**
 * File: EncodingFilterTest.java
 * Mục đích: Unit test kiểm thử chức năng thiết lập mã hóa UTF-8 của EncodingFilter bằng JUnit 5 và Mockito.
 */
package com.devjava.dencli.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class EncodingFilterTest {

    private EncodingFilter encodingFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    public void setUp() {
        encodingFilter = new EncodingFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    /**
     * Kiểm thử EncodingFilter thiết lập mã hóa UTF-8 cho cả request và response, sau đó chuyển tiếp chuỗi filter.
     */
    @Test
    public void testDoFilterSetsUtf8Encoding() throws IOException, ServletException {
        encodingFilter.doFilter(request, response, chain);

        // Xác minh request và response đều được setCharacterEncoding("UTF-8")
        verify(request).setCharacterEncoding("UTF-8");
        verify(response).setCharacterEncoding("UTF-8");

        // Xác minh chain.doFilter được gọi đúng 1 lần
        verify(chain, times(1)).doFilter(request, response);
    }
}
