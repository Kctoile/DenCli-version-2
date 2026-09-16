/**
 * File: ServiceDAOImpl.java
 * Mục đích: Triển khai các phương thức truy xuất bảng 'services' sử dụng JDBC thuần và PreparedStatement.
 */
package com.devjava.dencli.dao.impl;

import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.ServiceDAO;
import com.devjava.dencli.model.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAOImpl implements ServiceDAO {

    /**
     * Phương thức lấy toàn bộ danh sách dịch vụ nha khoa hiện có, sắp xếp theo tên dịch vụ.
     * @return Danh sách các dịch vụ
     */
    @Override // Ghi đè phương thức getAllServices từ interface ServiceDAO
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY service_id ASC";

        // Sử dụng try-with-resources để tự động giải phóng tài nguyên kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); // Chuẩn bị câu lệnh truy vấn qua JDBC
             ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn SELECT lấy dữ liệu

            while (rs.next()) { // Lặp qua tập kết quả ResultSet
                Service service = mapResultSetToService(rs); // Ánh xạ dữ liệu sang thực thể Service

                services.add(service); // Thêm dịch vụ vào danh sách
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận lỗi khi lấy danh sách dịch vụ
            e.printStackTrace();
        }

        return services;
    }

    /**
     * Phương thức tìm kiếm thông tin dịch vụ nha khoa theo mã service_id.
     * @param serviceId Mã dịch vụ
     * @return Đối tượng Service hoặc null nếu không tìm thấy
     */
    @Override // Ghi đè phương thức getServiceById từ interface ServiceDAO
    public Service getServiceById(int serviceId) {
        String sql = "SELECT * FROM services WHERE service_id = ?";

        // Sử dụng try-with-resources đóng Connection và PreparedStatement an toàn
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị câu lệnh tìm kiếm theo ID

            ps.setInt(1, serviceId);

            try (ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn trên SQL Server
                if (rs.next()) {
                    return mapResultSetToService(rs); // Ánh xạ kết quả sang thực thể Service
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            // In ngoại lệ khi truy vấn dịch vụ theo ID thất bại
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Phương thức lấy danh sách dịch vụ bệnh nhân đã đăng ký cho một lịch hẹn cụ thể.
     * @param appointmentId Mã lịch hẹn
     * @return Danh sách dịch vụ đính kèm
     */
    @Override // Ghi đè phương thức getServicesByAppointmentId từ interface ServiceDAO
    public List<Service> getServicesByAppointmentId(int appointmentId) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT s.* FROM services s "
                   + "INNER JOIN appointment_services asv ON s.service_id = asv.service_id "
                   + "WHERE asv.appointment_id = ?";

        // Sử dụng try-with-resources đóng tài nguyên tự động
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị truy vấn kết hợp bảng trung gian

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn qua JDBC
                while (rs.next()) { // Lặp qua danh sách dịch vụ của cuộc hẹn
                    Service service = mapResultSetToService(rs);

                    services.add(service);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận ngoại lệ hệ thống
            e.printStackTrace();
        }

        return services;
    }

    /**
     * Phương thức lấy danh sách dịch vụ có phân trang bằng cú pháp SQL Server OFFSET ... FETCH.
     * @param offset Số bản ghi bỏ qua
     * @param limit Số bản ghi tối đa
     * @return Danh sách dịch vụ của trang
     */
    @Override // Ghi đè phương thức getServicesWithPagination từ interface ServiceDAO
    public List<Service> getServicesWithPagination(int offset, int limit) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY service_id ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // Sử dụng try-with-resources để tự động giải phóng Connection
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị truy vấn phân trang

            ps.setInt(1, offset);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn phân trang qua JDBC
                while (rs.next()) { // Đọc từng dòng kết quả
                    Service s = mapResultSetToService(rs);

                    services.add(s);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            // In vết lỗi phân trang dịch vụ
            e.printStackTrace();
        }

        return services;
    }

    /**
     * Phương thức đếm tổng số dịch vụ nha khoa hiện có trong bảng services.
     * @return Tổng số lượng dịch vụ
     */
    @Override // Ghi đè phương thức countServices từ interface ServiceDAO
    public int countServices() {
        String sql = "SELECT COUNT(*) FROM services";

        // Sử dụng try-with-resources giải phóng tài nguyên tự động
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); // Chuẩn bị câu lệnh COUNT
             ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn đếm

            if (rs.next()) {
                return rs.getInt(1); // Trả về số lượng ở cột đầu tiên
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận lỗi khi đếm số dịch vụ
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Phương thức thêm mới một dịch vụ nha khoa vào cơ sở dữ liệu.
     * @param service Đối tượng dịch vụ cần thêm
     * @return true nếu thêm thành công
     */
    @Override // Ghi đè phương thức insertService từ interface ServiceDAO
    public boolean insertService(Service service) {
        String sql = "INSERT INTO services (service_name, description, price, duration_minutes) VALUES (?, ?, ?, ?)";

        // Sử dụng try-with-resources đóng tài nguyên tự động
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị câu lệnh INSERT

            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());
            ps.setBigDecimal(3, service.getPrice());

            if (service.getDurationMinutes() != null) {
                ps.setInt(4, service.getDurationMinutes());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            // Thực thi lệnh cập nhật qua executeUpdate
            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            // In ngoại lệ khi thêm mới dịch vụ thất bại
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức cập nhật thông tin dịch vụ nha khoa theo service_id.
     * @param service Đối tượng dịch vụ chứa thông tin mới
     * @return true nếu cập nhật thành công
     */
    @Override // Ghi đè phương thức updateService từ interface ServiceDAO
    public boolean updateService(Service service) {
        String sql = "UPDATE services SET service_name = ?, description = ?, price = ?, duration_minutes = ? "
                   + "WHERE service_id = ?";

        // Sử dụng try-with-resources đóng kết nối an toàn
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị câu lệnh UPDATE qua JDBC

            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());
            ps.setBigDecimal(3, service.getPrice());

            if (service.getDurationMinutes() != null) {
                ps.setInt(4, service.getDurationMinutes());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            ps.setInt(5, service.getServiceId());

            // Gọi phương thức executeUpdate để thực thi sửa đổi dữ liệu
            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận lỗi cập nhật thông tin dịch vụ
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức xóa dịch vụ nha khoa theo mã định danh.
     * @param serviceId Mã dịch vụ cần xóa
     * @return true nếu xóa thành công
     */
    @Override // Ghi đè phương thức deleteService từ interface ServiceDAO
    public boolean deleteService(int serviceId) {
        String sql = "DELETE FROM services WHERE service_id = ?";

        // Sử dụng try-with-resources đóng Connection và PreparedStatement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị câu lệnh DELETE

            ps.setInt(1, serviceId);

            // Gọi phương thức executeUpdate để thực hiện thao tác xóa
            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            // Bắt lỗi khi xóa dịch vụ
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức phụ trợ ánh xạ một dòng trong ResultSet sang đối tượng Service.
     * @param rs Tập kết quả ResultSet
     * @return Đối tượng Service
     * @throws SQLException khi trích xuất cột gặp lỗi
     */
    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();

        // Trích xuất các trường từ cột ResultSet của JDBC
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
