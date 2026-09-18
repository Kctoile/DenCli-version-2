/**
 * File: ReceptionServlet.java
 * Package: com.devjava.dencli.controller.staff
 * Mục đích: Servlet cho nhân viên lễ tân tiếp đón bệnh nhân, phân phòng và điều phối trạng thái lịch hẹn (UC-09, UC-10).
 */
package com.devjava.dencli.controller.staff;

import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.User;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.DateUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "StaffReceptionServlet", urlPatterns = {"/staff/reception"})
public class ReceptionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();
    private static final String JSON_CONTENT_TYPE = Constants.CONTENT_TYPE_JSON + ";charset=UTF-8";
    private static final String INVALID_APPOINTMENT_ACTION_MESSAGE = "Thao tác thất bại hoặc lịch hẹn không ở trạng thái hợp lệ.";

    /**
     * GET /staff/reception: Lấy danh sách lịch hẹn toàn hệ thống phục vụ tiếp đón và check-in.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AppointmentService appointmentService = ServiceFactory.getAppointmentService();
        String status = request.getParameter("status");
        String dateParam = request.getParameter("date");
        Date date = (dateParam != null && !dateParam.trim().isEmpty()) ? DateUtil.parseDate(dateParam) : null;

        List<Appointment> appointments = appointmentService.getAllAppointments(status, date, 1, 50);

        request.setAttribute("appointments", appointments);
        request.setAttribute("selectedStatus", status);
        request.setAttribute("selectedDate", dateParam);

        request.getRequestDispatcher("/staff/reception.jsp").forward(request, response);
    }

    /**
     * POST /staff/reception: Xử lý các hành động check-in, xác nhận hoặc hủy lịch hẹn.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        boolean isJson = (contentType != null && contentType.contains(Constants.CONTENT_TYPE_JSON))
                || Constants.CONTENT_TYPE_JSON.equalsIgnoreCase(request.getHeader("Accept"));

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        if (currentUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            if (isJson) {
                response.setContentType(JSON_CONTENT_TYPE);
                Map<String, Object> err = new HashMap<>();
                err.put("success", false);
                err.put("message", "Vui lòng đăng nhập.");
                response.getWriter().print(gson.toJson(err));
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
            return;
        }

        String action = null;
        int appointmentId = 0;
        String room = null;

        if (isJson) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            Map<?, ?> reqMap = gson.fromJson(sb.toString(), Map.class);
            if (reqMap != null) {
                action = (String) reqMap.get("action");
                Object appObj = reqMap.get("appointment_id");
                if (appObj != null) {
                    appointmentId = ((Number) appObj).intValue();
                }
                room = (String) reqMap.get("room");
            }
        } else {
            action = request.getParameter("action");
            String appStr = request.getParameter("appointment_id");
            if (appStr != null && !appStr.trim().isEmpty()) {
                try {
                    appointmentId = Integer.parseInt(appStr.trim());
                } catch (NumberFormatException ignored) {
                    // AppointmentId không hợp lệ sẽ được kiểm tra ở validate
                }
            }
            room = request.getParameter("room");
        }

        if (action == null || appointmentId <= 0) {
            handleError(response, isJson, request, "Thiếu thông tin hành động hoặc mã cuộc hẹn.");
            return;
        }

        AppointmentService appointmentService = ServiceFactory.getAppointmentService();
        boolean success = false;
        String msg = "";

        switch (action.toLowerCase()) {
            case "checkin":
                if (room == null || room.trim().isEmpty()) {
                    room = "Phòng 01";
                }
                success = appointmentService.checkInAppointment(appointmentId, room.trim());
                msg = "Tiếp đón và phân bổ " + room.trim() + " thành công!";
                break;

            case "confirm":
                success = appointmentService.confirmAppointment(appointmentId);
                msg = "Xác nhận lịch hẹn #" + appointmentId + " thành công!";
                break;

            case "cancel":
                success = appointmentService.cancelAppointment(appointmentId, currentUser.getUserId(), currentUser.getRoleId());
                msg = "Hủy lịch hẹn #" + appointmentId + " thành công!";
                break;

            default:
                handleError(response, isJson, request, "Hành động không hợp lệ.");
                return;
        }

        if (success) {
            if (isJson) {
                response.setContentType(JSON_CONTENT_TYPE);
                Map<String, Object> resData = new HashMap<>();
                resData.put("success", true);
                resData.put("message", msg);
                response.getWriter().print(gson.toJson(resData));
            } else {
                request.getSession().setAttribute(Constants.SESSION_SUCCESS_MESSAGE, msg);
                response.sendRedirect(request.getContextPath() + "/staff/reception");
            }
        } else {
            handleError(response, isJson, request, INVALID_APPOINTMENT_ACTION_MESSAGE);
        }
    }

    private void handleError(HttpServletResponse response, boolean isJson, HttpServletRequest request, String message)
            throws ServletException, IOException {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(JSON_CONTENT_TYPE);
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", message);
            response.getWriter().print(gson.toJson(err));
        } else {
            request.setAttribute("errorMessage", message);
            doGet(request, response);
        }
    }
}
