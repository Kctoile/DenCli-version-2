/**
 * File: RevenueReportServlet.java
 * Package: com.devjava.dencli.controller.admin
 * Mục đích: API endpoint trả về dữ liệu JSON biểu đồ doanh thu 12 tháng phục vụ Chart.js (UC-17).
 */
package com.devjava.dencli.controller.admin;

import com.devjava.dencli.model.dto.RevenueDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.devjava.dencli.util.Constants;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminRevenueReportServlet", urlPatterns = {"/admin/api/revenue"})
public class RevenueReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();

    /**
     * GET /admin/api/revenue: Trả về danh sách doanh thu 12 tháng theo định dạng JSON.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");

        String yearStr = request.getParameter("year");
        int year = LocalDate.now(ZoneId.of(Constants.DEFAULT_TIMEZONE)).getYear();

        if (yearStr != null && !yearStr.trim().isEmpty()) {
            try {
                year = Integer.parseInt(yearStr.trim());
            } catch (NumberFormatException ignored) {
                // Sử dụng năm hiện tại làm mặc định nếu tham số không hợp lệ
            }
        }

        AppointmentService appointmentService = ServiceFactory.getAppointmentService();
        List<RevenueDTO> revenueList = appointmentService.getMonthlyRevenueReport(year);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("year", year);
        result.put("data", revenueList);

        response.getWriter().print(gson.toJson(result));
    }
}
