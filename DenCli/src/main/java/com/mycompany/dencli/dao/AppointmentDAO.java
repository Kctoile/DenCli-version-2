/**
 * Purpose: DAO class for managing 'appointments' and 'appointment_services' table queries in SQL Server.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.dao;

import com.mycompany.dencli.models.Appointment;
import com.mycompany.dencli.utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentDAO {

    private static final Logger LOGGER = Logger.getLogger(AppointmentDAO.class.getName());

    /**
     * Hàm kiểm tra trùng lịch khám của bác sĩ tại một ngày và giờ cụ thể.
     * @param doctorId
     * @param date
     * @param time
     * @return 
     */
    public boolean checkDuplicateSlot(int doctorId, java.sql.Date date, java.sql.Time time) {
        String sql = "SELECT 1 FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? AND status <> 'Cancelled'";

        // Sử dụng try-with-resources để quản lý kết nối CSDL và PreparedStatement
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Khởi tạo PreparedStatement qua JDBC

            ps.setInt(1, doctorId);
            ps.setDate(2, date);
            ps.setTime(3, time);

            try (ResultSet rs = ps.executeQuery()) { // Thực hiện truy vấn SELECT thông qua thư viện JDBC
                return rs.next(); // Trả về true nếu đã có cuộc hẹn trùng lịch
            }

        } catch (Exception e) {
            e.printStackTrace(); // Ghi nhận ngoại lệ hệ thống
        }

        return false;
    }

    /**
     * Hàm thêm mới một lịch hẹn khám và các dịch vụ đặt trước đi kèm trong một Transaction.
     */
    public boolean insertAppointment(Appointment app, List<Integer> serviceIds) {
        String sqlApp = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes, room) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlService = "INSERT INTO appointment_services (appointment_id, service_id) VALUES (?, ?)";
        try (Connection conn = DBContext.getConnection()) { // Lấy kết nối CSDL từ DBContext
            try {

                conn.setAutoCommit(false); // Vô hiệu hóa tính năng tự động commit của JDBC để bắt đầu Transaction

                // Khởi tạo PreparedStatement cho việc chèn lịch hẹn và yêu cầu trả về khóa chính tự sinh
                try (PreparedStatement psApp = conn.prepareStatement(sqlApp, Statement.RETURN_GENERATED_KEYS)) { // Gọi hàm RETURN_GENERATED_KEYS từ thư viện JDBC

            psApp.setInt(1, app.getPatientId());
            psApp.setInt(2, app.getDoctorId());
            psApp.setDate(3, app.getAppointmentDate());
            psApp.setTime(4, app.getAppointmentTime());
            psApp.setString(5, app.getStatus());
            psApp.setString(6, app.getNotes());
            psApp.setString(7, app.getRoom());

            int affectedRows = psApp.executeUpdate(); // Chạy lệnh INSERT lịch hẹn của JDBC

            if (affectedRows == 0) {
                throw new SQLException("Tạo lịch hẹn thất bại, không có dòng nào được thêm.");
            }

            int appId = -1;

            try (ResultSet generatedKeys = psApp.getGeneratedKeys()) { // Lấy danh sách khóa chính được sinh ra của JDBC
                if (generatedKeys.next()) {
                    appId = generatedKeys.getInt(1); // Lấy mã appointment_id vừa tự sinh ra
                }
            }

            if (appId == -1) {
                throw new SQLException("Tạo lịch hẹn thất bại, không lấy được mã appointment_id.");
            }

            app.setAppointmentId(appId);

            // Nếu bệnh nhân có chọn dịch vụ cụ thể đi kèm
                    if (serviceIds != null && !serviceIds.isEmpty()) {
                        try (PreparedStatement psService = conn.prepareStatement(sqlService)) { // Tạo câu lệnh PreparedStatement cho bảng trung gian

                            for (int serviceId : serviceIds) { // Vòng lặp duyệt qua các ID dịch vụ để chuẩn bị chèn
                                psService.setInt(1, appId);
                                psService.setInt(2, serviceId);

                                psService.addBatch(); // Sử dụng tính năng addBatch của JDBC để tối ưu hóa hiệu năng chèn mảng
                            }

                            psService.executeBatch(); // Thực thi chạy nhiều lệnh chèn cùng lúc bằng executeBatch của JDBC
                        }
                    }

                    conn.commit(); // Commit toàn bộ Transaction nếu không xảy ra bất cứ lỗi nào qua JDBC
                    return true;
                }

            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to insert appointment", e);

                try {
                    conn.rollback(); // Rollback khôi phục lại dữ liệu nếu xảy ra lỗi trong quá trình chạy qua JDBC
                } catch (SQLException ex) {
                    LOGGER.log(Level.WARNING, "Failed to rollback appointment transaction", ex);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to open appointment transaction", e);
        }

        return false;
    }
}
