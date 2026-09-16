/**
 * File: LoginServletTest.java
 * Package: com.devjava.dencli.controller.auth
 * Mục đích: Unit test kiểm thử các kịch bản đăng nhập (GET forward, POST form, POST json, redirect targetUrl) bằng JUnit 5 và Mockito.
 */
package com.devjava.dencli.controller.auth;

import com.devjava.dencli.model.User;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.service.UserService;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServletTest {

    private LoginServlet loginServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        loginServlet = new LoginServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        userService = mock(UserService.class);

        ServiceFactory.setUserService(userService);
        when(request.getContextPath()).thenReturn("/DenCli");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getSession(true)).thenReturn(session);
    }

    /**
     * Kiểm thử GET /login chuyển tiếp tới /login.jsp.
     */
    @Test
    public void testDoGetForwardsToLoginJsp() throws Exception {
        loginServlet.doGet(request, response);

        verify(request).getRequestDispatcher("/login.jsp");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Kiểm thử POST /login dạng Form thành công và điều hướng sang dashboard tương ứng.
     */
    @Test
    public void testDoPostLoginFormSuccess() throws Exception {
        when(request.getParameter("email_or_phone")).thenReturn("admin@dental.com");
        when(request.getParameter("password")).thenReturn("admin123");

        User mockAdmin = new User();
        mockAdmin.setUserId(1);
        mockAdmin.setFullName("Admin User");
        mockAdmin.setRoleId(Constants.ROLE_ADMIN_ID);
        when(userService.login("admin@dental.com", "admin123")).thenReturn(mockAdmin);

        loginServlet.doPost(request, response);

        verify(session).setAttribute(eq(Constants.SESSION_USER), eq(mockAdmin));
        verify(session).setAttribute(eq(Constants.SESSION_ROLE), eq(Constants.ROLE_ADMIN_NAME));
        verify(response).sendRedirect("/DenCli/admin/dashboard");
    }

    /**
     * Kiểm thử POST /login thành công và ưu tiên điều hướng về targetUrl đã lưu trong session.
     */
    @Test
    public void testDoPostLoginRedirectsToTargetUrl() throws Exception {
        when(request.getParameter("email_or_phone")).thenReturn("doctor1@dental.com");
        when(request.getParameter("password")).thenReturn("123");
        when(session.getAttribute("targetUrl")).thenReturn("/DenCli/doctor/prescription");

        User mockDoctor = new User();
        mockDoctor.setUserId(2);
        mockDoctor.setFullName("Dr. Dental");
        mockDoctor.setRoleId(Constants.ROLE_DOCTOR_ID);
        when(userService.login("doctor1@dental.com", "123")).thenReturn(mockDoctor);

        loginServlet.doPost(request, response);

        verify(session).removeAttribute("targetUrl");
        verify(response).sendRedirect("/DenCli/doctor/prescription");
    }

    /**
     * Kiểm thử POST /login dạng JSON thành công trả về dữ liệu người dùng.
     */
    @Test
    public void testDoPostLoginJsonSuccess() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        BufferedReader reader = new BufferedReader(new StringReader("{\"email_or_phone\":\"patient@gmail.com\",\"password\":\"123456\"}"));
        when(request.getReader()).thenReturn(reader);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        User mockPatient = new User();
        mockPatient.setUserId(5);
        mockPatient.setFullName("Patient One");
        mockPatient.setRoleId(Constants.ROLE_CUSTOMER_ID);
        when(userService.login("patient@gmail.com", "123456")).thenReturn(mockPatient);

        loginServlet.doPost(request, response);

        pw.flush();
        String jsonResult = sw.toString();
        assertTrue(jsonResult.contains("\"success\":true"));
        assertTrue(jsonResult.contains("Patient One"));
        assertTrue(jsonResult.contains("CUSTOMER"));
    }

    /**
     * Kiểm thử POST /login thất bại khi sai mật khẩu.
     */
    @Test
    public void testDoPostLoginFailureInvalidCredentials() throws Exception {
        when(request.getParameter("email_or_phone")).thenReturn("patient@gmail.com");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        when(userService.login("patient@gmail.com", "wrongpassword")).thenReturn(null);

        loginServlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
