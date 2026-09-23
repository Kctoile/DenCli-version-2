package com.devjava.dencli.controller.auth;

import com.devjava.dencli.dao.UserDAO;
import com.devjava.dencli.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ForgotPasswordServletTest {

    private ForgotPasswordServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private UserDAO mockUserDAO;
    private UserService mockUserService;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new ForgotPasswordServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        mockUserDAO = mock(UserDAO.class);
        mockUserService = mock(UserService.class);

        // Inject mocks via reflection
        Field daoField = ForgotPasswordServlet.class.getDeclaredField("userDAO");
        daoField.setAccessible(true);
        daoField.set(servlet, mockUserDAO);

        Field serviceField = ForgotPasswordServlet.class.getDeclaredField("userService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, mockUserService);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
        when(request.getSession(true)).thenReturn(session);
    }

    @Test
    void testSendOtpEmailNotFound() throws Exception {
        when(request.getContentType()).thenReturn("application/x-www-form-urlencoded");
        when(request.getParameter("action")).thenReturn("send-otp");
        when(request.getParameter("email")).thenReturn("notfound@example.com");
        when(mockUserDAO.checkEmailExists("notfound@example.com")).thenReturn(false);

        servlet.doPost(request, response);

        String result = responseWriter.toString();
        assertTrue(result.contains("\"success\":false"));
        assertTrue(result.contains("chưa được đăng ký"));
    }

    @Test
    void testSendOtpSuccess() throws Exception {
        when(request.getContentType()).thenReturn("application/x-www-form-urlencoded");
        when(request.getParameter("action")).thenReturn("send-otp");
        when(request.getParameter("email")).thenReturn("existing@example.com");
        when(mockUserDAO.checkEmailExists("existing@example.com")).thenReturn(true);

        servlet.doPost(request, response);

        verify(session).setAttribute(eq("fp_otp"), anyString());
        verify(session).setAttribute(eq("fp_email"), eq("existing@example.com"));

        String result = responseWriter.toString();
        assertTrue(result.contains("\"success\":true"));
    }

    @Test
    void testVerifyAndResetSuccess() throws Exception {
        when(request.getContentType()).thenReturn("application/x-www-form-urlencoded");
        when(request.getParameter("action")).thenReturn("verify-reset");
        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("otp")).thenReturn("123456");
        when(request.getParameter("new_password")).thenReturn("newSecret123");

        when(session.getAttribute("fp_otp")).thenReturn("123456");
        when(session.getAttribute("fp_email")).thenReturn("user@example.com");
        when(session.getAttribute("fp_time")).thenReturn(System.currentTimeMillis());

        when(mockUserService.resetPassword("user@example.com", "newSecret123")).thenReturn(true);

        servlet.doPost(request, response);

        verify(session).removeAttribute("fp_otp");
        verify(session).removeAttribute("fp_email");
        verify(session).removeAttribute("fp_time");

        String result = responseWriter.toString();
        assertTrue(result.contains("\"success\":true"));
        assertTrue(result.contains("thành công"));
    }

    @Test
    void testVerifyAndResetWrongOtp() throws Exception {
        when(request.getContentType()).thenReturn("application/x-www-form-urlencoded");
        when(request.getParameter("action")).thenReturn("verify-reset");
        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("otp")).thenReturn("999999");
        when(request.getParameter("new_password")).thenReturn("newSecret123");

        when(session.getAttribute("fp_otp")).thenReturn("123456");
        when(session.getAttribute("fp_email")).thenReturn("user@example.com");
        when(session.getAttribute("fp_time")).thenReturn(System.currentTimeMillis());

        servlet.doPost(request, response);

        String result = responseWriter.toString();
        assertTrue(result.contains("\"success\":false"));
        assertTrue(result.contains("không chính xác"));
    }
}
