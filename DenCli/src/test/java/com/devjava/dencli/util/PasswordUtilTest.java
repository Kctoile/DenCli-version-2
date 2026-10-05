/**
 * File: PasswordUtilTest.java
 * Mục đích: Unit test kiểm thử tính đúng đắn của việc băm mật khẩu và kiểm tra mật khẩu bằng jBCrypt.
 */
package com.devjava.dencli.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PasswordUtilTest {

    /**
     * Phương thức kiểm thử việc băm mật khẩu và so khớp mật khẩu hợp lệ.
     */
    @Test
    public void testHashAndCheckPasswordValid() {
        String rawPassword = "SecurePassword@123";

        // Thực hiện băm mật khẩu
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        // Đảm bảo chuỗi băm không null và bắt đầu bằng định dạng chuẩn BCrypt ($2a$)
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$") || hashedPassword.startsWith("$2y$"));

        // Kiểm tra mật khẩu đúng phải trả về true
        assertTrue(PasswordUtil.checkPassword(rawPassword, hashedPassword));
    }

    /**
     * Phương thức kiểm thử việc kiểm tra mật khẩu sai phải trả về false.
     */
    @Test
    public void testCheckPasswordInvalid() {
        String rawPassword = "CorrectPassword";
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        // Kiểm tra mật khẩu sai phải trả về false
        assertFalse(PasswordUtil.checkPassword("WrongPassword", hashedPassword));
    }

    @Test
    public void testPlaintextPasswordIsRejected() {
        assertFalse(PasswordUtil.checkPassword("123", "123"));
    }

    @Test
    public void testHashedDevelopmentCredentialsRemainUsable() {
        assertTrue(PasswordUtil.checkPassword("admin", "$2a$12$/Tlf/YV6kktwrrFQQiInfuwva5foA/E7XiVcbF4U2uCWkxQHQVtCK"));
        assertTrue(PasswordUtil.checkPassword("123", "$2a$12$w75zjnNPy2pnMxZvFI5waetgOgpVw1.DsJatHY2pHHSwWqsyA0OI6"));
    }

    /**
     * Phương thức kiểm thử đầu vào rỗng hoặc null phải xử lý an toàn.
     */
    @Test
    public void testNullOrEmptyInput() {
        assertNull(PasswordUtil.hashPassword(null));
        assertNull(PasswordUtil.hashPassword("   "));
        assertFalse(PasswordUtil.checkPassword(null, "someHash"));
        assertFalse(PasswordUtil.checkPassword("pass", null));
    }
}
