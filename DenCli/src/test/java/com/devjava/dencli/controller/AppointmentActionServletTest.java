package com.devjava.dencli.controller;

import com.devjava.dencli.model.User;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppointmentActionServletTest {

    private AppointmentActionServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private AppointmentService appointmentService;
    private StringWriter responseBody;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = new AppointmentActionServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        appointmentService = mock(AppointmentService.class);
        responseBody = new StringWriter();

        ServiceFactory.setAppointmentService(appointmentService);
        when(request.getSession(false)).thenReturn(session);
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));
    }

    @Test
    public void testUnauthenticatedRequestReturnsUnauthorized() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(responseBody.toString().contains("Yêu cầu đăng nhập"));
    }

    @Test
    public void testUnsupportedActionReturnsBadRequest() throws Exception {
        givenUser(Constants.ROLE_STAFF_ID, 4);
        when(request.getParameter("action")).thenReturn("unknown");
        when(request.getParameter("appointment_id")).thenReturn("12");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertTrue(responseBody.toString().contains("Hành động không được hỗ trợ"));
    }

    @Test
    public void testStaffCheckInReturnsSuccess() throws Exception {
        givenUser(Constants.ROLE_STAFF_ID, 4);
        when(request.getParameter("action")).thenReturn("checkin");
        when(request.getParameter("appointment_id")).thenReturn("12");
        when(request.getParameter("room")).thenReturn("Room 02");
        when(appointmentService.checkInAppointment(12, "Room 02")).thenReturn(true);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(appointmentService).checkInAppointment(12, "Room 02");
        assertTrue(responseBody.toString().contains("\"success\":true"));
    }

    private void givenUser(int roleId, int userId) {
        User user = new User();
        user.setRoleId(roleId);
        user.setUserId(userId);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(user);
    }
}