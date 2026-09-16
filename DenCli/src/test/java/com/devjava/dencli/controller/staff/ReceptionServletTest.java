/**
 * File: ReceptionServletTest.java
 * Package: com.devjava.dencli.controller.staff
 * Mục đích: Unit test kiểm thử tiếp đón bệnh nhân, check-in và hủy lịch hẹn của ReceptionServlet.
 */
package com.devjava.dencli.controller.staff;

import com.devjava.dencli.model.User;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class ReceptionServletTest {

    private ReceptionServlet receptionServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private AppointmentService appointmentService;

    @BeforeEach
    public void setUp() {
        receptionServlet = new ReceptionServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        appointmentService = mock(AppointmentService.class);

        ServiceFactory.setAppointmentService(appointmentService);

        when(request.getContextPath()).thenReturn("/DenCli");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
    }

    /**
     * Kiểm thử GET /staff/reception lấy danh sách cuộc hẹn và forward tới reception.jsp.
     */
    @Test
    public void testDoGetFetchesAppointments() throws Exception {
        when(appointmentService.getAllAppointments(null, null, 1, 50)).thenReturn(new ArrayList<>());

        receptionServlet.doGet(request, response);

        verify(request).setAttribute(eq("appointments"), anyList());
        verify(request).getRequestDispatcher("/staff/reception.jsp");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Kiểm thử POST /staff/reception hành động checkin thành công.
     */
    @Test
    public void testDoPostCheckInSuccess() throws Exception {
        User staff = new User();
        staff.setUserId(4);
        staff.setRoleId(Constants.ROLE_STAFF_ID);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(staff);

        when(request.getParameter("action")).thenReturn("checkin");
        when(request.getParameter("appointment_id")).thenReturn("12");
        when(request.getParameter("room")).thenReturn("Phòng 02");

        when(appointmentService.checkInAppointment(12, "Phòng 02")).thenReturn(true);

        receptionServlet.doPost(request, response);

        verify(session).setAttribute(eq(Constants.SESSION_SUCCESS_MESSAGE), contains("Phòng 02"));
        verify(response).sendRedirect("/DenCli/staff/reception");
    }

    /**
     * Kiểm thử POST /staff/reception hành động cancel thành công.
     */
    @Test
    public void testDoPostCancelSuccess() throws Exception {
        User staff = new User();
        staff.setUserId(4);
        staff.setRoleId(Constants.ROLE_STAFF_ID);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(staff);

        when(request.getParameter("action")).thenReturn("cancel");
        when(request.getParameter("appointment_id")).thenReturn("12");

        when(appointmentService.cancelAppointment(12, 4, Constants.ROLE_STAFF_ID)).thenReturn(true);

        receptionServlet.doPost(request, response);

        verify(session).setAttribute(eq(Constants.SESSION_SUCCESS_MESSAGE), contains("thành công"));
        verify(response).sendRedirect("/DenCli/staff/reception");
    }
}
