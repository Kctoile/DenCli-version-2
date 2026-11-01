package com.devjava.dencli.controller.customer;

import com.devjava.dencli.model.User;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CustomerMedicalRecordServletTest {

    private CustomerMedicalRecordServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new CustomerMedicalRecordServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher("/customer/medical_records.jsp")).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/DenCli");
    }

    @Test
    void testUnauthenticatedRedirectsToLogin() throws Exception {
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/DenCli/login.jsp");
        verify(dispatcher, never()).forward(request, response);
    }

    @Test
    void testAuthenticatedUserForwardsToMedicalRecordsJsp() throws Exception {
        User user = new User();
        user.setUserId(9999); // non-existent ID returns empty list safely from DAO
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(user);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("records"), any());
        verify(dispatcher).forward(request, response);
    }
}

