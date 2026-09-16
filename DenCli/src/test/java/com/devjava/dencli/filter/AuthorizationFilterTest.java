/**
 * File: AuthorizationFilterTest.java
 * Mục đích: Unit test kiểm thử phân quyền theo vai trò (RBAC) của AuthorizationFilter bằng JUnit 5 và Mockito.
 */
package com.devjava.dencli.filter;

import com.devjava.dencli.model.User;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
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

public class AuthorizationFilterTest {

    private AuthorizationFilter authorizationFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        authorizationFilter = new AuthorizationFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/DenCli");
        when(request.getRequestDispatcher("/403.jsp")).thenReturn(dispatcher);
    }

    /**
     * Kiểm thử Quản trị viên (ADMIN - 1) truy cập phân hệ /admin.jsp thành công.
     */
    @Test
    public void testAdminAccessAdminPathSuccess() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/admin.jsp");
        when(request.getSession(false)).thenReturn(session);

        User admin = new User();
        admin.setUserId(1);
        admin.setRoleId(Constants.ROLE_ADMIN_ID);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(admin);

        authorizationFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Kiểm thử Bác sĩ (DOCTOR - 2) cố tình truy cập phân hệ Quản trị /admin.jsp bị chặn mã 403 và forward sang 403.jsp.
     */
    @Test
    public void testDoctorAccessAdminPathForbidden() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/admin.jsp");
        when(request.getSession(false)).thenReturn(session);

        User doctor = new User();
        doctor.setUserId(2);
        doctor.setRoleId(Constants.ROLE_DOCTOR_ID);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(doctor);

        authorizationFilter.doFilter(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(request).getRequestDispatcher("/403.jsp");
        verify(dispatcher).forward(request, response);
        verify(chain, never()).doFilter(request, response);
    }

    /**
     * Kiểm thử Bác sĩ (DOCTOR - 2) truy cập buồng khám /doctor.jsp thành công.
     */
    @Test
    public void testDoctorAccessDoctorPathSuccess() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/doctor.jsp");
        when(request.getSession(false)).thenReturn(session);

        User doctor = new User();
        doctor.setUserId(2);
        doctor.setRoleId(Constants.ROLE_DOCTOR_ID);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(doctor);

        authorizationFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Kiểm thử Bệnh nhân (CUSTOMER - 5) cố tình truy cập buồng khám Bác sĩ /doctor.jsp bị chặn 403.
     */
    @Test
    public void testCustomerAccessDoctorPathForbidden() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/doctor.jsp");
        when(request.getSession(false)).thenReturn(session);

        User customer = new User();
        customer.setUserId(5);
        customer.setRoleId(Constants.ROLE_CUSTOMER_ID);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(customer);

        authorizationFilter.doFilter(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(dispatcher).forward(request, response);
        verify(chain, never()).doFilter(request, response);
    }

    /**
     * Kiểm thử Bệnh nhân (CUSTOMER - 5) truy cập trang đặt lịch /book.jsp thành công.
     */
    @Test
    public void testCustomerAccessCustomerPathSuccess() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/book.jsp");
        when(request.getSession(false)).thenReturn(session);

        User customer = new User();
        customer.setUserId(5);
        customer.setRoleId(Constants.ROLE_CUSTOMER_ID);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(customer);

        authorizationFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Kiểm thử Lễ tân (STAFF - 4) truy cập bàn tiếp đón /staff.jsp thành công.
     */
    @Test
    public void testStaffAccessStaffPathSuccess() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/staff.jsp");
        when(request.getSession(false)).thenReturn(session);

        User staff = new User();
        staff.setUserId(4);
        staff.setRoleId(Constants.ROLE_STAFF_ID);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(staff);

        authorizationFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Kiểm thử truy cập phân hệ bảo vệ khi chưa đăng nhập sẽ chuyển hướng sang login.jsp.
     */
    @Test
    public void testUnauthenticatedAccessRedirectsToLogin() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/admin.jsp");
        when(request.getSession(false)).thenReturn(null);

        authorizationFilter.doFilter(request, response, chain);

        verify(response).sendRedirect("/DenCli/login.jsp");
        verify(chain, never()).doFilter(request, response);
    }

    /**
     * Kiểm thử đường dẫn công khai thông thường (/index.jsp) không bị chặn bởi AuthorizationFilter.
     */
    @Test
    public void testNonRoleProtectedPathAllowed() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/index.jsp");

        authorizationFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Kiểm thử yêu cầu API không đủ quyền sẽ trả về JSON HTTP 403 Forbidden thay vì forward 403.jsp.
     */
    @Test
    public void testApiForbiddenReturns403Json() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/DenCli/admin/users");
        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getSession(false)).thenReturn(session);

        User doctor = new User();
        doctor.setUserId(2);
        doctor.setRoleId(Constants.ROLE_DOCTOR_ID);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(doctor);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        authorizationFilter.doFilter(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(chain, never()).doFilter(request, response);
        assertTrue(sw.toString().contains("ERR_FORBIDDEN"));
    }
}
