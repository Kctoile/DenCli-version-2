/**
 * File: ExaminationServiceTest.java
 * Mục đích: Unit test kiểm thử Seam ghi nhận phiên khám bệnh lâm sàng nguyên tử (Candidate 03).
 */
package com.devjava.dencli.service;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.ExaminationResultDAO;
import com.devjava.dencli.dao.MedicineDAO;
import com.devjava.dencli.dao.PrescriptionDAO;
import com.devjava.dencli.model.ExaminationResult;
import com.devjava.dencli.model.PrescribedService;
import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.model.PrescriptionDetail;
import com.devjava.dencli.model.dto.ConsultationRequestDTO;
import com.devjava.dencli.model.dto.ConsultationResultDTO;
import com.devjava.dencli.util.Constants;

class ExaminationServiceTest {

    private ExaminationResultDAO examinationResultDAO;
    private AppointmentDAO appointmentDAO;
    private PrescriptionDAO prescriptionDAO;
    private MedicineDAO medicineDAO;
    private ExaminationService examinationService;

    @BeforeEach
    void setUp() {
        examinationResultDAO = mock(ExaminationResultDAO.class);
        appointmentDAO = mock(AppointmentDAO.class);
        prescriptionDAO = mock(PrescriptionDAO.class);
        medicineDAO = mock(MedicineDAO.class);
        examinationService = new ExaminationService(examinationResultDAO, appointmentDAO, prescriptionDAO, medicineDAO);
    }

    @Test
    void testRecordConsultationNullOrInvalidRequest() {
        // Null request
        ConsultationResultDTO res1 = examinationService.recordConsultation(null);
        assertFalse(res1.isSuccess());

        // Invalid appointmentId & doctorId
        ConsultationRequestDTO req2 = new ConsultationRequestDTO(0, 0, "");
        ConsultationResultDTO res2 = examinationService.recordConsultation(req2);
        assertFalse(res2.isSuccess());

        // Empty diagnosis
        ConsultationRequestDTO req3 = new ConsultationRequestDTO(10, 5, "   ");
        ConsultationResultDTO res3 = examinationService.recordConsultation(req3);
        assertFalse(res3.isSuccess());

        // Revisit date in the past
        ConsultationRequestDTO req4 = new ConsultationRequestDTO(10, 5, "Sâu răng nhẹ");
        req4.setRevisitDate(Date.valueOf(LocalDate.now().minusDays(2)));
        ConsultationResultDTO res4 = examinationService.recordConsultation(req4);
        assertFalse(res4.isSuccess());
    }

    @Test
    void testRecordConsultationInvalidMedicineDetails() {
        ConsultationRequestDTO req = new ConsultationRequestDTO(10, 5, "Viêm lợi");
        List<PrescriptionDetail> details = new ArrayList<>();
        PrescriptionDetail invalidDetail = new PrescriptionDetail();
        invalidDetail.setMedicineId(-1);
        invalidDetail.setPrescribedQuantity(0);
        details.add(invalidDetail);
        req.setPrescriptionDetails(details);

        ConsultationResultDTO res = examinationService.recordConsultation(req);
        assertFalse(res.isSuccess());
    }

    @Test
    void testRecordConsultationDoctorNotAssigned() throws Exception {
        Connection mockConn = mock(Connection.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(appointmentDAO.isAppointmentAssignedToDoctor(eq(10), eq(5), any(Connection.class))).thenReturn(false);

            ConsultationRequestDTO req = new ConsultationRequestDTO(10, 5, "Viêm nướu răng");
            ConsultationResultDTO res = examinationService.recordConsultation(req);

            assertFalse(res.isSuccess());
            verify(mockConn).rollback();
            verify(examinationResultDAO, never()).insertExaminationResult(any(), any());
        }
    }

