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
import java.util.List;

@WebServlet(name = "BookAppointmentServletV2", urlPatterns = {"/api/appointments/book"})
public class BookAppointmentServlet extends HttpServlet {

    private final Gson gson = new Gson();

    /**
     * Tiếp nhận yêu cầu POST để đặt lịch hẹn khám bệnh trực tuyến.
     */
    @Override // Ghi đè phương thức doPost từ HttpServlet
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Cấu hình phản hồi đầu ra là JSON và UTF-8
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // 1. Kiểm tra xác thực phiên làm việc (Session) của Bệnh nhân
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        if (currentUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"success\":false,\"message\":\"Yêu cầu đăng nhập trước khi đặt lịch hẹn.\",\"error_code\":\"ERR_UNAUTHORIZED\"}");
            return;
        }

        // Đảm bảo người dùng trong Session có vai trò là CUSTOMER (role_id = 5)
        if (currentUser.getRoleId() != Constants.ROLE_CUSTOMER_ID) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.print("{\"success\":false,\"message\":\"Chỉ bệnh nhân mới được phép đặt lịch hẹn trực tuyến.\",\"error_code\":\"ERR_FORBIDDEN\"}");
            return;
        }

        // 2. Tiếp nhận và phân tích dữ liệu đầu vào (JSON body hoặc Form parameters)
        BookingRequestDTO bookingDTO = null;
        String contentType = request.getContentType();

        if (contentType != null && contentType.contains("application/json")) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            try {
                bookingDTO = gson.fromJson(sb.toString(), BookingRequestDTO.class);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\":false,\"message\":\"Dữ liệu JSON không đúng định dạng.\",\"error_code\":\"ERR_INVALID_JSON\"}");
                return;
            }
        } else {
            // Đọc dữ liệu từ form thông thường
            bookingDTO = new BookingRequestDTO();
            String docIdStr = request.getParameter("doctor_id");
            if (docIdStr != null && !docIdStr.trim().isEmpty()) {
                try {
                    bookingDTO.setDoctorId(Integer.parseInt(docIdStr.trim()));
                } catch (NumberFormatException ignored) {
                }
            }
            bookingDTO.setAppointmentDate(request.getParameter("appointment_date"));
            bookingDTO.setAppointmentTime(request.getParameter("appointment_time"));
            bookingDTO.setNotes(request.getParameter("notes"));

            String[] svcIds = request.getParameterValues("service_ids");
            if (svcIds != null) {
                List<Integer> sList = new ArrayList<>();
                for (String s : svcIds) {
                    try {
                        sList.add(Integer.parseInt(s.trim()));
                    } catch (NumberFormatException ignored) {
                    }
                }
                bookingDTO.setServiceIds(sList);
            }
        }

        // 3. Kiểm tra tính hợp lệ của dữ liệu đầu vào bắt buộc
        if (bookingDTO == null || bookingDTO.getDoctorId() == null || bookingDTO.getDoctorId() <= 0
                || bookingDTO.getAppointmentDate() == null || bookingDTO.getAppointmentDate().trim().isEmpty()
                || bookingDTO.getAppointmentTime() == null || bookingDTO.getAppointmentTime().trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":false,\"message\":\"Thiếu thông tin bác sĩ, ngày hẹn hoặc giờ hẹn.\",\"error_code\":\"ERR_REQUIRED_FIELDS\"}");
            return;
        }

        // Kiểm tra hợp lệ định dạng ngày và giờ
        Date parsedDate = DateUtil.parseDate(bookingDTO.getAppointmentDate());
        Time parsedTime = DateUtil.parseTime(bookingDTO.getAppointmentTime());

        if (parsedDate == null || parsedTime == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":false,\"message\":\"Định dạng ngày hoặc giờ khám không hợp lệ.\",\"error_code\":\"ERR_INVALID_FORMAT\"}");
            return;
        }

        // 4. Gọi AppointmentService thực hiện nghiệp vụ đặt lịch (có kiểm tra trùng lịch)
        AppointmentService appointmentService = ServiceFactory.getAppointmentService();
        int appointmentId = appointmentService.bookAppointment(bookingDTO, currentUser.getUserId());

        if (appointmentId > 0) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print("{\"success\":true,\"message\":\"Đăng ký lịch hẹn thành công! Vui lòng chờ cuộc gọi xác nhận.\","
                    + "\"data\":{\"appointment_id\":" + appointmentId + ",\"status\":\"" + Constants.APPOINTMENT_PENDING + "\"}}");
        } else {
            // bookAppointment trả về -1 khi trùng lịch hoặc lỗi hệ thống
            // Kiểm tra xem có trùng lịch không để trả về mã lỗi 409 thích hợp
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.print("{\"success\":false,\"message\":\"Rất tiếc, khung giờ này của Bác sĩ đã bị trùng hoặc hệ thống bận. Vui lòng chọn khung giờ khác.\","
                    + "\"error_code\":\"ERR_SLOT_TAKEN\"}");
        }
    }
}
