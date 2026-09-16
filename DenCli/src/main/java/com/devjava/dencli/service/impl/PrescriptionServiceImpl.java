/**
 * File: PrescriptionServiceImpl.java
 * Mục đích: Triển khai các phương thức nghiệp vụ kê đơn thuốc và quản lý Transaction đa bảng thủ công (kê đơn & trừ tồn kho).
 */
package com.devjava.dencli.service.impl;

import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.MedicineDAO;
import com.devjava.dencli.dao.PrescriptionDAO;
import com.devjava.dencli.dao.impl.MedicineDAOImpl;
import com.devjava.dencli.dao.impl.PrescriptionDAOImpl;
import com.devjava.dencli.model.Medicine;
import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.model.PrescriptionDetail;
import com.devjava.dencli.service.PrescriptionService;
import com.devjava.dencli.util.Constants;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionDAO prescriptionDAO;
    private final MedicineDAO medicineDAO;

    // Constructor mặc định khởi tạo các DAO
    public PrescriptionServiceImpl() {
        this.prescriptionDAO = new PrescriptionDAOImpl();
        this.medicineDAO = new MedicineDAOImpl();
    }

    // Constructor hỗ trợ tiêm phụ thuộc DAO phục vụ kiểm thử
    public PrescriptionServiceImpl(PrescriptionDAO prescriptionDAO, MedicineDAO medicineDAO) {
        this.prescriptionDAO = prescriptionDAO;
        this.medicineDAO = medicineDAO;
    }

    /**
     * Phương thức thực hiện nghiệp vụ kê đơn thuốc và tự động trừ số lượng tồn kho trong một Transaction duy nhất.
     * Tầng Service chủ động mở Connection, tắt autoCommit, truyền Connection cho DAO và commit/rollback trong try-catch-finally.
     * @param prescription Đối tượng đơn thuốc chứa danh sách các chi tiết thuốc cần kê
     * @return true nếu kê đơn và trừ tồn kho thành công cho toàn bộ thuốc, ngược lại false
     */
    @Override // Ghi đè phương thức createPrescriptionWithStockDeduction từ interface PrescriptionService
    public boolean createPrescriptionWithStockDeduction(Prescription prescription) {
        if (prescription == null || prescription.getResultId() == null || prescription.getResultId() <= 0) {
            return false;
        }

        List<PrescriptionDetail> details = prescription.getDetails();
        if (details == null || details.isEmpty()) {
            return false;
        }

        Connection conn = null;

        try {
            // Bước 1: Tầng Service chủ động mở một kết nối duy nhất từ DBConnection
            conn = DBConnection.getConnection();

            // Bước 2: Vô hiệu hóa chế độ tự động commit để bắt đầu một Transaction thủ công
            conn.setAutoCommit(false);

            // Bước 3: Tạo bản ghi đơn thuốc vào bảng prescriptions thông qua PrescriptionDAO với Connection dùng chung
            int prescriptionId = prescriptionDAO.insertPrescription(prescription, conn);

            if (prescriptionId <= 0) {
                throw new SQLException("Thao tác tạo đơn thuốc thất bại, không thể lấy mã ID sinh ra.");
            }

            // Bước 4: Duyệt qua từng chi tiết thuốc để trừ kho và lưu vào bảng prescription_details
            for (PrescriptionDetail detail : details) {
                if (detail.getMedicineId() <= 0 || detail.getPrescribedQuantity() <= 0) {
                    throw new SQLException("Thông tin thuốc hoặc số lượng kê đơn không hợp lệ.");
                }

                // Trừ số lượng tồn kho của thuốc trong bảng medicines (kiểm tra tồn kho đủ)
                boolean stockDeducted = medicineDAO.deductStockQuantity(
                    detail.getMedicineId(), 
                    detail.getPrescribedQuantity(), 
                    conn
                );

                if (!stockDeducted) {
                    // Nếu số lượng tồn kho không đủ để trừ, phát sinh ngoại lệ để kích hoạt Rollback
                    throw new SQLException("Không đủ số lượng tồn kho cho thuốc có mã ID: " + detail.getMedicineId());
                }

                // Gán mã đơn thuốc vừa tạo cho chi tiết
                detail.setPrescriptionId(prescriptionId);

                // Lưu bản ghi chi tiết vào bảng prescription_details bằng Connection dùng chung
                boolean detailInserted = prescriptionDAO.insertPrescriptionDetail(detail, conn);

                if (!detailInserted) {
                    throw new SQLException("Lưu chi tiết đơn thuốc thất bại cho thuốc ID: " + detail.getMedicineId());
                }
            }

            // Bước 5: Xác nhận commit toàn bộ các thay đổi trong Transaction khi mọi thao tác đều thành công
            conn.commit();

            return true;

        } catch (Exception e) {
            // Ghi nhận ngoại lệ hệ thống
            e.printStackTrace();

            // Bước 6: Khôi phục lại trạng thái ban đầu nếu có bất kỳ lỗi nào xảy ra
            if (conn != null) {
                try {
                    conn.rollback();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            return false;

        } finally {
            // Bước 7: Luôn giải phóng và đóng kết nối cơ sở dữ liệu trong khối finally
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Trả lại chế độ auto-commit mặc định
                    conn.close(); // Đóng kết nối

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Phương thức lấy đơn thuốc gắn liền với kết quả khám bệnh.
     * @param resultId Mã kết quả khám
     * @return Đối tượng Prescription
     */
    @Override // Ghi đè phương thức getPrescriptionByResultId từ interface PrescriptionService
    public Prescription getPrescriptionByResultId(int resultId) {
        if (resultId <= 0) {
            return null;
        }

        return prescriptionDAO.getPrescriptionByResultId(resultId);
    }

    /**
     * Phương thức lấy toàn bộ danh mục thuốc trong kho.
     * @return Danh sách thuốc
     */
    @Override // Ghi đè phương thức getAllMedicines từ interface PrescriptionService
    public List<Medicine> getAllMedicines() {
        return medicineDAO.getAllMedicines();
    }

    /**
     * Phương thức tìm kiếm thuốc theo từ khóa có phân trang.
     * @param keyword Từ khóa tìm kiếm
     * @param page Trang
     * @param pageSize Số lượng bản ghi trên một trang
     * @return Danh sách thuốc
     */
    @Override // Ghi đè phương thức searchMedicines từ interface PrescriptionService
    public List<Medicine> searchMedicines(String keyword, int page, int pageSize) {
        int validPage = (page <= 0) ? Constants.DEFAULT_PAGE_NUMBER : page;
        int validPageSize = (pageSize <= 0) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        int offset = (validPage - 1) * validPageSize;

        return medicineDAO.getMedicinesWithPagination(keyword, offset, validPageSize);
    }

    /**
     * Phương thức đếm tổng số loại thuốc thỏa từ khóa tìm kiếm.
     * @param keyword Từ khóa
     * @return Tổng số loại thuốc
     */
    @Override // Ghi đè phương thức countMedicines từ interface PrescriptionService
    public int countMedicines(String keyword) {
        return medicineDAO.countMedicines(keyword);
    }

    /**
     * Phương thức thêm mới một loại thuốc vào danh mục kho.
     * @param medicine Đối tượng thuốc
     * @return true nếu thêm thành công
     */
    @Override // Ghi đè phương thức addMedicine từ interface PrescriptionService
    public boolean addMedicine(Medicine medicine) {
        if (medicine == null || medicine.getMedicineName() == null || medicine.getMedicineName().trim().isEmpty() ||
            medicine.getPrice() == null || medicine.getStockQuantity() < 0) {
            return false;
        }

        return medicineDAO.insertMedicine(medicine);
    }

    /**
     * Phương thức cập nhật thông tin thuốc trong kho.
     * @param medicine Đối tượng thuốc
     * @return true nếu cập nhật thành công
     */
    @Override // Ghi đè phương thức updateMedicine từ interface PrescriptionService
    public boolean updateMedicine(Medicine medicine) {
        if (medicine == null || medicine.getMedicineId() <= 0) {
            return false;
        }

        return medicineDAO.updateMedicine(medicine);
    }
}
