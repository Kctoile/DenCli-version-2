/**
 * File: DashboardServlet.java
 * Package: com.devjava.dencli.controller.admin
 * Mục đích: Servlet tổng hợp dữ liệu thống kê tổng quan (Dashboard) cho Quản trị viên phòng khám (UC-14).
 */
package com.devjava.dencli.controller.admin;

import com.devjava.dencli.model.dto.RevenueDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.service.UserService;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class DashboardServlet extends HttpServlet {

    /**
     * GET /admin/dashboard: Thống kê số lượng người dùng, bác sĩ, nhân viên, lịch hẹn và doanh thu 12 tháng.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserService userService = ServiceFactory.getUserService();
        AppointmentService appointmentService = ServiceFactory.getAppointmentService();

        int totalUsers = userService.countUsersByRole(0);
        int totalDoctors = userService.countUsersByRole(Constants.ROLE_DOCTOR_ID);
        int totalStaff = userService.countUsersByRole(Constants.ROLE_STAFF_ID);
        int totalCustomers = userService.countUsersByRole(Constants.ROLE_CUSTOMER_ID);
        int totalAppointments = appointmentService.countAllAppointments(null, null);

        int currentYear = LocalDate.now().getYear();
        List<RevenueDTO> monthlyRevenues = appointmentService.getMonthlyRevenueReport(currentYear);

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalDoctors", totalDoctors);
        request.setAttribute("totalStaff", totalStaff);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("totalAppointments", totalAppointments);
        request.setAttribute("currentYear", currentYear);
        request.setAttribute("monthlyRevenues", monthlyRevenues);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
