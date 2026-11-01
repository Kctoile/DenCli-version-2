/**
 * File: ExaminationServiceImpl.java
 * Mục đích: Triển khai các phương thức nghiệp vụ khám bệnh, chẩn đoán và quản lý Transaction ghi nhận kết quả khám.
 */
package com.devjava.dencli.service.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.ExaminationResultDAO;
import com.devjava.dencli.dao.impl.AppointmentDAOImpl;
import com.devjava.dencli.dao.impl.ExaminationResultDAOImpl;
import com.devjava.dencli.model.ExaminationResult;
import com.devjava.dencli.model.PrescribedService;
import com.devjava.dencli.service.ExaminationService;
import com.devjava.dencli.util.Constants;

@SuppressWarnings({"java:S2143", "squid:S2143"})
public class ExaminationServiceImpl implements ExaminationService {

    private static final Logger LOGGER = Logger.getLogger(ExaminationServiceImpl.class.getName());

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
    public int recordExamination(int appointmentId, int doctorId, String diagnosis, List<PrescribedService> additionalServices,
                                 Date revisitDate, String revisitNote) {
        if (isRecordInputInvalid(appointmentId, doctorId, diagnosis, revisitDate)) {
            return -1;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction thủ công

            if (!appointmentDAO.isAppointmentAssignedToDoctor(appointmentId, doctorId, conn)) {
                conn.rollback();
                return -1;
            }

            int resultId = saveExaminationResult(appointmentId, diagnosis, conn);
            saveRevisit(appointmentId, doctorId, revisitDate, revisitNote, conn);
            saveAdditionalServices(resultId, additionalServices, conn);

            conn.commit();
            return resultId;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            rollbackQuietly(conn);
            return -1;

        } finally {
            closeQuietly(conn);
        }
    }

    private boolean isRecordInputInvalid(int appointmentId, int doctorId, String diagnosis, Date revisitDate) {
        if (appointmentId <= 0 || doctorId <= 0 || diagnosis == null || diagnosis.trim().isEmpty()) {
            return true;
        }
        return revisitDate != null && revisitDate.before(Date.valueOf(java.time.LocalDate.now(java.time.ZoneId.systemDefault())));
    }

    private int saveExaminationResult(int appointmentId, String diagnosis, Connection conn) throws SQLException {
        ExaminationResult er = new ExaminationResult();
        er.setAppointmentId(appointmentId);
        er.setResultDetails(diagnosis.trim());
        er.setExaminationDate(new Timestamp(System.currentTimeMillis()));

        int resultId = examinationResultDAO.insertExaminationResult(er, conn);
        if (resultId <= 0) {
            throw new SQLException("Không thể lưu bản ghi kết quả khám lâm sàng.");
        }
        return resultId;
    }

    private void saveRevisit(int appointmentId, int doctorId, Date revisitDate, String revisitNote, Connection conn) throws SQLException {
        if (revisitDate == null) {
            return;
        }
        String cleanNote = (revisitNote != null && !revisitNote.isBlank()) ? revisitNote.trim() : null;
        if (!appointmentDAO.updateRevisitInfo(appointmentId, doctorId, revisitDate, cleanNote, conn)) {
            throw new SQLException("Không thể lưu lịch tái khám.");
        }
    }

    private void saveAdditionalServices(int resultId, List<PrescribedService> additionalServices, Connection conn) throws SQLException {
        if (additionalServices == null || additionalServices.isEmpty()) {
            return;
        }
        for (PrescribedService ps : additionalServices) {
            ps.setResultId(resultId);
            if (ps.getStatus() == null) {
                ps.setStatus(Constants.PRESCRIBED_PENDING);
            }
            if (!examinationResultDAO.insertPrescribedService(ps, conn)) {
                throw new SQLException("Lưu dịch vụ chỉ định thất bại cho service_id: " + ps.getServiceId());
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
