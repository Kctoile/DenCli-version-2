/**
 * File: PrescriptionService.java
 * Mục đích: Triển khai các phương thức nghiệp vụ kê đơn thuốc và quản lý Transaction đa bảng thủ công (kê đơn & trừ tồn kho).
 */
package com.devjava.dencli.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.MedicineDAO;
import com.devjava.dencli.dao.PrescriptionDAO;

import com.devjava.dencli.model.Medicine;
import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.model.PrescriptionDetail;

public class PrescriptionService {

    private static final Logger LOGGER = Logger.getLogger(PrescriptionService.class.getName());

    private final PrescriptionDAO prescriptionDAO;
    private final MedicineDAO medicineDAO;

    // Constructor mặc định khởi tạo các DAO
    public PrescriptionService() {
        this.prescriptionDAO = new PrescriptionDAO();
        this.medicineDAO = new MedicineDAO();
    }

    // Constructor hỗ trợ tiêm phụ thuộc DAO phục vụ kiểm thử
    public PrescriptionService(PrescriptionDAO prescriptionDAO, MedicineDAO medicineDAO) {
        this.prescriptionDAO = prescriptionDAO;
        this.medicineDAO = medicineDAO;
    }

    /**
     * Phương thức thực hiện nghiệp vụ kê đơn thuốc và tự động trừ số lượng tồn
     * kho trong một Transaction duy nhất. Tầng Service chủ động mở Connection,
     * tắt autoCommit, truyền Connection cho DAO và commit/rollback trong
     * try-catch-finally.
     *
     * @param prescription Đối tượng đơn thuốc chứa danh sách các chi tiết thuốc
     * cần kê
     * @return true nếu kê đơn và trừ tồn kho thành công cho toàn bộ thuốc,
     * ngược lại false
     */
    public boolean createPrescriptionWithStockDeduction(Prescription prescription, int doctorId) {
        if (isPrescriptionInputInvalid(prescription, doctorId)) {
            return false;
        }

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            if (!prescriptionDAO.isResultAssignedToDoctor(prescription.getResultId(), doctorId, conn)) {
                conn.rollback();
                return false;
            }

            int prescriptionId = prescriptionDAO.insertPrescription(prescription, conn);
            if (prescriptionId <= 0) {
                throw new SQLException("Thao tác tạo đơn thuốc thất bại, không thể lấy mã ID sinh ra.");
            }

            processDetailsStockAndInsertion(prescriptionId, prescription.getDetails(), conn);

            conn.commit();
            return true;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            rollbackQuietly(conn);
            return false;

        } finally {
            closeQuietly(conn);
        }
    }

    private boolean isPrescriptionInputInvalid(Prescription prescription, int doctorId) {
        if (prescription == null || prescription.getResultId() == null || prescription.getResultId() <= 0 || doctorId <= 0) {
            return true;
        }
        List<PrescriptionDetail> details = prescription.getDetails();
        return details == null || details.isEmpty();
    }

    private void processDetailsStockAndInsertion(int prescriptionId, List<PrescriptionDetail> details, Connection conn) throws SQLException {
        for (PrescriptionDetail detail : details) {
            if (detail.getMedicineId() <= 0 || detail.getPrescribedQuantity() <= 0) {
                throw new SQLException("Thông tin thuốc hoặc số lượng kê đơn không hợp lệ.");
            }

            boolean stockDeducted = medicineDAO.deductStockQuantity(
                    detail.getMedicineId(),
                    detail.getPrescribedQuantity(),
                    conn
            );
            if (!stockDeducted) {
                throw new SQLException("Không đủ số lượng tồn kho cho thuốc có mã ID: " + detail.getMedicineId());
            }

            detail.setPrescriptionId(prescriptionId);
            boolean detailInserted = prescriptionDAO.insertPrescriptionDetail(detail, conn);
            if (!detailInserted) {
                throw new SQLException("Lưu chi tiết đơn thuốc thất bại cho thuốc ID: " + detail.getMedicineId());
            }
        }
    }

    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    private void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    /**
     * Phương thức lấy đơn thuốc gắn liền với kết quả khám bệnh.
     *
     * @param resultId Mã kết quả khám
     * @return Đối tượng Prescription
     */
    public Prescription getPrescriptionByResultId(int resultId) {
        if (resultId <= 0) {
            return null;
        }

        return prescriptionDAO.getPrescriptionByResultId(resultId);
    }

    /**
     * Phương thức lấy toàn bộ danh mục thuốc trong kho.
     *
     * @return Danh sách thuốc
     */
    public List<Medicine> getAllMedicines() {
        return medicineDAO.getAllMedicines();
    }

    /**
     * Phương thức thêm mới một loại thuốc vào danh mục kho.
     *
     * @param medicine Đối tượng thuốc
     * @return true nếu thêm thành công
     */
    public boolean addMedicine(Medicine medicine) {
        if (medicine == null || medicine.getMedicineName() == null || medicine.getMedicineName().trim().isEmpty()
                || medicine.getPrice() == null || medicine.getStockQuantity() < 0) {
            return false;
        }

        return medicineDAO.insertMedicine(medicine);
    }

    /**
     * Phương thức cập nhật thông tin thuốc trong kho.
     *
     * @param medicine Đối tượng thuốc
     * @return true nếu cập nhật thành công
     */
    public boolean updateMedicine(Medicine medicine) {
        if (medicine == null || medicine.getMedicineId() <= 0) {
            return false;
        }

        return medicineDAO.updateMedicine(medicine);
    }
}
