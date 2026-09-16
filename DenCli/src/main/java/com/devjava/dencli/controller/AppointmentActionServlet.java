/**
 * File: AppointmentActionServlet.java
 * Package: com.devjava.dencli.controller
 * Mục đích: Servlet xử lý các thao tác điều phối lịch hẹn (Check-in, Hoàn thành, Hủy lịch)
 *           dành cho Nhân viên Lễ tân (STAFF), Bác sĩ (DOCTOR) và Quản trị viên (ADMIN).
 */
package com.devjava.dencli.controller;

import com.devjava.dencli.model.User;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AppointmentActionServlet", urlPatterns = {"/api/appointments/action"})
public class AppointmentActionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 1. Kiểm tra xác thực phiên đăng nhập
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        if (currentUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"success\":false,\"message\":\"Yêu cầu đăng nhập trước khi thực hiện.\"}");
            return;
        }

        String action = request.getParameter("action");
        String appIdStr = request.getParameter("appointment_id");

        if (action == null || appIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":false,\"message\":\"Thiếu tham số hành động hoặc mã lịch hẹn.\"}");
            return;
        }

        int appointmentId;
        try {
            appointmentId = Integer.parseInt(appIdStr.trim());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":false,\"message\":\"Mã lịch hẹn không hợp lệ.\"}");
            return;
        }

        AppointmentService service = ServiceFactory.getAppointmentService();
        boolean success = false;
        String successMsg = "Thao tác thành công!";

        switch (action.toLowerCase()) {
            case "checkin":
                // Chỉ Lễ tân (STAFF) hoặc ADMIN mới được check-in
                if (currentUser.getRoleId() != Constants.ROLE_STAFF_ID && currentUser.getRoleId() != Constants.ROLE_ADMIN_ID) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    out.print("{\"success\":false,\"message\":\"Chỉ lễ tân mới được thực hiện check-in.\"}");
                    return;
                }
                String room = request.getParameter("room");
                if (room == null || room.trim().isEmpty()) {
                    room = "Phòng 01";
                }
                success = service.checkInAppointment(appointmentId, room.trim());
                successMsg = "Tiếp đón và phân phòng khám thành công!";
                break;

            case "complete":
                // Bác sĩ (DOCTOR) hoặc ADMIN mới được hoàn thành khám
                if (currentUser.getRoleId() != Constants.ROLE_DOCTOR_ID && currentUser.getRoleId() != Constants.ROLE_ADMIN_ID) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    out.print("{\"success\":false,\"message\":\"Chỉ bác sĩ phụ trách mới được hoàn thành ca khám.\"}");
                    return;
                }
                success = service.completeAppointment(appointmentId);
                successMsg = "Đã cập nhật hoàn thành ca khám!";
                break;

            case "cancel":
                success = service.cancelAppointment(appointmentId, currentUser.getUserId(), currentUser.getRoleId());
                successMsg = "Đã hủy lịch hẹn thành công!";
                break;

            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\":false,\"message\":\"Hành động không được hỗ trợ.\"}");
                return;
        }

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.print("{\"success\":true,\"message\":\"" + successMsg + "\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":false,\"message\":\"Thao tác thất bại hoặc lịch hẹn không ở trạng thái hợp lệ.\"}");
        }
    }
}
