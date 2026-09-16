/**
 * File: PasswordUtil.java
 * Mục đích: Cung cấp các phương thức băm mật khẩu và kiểm tra mật khẩu sử dụng thư viện jBCrypt.
 */
package com.devjava.dencli.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    // Khởi tạo constructor private để ngăn tạo đối tượng từ lớp tiện ích này
    private PasswordUtil() {
    }

    /**
     * Phương thức thực hiện băm mật khẩu thô thành chuỗi băm an toàn bằng thuật toán BCrypt.
     * @param plainPassword Mật khẩu chưa mã hóa từ người dùng
     * @return Chuỗi mật khẩu đã được băm an toàn
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            return null;
        }

        // Tạo chuỗi salt ngẫu nhiên từ thư viện BCrypt với độ mạnh mặc định
        String salt = BCrypt.gensalt(12);

        // Gọi hàm hashpw từ thư viện BCrypt để băm mật khẩu với salt vừa tạo
        return BCrypt.hashpw(plainPassword, salt);
    }

    /**
     * Phương thức kiểm tra mật khẩu thô người dùng nhập vào có khớp với chuỗi băm trong cơ sở dữ liệu hay không.
     * Hỗ trợ tương thích ngược với dữ liệu mẫu khởi tạo (như '123', 'admin' trong file SQL) và tự động băm BCrypt.
     * @param plainPassword Mật khẩu thô do người dùng nhập
     * @param hashedPassword Mật khẩu đã băm (hoặc plain-text mẫu) lưu trong cơ sở dữ liệu
     * @return true nếu mật khẩu trùng khớp, ngược lại false
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        // 1. Kiểm tra nếu mật khẩu trong CSDL tuân thủ định dạng chuỗi băm của BCrypt ($2a$, $2b$, $2y$)
        if (hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$") || hashedPassword.startsWith("$2y$")) {
            try {
                // Gọi hàm checkpw từ thư viện BCrypt để so sánh
                return BCrypt.checkpw(plainPassword, hashedPassword);

            } catch (Exception e) {
                // Ghi nhận lỗi nếu chuỗi băm bị lỗi định dạng
                return false;
            }
        }

        // 2. Hỗ trợ tương thích ngược cho dữ liệu hạt giống ban đầu (seed data từ file SQL chưa kịp băm)
        return plainPassword.equals(hashedPassword);
    }
}
