package com.devjava.dencli.controller;

import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API lấy danh sách các khung giờ đã được đặt của một bác sĩ theo ngày.
 * Phục vụ đồng bộ trạng thái disabled trên giao diện đặt lịch khám.
 */
@WebServlet(name = "AppointmentSlotServlet", urlPatterns = {"/api/appointments/booked-slots"})
public class AppointmentSlotServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");

        String doctorIdParam = request.getParameter("doctorId");
        String dateParam = request.getParameter("date");

        Map<String, Object> result = new HashMap<>();

        if (doctorIdParam == null || dateParam == null || doctorIdParam.trim().isEmpty() || dateParam.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "Thiếu tham số doctorId hoặc date");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(gson.toJson(result));
            return;
        }

        try {
            int doctorId = Integer.parseInt(doctorIdParam.trim());
            AppointmentService appointmentService = ServiceFactory.getAppointmentService();
            List<String> bookedSlots = appointmentService.getBookedTimeSlots(doctorId, dateParam.trim());

            result.put("success", true);
            result.put("doctorId", doctorId);
            result.put("date", dateParam.trim());
            result.put("bookedSlots", bookedSlots);
            response.getWriter().print(gson.toJson(result));
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "doctorId không hợp lệ");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(gson.toJson(result));
        }
    }
}
