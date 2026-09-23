package com.devjava.dencli.service;

import com.devjava.dencli.service.impl.AppointmentServiceImpl;
import com.devjava.dencli.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ISTQB State Transition Testing - Kiểm thử Máy trạng thái Cuộc hẹn Nha khoa")
class AppointmentStateValidationTest {

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentServiceImpl();
    }

    @Test
    @DisplayName("Test các bước chuyển trạng thái hợp lệ trong vòng đời cuộc hẹn")
    void testValidTransitions() {
        // Pending -> Confirmed
        assertTrue(appointmentService.isValidStatusTransition("Pending", "Confirmed"));
        // Pending -> Cancelled
        assertTrue(appointmentService.isValidStatusTransition("Pending", "Cancelled"));
        // Confirmed -> Checked In
        assertTrue(appointmentService.isValidStatusTransition("Confirmed", "Checked In"));
        // Confirmed -> Cancelled
        assertTrue(appointmentService.isValidStatusTransition("Confirmed", "Cancelled"));
        // Checked In -> Completed
        assertTrue(appointmentService.isValidStatusTransition("Checked In", "Completed"));
        // Checked In -> Cancelled
        assertTrue(appointmentService.isValidStatusTransition("Checked In", "Cancelled"));
    }

    @Test
    @DisplayName("Test các bước chuyển trạng thái bất hợp lệ (State Machine violations)")
    void testInvalidTransitions() {
        // Không thể nhảy cóc từ Pending sang Completed hoặc Checked In
        assertFalse(appointmentService.isValidStatusTransition("Pending", "Completed"));
        assertFalse(appointmentService.isValidStatusTransition("Pending", "Checked In"));

        // Không thể quay lui trạng thái
        assertFalse(appointmentService.isValidStatusTransition("Confirmed", "Pending"));
        assertFalse(appointmentService.isValidStatusTransition("Checked In", "Pending"));
        assertFalse(appointmentService.isValidStatusTransition("Checked In", "Confirmed"));

        // Trạng thái kết thúc (Completed / Cancelled) không được phép chuyển sang bất kỳ trạng thái nào
        assertFalse(appointmentService.isValidStatusTransition("Completed", "Pending"));
        assertFalse(appointmentService.isValidStatusTransition("Completed", "Confirmed"));
        assertFalse(appointmentService.isValidStatusTransition("Completed", "Cancelled"));

        assertFalse(appointmentService.isValidStatusTransition("Cancelled", "Pending"));
        assertFalse(appointmentService.isValidStatusTransition("Cancelled", "Confirmed"));
        assertFalse(appointmentService.isValidStatusTransition("Cancelled", "Completed"));
    }

    @Test
    @DisplayName("Test kiểm tra biên với giá trị null")
    void testNullStatusHandling() {
        assertFalse(appointmentService.isValidStatusTransition(null, Constants.APPOINTMENT_CONFIRMED));
        assertFalse(appointmentService.isValidStatusTransition(Constants.APPOINTMENT_PENDING, null));
        assertFalse(appointmentService.isValidStatusTransition(null, null));
    }
}
