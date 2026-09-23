package com.devjava.dencli.controller.customer;

import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.User;
import com.devjava.dencli.model.dto.InvoiceResponseDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.BillingService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerInvoiceServletTest {

    private CustomerInvoiceServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new CustomerInvoiceServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/DenCli");
    }

    @Test
    void testUnauthenticatedRedirectsToLogin() throws Exception {
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/DenCli/login.jsp");
    }

    @Test
    void testInvalidAppointmentIdReturnsBadRequest() throws Exception {
        User user = new User();
        user.setUserId(10);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(user);
        when(request.getParameter("appointment_id")).thenReturn("invalid");

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã lịch hẹn không hợp lệ.");
    }
}
