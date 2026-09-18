/**
 * File: Constants.java
 * Mục đích: Chứa toàn bộ các hằng số hệ thống (vai trò, trạng thái, kết nối CSDL, cấu hình phân trang).
 */
package com.devjava.dencli.util;

public final class Constants {

    // Khởi tạo constructor private để ngăn chặn việc tạo đối tượng từ lớp tiện ích này
    private Constants() {
    }

    // --- CẤU HÌNH CƠ SỞ DỮ LIỆU ---
    public static final String DB_HOST = getConfig("DENCLI_DB_HOST", "localhost");
    public static final String DB_PORT = getConfig("DENCLI_DB_PORT", "1433");
    public static final String DB_NAME = getConfig("DENCLI_DB_NAME", "Dental");
    public static final String DB_USER = getConfig("DENCLI_DB_USER", "sa");
    public static final String DB_PASSWORD = getConfig("DENCLI_DB_PASSWORD", "");

    private static String getConfig(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(key);
        }
        return value == null || value.isBlank() ? defaultValue : value;
    }

    // --- VAI TRÒ NGƯỜI DÙNG (ROLES) ---
    public static final int ROLE_ADMIN_ID = 1;
    public static final int ROLE_DOCTOR_ID = 2;
    public static final int ROLE_STAFF_ID = 4;
    public static final int ROLE_CUSTOMER_ID = 5;

    public static final String ROLE_ADMIN_NAME = "ADMIN";
    public static final String ROLE_DOCTOR_NAME = "DOCTOR";
    public static final String ROLE_STAFF_NAME = "STAFF";
    public static final String ROLE_CUSTOMER_NAME = "CUSTOMER";

    // --- TRẠNG THÁI LỊCH HẸN (APPOINTMENT STATUS) ---
    public static final String APPOINTMENT_PENDING = "Pending";
    public static final String APPOINTMENT_CONFIRMED = "Confirmed";
    public static final String APPOINTMENT_CHECKED_IN = "Checked In";
    public static final String APPOINTMENT_COMPLETED = "Completed";
    public static final String APPOINTMENT_CANCELLED = "Cancelled";

    // --- TRẠNG THÁI DỊCH VỤ ĐƯỢC CHỈ ĐỊNH (PRESCRIBED SERVICES STATUS) ---
    public static final String PRESCRIBED_PENDING = "Pending";
    public static final String PRESCRIBED_COMPLETED = "Completed";

    // --- CẤU HÌNH PHÂN TRANG (PAGINATION) ---
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 1;

    // --- CẤU HÌNH PHIÊN LÀM VIỆC (SESSION KEYS) ---
    public static final String SESSION_USER = "user";
    public static final String SESSION_ROLE = "role";
    public static final String SESSION_ERROR_MESSAGE = "errorMessage";
    public static final String SESSION_SUCCESS_MESSAGE = "successMessage";

    // --- ĐỊNH DẠNG VÀ MÚI GIỜ (CONTENT TYPE & TIMEZONE) ---
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String DEFAULT_TIMEZONE = "Asia/Ho_Chi_Minh";
}
