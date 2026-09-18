/**
 * File: BookAppointmentServlet.java
 * Package: com.devjava.dencli.controller
 * Mục đích: Servlet xử lý yêu cầu đặt lịch hẹn trực tuyến của bệnh nhân (UC-05),
 *           sử dụng AppointmentService và ServiceFactory theo kiến trúc 3 tầng MVC.
 */
package com.devjava.dencli.controller;

import com.devjava.dencli.model.User;
import com.devjava.dencli.model.dto.BookingRequestDTO;
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
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "BookAppointmentServletV2", urlPatterns = {"/api/appointments/book"})
public class BookAppointmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Gson GSON = new Gson();
    private static final String INVALID_FORMAT_ERROR_CODE = "ERR_INVALID_FORMAT";

    /**
     * Tiếp nhận yêu cầu POST để đặt lịch hẹn khám bệnh trực tuyến.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Cấu hình phản hồi đầu ra là JSON và UTF-8
        response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        try {
            User currentUser = getCurrentCustomer(request, response);
            if (currentUser == null) {
                return;
            }

            BookingRequestDTO bookingDTO = parseRequest(request, response);
            if (bookingDTO == null) {
                return;
            }

            if (!validateBookingData(bookingDTO, response)) {
                return;
            }

            processBooking(bookingDTO, currentUser.getUserId(), response);
        } catch (IOException e) {
            throw new ServletException("Failed to process appointment booking request.", e);
        }
    }

    private User getCurrentCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        if (user == null) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Yêu cầu đăng nhập trước khi đặt lịch hẹn.", "ERR_UNAUTHORIZED");
            return null;
        }
        if (user.getRoleId() != Constants.ROLE_CUSTOMER_ID) {
            sendError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Chỉ bệnh nhân mới được phép đặt lịch hẹn trực tuyến.", "ERR_FORBIDDEN");
            return null;
        }
        return user;
    }

    private BookingRequestDTO parseRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains(Constants.CONTENT_TYPE_JSON)) {
            return parseJsonRequest(request, response);
        }
        return parseFormRequest(request, response);
    }

    private BookingRequestDTO parseJsonRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        try {
            return GSON.fromJson(sb.toString(), BookingRequestDTO.class);
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Dữ liệu JSON không đúng định dạng.", "ERR_INVALID_JSON");
            return null;
        }
    }

    private BookingRequestDTO parseFormRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BookingRequestDTO dto = new BookingRequestDTO();
        String docIdStr = request.getParameter("doctor_id");
        if (docIdStr != null && !docIdStr.trim().isEmpty()) {
            try {
                dto.setDoctorId(Integer.parseInt(docIdStr.trim()));
            } catch (NumberFormatException e) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Mã bác sĩ không hợp lệ.", INVALID_FORMAT_ERROR_CODE);
                return null;
            }
        }
        dto.setAppointmentDate(request.getParameter("appointment_date"));
        dto.setAppointmentTime(request.getParameter("appointment_time"));
        dto.setNotes(request.getParameter("notes"));

        String[] svcIds = request.getParameterValues("service_ids");
        if (svcIds != null) {
            List<Integer> sList = new ArrayList<>();
            for (String s : svcIds) {
                try {
                    sList.add(Integer.parseInt(s.trim()));
                } catch (NumberFormatException e) {
                    sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                            "Mã dịch vụ không hợp lệ.", INVALID_FORMAT_ERROR_CODE);
                    return null;
                }
            }
            dto.setServiceIds(sList);
        }
        return dto;
    }

    private boolean validateBookingData(BookingRequestDTO dto, HttpServletResponse response) throws IOException {
        if (dto.getDoctorId() == null || dto.getDoctorId() <= 0
                || dto.getAppointmentDate() == null || dto.getAppointmentDate().trim().isEmpty()
                || dto.getAppointmentTime() == null || dto.getAppointmentTime().trim().isEmpty()) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Thiếu thông tin bác sĩ, ngày hẹn hoặc giờ hẹn.", "ERR_REQUIRED_FIELDS");
            return false;
        }

        Date parsedDate = DateUtil.parseDate(dto.getAppointmentDate());
        Time parsedTime = DateUtil.parseTime(dto.getAppointmentTime());

        if (parsedDate == null || parsedTime == null) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Định dạng ngày hoặc giờ khám không hợp lệ.", INVALID_FORMAT_ERROR_CODE);
            return false;
        }
        return true;
    }

    private void processBooking(BookingRequestDTO dto, int userId, HttpServletResponse response) throws IOException {
        AppointmentService appointmentService = ServiceFactory.getAppointmentService();
        int appointmentId = appointmentService.bookAppointment(dto, userId);

        if (appointmentId > 0) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            PrintWriter out = response.getWriter();
            out.print("{\"success\":true,\"message\":\"Đăng ký lịch hẹn thành công! Vui lòng chờ cuộc gọi xác nhận.\","
                    + "\"data\":{\"appointment_id\":" + appointmentId + ",\"status\":\"" + Constants.APPOINTMENT_PENDING + "\"}}");
        } else {
            sendError(response, HttpServletResponse.SC_CONFLICT,
                    "Rất tiếc, khung giờ này của Bác sĩ đã bị trùng hoặc hệ thống bận. Vui lòng chọn khung giờ khác.",
                    "ERR_SLOT_TAKEN");
        }
    }

    private void sendError(HttpServletResponse response, int status, String message, String errorCode) throws IOException {
        response.setStatus(status);
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        error.put("error_code", errorCode);
        response.getWriter().print(GSON.toJson(error));
    }
}
