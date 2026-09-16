/**
 * File: DateUtil.java
 * Mục đích: Tiện ích xử lý và chuyển đổi kiểu dữ liệu ngày, giờ (Date, Time, Timestamp) cho ứng dụng.
 */
package com.devjava.dencli.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class DateUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String TIME_SHORT_FORMAT = "HH:mm";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Khởi tạo constructor private để ngăn tạo đối tượng từ lớp tiện ích này
    private DateUtil() {
    }

    /**
     * Phương thức chuyển đổi chuỗi ngày định dạng yyyy-MM-dd sang đối tượng java.sql.Date.
     * @param dateStr Chuỗi ngày tháng cần chuyển đổi
     * @return Đối tượng java.sql.Date, hoặc null nếu chuỗi không hợp lệ
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            // Khởi tạo đối tượng định dạng SimpleDateFormat
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            sdf.setLenient(false);

            // Gọi hàm parse từ thư viện Java để trích xuất ngày
            java.util.Date parsed = sdf.parse(dateStr.trim());

            return new Date(parsed.getTime());

        } catch (ParseException e) {
            // Định dạng ngày không hợp lệ, trả về null theo thiết kế
            return null;
        }
    }

    /**
     * Phương thức chuyển đổi chuỗi giờ định dạng HH:mm hoặc HH:mm:ss sang java.sql.Time.
     * @param timeStr Chuỗi giờ cần chuyển đổi
     * @return Đối tượng java.sql.Time, hoặc null nếu không hợp lệ
     */
    public static Time parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }

        String trimmed = timeStr.trim();

        // Kiểm tra nếu chỉ có định dạng HH:mm thì bổ sung :00
        if (trimmed.length() == 5) {
            trimmed = trimmed + ":00";
        }

        try {
            // Khởi tạo SimpleDateFormat để phân tích giờ phút giây
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
            sdf.setLenient(false);

            // Gọi hàm parse từ thư viện Java
            java.util.Date parsed = sdf.parse(trimmed);

            return new Time(parsed.getTime());

        } catch (ParseException e) {
            // Định dạng giờ không hợp lệ, trả về null theo thiết kế
            return null;
        }
    }

    /**
     * Phương thức chuyển đổi chuỗi ngày giờ sang java.sql.Timestamp.
     * @param dateTimeStr Chuỗi ngày giờ (yyyy-MM-dd HH:mm:ss)
     * @return Đối tượng Timestamp tương ứng, hoặc null nếu không hợp lệ
     */
    public static Timestamp parseTimestamp(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }

        try {
            // Gọi hàm parse của SimpleDateFormat để chuyển chuỗi sang java.util.Date
            SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
            java.util.Date parsed = sdf.parse(dateTimeStr.trim());

            return new Timestamp(parsed.getTime());

        } catch (ParseException e) {
            // Định dạng ngày giờ không hợp lệ, trả về null theo thiết kế
            return null;
        }
    }

    /**
     * Phương thức lấy thời điểm hiện tại dưới dạng java.sql.Timestamp.
     * @return Timestamp thời điểm hiện tại của hệ thống
     */
    public static Timestamp getCurrentTimestamp() {
        // Lấy thời gian hiện tại từ hệ thống Java
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Phương thức định dạng đối tượng java.sql.Date sang chuỗi hiển thị yyyy-MM-dd.
     * @param date Đối tượng ngày
     * @return Chuỗi ngày tháng hoặc rỗng nếu date null
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }

        // Định dạng ngày bằng SimpleDateFormat của Java
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }
}
