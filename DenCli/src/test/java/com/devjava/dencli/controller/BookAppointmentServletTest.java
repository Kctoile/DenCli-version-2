package com.devjava.dencli.controller;

import com.devjava.dencli.model.User;
import com.devjava.dencli.model.dto.BookingRequestDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookAppointmentServletTest {

    private BookAppointmentServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private AppointmentService appointmentService;
    private StringWriter responseBody;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = new BookAppointmentServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        appointmentService = mock(AppointmentService.class);
        responseBody = new StringWriter();

        ServiceFactory.setAppointmentService(appointmentService);
        when(request.getSession(false)).thenReturn(session);
        when(request.getContentType()).thenReturn(null);
        when(request.getHeader("Accept")).thenReturn(null);
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));
    }

    @Test
    public void testUnauthenticatedRequestReturnsUnauthorized() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(responseBody.toString().contains("ERR_UNAUTHORIZED"));
    }

    @Test
    public void testInvalidDoctorIdReturnsBadRequest() throws Exception {
        givenCustomer();
        when(request.getParameter("doctor_id")).thenReturn("invalid");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertTrue(responseBody.toString().contains("ERR_INVALID_FORMAT"));
    }

    @Test
    public void testValidFormBookingReturnsCreated() throws Exception {
        givenCustomer();
        when(request.getParameter("doctor_id")).thenReturn("2");
        when(request.getParameter("appointment_date")).thenReturn("2026-10-15");
        when(request.getParameter("appointment_time")).thenReturn("09:00");
        when(appointmentService.bookAppointment(any(BookingRequestDTO.class), eq(5))).thenReturn(42);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(appointmentService).bookAppointment(any(BookingRequestDTO.class), eq(5));
        assertTrue(responseBody.toString().contains("\"appointment_id\":42"));
    }

    private void givenCustomer() {
        User customer = new User();
        customer.setUserId(5);
        customer.setRoleId(Constants.ROLE_CUSTOMER_ID);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(customer);
    }
}