/**
 * File: UserDAOImpl.java
 * Mục đích: Triển khai các phương thức truy xuất bảng 'users' sử dụng JDBC thuần và PreparedStatement.
 */
package com.devjava.dencli.dao.impl;

import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.UserDAO;
import com.devjava.dencli.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());

    /**
     * Phương thức kiểm tra email đã tồn tại trong bảng users bằng cách đếm bản ghi.
     * @param email Địa chỉ email cần kiểm tra
     * @return true nếu đã có bản ghi tồn tại
     */
    @Override // Ghi đè phương thức checkEmailExists từ interface UserDAO
    public boolean checkEmailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        // Sử dụng try-with-resources để tự động giải phóng Connection và PreparedStatement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Gọi hàm prepareStatement từ JDBC Connection

            ps.setString(1, email); // Gán tham số email vào câu truy vấn

            try (ResultSet rs = ps.executeQuery()) { // Thực thi câu truy vấn bằng hàm executeQuery của PreparedStatement
                return rs.next(); // Trả về true nếu con trỏ ResultSet có dữ liệu
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận lỗi truy vấn ra console
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức kiểm tra số điện thoại đã đăng ký trong bảng users hay chưa.
     * @param phone Số điện thoại cần kiểm tra
     * @return true nếu đã tồn tại
     */
    @Override // Ghi đè phương thức checkPhoneExists từ interface UserDAO
    public boolean checkPhoneExists(String phone) {
        String sql = "SELECT 1 FROM users WHERE phone = ?";

        // Sử dụng try-with-resources để tự động đóng kết nối JDBC
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Khởi tạo PreparedStatement qua JDBC

            ps.setString(1, phone); // Thiết lập giá trị chuỗi cho tham số thứ nhất

            try (ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn SELECT dữ liệu từ SQL Server
                return rs.next(); // Kiểm tra có bản ghi nào trùng khớp không
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận ngoại lệ khi truy vấn CSDL
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức thêm mới một người dùng vào bảng users bằng câu lệnh INSERT.
     * @param user Đối tượng người dùng cần thêm
     * @return true nếu số dòng bị tác động lớn hơn 0
     */
    @Override // Ghi đè phương thức insertUser từ interface UserDAO
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (full_name, email, password, phone, role_id, gender, dob, address, created_at, display_order) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Quản lý kết nối an toàn với khối try-with-resources
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị câu lệnh INSERT trong JDBC

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setInt(5, user.getRoleId());
            ps.setString(6, user.getGender());
            ps.setDate(7, user.getDob());
            ps.setString(8, user.getAddress());

            // Thiết lập thời gian hiện tại từ hệ thống Java
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(9, (user.getCreatedAt() != null) ? user.getCreatedAt() : currentTimestamp);

            ps.setInt(10, user.getDisplayOrder());

            // Gọi phương thức executeUpdate của PreparedStatement để thực hiện ghi dữ liệu
            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;

        } catch (ClassNotFoundException | SQLException e) {
            // In vết lỗi chi tiết khi thực thi câu lệnh INSERT thất bại
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức tìm kiếm người dùng theo email hoặc số điện thoại dùng cho chức năng xác thực đăng nhập.
     * @param emailOrPhone Email hoặc số điện thoại đăng nhập
     * @return Thực thể User hoặc null nếu không tồn tại
     */
    @Override // Ghi đè phương thức getUserByEmailOrPhone từ interface UserDAO
    public User getUserByEmailOrPhone(String emailOrPhone) {
        String sql = "SELECT u.*, r.role_name FROM users u "
                   + "LEFT JOIN roles r ON u.role_id = r.role_id "
                   + "WHERE u.email = ? OR u.phone = ?";

        // Sử dụng try-with-resources đảm bảo đóng tài nguyên ngay sau khi xử lý xong
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị truy vấn SELECT kết hợp JOIN

            ps.setString(1, emailOrPhone);
            ps.setString(2, emailOrPhone);

            try (ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn đọc dữ liệu từ SQL Server
                if (rs.next()) {
                    return mapResultSetToUser(rs); // Chuyển đổi dòng hiện tại thành đối tượng User
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Login lookup failed for identifier: " + emailOrPhone, e);
            throw new IllegalStateException("Unable to query the user account.", e);
        }

        return null;
    }

    /**
     * Phương thức lấy thông tin người dùng theo khóa chính user_id.
     * @param userId Mã người dùng cần lấy
     * @return Đối tượng User tương ứng
     */
    @Override // Ghi đè phương thức getUserById từ interface UserDAO
    public User getUserById(int userId) {
        String sql = "SELECT u.*, r.role_name FROM users u "
                   + "LEFT JOIN roles r ON u.role_id = r.role_id "
                   + "WHERE u.user_id = ?";

        // Sử dụng try-with-resources đóng Connection và PreparedStatement tự động
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị câu lệnh tìm kiếm theo ID

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn SELECT qua JDBC
                if (rs.next()) {
                    return mapResultSetToUser(rs); // Ánh xạ dữ liệu ResultSet sang User
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Bắt và in ngoại lệ hệ thống
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Phương thức lấy danh sách tất cả các bác sĩ nha khoa (role_id = 2) sắp xếp theo thứ tự hiển thị.
     * @return Danh sách đối tượng User có vai trò bác sĩ
     */
    @Override // Ghi đè phương thức getDoctors từ interface UserDAO
    public List<User> getDoctors() {
        List<User> doctors = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name FROM users u "
                   + "LEFT JOIN roles r ON u.role_id = r.role_id "
                   + "WHERE u.role_id = 2 ORDER BY u.display_order ASC, u.full_name ASC";

        // Sử dụng try-with-resources để tự động giải phóng tài nguyên kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); // Chuẩn bị truy vấn lấy danh sách bác sĩ
             ResultSet rs = ps.executeQuery()) { // Thực thi lệnh truy vấn lấy tập kết quả

            while (rs.next()) { // Duyệt qua từng bản ghi trả về từ ResultSet
                User doctor = mapResultSetToUser(rs); // Đóng gói dữ liệu sang thực thể User

                doctors.add(doctor); // Thêm bác sĩ vào danh sách kết quả
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận lỗi khi tải danh sách bác sĩ
            e.printStackTrace();
        }

        return doctors;
    }

    /**
     * Phương thức lấy danh sách người dùng theo vai trò có phân trang bằng cú pháp SQL Server OFFSET ... FETCH.
     * @param roleId Mã vai trò
     * @param offset Số bản ghi cần bỏ qua
     * @param limit Số bản ghi tối đa cần lấy
     * @return Danh sách người dùng của trang hiện tại
     */
    @Override // Ghi đè phương thức getUsersByRole từ interface UserDAO
    public List<User> getUsersByRole(int roleId, int offset, int limit) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name FROM users u "
                   + "LEFT JOIN roles r ON u.role_id = r.role_id "
                   + "WHERE (? = 0 OR u.role_id = ?) "
                   + "ORDER BY u.user_id DESC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // Áp dụng try-with-resources đóng Connection và PreparedStatement an toàn
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Khởi tạo PreparedStatement cho phân trang

            ps.setInt(1, roleId);
            ps.setInt(2, roleId);
            ps.setInt(3, offset); // Số bản ghi bỏ qua
            ps.setInt(4, limit);  // Số bản ghi lấy ra

            try (ResultSet rs = ps.executeQuery()) { // Thực thi câu lệnh truy vấn phân trang trên SQL Server
                while (rs.next()) { // Lặp qua từng dòng kết quả
                    User user = mapResultSetToUser(rs);

                    list.add(user); // Thêm người dùng vào danh sách
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Bắt lỗi khi phân trang người dùng
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Phương thức đếm tổng số bản ghi người dùng theo vai trò.
     * @param roleId Mã vai trò (nếu bằng 0 thì đếm toàn bộ)
     * @return Tổng số người dùng
     */
    @Override // Ghi đè phương thức countUsersByRole từ interface UserDAO
    public int countUsersByRole(int roleId) {
        String sql = "SELECT COUNT(*) FROM users WHERE (? = 0 OR role_id = ?)";

        // Sử dụng try-with-resources để tự động đóng kết nối JDBC
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị truy vấn đếm COUNT

            ps.setInt(1, roleId);
            ps.setInt(2, roleId);

            try (ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn đếm từ SQL Server
                if (rs.next()) {
                    return rs.getInt(1); // Lấy giá trị đếm ở cột thứ nhất
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận lỗi khi đếm số lượng người dùng
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Phương thức cập nhật thông tin cá nhân của người dùng.
     * @param user Đối tượng người dùng chứa thông tin mới
     * @return true nếu cập nhật thành công
     */
    @Override // Ghi đè phương thức updateUser từ interface UserDAO
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name = ?, phone = ?, gender = ?, dob = ?, address = ?, display_order = ? "
                   + "WHERE user_id = ?";

        // Sử dụng try-with-resources đóng tài nguyên sau khi cập nhật
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị câu lệnh UPDATE qua JDBC

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getGender());
            ps.setDate(4, user.getDob());
            ps.setString(5, user.getAddress());
            ps.setInt(6, user.getDisplayOrder());
            ps.setInt(7, user.getUserId());

            // Gọi phương thức executeUpdate để thực hiện sửa đổi dữ liệu
            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            // In ngoại lệ khi cập nhật thông tin người dùng thất bại
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức cập nhật mật khẩu mới đã băm cho người dùng.
     * @param userId Mã người dùng
     * @param newHashedPassword Mật khẩu mới đã được băm an toàn
     * @return true nếu cập nhật thành công
     */
    @Override // Ghi đè phương thức updatePassword từ interface UserDAO
    public boolean updatePassword(int userId, String newHashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        // Sử dụng try-with-resources để tự động giải phóng kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // Chuẩn bị câu lệnh cập nhật mật khẩu

            ps.setString(1, newHashedPassword);
            ps.setInt(2, userId);

            // Gọi phương thức executeUpdate từ PreparedStatement của JDBC
            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận lỗi đổi mật khẩu
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức phụ trợ ánh xạ từng dòng của ResultSet sang đối tượng User.
     * @param rs Tập kết quả ResultSet đang ở dòng dữ liệu hợp lệ
     * @return Đối tượng User đã được điền dữ liệu
     * @throws SQLException khi trích xuất cột gặp lỗi
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();

        // Trích xuất các trường từ cột ResultSet của JDBC
        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setRoleId(rs.getInt("role_id"));
        user.setGender(rs.getString("gender"));
        user.setDob(rs.getDate("dob"));
        user.setAddress(rs.getString("address"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setDisplayOrder(rs.getInt("display_order"));

        try {
            // Đọc thêm tên vai trò nếu có trong câu truy vấn JOIN
            user.setRoleName(rs.getString("role_name"));

        } catch (SQLException ignored) {
            // Bỏ qua nếu cột role_name không được chọn trong câu SELECT
        }

        return user;
    }
}
