/**
 * File: AppointmentServiceTest.java
 * Mục đích: Unit test kiểm thử các kịch bản nghiệp vụ lịch hẹn, xung đột thời gian và phân quyền hủy lịch.
 */
package com.devjava.dencli.service;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.ServiceDAO;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.dto.BookingRequestDTO;
import com.devjava.dencli.service.impl.AppointmentServiceImpl;
import com.devjava.dencli.util.Constants;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    private AppointmentDAO appointmentDAO;
    private ServiceDAO serviceDAO;
    private AppointmentService appointmentService;

    /**
     * Khởi tạo mock objects trước mỗi kịch bản test.
     */
    @BeforeEach
    public void setUp() {
        appointmentDAO = mock(AppointmentDAO.class);
        serviceDAO = mock(ServiceDAO.class);
        appointmentService = new AppointmentServiceImpl(appointmentDAO, serviceDAO);
    }

    /**
     * Kiểm thử đặt lịch bị từ chối do trùng slot khám với lịch hẹn khác của bác sĩ.
     */
    @Test
    public void testBookAppointmentConflictSlot() {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setDoctorId(2);
        request.setAppointmentDate("2026-09-20");
        request.setAppointmentTime("09:00");
        request.setServiceIds(List.of(1, 2));

        // Mock DAO trả về true (đã có lịch trùng)
        when(appointmentDAO.checkDuplicateSlot(eq(2), any(Date.class), any(Time.class))).thenReturn(true);

        int resultId = appointmentService.bookAppointment(request, 10);

        // Kết quả phải trả về -1 (từ chối đặt lịch)
        assertEquals(-1, resultId);
        verify(appointmentDAO, never()).insertAppointmentWithServices(any(Appointment.class), anyList());
    }

    /**
     * Kiểm thử đặt lịch thành công khi bác sĩ còn trống lịch.
     */
    @Test
    public void testBookAppointmentSuccess() {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setDoctorId(2);
        request.setAppointmentDate("2026-09-20");
        request.setAppointmentTime("09:00");
        request.setServiceIds(List.of(1));

        // Mock DAO trả về false (không trùng lịch) và sinh mã ID = 99
        when(appointmentDAO.checkDuplicateSlot(eq(2), any(Date.class), any(Time.class))).thenReturn(false);
        when(appointmentDAO.insertAppointmentWithServices(any(Appointment.class), anyList())).thenReturn(99);

        int resultId = appointmentService.bookAppointment(request, 10);

        assertEquals(99, resultId);
    }

    /**
     * Kiểm thử tiếp đón bệnh nhân và gán phòng khám thành công.
     */
    @Test
    public void testCheckInAppointmentSuccess() {
        when(appointmentDAO.updateAppointmentRoom(15, "Phòng 201")).thenReturn(true);
        when(appointmentDAO.updateAppointmentStatus(15, Constants.APPOINTMENT_CHECKED_IN)).thenReturn(true);

        boolean checkedIn = appointmentService.checkInAppointment(15, "Phòng 201");

        assertTrue(checkedIn);
    }

    /**
     * Kiểm thử bệnh nhân chỉ được phép hủy lịch hẹn do chính mình đặt.
     */
    @Test
    public void testCancelAppointmentOwnership() {
        Appointment app = new Appointment();
        app.setAppointmentId(100);
        app.setPatientId(5); // Thuộc về bệnh nhân ID 5
        app.setStatus(Constants.APPOINTMENT_PENDING);

        when(appointmentDAO.getAppointmentById(100)).thenReturn(app);
        when(appointmentDAO.updateAppointmentStatus(100, Constants.APPOINTMENT_CANCELLED)).thenReturn(true);

        // Bệnh nhân ID 5 hủy lịch của chính mình -> Thành công
        boolean cancelOwn = appointmentService.cancelAppointment(100, 5, Constants.ROLE_CUSTOMER_ID);
        assertTrue(cancelOwn);

        // Bệnh nhân ID 8 cố tình hủy lịch của bệnh nhân ID 5 -> Thất bại
        boolean cancelOther = appointmentService.cancelAppointment(100, 8, Constants.ROLE_CUSTOMER_ID);
        assertFalse(cancelOther);
    }
}
