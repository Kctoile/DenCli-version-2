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

    private static final long serialVersionUID = 1L;
    private static final String INVALID_ACTION_MESSAGE = "Thao tác thất bại hoặc lịch hẹn không ở trạng thái hợp lệ.";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        try {
            // 1. Kiểm tra xác thực phiên đăng nhập
            HttpSession session = request.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

            if (currentUser == null) {
                sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, false, "Yêu cầu đăng nhập trước khi thực hiện.");
                return;
            }

            String action = request.getParameter("action");
            String appIdStr = request.getParameter("appointment_id");

            if (action == null || appIdStr == null) {
                sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, false, "Thiếu tham số hành động hoặc mã lịch hẹn.");
                return;
            }

            int appointmentId;
            try {
                appointmentId = Integer.parseInt(appIdStr.trim());
            } catch (NumberFormatException e) {
                sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, false, "Mã lịch hẹn không hợp lệ.");
                return;
            }

            executeAction(request, response, currentUser, action.toLowerCase(), appointmentId);
        } catch (IOException e) {
            throw new ServletException("Failed to write appointment action response.", e);
        }
    }

    private void executeAction(HttpServletRequest request, HttpServletResponse response,
                               User currentUser, String action, int appointmentId) throws IOException {
        switch (action) {
            case "checkin":
                handleCheckIn(request, response, currentUser, appointmentId);
                break;
            case "complete":
                handleComplete(response, currentUser, appointmentId);
                break;
            case "cancel":
                handleCancel(response, currentUser, appointmentId);
                break;
            default:
                sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, false, "Hành động không được hỗ trợ.");
                break;
        }
    }

    private void handleCheckIn(HttpServletRequest request, HttpServletResponse response,
                               User currentUser, int appointmentId) throws IOException {
        if (currentUser.getRoleId() != Constants.ROLE_STAFF_ID && currentUser.getRoleId() != Constants.ROLE_ADMIN_ID) {
            sendJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, false, "Chỉ lễ tân mới được thực hiện check-in.");
            return;
        }
        String room = request.getParameter("room");
        if (room == null || room.trim().isEmpty()) {
            room = "Phòng 01";
        }
        AppointmentService service = ServiceFactory.getAppointmentService();
        boolean success = service.checkInAppointment(appointmentId, room.trim());
        if (success) {
            sendJsonResponse(response, HttpServletResponse.SC_OK, true, "Tiếp đón và phân phòng khám thành công!");
        } else {
            sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, false, INVALID_ACTION_MESSAGE);
        }
    }

    private void handleComplete(HttpServletResponse response, User currentUser, int appointmentId) throws IOException {
        if (currentUser.getRoleId() != Constants.ROLE_DOCTOR_ID && currentUser.getRoleId() != Constants.ROLE_ADMIN_ID) {
            sendJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, false, "Chỉ bác sĩ phụ trách mới được hoàn thành ca khám.");
            return;
        }
        AppointmentService service = ServiceFactory.getAppointmentService();
        boolean success = service.completeAppointment(appointmentId);
        if (success) {
            sendJsonResponse(response, HttpServletResponse.SC_OK, true, "Đã cập nhật hoàn thành ca khám!");
        } else {
            sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, false, INVALID_ACTION_MESSAGE);
        }
    }

    private void handleCancel(HttpServletResponse response, User currentUser, int appointmentId) throws IOException {
        AppointmentService service = ServiceFactory.getAppointmentService();
        boolean success = service.cancelAppointment(appointmentId, currentUser.getUserId(), currentUser.getRoleId());
        if (success) {
            sendJsonResponse(response, HttpServletResponse.SC_OK, true, "Đã hủy lịch hẹn thành công!");
        } else {
            sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, false, INVALID_ACTION_MESSAGE);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, int status, boolean success, String message) throws IOException {
        response.setStatus(status);
        PrintWriter out = response.getWriter();
        out.print("{\"success\":" + success + ",\"message\":\"" + message + "\"}");
    }
}
