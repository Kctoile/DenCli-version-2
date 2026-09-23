package com.devjava.dencli.controller.auth;

import com.devjava.dencli.model.User;
import com.devjava.dencli.service.UserService;
import com.devjava.dencli.util.Constants;
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

public class ChangePasswordServletTest {

    private ChangePasswordServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private UserService mockUserService;
    private StringWriter responseWriter;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new ChangePasswordServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        mockUserService = mock(UserService.class);

        Field serviceField = ChangePasswordServlet.class.getDeclaredField("userService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, mockUserService);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
        when(request.getSession(false)).thenReturn(session);

        testUser = new User();
        testUser.setUserId(5);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(testUser);
    }

    @Test
    void testChangePasswordMismatch() throws Exception {
        when(request.getContentType()).thenReturn("application/x-www-form-urlencoded");
        when(request.getParameter("old_password")).thenReturn("oldpass123");
        when(request.getParameter("new_password")).thenReturn("newpass123");
        when(request.getParameter("confirm_password")).thenReturn("differentpass");

        servlet.doPost(request, response);

        String result = responseWriter.toString();
        assertTrue(result.contains("\"success\":false"));
        assertTrue(result.contains("không khớp"));
    }

    @Test
    void testChangePasswordSuccess() throws Exception {
        when(request.getContentType()).thenReturn("application/x-www-form-urlencoded");
        when(request.getParameter("old_password")).thenReturn("oldpass123");
        when(request.getParameter("new_password")).thenReturn("newpass123");
        when(request.getParameter("confirm_password")).thenReturn("newpass123");

        when(mockUserService.changePassword(5, "oldpass123", "newpass123")).thenReturn(true);

        servlet.doPost(request, response);

        String result = responseWriter.toString();
        assertTrue(result.contains("\"success\":true"));
        assertTrue(result.contains("thành công"));
    }
}
