/**
 * Purpose: DAO class for managing 'users' table queries in SQL Server.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.dao;

import com.mycompany.dencli.models.User;
import com.mycompany.dencli.utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Hàm kiểm tra xem email đã tồn tại trong cơ sở dữ liệu hay chưa.
     */
    public boolean checkEmailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        // Sử dụng try-with-resources để tự động đóng kết nối và lệnh truy vấn
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Gọi hàm prepareStatement từ JDBC thư viện

            ps.setString(1, email); // Gán tham số email vào câu truy vấn PreparedStatement

            try (ResultSet rs = ps.executeQuery()) { // Thực thi câu lệnh truy vấn SELECT từ thư viện JDBC
                return rs.next(); // Trả về true nếu tìm thấy dòng kết quả
            }

        } catch (Exception e) {
            e.printStackTrace(); // In vết lỗi hệ thống
        }

        return false;
    }

    /**
     * Hàm kiểm tra xem số điện thoại đã tồn tại trong cơ sở dữ liệu hay chưa.
     */
    public boolean checkPhoneExists(String phone) {
        String sql = "SELECT 1 FROM users WHERE phone = ?";

        // Sử dụng try-with-resources để quản lý kết nối cơ sở dữ liệu an toàn
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Khởi tạo PreparedStatement từ JDBC

            ps.setString(1, phone); // Thiết lập tham số số điện thoại

            try (ResultSet rs = ps.executeQuery()) { // Thực hiện truy vấn SELECT dữ liệu qua JDBC
                return rs.next(); // Trả về true nếu số điện thoại đã được đăng ký
            }

        } catch (Exception e) {
            e.printStackTrace(); // In lỗi hệ thống ra console
        }

        return false;
    }

    /**
     * Hàm thêm mới một tài khoản người dùng (Bệnh nhân) vào cơ sở dữ liệu.
     */
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (full_name, email, password, phone, role_id, gender, dob, address, created_at, display_order) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Sử dụng try-with-resources để tự động giải phóng tài nguyên kết nối
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị câu lệnh INSERT trong JDBC

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setInt(5, user.getRoleId());
            ps.setString(6, user.getGender());
            ps.setDate(7, user.getDob());
            ps.setString(8, user.getAddress());
            ps.setTimestamp(9, new Timestamp(System.currentTimeMillis())); // Thiết lập thời gian hiện tại từ hệ thống Java
            ps.setInt(10, user.getDisplayOrder());

            int rows = ps.executeUpdate(); // Thực thi lệnh cập nhật INSERT của JDBC
            return rows > 0; // Trả về true nếu chèn dữ liệu thành công

        } catch (Exception e) {
            e.printStackTrace(); // Ghi nhận lỗi phát sinh khi thao tác CSDL
        }

        return false;
    }

    /**
     * Hàm tìm kiếm người dùng theo địa chỉ email phục vụ chức năng đăng nhập.
     */
    public User getUserByEmail(String emailOrPhone) {
        String sql = "SELECT * FROM users WHERE email = ? OR phone = ?";

        // Tạo đối tượng kết nối và chuẩn bị truy vấn SQL
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Tạo PreparedStatement trong JDBC

            ps.setString(1, emailOrPhone);
            ps.setString(2, emailOrPhone);

            try (ResultSet rs = ps.executeQuery()) { // Thực hiện thực thi truy vấn của JDBC
                if (rs.next()) {
                    // Trích xuất dữ liệu từ ResultSet của JDBC và tạo đối tượng User
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getInt("role_id"),
                        rs.getString("gender"),
                        rs.getDate("dob"),
                        rs.getString("address"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("display_order")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ngoại lệ hệ thống
        }

        return null;
    }

    /**
     * Hàm lấy danh sách tất cả các bác sĩ nha khoa (role_id = 2) từ bảng users.
     */
    public List<User> getDoctors() {
        List<User> list = new ArrayList<>(); // Khởi tạo danh sách ArrayList từ thư viện Java
        String sql = "SELECT * FROM users WHERE role_id = 2 ORDER BY display_order ASC";

        // Thực hiện kết nối và lấy danh sách bác sĩ
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); // Chuẩn bị câu lệnh truy vấn JDBC
             ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn lấy dữ liệu của JDBC

            while (rs.next()) { // Lặp qua tập kết quả ResultSet của JDBC
                User doc = new User(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getInt("role_id"),
                    rs.getString("gender"),
                    rs.getDate("dob"),
                    rs.getString("address"),
                    rs.getTimestamp("created_at"),
                    rs.getInt("display_order")
                );

                list.add(doc); // Thêm đối tượng vào danh sách ArrayList
            }

        } catch (Exception e) {
            e.printStackTrace(); // In lỗi hệ thống
        }

        return list;
    }
}
