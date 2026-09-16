/**
 * File: UserServiceImpl.java
 * Mục đích: Triển khai các phương thức xử lý nghiệp vụ người dùng, xác thực và phân quyền tài khoản.
 */
package com.devjava.dencli.service.impl;

import com.devjava.dencli.dao.UserDAO;
import com.devjava.dencli.dao.impl.UserDAOImpl;
import com.devjava.dencli.model.User;
import com.devjava.dencli.service.UserService;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.PasswordUtil;
import java.util.Collections;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    // Constructor mặc định khởi tạo đối tượng UserDAOImpl
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    // Constructor cho phép tiêm phụ thuộc UserDAO phục vụ viết Unit Test
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Phương thức xác thực thông tin đăng nhập của người dùng qua email hoặc số điện thoại.
     * @param emailOrPhone Chuỗi email hoặc số điện thoại
     * @param rawPassword Mật khẩu chưa mã hóa
     * @return Đối tượng User nếu mật khẩu khớp, ngược lại trả về null
     */
    @Override // Ghi đè phương thức login từ interface UserService
    public User login(String emailOrPhone, String rawPassword) {
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty() || rawPassword == null || rawPassword.trim().isEmpty()) {
            return null;
        }

        // Tìm kiếm người dùng trong cơ sở dữ liệu qua UserDAO
        User user = userDAO.getUserByEmailOrPhone(emailOrPhone.trim());

        if (user == null) {
            return null;
        }

        // Gọi hàm kiểm tra mật khẩu bằng thuật toán BCrypt từ PasswordUtil
        boolean isMatch = PasswordUtil.checkPassword(rawPassword, user.getPassword());

        if (isMatch) {
            return user;
        }

        return null;
    }

    /**
     * Phương thức kiểm tra tính hợp lệ và đăng ký tài khoản mới cho bệnh nhân.
     * @param user Đối tượng người dùng
     * @param rawPassword Mật khẩu thô cần băm
     * @return true nếu đăng ký thành công
     */
    @Override // Ghi đè phương thức registerCustomer từ interface UserService
    public boolean registerCustomer(User user, String rawPassword) {
        if (user == null || rawPassword == null || rawPassword.trim().length() < 6) {
            return false;
        }

        // Kiểm tra tính bắt buộc của họ tên và số điện thoại
        if (user.getFullName() == null || user.getFullName().trim().isEmpty() ||
            user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            return false;
        }

        // Kiểm tra xem số điện thoại đã được đăng ký trước đó chưa
        if (userDAO.checkPhoneExists(user.getPhone().trim())) {
            return false;
        }

        // Nếu người dùng có nhập email thì kiểm tra email trùng lặp
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            if (userDAO.checkEmailExists(user.getEmail().trim())) {
                return false;
            }
        }

        // Gọi hàm băm mật khẩu BCrypt từ lớp tiện ích PasswordUtil
        String hashedPassword = PasswordUtil.hashPassword(rawPassword.trim());
        user.setPassword(hashedPassword);

        // Gán vai trò mặc định cho tài khoản đăng ký trực tuyến là Bệnh nhân (CUSTOMER)
        user.setRoleId(Constants.ROLE_CUSTOMER_ID);

        // Gọi hàm insertUser của tầng DAO để ghi dữ liệu vào CSDL
        return userDAO.insertUser(user);
    }

    /**
     * Phương thức lấy thông tin người dùng theo mã định danh user_id.
     * @param userId Mã người dùng
     * @return Đối tượng User
     */
    @Override // Ghi đè phương thức getUserById từ interface UserService
    public User getUserById(int userId) {
        if (userId <= 0) {
            return null;
        }

        return userDAO.getUserById(userId);
    }

    /**
     * Phương thức lấy danh sách toàn bộ bác sĩ nha khoa trong hệ thống.
     * @return Danh sách bác sĩ
     */
    @Override // Ghi đè phương thức getDoctors từ interface UserService
    public List<User> getDoctors() {
        return userDAO.getDoctors();
    }

    /**
     * Phương thức lấy danh sách người dùng theo vai trò có phân trang.
     * @param roleId Mã vai trò
     * @param page Trang hiện tại
     * @param pageSize Số lượng bản ghi trên một trang
     * @return Danh sách người dùng
     */
    @Override // Ghi đè phương thức getUsersByRole từ interface UserService
    public List<User> getUsersByRole(int roleId, int page, int pageSize) {
        int validPage = (page <= 0) ? Constants.DEFAULT_PAGE_NUMBER : page;
        int validPageSize = (pageSize <= 0) ? Constants.DEFAULT_PAGE_SIZE : pageSize;

        // Tính toán số bản ghi cần bỏ qua cho phân trang SQL Server
        int offset = (validPage - 1) * validPageSize;

        return userDAO.getUsersByRole(roleId, offset, validPageSize);
    }

    /**
     * Phương thức đếm tổng số lượng người dùng theo vai trò phục vụ tính tổng số trang.
     * @param roleId Mã vai trò
     * @return Tổng số lượng bản ghi
     */
    @Override // Ghi đè phương thức countUsersByRole từ interface UserService
    public int countUsersByRole(int roleId) {
        return userDAO.countUsersByRole(roleId);
    }

    /**
     * Phương thức cập nhật thông tin hồ sơ của người dùng.
     * @param user Đối tượng người dùng chứa thông tin mới
     * @return true nếu cập nhật thành công
     */
    @Override // Ghi đè phương thức updateProfile từ interface UserService
    public boolean updateProfile(User user) {
        if (user == null || user.getUserId() <= 0) {
            return false;
        }

        return userDAO.updateUser(user);
    }

    /**
     * Phương thức thay đổi mật khẩu người dùng với cơ chế xác thực mật khẩu cũ.
     * @param userId Mã người dùng
     * @param oldPassword Mật khẩu hiện tại
     * @param newPassword Mật khẩu mới mong muốn
     * @return true nếu đổi thành công, ngược lại false
     */
    @Override // Ghi đè phương thức changePassword từ interface UserService
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        if (userId <= 0 || oldPassword == null || newPassword == null || newPassword.trim().length() < 6) {
            return false;
        }

        User user = userDAO.getUserById(userId);
        if (user == null) {
            return false;
        }

        // Xác thực mật khẩu cũ bằng BCrypt
        boolean isOldMatch = PasswordUtil.checkPassword(oldPassword, user.getPassword());
        if (!isOldMatch) {
            return false;
        }

        // Băm mật khẩu mới và lưu vào cơ sở dữ liệu
        String hashedNewPassword = PasswordUtil.hashPassword(newPassword.trim());

        return userDAO.updatePassword(userId, hashedNewPassword);
    }
}
