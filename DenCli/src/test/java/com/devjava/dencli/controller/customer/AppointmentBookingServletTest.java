/**
 * File: AppointmentBookingServletTest.java
 * Package: com.devjava.dencli.controller.customer
 * Mục đích: Unit test kiểm thử chức năng đặt lịch khám bệnh nhân bằng JUnit 5 và Mockito.
 */
package com.devjava.dencli.controller.customer;

import com.devjava.dencli.model.User;
import com.devjava.dencli.model.dto.BookingRequestDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.service.UserService;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class AppointmentBookingServletTest {

    private AppointmentBookingServlet bookingServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private AppointmentService appointmentService;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        bookingServlet = new AppointmentBookingServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        appointmentService = mock(AppointmentService.class);
        userService = mock(UserService.class);

        ServiceFactory.setAppointmentService(appointmentService);
        ServiceFactory.setUserService(userService);

        when(request.getContextPath()).thenReturn("/DenCli");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
    }

    /**
     * Kiểm thử GET /customer/book lấy danh sách bác sĩ và dịch vụ chuyển tiếp sang book.jsp.
     */
    @Test
    public void testDoGetFetchesDoctorsAndServices() throws Exception {
        when(userService.getDoctors()).thenReturn(new ArrayList<>());

        bookingServlet.doGet(request, response);

        verify(request).setAttribute(eq("doctors"), anyList());
        verify(request).setAttribute(eq("services"), anyList());
        verify(request).getRequestDispatcher("/customer/book.jsp");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Kiểm thử POST /customer/book đặt lịch thành công khi không có xung đột giờ khám.
     */
    @Test
    public void testDoPostBookingSuccess() throws Exception {
        User patient = new User();
        patient.setUserId(5);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(patient);

        when(request.getParameter("doctor_id")).thenReturn("2");
        when(request.getParameter("appointment_date")).thenReturn("2026-10-15");
        when(request.getParameter("appointment_time")).thenReturn("09:00");
        when(request.getParameter("notes")).thenReturn("Khám răng định kỳ");
        when(request.getParameterValues("service_ids")).thenReturn(new String[]{"1", "2"});

        when(appointmentService.bookAppointment(any(BookingRequestDTO.class), eq(5))).thenReturn(101);

        bookingServlet.doPost(request, response);

        verify(session).setAttribute(eq(Constants.SESSION_SUCCESS_MESSAGE), contains("101"));
        verify(response).sendRedirect("/DenCli/customer/profile");
    }

    /**
     * Kiểm thử POST /customer/book phát sinh lỗi trùng lịch (Slot Conflict).
     */
    @Test
    public void testDoPostBookingSlotConflict() throws Exception {
        User patient = new User();
        patient.setUserId(5);
        when(session.getAttribute(Constants.SESSION_USER)).thenReturn(patient);

        when(request.getParameter("doctor_id")).thenReturn("2");
        when(request.getParameter("appointment_date")).thenReturn("2026-10-15");
        when(request.getParameter("appointment_time")).thenReturn("09:00");

        // Giả lập AppointmentService trả về -1 do trùng lịch
        when(appointmentService.bookAppointment(any(BookingRequestDTO.class), eq(5))).thenReturn(-1);

        bookingServlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), contains("trùng lịch"));
        verify(dispatcher).forward(request, response);
    }
}
