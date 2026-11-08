/**
 * File: ServiceDAO.java
 * Mục đích: Triển khai các phương thức truy xuất dữ liệu từ bảng 'services' sử dụng JDBC thuần.
 */
package com.devjava.dencli.dao;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.devjava.dencli.model.Service;

public class ServiceDAO {

    private static final Logger LOGGER = Logger.getLogger(ServiceDAO.class.getName());

    /**
     * Phương thức lấy toàn bộ danh sách dịch vụ nha khoa hiện có.
     * @return Danh sách các dịch vụ (List of Service)
     */
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT service_id, service_name, description, price, duration_minutes FROM services ORDER BY service_id ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        return services;
    }

    /**
     * Phương thức lấy danh sách dịch vụ bệnh nhân đã đăng ký cho một lịch hẹn cụ thể.
     * @param appointmentId Mã lịch hẹn
     * @return Danh sách dịch vụ đính kèm
     */
    public List<Service> getServicesByAppointmentId(int appointmentId) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT s.* FROM services s "
                   + "INNER JOIN appointment_services asv ON s.service_id = asv.service_id "
                   + "WHERE asv.appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        return services;
    }

    /**
     * Phương thức phụ trợ ánh xạ một dòng trong ResultSet sang đối tượng Service.
     * @param rs Tập kết quả ResultSet
     * @return Đối tượng Service
     * @throws SQLException khi trích xuất cột gặp lỗi
     */
    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setServiceId(rs.getInt("service_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setDescription(rs.getString("description"));
        service.setPrice(rs.getBigDecimal("price"));

        int duration = rs.getInt("duration_minutes");
        if (!rs.wasNull()) {
            service.setDurationMinutes(duration);
        }

        return service;
    }
}
