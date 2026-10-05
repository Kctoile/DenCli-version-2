/**
 * File: PrescriptionServiceTest.java
 * Mục đích: Unit test kiểm thử nghiệp vụ kê đơn thuốc, kiểm tra tính hợp lệ và quản lý danh mục kho thuốc.
 */
package com.devjava.dencli.service;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devjava.dencli.dao.MedicineDAO;
import com.devjava.dencli.dao.PrescriptionDAO;
import com.devjava.dencli.model.Medicine;
import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.service.impl.PrescriptionServiceImpl;

public class PrescriptionServiceTest {

    private PrescriptionDAO prescriptionDAO;
    private MedicineDAO medicineDAO;
    private PrescriptionService prescriptionService;

    /**
     * Khởi tạo mock objects trước mỗi kịch bản test.
     */
    @BeforeEach
    public void setUp() {
        prescriptionDAO = mock(PrescriptionDAO.class);
        medicineDAO = mock(MedicineDAO.class);
        prescriptionService = new PrescriptionServiceImpl(prescriptionDAO, medicineDAO);
    }

    /**
     * Kiểm thử từ chối kê đơn khi đối tượng prescription rỗng hoặc không có danh sách chi tiết thuốc.
     */
    @Test
    public void testCreatePrescriptionInvalidInput() {
        // Test null
        assertFalse(prescriptionService.createPrescriptionWithStockDeduction(null, 2));

        // Test không có kết quả khám (result_id null)
        Prescription p1 = new Prescription();
        assertFalse(prescriptionService.createPrescriptionWithStockDeduction(p1, 2));

        // Test danh sách chi tiết thuốc rỗng
        Prescription p2 = new Prescription(1, 10, "Huong dan");
        p2.setDetails(Collections.emptyList());
        assertFalse(prescriptionService.createPrescriptionWithStockDeduction(p2, 2));
    }

    /**
     * Kiểm thử thêm mới một loại thuốc vào kho thành công.
     */
    @Test
    public void testAddMedicineSuccess() {
        Medicine med = new Medicine();
        med.setMedicineName("Paracetamol 500mg");
        med.setPrice(new BigDecimal("15000"));
        med.setStockQuantity(100);

        when(medicineDAO.insertMedicine(med)).thenReturn(true);

        boolean added = prescriptionService.addMedicine(med);

        assertTrue(added);
    }

    /**
     * Kiểm thử từ chối thêm thuốc khi thiếu tên hoặc giá không hợp lệ.
     */
    @Test
    public void testAddMedicineInvalid() {
        Medicine med = new Medicine();
        med.setMedicineName(""); // Tên trống
        med.setPrice(null);

        boolean added = prescriptionService.addMedicine(med);

        assertFalse(added);
        verify(medicineDAO, never()).insertMedicine(any(Medicine.class));
    }
}
