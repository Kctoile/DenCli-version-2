/**
 * File: ExaminationService.java
 * Mục đích: Triển khai các phương thức nghiệp vụ khám bệnh, chẩn đoán và quản lý Transaction nguyên tử (Consultation Seam).
 */
package com.devjava.dencli.service;

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
import com.devjava.dencli.dao.MedicineDAO;
import com.devjava.dencli.dao.PrescriptionDAO;
import com.devjava.dencli.model.ExaminationResult;
import com.devjava.dencli.model.PrescribedService;
import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.model.PrescriptionDetail;
import com.devjava.dencli.model.dto.ConsultationRequestDTO;
import com.devjava.dencli.model.dto.ConsultationResultDTO;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.websocket.AppointmentWebSocket;

@SuppressWarnings({"java:S2143", "squid:S2143"})
public class ExaminationService {

    private static final Logger LOGGER = Logger.getLogger(ExaminationService.class.getName());

    private final ExaminationResultDAO examinationResultDAO;
    private final AppointmentDAO appointmentDAO;
    private final PrescriptionDAO prescriptionDAO;
    private final MedicineDAO medicineDAO;

    // Constructor mặc định khởi tạo các đối tượng DAO
    public ExaminationService() {
        this.examinationResultDAO = new ExaminationResultDAO();
        this.appointmentDAO = new AppointmentDAO();
        this.prescriptionDAO = new PrescriptionDAO();
        this.medicineDAO = new MedicineDAO();
    }

    // Constructor hỗ trợ tiêm phụ thuộc cho Unit Test
    public ExaminationService(ExaminationResultDAO examinationResultDAO, AppointmentDAO appointmentDAO) {
        this(examinationResultDAO, appointmentDAO, new PrescriptionDAO(), new MedicineDAO());
    }

    public ExaminationService(ExaminationResultDAO examinationResultDAO, AppointmentDAO appointmentDAO,
                              PrescriptionDAO prescriptionDAO, MedicineDAO medicineDAO) {
        this.examinationResultDAO = examinationResultDAO;
        this.appointmentDAO = appointmentDAO;
        this.prescriptionDAO = prescriptionDAO;
        this.medicineDAO = medicineDAO;
    }

    /**
     * Phương thức hợp nhất ghi nhận toàn diện phiên khám lâm sàng (Clinical Consultation Seam).
     * Thực hiện trong một Transaction nguyên tử duy nhất:
     * 1. Kiểm tra quyền phụ trách lịch hẹn của Bác sĩ
     * 2. Lưu kết quả chẩn đoán lâm sàng
     * 3. Lưu thông tin hẹn tái khám (nếu có)
     * 4. Lưu các dịch vụ/thủ thuật chỉ định thêm (nếu có)
     * 5. Lưu đơn thuốc và trừ tồn kho tự động (nếu có)
     * 6. Cập nhật trạng thái lịch hẹn sang Completed (nếu requested) và phát tán sự kiện WebSocket
     *
     * @param request DTO đóng gói toàn bộ dữ liệu phiên khám
     * @return ConsultationResultDTO chứa kết quả và các ID sinh ra
     */
    public ConsultationResultDTO recordConsultation(ConsultationRequestDTO request) {
        if (request == null || isRecordInputInvalid(request.getAppointmentId(), request.getDoctorId(),
                request.getDiagnosis(), request.getRevisitDate())) {
            return ConsultationResultDTO.failure("Thông tin yêu cầu khám bệnh không hợp lệ.");
        }

        List<PrescriptionDetail> details = request.getPrescriptionDetails();
        if (details != null && !details.isEmpty()) {
            for (PrescriptionDetail d : details) {
                if (d.getMedicineId() <= 0 || d.getPrescribedQuantity() <= 0) {
                    return ConsultationResultDTO.failure("Thông tin thuốc kê đơn hoặc số lượng không hợp lệ.");
                }
            }
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction thủ công

            if (!appointmentDAO.isAppointmentAssignedToDoctor(request.getAppointmentId(), request.getDoctorId(), conn)) {
                conn.rollback();
                return ConsultationResultDTO.failure("Lịch hẹn không thuộc quyền tiếp nhận của bác sĩ.");
            }

            int resultId = saveExaminationResult(request.getAppointmentId(), request.getDiagnosis(), conn);
            saveRevisit(request.getAppointmentId(), request.getDoctorId(), request.getRevisitDate(), request.getRevisitNote(), conn);
            saveAdditionalServices(resultId, request.getAdditionalServices(), conn);

            int prescriptionId = 0;
            if (details != null && !details.isEmpty()) {
                prescriptionId = savePrescriptionAndDeductStock(resultId, request.getPrescriptionInstructions(), details, conn);
            }

            boolean completed = false;
            if (request.isCompleteAppointment()) {
                completed = appointmentDAO.completeAppointmentForDoctorIfCurrent(
                        request.getAppointmentId(), request.getDoctorId(),
                        Constants.APPOINTMENT_CHECKED_IN, Constants.APPOINTMENT_COMPLETED, conn);
            }

            conn.commit();

            if (completed) {
                broadcastAppointmentUpdated(request.getAppointmentId(), Constants.APPOINTMENT_COMPLETED);
            }

            return ConsultationResultDTO.success(resultId, prescriptionId, request.getAppointmentId(), completed,
                    "Hoàn tất phiên khám và ghi nhận kết quả thành công!");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            rollbackQuietly(conn);
            return ConsultationResultDTO.failure("Lỗi xử lý phiên khám bệnh: " + e.getMessage());

        } finally {
            closeQuietly(conn);
        }
    }

