package com.devjava.dencli.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet quản lý cấu hình hệ thống phòng khám (UC-14 mở rộng).
 */
@WebServlet(name = "AdminSettingsServlet", urlPatterns = {"/admin/settings"})
@SuppressWarnings({"java:S1989", "squid:S1989"})
public class SettingsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String KEY_CLINIC_NAME = "clinic_name";
    public static final String KEY_HOTLINE = "hotline";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_OPEN_HOURS = "open_hours";
    public static final String KEY_AUTO_CANCEL_HOURS = "auto_cancel_hours";
    public static final String KEY_NOTIFY_EMAIL = "notify_email";

    // Cấu hình runtime lưu trữ trong bộ nhớ (fallback cấu hình mặc định)
    private static final Map<String, String> SETTINGS = new HashMap<>();

    static {
        SETTINGS.put(KEY_CLINIC_NAME, "Nha Khoa DenCli Dental Clinic");
        SETTINGS.put(KEY_HOTLINE, "1900 6868");
        SETTINGS.put(KEY_EMAIL, "contact@dencli.com");
        SETTINGS.put(KEY_ADDRESS, "123 Đường Nguyễn Tri Phương, Quận 10, TP.HCM");
        SETTINGS.put(KEY_OPEN_HOURS, "08:00 - 20:00 (Thứ 2 - Chủ Nhật)");
        SETTINGS.put(KEY_AUTO_CANCEL_HOURS, "24");
        SETTINGS.put(KEY_NOTIFY_EMAIL, "true");
    }

    public static Map<String, String> getSettings() {
        return SETTINGS;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("settings", SETTINGS);
        request.getRequestDispatcher("/admin/settings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String clinicName = request.getParameter(KEY_CLINIC_NAME);
        String hotline = request.getParameter(KEY_HOTLINE);
        String email = request.getParameter(KEY_EMAIL);
        String address = request.getParameter(KEY_ADDRESS);
        String openHours = request.getParameter(KEY_OPEN_HOURS);
        String autoCancelHours = request.getParameter(KEY_AUTO_CANCEL_HOURS);
        String notifyEmail = request.getParameter(KEY_NOTIFY_EMAIL) != null ? "true" : "false";

        if (clinicName != null && !clinicName.isBlank()) SETTINGS.put(KEY_CLINIC_NAME, clinicName.trim());
        if (hotline != null && !hotline.isBlank()) SETTINGS.put(KEY_HOTLINE, hotline.trim());
        if (email != null && !email.isBlank()) SETTINGS.put(KEY_EMAIL, email.trim());
        if (address != null && !address.isBlank()) SETTINGS.put(KEY_ADDRESS, address.trim());
        if (openHours != null && !openHours.isBlank()) SETTINGS.put(KEY_OPEN_HOURS, openHours.trim());
        if (autoCancelHours != null && !autoCancelHours.isBlank()) SETTINGS.put(KEY_AUTO_CANCEL_HOURS, autoCancelHours.trim());
        SETTINGS.put(KEY_NOTIFY_EMAIL, notifyEmail);

        request.setAttribute("settings", SETTINGS);
        request.setAttribute("successMessage", "Cập nhật cấu hình hệ thống thành công!");
        request.getRequestDispatcher("/admin/settings.jsp").forward(request, response);
    }
}
