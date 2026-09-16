/**
 * File: UserDAO.java
 * Mục đích: Interface định nghĩa các phương thức thao tác dữ liệu với bảng 'users' trong cơ sở dữ liệu.
 */
package com.devjava.dencli.dao;

import com.devjava.dencli.model.User;
import java.util.List;

public interface UserDAO {

    /**
     * Phương thức kiểm tra địa chỉ email đã tồn tại trong hệ thống hay chưa.
     * @param email Địa chỉ email cần kiểm tra
     * @return true nếu email đã tồn tại, ngược lại false
     */
    boolean checkEmailExists(String email);

    /**
     * Phương thức kiểm tra số điện thoại đã tồn tại trong hệ thống hay chưa.
     * @param phone Số điện thoại cần kiểm tra
     * @return true nếu số điện thoại đã tồn tại, ngược lại false
     */
    boolean checkPhoneExists(String phone);

    /**
     * Phương thức thêm mới một người dùng vào bảng users.
     * @param user Đối tượng người dùng cần lưu trữ
     * @return true nếu thêm thành công, ngược lại false
     */
    boolean insertUser(User user);

    /**
     * Phương thức tìm kiếm người dùng theo email hoặc số điện thoại phục vụ đăng nhập.
     * @param emailOrPhone Chuỗi email hoặc số điện thoại
     * @return Đối tượng User tìm thấy, hoặc null nếu không tồn tại
     */
    User getUserByEmailOrPhone(String emailOrPhone);

    /**
     * Phương thức tìm kiếm người dùng theo mã định danh (user_id).
     * @param userId Mã người dùng
     * @return Đối tượng User, hoặc null nếu không tìm thấy
     */
    User getUserById(int userId);

    /**
     * Phương thức lấy danh sách toàn bộ bác sĩ nha khoa (role_id = 2) đang hoạt động.
     * @return Danh sách các bác sĩ
     */
    List<User> getDoctors();

    /**
     * Phương thức lấy danh sách người dùng theo vai trò có hỗ trợ phân trang SQL Server.
     * @param roleId Mã vai trò người dùng
     * @param offset Số lượng bản ghi cần bỏ qua
     * @param limit Số lượng bản ghi tối đa trên một trang
     * @return Danh sách người dùng thỏa mãn
     */
    List<User> getUsersByRole(int roleId, int offset, int limit);

    /**
     * Phương thức đếm tổng số lượng người dùng theo một vai trò cụ thể.
     * @param roleId Mã vai trò cần đếm
     * @return Tổng số bản ghi người dùng
     */
    int countUsersByRole(int roleId);

    /**
     * Phương thức cập nhật thông tin cá nhân của người dùng.
     * @param user Đối tượng người dùng với thông tin mới
     * @return true nếu cập nhật thành công, ngược lại false
     */
    boolean updateUser(User user);

    /**
     * Phương thức cập nhật mật khẩu đã băm cho người dùng.
     * @param userId Mã người dùng
     * @param newHashedPassword Mật khẩu mới đã được băm BCrypt
     * @return true nếu cập nhật mật khẩu thành công, ngược lại false
     */
    boolean updatePassword(int userId, String newHashedPassword);
}