    /**
     * Phương thức ghi nhận chẩn đoán kết quả khám và các dịch vụ phát sinh (tương thích ngược).
     * @param appointmentId Mã lịch hẹn
     * @param doctorId Mã bác sĩ
     * @param diagnosis Nội dung chẩn đoán
     * @param additionalServices Danh sách dịch vụ chỉ định thêm
     * @param revisitDate Ngày hẹn tái khám
     * @param revisitNote Ghi chú tái khám
     * @return Mã result_id nếu thành công, -1 nếu thất bại
     */
    public int recordExamination(int appointmentId, int doctorId, String diagnosis, List<PrescribedService> additionalServices,
                                 Date revisitDate, String revisitNote) {
        ConsultationRequestDTO req = new ConsultationRequestDTO();
        req.setAppointmentId(appointmentId);
        req.setDoctorId(doctorId);
        req.setDiagnosis(diagnosis);
        req.setAdditionalServices(additionalServices);
        req.setRevisitDate(revisitDate);
        req.setRevisitNote(revisitNote);
        req.setCompleteAppointment(false); // Giữ hành vi tương thích ngược

        ConsultationResultDTO res = recordConsultation(req);
        return res.isSuccess() ? res.getResultId() : -1;
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

    private int savePrescriptionAndDeductStock(int resultId, String instructions, List<PrescriptionDetail> details, Connection conn) throws SQLException {
        Prescription prescription = new Prescription();
        prescription.setResultId(resultId);
        prescription.setInstructions(instructions);
        prescription.setDetails(details);

        int prescriptionId = prescriptionDAO.insertPrescription(prescription, conn);
        if (prescriptionId <= 0) {
            throw new SQLException("Thao tác tạo đơn thuốc thất bại, không thể lấy mã ID sinh ra.");
        }

        for (PrescriptionDetail detail : details) {
            boolean stockDeducted = medicineDAO.deductStockQuantity(detail.getMedicineId(), detail.getPrescribedQuantity(), conn);
            if (!stockDeducted) {
                throw new SQLException("Không đủ số lượng tồn kho cho thuốc có mã ID: " + detail.getMedicineId());
            }

            detail.setPrescriptionId(prescriptionId);
            boolean detailInserted = prescriptionDAO.insertPrescriptionDetail(detail, conn);
            if (!detailInserted) {
                throw new SQLException("Lưu chi tiết đơn thuốc thất bại cho thuốc ID: " + detail.getMedicineId());
            }
        }

        return prescriptionId;
    }

    private void broadcastAppointmentUpdated(int appointmentId, String status) {
        try {
            AppointmentWebSocket.broadcast("{\"event\":\"APPOINTMENT_UPDATED\",\"appointmentId\":" + appointmentId + ",\"status\":\"" + status + "\"}");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to broadcast appointment update: " + appointmentId, e);
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
    public List<PrescribedService> getPrescribedServices(int resultId) {
        if (resultId <= 0) {
            return List.of();
        }

        return examinationResultDAO.getPrescribedServicesByResultId(resultId);
    }
}
