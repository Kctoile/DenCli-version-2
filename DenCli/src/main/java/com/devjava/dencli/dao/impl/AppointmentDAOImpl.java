/**
 * File: AppointmentDAOImpl.java
 * Mục đích: Triển khai các phương thức truy xuất bảng 'appointments' và 'appointment_services' sử dụng JDBC thuần.
 */
package com.devjava.dencli.dao.impl;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.dto.RevenueDTO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAOImpl implements AppointmentDAO {

    /**
     * Phương thức kiểm tra bác sĩ có bị trùng lịch vào cùng ngày và giờ hay không.
     * @param doctorId Mã bác sĩ phụ trách
     * @param date Ngày hẹn khám
     * @param time Giờ hẹn khám
     * @return true nếu đã có lịch trùng khác trạng thái Cancelled
     */
    @Override // Ghi đè phương thức checkDuplicateSlot từ interface AppointmentDAO
    public boolean checkDuplicateSlot(int doctorId, Date date, Time time) {
        String sql = "SELECT 1 FROM appointments "
                   + "WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? AND status <> 'Cancelled'";

        // Áp dụng try-with-resources tự động giải phóng Connection và PreparedStatement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Khởi tạo câu lệnh PreparedStatement

            ps.setInt(1, doctorId);
            ps.setDate(2, date);
            ps.setTime(3, time);

            try (ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn kiểm tra trùng slot
                return rs.next(); // Trả về true nếu đã có bản ghi tồn tại
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận ngoại lệ khi kiểm tra trùng lịch
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức thêm mới một lịch hẹn kèm danh sách dịch vụ đi kèm sử dụng Transaction trên Connection.
     * @param appointment Đối tượng lịch hẹn
     * @param serviceIds Danh sách mã dịch vụ
     * @return Mã appointment_id vừa tạo, hoặc -1 nếu thất bại
     */
    @Override // Ghi đè phương thức insertAppointmentWithServices từ interface AppointmentDAO
    public int insertAppointmentWithServices(Appointment appointment, List<Integer> serviceIds) {
        String sqlApp = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes, room) "
                      + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlService = "INSERT INTO appointment_services (appointment_id, service_id) VALUES (?, ?)";

        // Sử dụng try-with-resources để tự động đóng kết nối sau khi hoàn tất Transaction
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Vô hiệu hóa chế độ tự động commit để bắt đầu Transaction thủ công

            int generatedAppId = -1;

            // Chuẩn bị PreparedStatement và yêu cầu trả về khóa chính tự tăng
            try (PreparedStatement psApp = conn.prepareStatement(sqlApp, Statement.RETURN_GENERATED_KEYS)) {
                if (appointment.getPatientId() != null) {
                    psApp.setInt(1, appointment.getPatientId());
                } else {
                    psApp.setNull(1, java.sql.Types.INTEGER);
                }

                if (appointment.getDoctorId() != null) {
                    psApp.setInt(2, appointment.getDoctorId());
                } else {
                    psApp.setNull(2, java.sql.Types.INTEGER);
                }

                psApp.setDate(3, appointment.getAppointmentDate());
                psApp.setTime(4, appointment.getAppointmentTime());
                psApp.setString(5, appointment.getStatus());
                psApp.setString(6, appointment.getNotes());
                psApp.setString(7, appointment.getRoom());

                int rows = psApp.executeUpdate(); // Thực thi câu lệnh chèn lịch hẹn
                if (rows == 0) {
                    conn.rollback(); // Rollback nếu không có dòng nào được tạo
                    return -1;
                }

                try (ResultSet rsKeys = psApp.getGeneratedKeys()) { // Lấy khóa chính vừa sinh
                    if (rsKeys.next()) {
                        generatedAppId = rsKeys.getInt(1);
                    }
                }
            }

            // Nếu có danh sách dịch vụ đi kèm, tiến hành chèn vào bảng trung gian
            if (serviceIds != null && !serviceIds.isEmpty() && generatedAppId > 0) {
                try (PreparedStatement psService = conn.prepareStatement(sqlService)) {
                    for (int serviceId : serviceIds) { // Lặp qua danh sách ID dịch vụ
                        psService.setInt(1, generatedAppId);
                        psService.setInt(2, serviceId);

                        psService.addBatch(); // Sử dụng cơ chế Batching của JDBC để tối ưu hiệu năng
                    }

                    psService.executeBatch(); // Thực thi toàn bộ lệnh chèn trung gian
                }
            }

            conn.commit(); // Xác nhận Transaction thành công
            return generatedAppId;

        } catch (ClassNotFoundException | SQLException e) {
            // In ngoại lệ khi thêm lịch hẹn thất bại
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Phương thức lấy thông tin chi tiết của một cuộc hẹn theo mã ID.
     * @param appointmentId Mã cuộc hẹn
     * @return Đối tượng Appointment kèm tên bệnh nhân, số điện thoại và tên bác sĩ
     */
    @Override // Ghi đè phương thức getAppointmentById từ interface AppointmentDAO
    public Appointment getAppointmentById(int appointmentId) {
        String sql = "SELECT a.*, p.full_name AS patient_name, p.phone AS patient_phone, d.full_name AS doctor_name "
                   + "FROM appointments a "
                   + "LEFT JOIN users p ON a.patient_id = p.user_id "
                   + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                   + "WHERE a.appointment_id = ?";

        // Sử dụng try-with-resources đóng tài nguyên tự động
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị câu lệnh tìm kiếm theo ID

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn tìm kiếm
                if (rs.next()) {
                    return mapResultSetToAppointment(rs);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận ngoại lệ khi truy vấn cuộc hẹn theo ID
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Phương thức lấy danh sách lịch hẹn của một bệnh nhân có phân trang.
     * @param patientId Mã bệnh nhân
     * @param offset Số bản ghi bỏ qua
     * @param limit Số bản ghi tối đa
     * @return Danh sách lịch hẹn
     */
    @Override // Ghi đè phương thức getAppointmentsByPatient từ interface AppointmentDAO
    public List<Appointment> getAppointmentsByPatient(int patientId, int offset, int limit) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.full_name AS patient_name, p.phone AS patient_phone, d.full_name AS doctor_name "
                   + "FROM appointments a "
                   + "LEFT JOIN users p ON a.patient_id = p.user_id "
                   + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                   + "WHERE a.patient_id = ? "
                   + "ORDER BY a.appointment_date DESC, a.appointment_time DESC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // Sử dụng try-with-resources đóng Connection và PreparedStatement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị truy vấn phân trang

            ps.setInt(1, patientId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            try (ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn lấy danh sách lịch hẹn của bệnh nhân
                while (rs.next()) {
                    list.add(mapResultSetToAppointment(rs));
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            // In ngoại lệ hệ thống
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Phương thức đếm tổng số cuộc hẹn của một bệnh nhân.
     * @param patientId Mã bệnh nhân
     * @return Tổng số cuộc hẹn
     */
    @Override // Ghi đè phương thức countAppointmentsByPatient từ interface AppointmentDAO
    public int countAppointmentsByPatient(int patientId) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE patient_id = ?";

        // Sử dụng try-with-resources để tự động giải phóng kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Phương thức lấy danh sách lịch hẹn phân công cho bác sĩ theo ngày (tùy chọn) có phân trang.
     * @param doctorId Mã bác sĩ
     * @param date Ngày hẹn (nếu null thì lấy toàn bộ)
     * @param offset Số bản ghi bỏ qua
     * @param limit Số bản ghi tối đa
     * @return Danh sách lịch hẹn phân công
     */
    @Override // Ghi đè phương thức getAppointmentsByDoctor từ interface AppointmentDAO
    public List<Appointment> getAppointmentsByDoctor(int doctorId, Date date, int offset, int limit) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.full_name AS patient_name, p.phone AS patient_phone, d.full_name AS doctor_name "
                   + "FROM appointments a "
                   + "LEFT JOIN users p ON a.patient_id = p.user_id "
                   + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                   + "WHERE a.doctor_id = ? AND (? IS NULL OR a.appointment_date = ?) "
                   + "ORDER BY a.appointment_date ASC, a.appointment_time ASC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // Sử dụng try-with-resources đóng tài nguyên sau truy vấn
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setDate(2, date);
            ps.setDate(3, date);
            ps.setInt(4, offset);
            ps.setInt(5, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAppointment(rs));
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Phương thức đếm tổng số lịch hẹn phân công cho bác sĩ theo ngày.
     * @param doctorId Mã bác sĩ
     * @param date Ngày hẹn
     * @return Số lượng cuộc hẹn
     */
    @Override // Ghi đè phương thức countAppointmentsByDoctor từ interface AppointmentDAO
    public int countAppointmentsByDoctor(int doctorId, Date date) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND (? IS NULL OR appointment_date = ?)";

        // Sử dụng try-with-resources đóng tài nguyên tự động
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setDate(2, date);
            ps.setDate(3, date);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Phương thức lấy danh sách lịch hẹn chung có lọc theo trạng thái và ngày phục vụ quản trị viên/lễ tân.
     * @param status Trạng thái lọc (nếu null/rỗng thì lấy tất cả)
     * @param date Ngày lọc (nếu null thì lấy tất cả)
     * @param offset Số bản ghi bỏ qua
     * @param limit Số bản ghi tối đa
     * @return Danh sách lịch hẹn phù hợp
     */
    @Override // Ghi đè phương thức getAllAppointmentsWithPagination từ interface AppointmentDAO
    public List<Appointment> getAllAppointmentsWithPagination(String status, Date date, int offset, int limit) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.full_name AS patient_name, p.phone AS patient_phone, d.full_name AS doctor_name "
                   + "FROM appointments a "
                   + "LEFT JOIN users p ON a.patient_id = p.user_id "
                   + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                   + "WHERE (? IS NULL OR ? = '' OR a.status = ?) "
                   + "  AND (? IS NULL OR a.appointment_date = ?) "
                   + "ORDER BY a.appointment_date DESC, a.appointment_time DESC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // Sử dụng try-with-resources đóng tài nguyên an toàn
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, status);
            ps.setString(3, status);
            ps.setDate(4, date);
            ps.setDate(5, date);
            ps.setInt(6, offset);
            ps.setInt(7, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAppointment(rs));
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Phương thức đếm tổng số lịch hẹn theo tiêu chí lọc trạng thái và ngày.
     * @param status Trạng thái
     * @param date Ngày
     * @return Tổng số lượng bản ghi
     */
    @Override // Ghi đè phương thức countAllAppointments từ interface AppointmentDAO
    public int countAllAppointments(String status, Date date) {
        String sql = "SELECT COUNT(*) FROM appointments "
                   + "WHERE (? IS NULL OR ? = '' OR status = ?) "
                   + "  AND (? IS NULL OR appointment_date = ?)";

        // Sử dụng try-with-resources đóng tài nguyên tự động
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, status);
            ps.setString(3, status);
            ps.setDate(4, date);
            ps.setDate(5, date);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Phương thức cập nhật trạng thái lịch hẹn.
     * @param appointmentId Mã lịch hẹn
     * @param status Trạng thái mới
     * @return true nếu cập nhật thành công
     */
    @Override // Ghi đè phương thức updateAppointmentStatus từ interface AppointmentDAO
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";

        // Sử dụng try-with-resources đóng kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, appointmentId);

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức cập nhật phòng khám cho lịch hẹn khi bệnh nhân làm thủ tục tiếp đón.
     * @param appointmentId Mã lịch hẹn
     * @param room Tên phòng khám
     * @return true nếu cập nhật thành công
     */
    @Override // Ghi đè phương thức updateAppointmentRoom từ interface AppointmentDAO
    public boolean updateAppointmentRoom(int appointmentId, String room) {
        String sql = "UPDATE appointments SET room = ? WHERE appointment_id = ?";

        // Sử dụng try-with-resources đóng kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room);
            ps.setInt(2, appointmentId);

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức thống kê doanh thu tổng hợp của phòng khám theo từng tháng trong năm phục vụ biểu đồ Chart.js.
     * @param year Năm cần thống kê
     * @return Danh sách 12 đối tượng RevenueDTO tương ứng 12 tháng
     */
    @Override // Ghi đè phương thức getMonthlyRevenueStatistics từ interface AppointmentDAO
    public List<RevenueDTO> getMonthlyRevenueStatistics(int year) {
        List<RevenueDTO> result = new ArrayList<>();

        // Khởi tạo trước danh sách rỗng cho 12 tháng từ tháng 1 đến tháng 12
        for (int m = 1; m <= 12; m++) {
            String label = "Tháng " + m;
            result.add(new RevenueDTO(year, m, label, BigDecimal.ZERO, 0));
        }

        // Truy vấn tính tổng tiền dịch vụ đã thực hiện từ các cuộc hẹn hoàn tất (Completed)
        String sql = "SELECT MONTH(a.appointment_date) AS [month], "
                   + "       COUNT(DISTINCT a.appointment_id) AS total_appointments, "
                   + "       ISNULL(SUM(s.price), 0) AS total_revenue "
                   + "FROM appointments a "
                   + "LEFT JOIN appointment_services asv ON a.appointment_id = asv.appointment_id "
                   + "LEFT JOIN services s ON asv.service_id = s.service_id "
                   + "WHERE YEAR(a.appointment_date) = ? AND a.status = 'Completed' "
                   + "GROUP BY MONTH(a.appointment_date)";

        // Sử dụng try-with-resources để tự động giải phóng tài nguyên
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    int totalApps = rs.getInt("total_appointments");
                    BigDecimal rev = rs.getBigDecimal("total_revenue");

                    // Cập nhật giá trị vào danh sách tháng tương ứng (vị trí index = month - 1)
                    if (month >= 1 && month <= 12) {
                        RevenueDTO dto = result.get(month - 1);
                        dto.setTotalAppointments(totalApps);
                        dto.setRevenue(rev != null ? rev : BigDecimal.ZERO);
                    }
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Phương thức phụ trợ ánh xạ một dòng ResultSet sang đối tượng Appointment.
     * @param rs Tập kết quả ResultSet
     * @return Đối tượng Appointment
     * @throws SQLException khi trích xuất cột gặp lỗi
     */
    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment app = new Appointment();

        app.setAppointmentId(rs.getInt("appointment_id"));

        int patientId = rs.getInt("patient_id");
        if (!rs.wasNull()) {
            app.setPatientId(patientId);
        }

        int doctorId = rs.getInt("doctor_id");
        if (!rs.wasNull()) {
            app.setDoctorId(doctorId);
        }

        app.setAppointmentDate(rs.getDate("appointment_date"));
        app.setAppointmentTime(rs.getTime("appointment_time"));
        app.setStatus(rs.getString("status"));
        app.setNotes(rs.getString("notes"));
        app.setRoom(rs.getString("room"));

        try {
            app.setPatientName(rs.getString("patient_name"));
            app.setPatientPhone(rs.getString("patient_phone"));
            app.setDoctorName(rs.getString("doctor_name"));
        } catch (SQLException ignored) {
            // Bỏ qua nếu truy vấn không kèm các cột kết nối JOIN
        }

        return app;
    }
}
