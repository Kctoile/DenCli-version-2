/**
 * File: UserServiceTest.java
 * Mục đích: Unit test kiểm thử các kịch bản nghiệp vụ của UserService bằng JUnit 5 và Mockito.
 */
package com.devjava.dencli.service;

import com.devjava.dencli.dao.UserDAO;
import com.devjava.dencli.model.User;
import com.devjava.dencli.service.impl.UserServiceImpl;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserDAO userDAO;
    private UserService userService;

    /**
     * Phương thức thiết lập môi trường và mock objects trước mỗi test case.
     */
    @BeforeEach
    public void setUp() {
        userDAO = mock(UserDAO.class);
        userService = new UserServiceImpl(userDAO);
    }

    /**
     * Kiểm thử đăng nhập thành công khi mật khẩu chính xác.
     */
    @Test
    public void testLoginSuccess() {
        String email = "doctor@gmail.com";
        String rawPassword = "DoctorPassword123";
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        User mockUser = new User();
        mockUser.setUserId(2);
        mockUser.setEmail(email);
        mockUser.setPassword(hashedPassword);
        mockUser.setRoleId(Constants.ROLE_DOCTOR_ID);

        when(userDAO.getUserByEmailOrPhone(email)).thenReturn(mockUser);

        User result = userService.login(email, rawPassword);

        assertNotNull(result);
        assertEquals(2, result.getUserId());
        assertEquals(Constants.ROLE_DOCTOR_ID, result.getRoleId());
    }

    /**
     * Kiểm thử đăng nhập thất bại khi nhập sai mật khẩu.
     */
    @Test
    public void testLoginWrongPassword() {
        String email = "doctor@gmail.com";
        String rawPassword = "DoctorPassword123";
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(hashedPassword);

        when(userDAO.getUserByEmailOrPhone(email)).thenReturn(mockUser);

        User result = userService.login(email, "WrongPassword");

        assertNull(result);
    }

    /**
     * Kiểm thử đăng ký bệnh nhân thành công với dữ liệu hợp lệ.
     */
    @Test
    public void testRegisterCustomerSuccess() {
        User newUser = new User();
        newUser.setFullName("Nguyen Van A");
        newUser.setPhone("0905123456");
        newUser.setEmail("nguyenvana@gmail.com");

        when(userDAO.checkPhoneExists("0905123456")).thenReturn(false);
        when(userDAO.checkEmailExists("nguyenvana@gmail.com")).thenReturn(false);
        when(userDAO.insertUser(any(User.class))).thenReturn(true);

        boolean registered = userService.registerCustomer(newUser, "SecurePass123");

        assertTrue(registered);
        assertEquals(Constants.ROLE_CUSTOMER_ID, newUser.getRoleId());
        assertNotNull(newUser.getPassword());
    }

    /**
     * Kiểm thử đăng ký thất bại khi số điện thoại đã tồn tại.
     */
    @Test
    public void testRegisterCustomerDuplicatePhone() {
        User newUser = new User();
        newUser.setFullName("Nguyen Van B");
        newUser.setPhone("0905999888");

        when(userDAO.checkPhoneExists("0905999888")).thenReturn(true);

        boolean registered = userService.registerCustomer(newUser, "SecurePass123");

        assertFalse(registered);
        verify(userDAO, never()).insertUser(any(User.class));
    }
}
