/**
 * File: AuthenticationFilterTest.java
 * Mục đích: Unit test kiểm thử chức năng xác thực người dùng của AuthenticationFilter bằng JUnit 5 và Mockito.
 */
package com.devjava.dencli.filter;

import com.devjava.dencli.model.User;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationFilterTest {

    private AuthenticationFilter authenticationFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        authenticationFilter = new AuthenticationFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        session = mock(HttpSession.class);

        when(request.getContextPath()).thenReturn("/DenCli");
    }

    /**
     * Kiểm thử truy cập các tài nguyên công khai (public/static/login/register) được bypass mà không cần đăng nhập.
     */
    @Test
    public void testPublicResourceBypassed() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/login.jsp");

        authenticationFilter.doFilter(request, response, chain);

        // Đảm bảo chain.doFilter được gọi trực tiếp, không redirect
        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    /**
     * Kiểm thử truy cập tài nguyên tĩnh (/css/style.css) được bypass trực tiếp.
     */
    @Test
    public void testStaticCssBypassed() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/css/style.css");

        authenticationFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    /**
     * Kiểm thử người dùng đã đăng nhập (có User trong session) được phép truy cập trang bảo mật.
     */
    @Test
    public void testAuthenticatedUserAllowed() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/admin.jsp");
        when(request.getSession(false)).thenReturn(session);

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setFullName("Admin User");
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(mockUser);

        authenticationFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    /**
     * Kiểm thử người dùng chưa đăng nhập cố tình truy cập trang bảo mật sẽ bị redirect về login.jsp kèm lưu targetUrl.
     */
    @Test
    public void testUnauthenticatedPageRedirectsToLogin() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/admin.jsp");
        when(request.getQueryString()).thenReturn("tab=users");
        when(request.getSession(false)).thenReturn(null);
        when(request.getSession(true)).thenReturn(session);

        authenticationFilter.doFilter(request, response, chain);

        // Kiểm tra lưu URL mục tiêu và thông báo lỗi vào session
        verify(session).setAttribute("targetUrl", "/DenCli/admin.jsp?tab=users");
        verify(session).setAttribute(eq(Constants.SESSION_ERROR_MESSAGE), anyString());

        // Kiểm tra chuyển hướng về login.jsp
        verify(response).sendRedirect("/DenCli/login.jsp");
        verify(chain, never()).doFilter(request, response);
    }

    /**
     * Kiểm thử người dùng chưa đăng nhập gọi API JSON sẽ nhận mã HTTP 401 Unauthorized thay vì redirect.
     */
    @Test
    public void testUnauthenticatedApiReturns401() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/api/appointments/action");
        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getSession(false)).thenReturn(null);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        authenticationFilter.doFilter(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(response, never()).sendRedirect(anyString());
        verify(chain, never()).doFilter(request, response);

        assertTrue(sw.toString().contains("ERR_UNAUTHORIZED"));
    }
}
