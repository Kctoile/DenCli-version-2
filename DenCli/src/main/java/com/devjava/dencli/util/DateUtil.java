/**
 * File: DateUtil.java
 * Mục đích: Tiện ích xử lý và chuyển đổi kiểu dữ liệu ngày, giờ (Date, Time, Timestamp) dùng stdlib Java thuần.
 */
package com.devjava.dencli.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public final class DateUtil {

    private DateUtil() {
    }

    /**
     * Chuyển đổi chuỗi ngày định dạng yyyy-MM-dd sang java.sql.Date.
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return Date.valueOf(dateStr.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Chuyển đổi chuỗi giờ định dạng HH:mm hoặc HH:mm:ss sang java.sql.Time.
     */
    public static Time parseTime(String timeStr) {
        if (timeStr == null || timeStr.isBlank()) {
            return null;
        }
        String trimmed = timeStr.trim();
        if (trimmed.length() == 5) {
            trimmed = trimmed + ":00";
        }
        try {
            return Time.valueOf(trimmed);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Chuyển đổi chuỗi ngày giờ sang java.sql.Timestamp.
     */
    public static Timestamp parseTimestamp(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            return null;
        }
        try {
            return Timestamp.valueOf(dateTimeStr.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Lấy thời điểm hiện tại dưới dạng java.sql.Timestamp.
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Định dạng java.sql.Date sang chuỗi yyyy-MM-dd bằng stdlib toString().
     */
    public static String formatDate(Date date) {
        return date != null ? date.toString() : "";
    }
}
