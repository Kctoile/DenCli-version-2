/**
 * File: UserService.java
 * Mục đích: Interface định nghĩa các phương thức xử lý nghiệp vụ liên quan đến người dùng (đăng nhập, đăng ký, thông tin tài khoản).
 */
package com.devjava.dencli.service;

import com.devjava.dencli.model.User;
import java.util.List;

public interface UserService {

    /**
     * Phương thức xác thực thông tin đăng nhập của người dùng qua email/số điện thoại và mật khẩu.
     * @param emailOrPhone Địa chỉ email hoặc số điện thoại người dùng nhập
     * @param rawPassword Mật khẩu thô do người dùng nhập
     * @return Đối tượng User nếu đăng nhập thành công, hoặc null nếu thông tin không chính xác
     */
    User login(String emailOrPhone, String rawPassword);

    /**
     * Phương thức đăng ký tài khoản mới cho bệnh nhân (vai trò CUSTOMER).
     * @param user Đối tượng chứa thông tin cá nhân của người dùng
     * @param rawPassword Mật khẩu thô cần được băm an toàn trước khi lưu trữ
     * @return true nếu đăng ký thành công, false nếu email/sđt đã tồn tại hoặc dữ liệu không hợp lệ
     */
    boolean registerCustomer(User user, String rawPassword);

    /**
     * Phương thức lấy thông tin người dùng theo mã định danh user_id.
     * @param userId Mã người dùng
     * @return Đối tượng User tương ứng
     */
    User getUserById(int userId);

    /**
     * Phương thức lấy danh sách tất cả các bác sĩ nha khoa đang hoạt động trong hệ thống.
     * @return Danh sách các bác sĩ
     */
    List<User> getDoctors();

    /**
     * Phương thức lấy danh sách người dùng theo vai trò có hỗ trợ phân trang.
     * @param roleId Mã vai trò (1: ADMIN, 2: DOCTOR, 4: STAFF, 5: CUSTOMER, 0: Tất cả)
     * @param page Số trang hiện tại (bắt đầu từ 1)
     * @param pageSize Số lượng bản ghi trên một trang
     * @return Danh sách người dùng của trang
     */
    List<User> getUsersByRole(int roleId, int page, int pageSize);

    /**
     * Phương thức đếm tổng số lượng người dùng theo vai trò.
     * @param roleId Mã vai trò cần đếm
     * @return Tổng số người dùng
     */
    int countUsersByRole(int roleId);

    /**
     * Phương thức cập nhật thông tin hồ sơ cá nhân của người dùng.
     * @param user Đối tượng người dùng chứa thông tin cập nhật
     * @return true nếu cập nhật thành công, ngược lại false
     */
    boolean updateProfile(User user);

    /**
     * Phương thức đổi mật khẩu tài khoản người dùng có xác thực mật khẩu cũ.
     * @param userId Mã người dùng
     * @param oldPassword Mật khẩu hiện tại
     * @param newPassword Mật khẩu mới mong muốn
     * @return true nếu đổi mật khẩu thành công, false nếu mật khẩu cũ không đúng
     */
    boolean changePassword(int userId, String oldPassword, String newPassword);
}
