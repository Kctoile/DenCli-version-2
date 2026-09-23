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
public class SettingsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Cấu hình runtime lưu trữ trong bộ nhớ (fallback cấu hình mặc định)
    private static final Map<String, String> SETTINGS = new HashMap<>();

    static {
        SETTINGS.put("clinic_name", "Nha Khoa DenCli Dental Clinic");
        SETTINGS.put("hotline", "1900 6868");
        SETTINGS.put("email", "contact@dencli.com");
        SETTINGS.put("address", "123 Đường Nguyễn Tri Phương, Quận 10, TP.HCM");
        SETTINGS.put("open_hours", "08:00 - 20:00 (Thứ 2 - Chủ Nhật)");
        SETTINGS.put("auto_cancel_hours", "24");
        SETTINGS.put("notify_email", "true");
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

        String clinicName = request.getParameter("clinic_name");
        String hotline = request.getParameter("hotline");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String openHours = request.getParameter("open_hours");
        String autoCancelHours = request.getParameter("auto_cancel_hours");
        String notifyEmail = request.getParameter("notify_email") != null ? "true" : "false";

        if (clinicName != null && !clinicName.isBlank()) SETTINGS.put("clinic_name", clinicName.trim());
        if (hotline != null && !hotline.isBlank()) SETTINGS.put("hotline", hotline.trim());
        if (email != null && !email.isBlank()) SETTINGS.put("email", email.trim());
        if (address != null && !address.isBlank()) SETTINGS.put("address", address.trim());
        if (openHours != null && !openHours.isBlank()) SETTINGS.put("open_hours", openHours.trim());
        if (autoCancelHours != null && !autoCancelHours.isBlank()) SETTINGS.put("auto_cancel_hours", autoCancelHours.trim());
        SETTINGS.put("notify_email", notifyEmail);

        request.setAttribute("settings", SETTINGS);
        request.setAttribute("successMessage", "Cập nhật cấu hình hệ thống thành công!");
        request.getRequestDispatcher("/admin/settings.jsp").forward(request, response);
    }
}
