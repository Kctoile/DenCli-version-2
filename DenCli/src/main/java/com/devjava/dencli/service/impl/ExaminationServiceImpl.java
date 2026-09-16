/**
 * File: ExaminationServiceImpl.java
 * Mục đích: Triển khai các phương thức nghiệp vụ khám bệnh, chẩn đoán và quản lý Transaction ghi nhận kết quả khám.
 */
package com.devjava.dencli.service.impl;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.ExaminationResultDAO;
import com.devjava.dencli.dao.impl.AppointmentDAOImpl;
import com.devjava.dencli.dao.impl.ExaminationResultDAOImpl;
import com.devjava.dencli.model.ExaminationResult;
import com.devjava.dencli.model.PrescribedService;
import com.devjava.dencli.service.ExaminationService;
import com.devjava.dencli.util.Constants;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ExaminationServiceImpl implements ExaminationService {

    private final ExaminationResultDAO examinationResultDAO;
    private final AppointmentDAO appointmentDAO;

    // Constructor mặc định khởi tạo các đối tượng DAO
    public ExaminationServiceImpl() {
        this.examinationResultDAO = new ExaminationResultDAOImpl();
        this.appointmentDAO = new AppointmentDAOImpl();
    }

    // Constructor hỗ trợ tiêm phụ thuộc cho Unit Test
    public ExaminationServiceImpl(ExaminationResultDAO examinationResultDAO, AppointmentDAO appointmentDAO) {
        this.examinationResultDAO = examinationResultDAO;
        this.appointmentDAO = appointmentDAO;
    }

    /**
     * Phương thức ghi nhận chẩn đoán kết quả khám và các dịch vụ phát sinh trong một Transaction cơ sở dữ liệu.
     * @param appointmentId Mã lịch hẹn
     * @param diagnosis Nội dung chẩn đoán
     * @param additionalServices Danh sách dịch vụ chỉ định thêm
     * @return Mã result_id nếu thành công, -1 nếu thất bại
     */
    @Override // Ghi đè phương thức recordExamination từ interface ExaminationService
    public int recordExamination(int appointmentId, String diagnosis, List<PrescribedService> additionalServices) {
        if (appointmentId <= 0 || diagnosis == null || diagnosis.trim().isEmpty()) {
            return -1;
        }

        Connection conn = null;

        try {
            // Mở kết nối duy nhất từ tầng Service
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction thủ công

            // Chuẩn bị thực thể ExaminationResult
            ExaminationResult er = new ExaminationResult();
            er.setAppointmentId(appointmentId);
            er.setResultDetails(diagnosis.trim());
            er.setExaminationDate(new Timestamp(System.currentTimeMillis()));

            // Thêm kết quả khám vào bảng examination_results qua Connection dùng chung
            int resultId = examinationResultDAO.insertExaminationResult(er, conn);

            if (resultId <= 0) {
                throw new SQLException("Không thể lưu bản ghi kết quả khám lâm sàng.");
            }

            // Nếu có các dịch vụ chỉ định thêm, tiến hành lưu vào bảng prescribed_services
            if (additionalServices != null && !additionalServices.isEmpty()) {
                for (PrescribedService ps : additionalServices) {
                    ps.setResultId(resultId);

                    if (ps.getStatus() == null) {
                        ps.setStatus(Constants.PRESCRIBED_PENDING);
                    }

                    boolean inserted = examinationResultDAO.insertPrescribedService(ps, conn);
                    if (!inserted) {
                        throw new SQLException("Lưu dịch vụ chỉ định thất bại cho service_id: " + ps.getServiceId());
                    }
                }
            }

            // Xác nhận hoàn tất Transaction
            conn.commit();

            return resultId;

        } catch (Exception e) {
            e.printStackTrace();

            // Rollback nếu có bất kỳ lỗi nào xảy ra
            if (conn != null) {
                try {
                    conn.rollback();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            return -1;

        } finally {
            // Luôn giải phóng tài nguyên trong khối finally
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Phương thức lấy thông tin kết quả khám của cuộc hẹn.
     * @param appointmentId Mã cuộc hẹn
     * @return Đối tượng ExaminationResult
     */
    @Override // Ghi đè phương thức getExaminationByAppointmentId từ interface ExaminationService
    public ExaminationResult getExaminationByAppointmentId(int appointmentId) {
        if (appointmentId <= 0) {
            return null;
        }

        return examinationResultDAO.getExaminationResultByAppointmentId(appointmentId);
    }

    /**
     * Phương thức lấy danh sách các dịch vụ chỉ định theo mã kết quả khám.
     * @param resultId Mã kết quả khám
     * @return Danh sách dịch vụ chỉ định
     */
    @Override // Ghi đè phương thức getPrescribedServices từ interface ExaminationService
    public List<PrescribedService> getPrescribedServices(int resultId) {
        if (resultId <= 0) {
            return List.of();
        }

        return examinationResultDAO.getPrescribedServicesByResultId(resultId);
    }
}