    @Test
    void testRecordConsultationSuccessFullFlow() throws Exception {
        Connection mockConn = mock(Connection.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);

            when(appointmentDAO.isAppointmentAssignedToDoctor(eq(10), eq(5), any(Connection.class))).thenReturn(true);
            when(examinationResultDAO.insertExaminationResult(any(ExaminationResult.class), any(Connection.class))).thenReturn(101);
            when(appointmentDAO.updateRevisitInfo(eq(10), eq(5), any(Date.class), anyString(), any(Connection.class))).thenReturn(true);
            when(examinationResultDAO.insertPrescribedService(any(PrescribedService.class), any(Connection.class))).thenReturn(true);

            when(prescriptionDAO.insertPrescription(any(Prescription.class), any(Connection.class))).thenReturn(201);
            when(medicineDAO.deductStockQuantity(eq(1), eq(2), any(Connection.class))).thenReturn(true);
            when(prescriptionDAO.insertPrescriptionDetail(any(PrescriptionDetail.class), any(Connection.class))).thenReturn(true);

            when(appointmentDAO.completeAppointmentForDoctorIfCurrent(eq(10), eq(5), eq(Constants.APPOINTMENT_CHECKED_IN),
                    eq(Constants.APPOINTMENT_COMPLETED), any(Connection.class))).thenReturn(true);

            ConsultationRequestDTO req = new ConsultationRequestDTO(10, 5, "Nứt men răng R.21");
            req.setRevisitDate(Date.valueOf(LocalDate.now().plusDays(7)));
            req.setRevisitNote("Kiểm tra vết trám");

            List<PrescribedService> svcs = new ArrayList<>();
            PrescribedService ps = new PrescribedService();
            ps.setServiceId(3);
            svcs.add(ps);
            req.setAdditionalServices(svcs);

            List<PrescriptionDetail> details = new ArrayList<>();
            PrescriptionDetail detail = new PrescriptionDetail();
            detail.setMedicineId(1);
            detail.setPrescribedQuantity(2);
            detail.setDosage("2 viên/ngày");
            details.add(detail);
            req.setPrescriptionDetails(details);
            req.setPrescriptionInstructions("Uống sau bữa ăn");
            req.setCompleteAppointment(true);

            ConsultationResultDTO res = examinationService.recordConsultation(req);

            assertTrue(res.isSuccess());
            assertEquals(101, res.getResultId());
            assertEquals(201, res.getPrescriptionId());
            assertEquals(10, res.getAppointmentId());
            assertTrue(res.isAppointmentCompleted());

            verify(mockConn).commit();
        }
    }

    @Test
    void testRecordConsultationRollbackWhenStockInsufficient() throws Exception {
        Connection mockConn = mock(Connection.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);

            when(appointmentDAO.isAppointmentAssignedToDoctor(eq(10), eq(5), any(Connection.class))).thenReturn(true);
            when(examinationResultDAO.insertExaminationResult(any(ExaminationResult.class), any(Connection.class))).thenReturn(101);
            when(prescriptionDAO.insertPrescription(any(Prescription.class), any(Connection.class))).thenReturn(201);
            // Hết hàng tồn kho
            when(medicineDAO.deductStockQuantity(eq(1), eq(999), any(Connection.class))).thenReturn(false);

            ConsultationRequestDTO req = new ConsultationRequestDTO(10, 5, "Nhổ răng khôn");
            List<PrescriptionDetail> details = new ArrayList<>();
            PrescriptionDetail detail = new PrescriptionDetail();
            detail.setMedicineId(1);
            detail.setPrescribedQuantity(999);
            details.add(detail);
            req.setPrescriptionDetails(details);

            ConsultationResultDTO res = examinationService.recordConsultation(req);

            assertFalse(res.isSuccess());
            verify(mockConn).rollback();
        }
    }

    @Test
    void testGetExaminationByAppointmentId() {
        ExaminationResult er = new ExaminationResult();
        er.setResultId(101);
        when(examinationResultDAO.getExaminationResultByAppointmentId(10)).thenReturn(er);

        assertEquals(er, examinationService.getExaminationByAppointmentId(10));
        assertNull(examinationService.getExaminationByAppointmentId(-1));
    }

    @Test
    void testGetPrescribedServices() {
        when(examinationResultDAO.getPrescribedServicesByResultId(101)).thenReturn(List.of(new PrescribedService()));

        List<PrescribedService> list = examinationService.getPrescribedServices(101);
        assertNotNull(list);
        assertEquals(1, list.size());
        assertTrue(examinationService.getPrescribedServices(0).isEmpty());
    }

    @Test
    void testRecordExaminationBackwardCompatibility() throws Exception {
        Connection mockConn = mock(Connection.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(appointmentDAO.isAppointmentAssignedToDoctor(eq(10), eq(5), any(Connection.class))).thenReturn(true);
            when(examinationResultDAO.insertExaminationResult(any(ExaminationResult.class), any(Connection.class))).thenReturn(101);

            int resultId = examinationService.recordExamination(10, 5, "Khám tổng quát", null, null, null);

            assertEquals(101, resultId);
            verify(mockConn).commit();
        }
    }
}

