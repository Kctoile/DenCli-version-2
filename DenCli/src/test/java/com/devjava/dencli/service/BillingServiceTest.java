/**
 * File: BillingServiceTest.java
 * Mục đích: Unit test kiểm thử tính toán chính xác tổng tiền dịch vụ và tiền thuốc cho hóa đơn viện phí.
 */
package com.devjava.dencli.service;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.ExaminationResultDAO;
import com.devjava.dencli.dao.PrescriptionDAO;
import com.devjava.dencli.dao.ServiceDAO;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.ExaminationResult;
import com.devjava.dencli.model.PrescribedService;
import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.model.PrescriptionDetail;
import com.devjava.dencli.model.Service;
import com.devjava.dencli.model.dto.InvoiceResponseDTO;
import com.devjava.dencli.service.impl.BillingServiceImpl;
import com.devjava.dencli.util.Constants;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BillingServiceTest {

    private AppointmentDAO appointmentDAO;
    private ServiceDAO serviceDAO;
    private ExaminationResultDAO examinationResultDAO;
    private PrescriptionDAO prescriptionDAO;
    private BillingService billingService;

    /**
     * Khởi tạo các mock objects và inject vào BillingServiceImpl.
     */
    @BeforeEach
    public void setUp() {
        appointmentDAO = mock(AppointmentDAO.class);
        serviceDAO = mock(ServiceDAO.class);
        examinationResultDAO = mock(ExaminationResultDAO.class);
        prescriptionDAO = mock(PrescriptionDAO.class);

        billingService = new BillingServiceImpl(appointmentDAO, serviceDAO, examinationResultDAO, prescriptionDAO);
    }

    /**
     * Kiểm thử tính tổng hóa đơn gồm dịch vụ đặt trước + dịch vụ chỉ định thêm + đơn thuốc.
     */
    @Test
    public void testCalculateInvoiceComprehensive() {
        int appointmentId = 12;

        Appointment app = new Appointment();
        app.setAppointmentId(appointmentId);
        app.setPatientName("Tran Thi Mai");
        app.setDoctorName("BS. Le Hoang");
        app.setAppointmentDate(Date.valueOf("2026-09-18"));
        app.setStatus(Constants.APPOINTMENT_COMPLETED);

        when(appointmentDAO.getAppointmentById(appointmentId)).thenReturn(app);

        // Dịch vụ ban đầu: Khám tổng quát (100,000)
        Service s1 = new Service(1, "Khám tổng quát", "Khám", new BigDecimal("100000"), 30);
        when(serviceDAO.getServicesByAppointmentId(appointmentId)).thenReturn(List.of(s1));

        // Kết quả khám: Result ID 50
        ExaminationResult result = new ExaminationResult(50, appointmentId, "Viêm nướu răng", null);
        when(examinationResultDAO.getExaminationResultByAppointmentId(appointmentId)).thenReturn(result);

        // Dịch vụ phát sinh khi khám: Cạo vôi răng (150,000)
        PrescribedService ps1 = new PrescribedService(50, 5, "Completed", "Cạo vôi");
        ps1.setServiceName("Cạo vôi răng");
        ps1.setPrice(new BigDecimal("150000"));
        when(examinationResultDAO.getPrescribedServicesByResultId(50)).thenReturn(List.of(ps1));

        // Đơn thuốc: Kháng sinh (đơn giá 25,000, số lượng 4 = 100,000)
        Prescription pres = new Prescription(30, 50, "Uống sau ăn");
        PrescriptionDetail pd = new PrescriptionDetail(30, 1, 4, 4, new BigDecimal("25000"));
        pres.setDetails(List.of(pd));
        when(prescriptionDAO.getPrescriptionByResultId(50)).thenReturn(pres);

        InvoiceResponseDTO invoice = billingService.calculateInvoice(appointmentId);

        assertNotNull(invoice);
        // Tổng tiền dịch vụ: 100,000 + 150,000 = 250,000
        assertEquals(new BigDecimal("250000"), invoice.getServicesTotal());
        // Tổng tiền thuốc: 25,000 * 4 = 100,000
        assertEquals(new BigDecimal("100000"), invoice.getMedicinesTotal());
        // Tổng cộng viện phí: 250,000 + 100,000 = 350,000
        assertEquals(new BigDecimal("350000"), invoice.getGrandTotal());
    }

    /**
     * Kiểm thử đánh dấu thanh toán hóa đơn thành công.
     */
    @Test
    public void testPayInvoiceSuccess() {
        when(appointmentDAO.updateAppointmentStatus(20, Constants.APPOINTMENT_COMPLETED)).thenReturn(true);

        boolean paid = billingService.payInvoice(20);

        assertTrue(paid);
    }
}
