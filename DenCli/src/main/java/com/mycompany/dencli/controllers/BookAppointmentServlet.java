/**
 * Purpose: Servlet controller to handle online appointment booking (UC-05).
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.controllers;

import com.mycompany.dencli.dao.AppointmentDAO;
import com.mycompany.dencli.models.Appointment;
import com.mycompany.dencli.models.User;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "BookAppointmentServlet", urlPatterns = {"/api/appointments/book"}) // Ánh xạ Servlet tới url đặt lịch
public class BookAppointmentServlet extends HttpServlet {

    /**
     * Phương thức tiếp nhận yêu cầu POST để đặt lịch hẹn khám trực tuyến.
     */
    @Override // Ghi đè phương thức doPost của HttpServlet để xử lý request POST đặt lịch
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Cấu hình mã hóa phản hồi đầu ra là JSON và UTF-8
        response.setContentType("application/json;charset=UTF-8"); // Đặt kiểu nội dung JSON cho response

        PrintWriter out = response.getWriter(); // Lấy luồng xuất của Servlet
        
        // 1. Kiểm tra xác thực phiên làm việc (Session) của Bệnh nhân
        HttpSession session = request.getSession(false); // Lấy Session hiện tại, không tự động tạo mới
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Trả về HTTP 401 Unauthorized
            
            out.print("{\"success\":false,\"message\":\"Yêu cầu đăng nhập trước khi đặt lịch hẹn.\",\"error_code\":\"ERR_UNAUTHORIZED\"}");
            return;
        }

        // Đảm bảo người dùng trong Session có vai trò là CUSTOMER (role_id = 5)
        if (currentUser.getRoleId() != 5) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Trả về HTTP 403 Forbidden
            
            out.print("{\"success\":false,\"message\":\"Chỉ bệnh nhân mới được phép đặt lịch hẹn trực tuyến.\",\"error_code\":\"ERR_FORBIDDEN\"}");
            return;
        }

        // 2. Phân tích các trường thông tin trong request
        String doctorIdStr = null;
        String dateStr = null;
        String timeStr = null;
        String notes = null;
        List<Integer> serviceIds = new ArrayList<>(); // Mảng chứa danh sách ID dịch vụ đặt trước

        String contentType = request.getContentType(); // Lấy Content-Type gửi từ client

        if (contentType != null && contentType.contains("application/json")) {
            // Đọc chuỗi JSON gửi lên từ request body
            StringBuilder sb = new StringBuilder();
            
            try (BufferedReader reader = request.getReader()) {
                String line;
                
                while ((line = reader.readLine()) != null) { // Đọc từng dòng dữ liệu đầu vào
                    sb.append(line);
                }
            }
            
            String json = sb.toString();
            
            // Trích xuất các trường từ chuỗi JSON dùng Regex
            doctorIdStr = getJsonValue(json, "doctor_id");
            dateStr = getJsonValue(json, "appointment_date");
            timeStr = getJsonValue(json, "appointment_time");
            notes = getJsonValue(json, "notes");

            // Phân tích mảng dịch vụ dạng JSON [1, 3] sang danh sách số nguyên
            String servicesArrayStr = getJsonArrayString(json, "service_ids");
            
            if (servicesArrayStr != null && !servicesArrayStr.trim().isEmpty()) {
                String[] tokens = servicesArrayStr.split(","); // Tách mảng bằng dấu phẩy
                
                for (String t : tokens) {
                    try {
                        serviceIds.add(Integer.parseInt(t.trim())); // Ép kiểu số nguyên từ chuỗi
                    } catch (NumberFormatException e) {
                        // Bỏ qua giá trị lỗi
                    }
                }
            }

        } else {
            // Lọc dữ liệu từ form parameter của Servlet
            doctorIdStr = request.getParameter("doctor_id");
            
            if (doctorIdStr == null) {
                doctorIdStr = request.getParameter("doctorId");
            }
            
            dateStr = request.getParameter("appointment_date");
            
            if (dateStr == null) {
                dateStr = request.getParameter("date");
            }
            
            timeStr = request.getParameter("appointment_time");
            
            if (timeStr == null) {
                timeStr = request.getParameter("slotTime");
            }
            
            notes = request.getParameter("notes");

            // Lấy tham số dịch vụ mảng từ form data
            String[] svcParams = request.getParameterValues("service_ids");
            
            if (svcParams == null) {
                svcParams = request.getParameterValues("serviceIds");
            }
            
            if (svcParams != null) {
                for (String p : svcParams) {
                    try {
                        serviceIds.add(Integer.parseInt(p.trim()));
                    } catch (NumberFormatException e) {
                        // Bỏ qua phần tử lỗi
                    }
                }
            }
        }

        // Kiểm tra hợp lệ dữ liệu cơ bản
        if (doctorIdStr == null || dateStr == null || timeStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            
            out.print("{\"success\":false,\"message\":\"Thiếu thông tin bác sĩ, ngày hẹn hoặc giờ hẹn.\",\"error_code\":\"ERR_REQUIRED_FIELDS\"}");
            return;
        }

        int doctorId;
        Date appointmentDate;
        Time appointmentTime;

        try {
            doctorId = Integer.parseInt(doctorIdStr.trim());
            
            appointmentDate = Date.valueOf(dateStr.trim()); // Gọi hàm chuyển đổi java.sql.Date
            
            // Xử lý chuỗi thời gian gửi lên (đảm bảo đúng chuẩn hh:mm:ss của java.sql.Time)
            String rawTime = timeStr.trim();
            
            if (rawTime.length() == 5) {
                rawTime += ":00"; // Định dạng hh:mm -> hh:mm:00
            }
            
            appointmentTime = Time.valueOf(rawTime); // Gọi hàm chuyển đổi java.sql.Time
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            
            out.print("{\"success\":false,\"message\":\"Định dạng dữ liệu ngày giờ hoặc ID không hợp lệ.\",\"error_code\":\"ERR_INVALID_FORMAT\"}");
            return;
        }

        AppointmentDAO appDAO = new AppointmentDAO(); // Khởi tạo AppointmentDAO kết nối CSDL

        // 3. Kiểm tra trùng lịch hẹn của bác sĩ
        boolean isConflict = appDAO.checkDuplicateSlot(doctorId, appointmentDate, appointmentTime);

        if (isConflict) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // Trả về HTTP 409 Conflict
            
            out.print("{\"success\":false,\"message\":\"Rất tiếc, khung giờ này của Bác sĩ đã bị trùng. Vui lòng chọn khung giờ khác.\",\"error_code\":\"ERR_SLOT_TAKEN\"}");
            return;
        }

        // 4. Tạo đối tượng lịch hẹn mới và thực hiện lưu trữ trong database
        Appointment app = new Appointment();
        app.setPatientId(currentUser.getUserId());
        app.setDoctorId(doctorId);
        app.setAppointmentDate(appointmentDate);
        app.setAppointmentTime(appointmentTime);
        app.setStatus("Pending"); // Mặc định trạng thái ban đầu là Pending (Chờ xác nhận)
        app.setNotes(notes);
        app.setRoom(null); // Phòng khám sẽ được gán sau bởi tiếp đón khi check-in

        boolean isSuccess = appDAO.insertAppointment(app, serviceIds); // Chạy nghiệp vụ đặt lịch qua DAO

        if (isSuccess) {
            response.setStatus(HttpServletResponse.SC_CREATED); // Trả về HTTP 201 Created
            
            out.print("{\"success\":true,\"message\":\"Đăng ký lịch hẹn thành công! Vui lòng chờ cuộc gọi xác nhận.\",\"data\":{\"appointment_id\":" + app.getAppointmentId() + ",\"status\":\"Pending\"}}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Trả về HTTP 500
            
            out.print("{\"success\":false,\"message\":\"Lỗi hệ thống trong quá trình lưu lịch hẹn.\",\"error_code\":\"ERR_SYSTEM_ERROR\"}");
        }
    }

    /**
     * ponytail: Trích xuất giá trị trường đơn từ chuỗi JSON dùng Regex.
     */
    private String getJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\""); // Nạp mẫu Regex
        Matcher matcher = pattern.matcher(json);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        // Hỗ trợ trích xuất số nếu không chứa dấu ngoặc kép
        Pattern numPattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");
        Matcher numMatcher = numPattern.matcher(json);
        
        if (numMatcher.find()) {
            return numMatcher.group(1);
        }
        
        return null;
    }

    /**
     * ponytail: Trích xuất nội dung của một mảng JSON phẳng (ví dụ: "[1, 3]") trả về "1, 3".
     */
    private String getJsonArrayString(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\\[([^\\]]*)\\]"); // Nạp mẫu Regex cho mảng []
        Matcher matcher = pattern.matcher(json);
        
        if (matcher.find()) {
            return matcher.group(1); // Trả về chuỗi các phần tử bên trong ngoặc vuông
        }
        
        return null;
    }
}
