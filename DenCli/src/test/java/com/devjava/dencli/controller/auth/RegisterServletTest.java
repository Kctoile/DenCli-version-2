/**
 * File: RegisterServletTest.java
 * Package: com.devjava.dencli.controller.auth
 * Mục đích: Unit test kiểm thử các kịch bản đăng ký tài khoản (GET forward, POST hợp lệ, POST trùng lặp) bằng JUnit 5 và Mockito.
 */
package com.devjava.dencli.controller.auth;

import com.devjava.dencli.model.User;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class RegisterServletTest {

    private RegisterServlet registerServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        registerServlet = new RegisterServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        userService = mock(UserService.class);

        ServiceFactory.setUserService(userService);
        when(request.getContextPath()).thenReturn("/DenCli");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
    }

    /**
     * Kiểm thử GET /register chuyển tiếp tới /register.jsp.
     */
    @Test
    public void testDoGetForwardsToRegisterJsp() throws Exception {
        registerServlet.doGet(request, response);

        verify(request).getRequestDispatcher("/register.jsp");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Kiểm thử POST /register đăng ký thành công và chuyển hướng về /login.jsp.
     */
    @Test
    public void testDoPostRegisterSuccess() throws Exception {
        when(request.getParameter("full_name")).thenReturn("Nguyen Van Test");
        when(request.getParameter("phone")).thenReturn("0912345678");
        when(request.getParameter("email")).thenReturn("test@gmail.com");
        when(request.getParameter("password")).thenReturn("Pass123456");

        when(userService.registerCustomer(any(User.class), eq("Pass123456"))).thenReturn(true);

        registerServlet.doPost(request, response);

        verify(response).sendRedirect("/DenCli/login.jsp");
    }

    /**
     * Kiểm thử POST /register thất bại do thiếu trường bắt buộc.
     */
    @Test
    public void testDoPostRegisterMissingFields() throws Exception {
        when(request.getParameter("full_name")).thenReturn("");
        when(request.getParameter("phone")).thenReturn("");
        when(request.getParameter("password")).thenReturn("123");

        registerServlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
