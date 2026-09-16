/**
 * File: AppointmentBookingServlet.java
 * Package: com.devjava.dencli.controller.customer
 * Mục đích: Servlet xử lý nghiệp vụ đặt lịch khám trực tuyến của bệnh nhân (UC-05).
 */
package com.devjava.dencli.controller.customer;

import com.devjava.dencli.dao.ServiceDAO;
import com.devjava.dencli.dao.impl.ServiceDAOImpl;
import com.devjava.dencli.model.Service;
import com.devjava.dencli.model.User;
import com.devjava.dencli.model.dto.BookingRequestDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CustomerBookingServlet", urlPatterns = {"/customer/book"})
public class AppointmentBookingServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final ServiceDAO serviceDAO = new ServiceDAOImpl();

    /**
     * GET /customer/book: Lấy danh sách bác sĩ và danh mục dịch vụ, chuyển tiếp sang trang đặt lịch.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> doctors = ServiceFactory.getUserService().getDoctors();
        List<Service> services = serviceDAO.getAllServices();

        request.setAttribute("doctors", doctors);
        request.setAttribute("services", services);

        request.getRequestDispatcher("/customer/book.jsp").forward(request, response);
    }

    /**
     * POST /customer/book: Tiếp nhận dữ liệu đặt lịch, kiểm tra trùng lịch (slot conflict) và tạo cuộc hẹn.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        boolean isJson = (contentType != null && contentType.contains("application/json"))
                || "application/json".equalsIgnoreCase(request.getHeader("Accept"));

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        if (currentUser == null) {
            if (isJson) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                Map<String, Object> err = new HashMap<>();
                err.put("success", false);
                err.put("message", "Vui lòng đăng nhập để thực hiện đặt lịch.");
                response.getWriter().print(gson.toJson(err));
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
            return;
        }

        BookingRequestDTO bookingDto = new BookingRequestDTO();

        if (isJson) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            bookingDto = gson.fromJson(sb.toString(), BookingRequestDTO.class);
        } else {
            String doctorIdStr = request.getParameter("doctor_id");
            String dateStr = request.getParameter("appointment_date");
            String timeStr = request.getParameter("appointment_time");
            String notes = request.getParameter("notes");
            String[] serviceIdStrs = request.getParameterValues("service_ids");

            if (doctorIdStr != null && !doctorIdStr.trim().isEmpty()) {
                try {
                    bookingDto.setDoctorId(Integer.parseInt(doctorIdStr.trim()));
                } catch (NumberFormatException ignored) {}
            }
            bookingDto.setAppointmentDate(dateStr);
            bookingDto.setAppointmentTime(timeStr);
            bookingDto.setNotes(notes);

            List<Integer> svcIds = new ArrayList<>();
            if (serviceIdStrs != null) {
                for (String s : serviceIdStrs) {
                    try {
                        svcIds.add(Integer.parseInt(s.trim()));
                    } catch (NumberFormatException ignored) {}
                }
            }
            bookingDto.setServiceIds(svcIds);
        }

        // Validate cơ bản
        if (bookingDto == null || bookingDto.getDoctorId() == null || bookingDto.getAppointmentDate() == null
                || bookingDto.getAppointmentTime() == null) {
            handleBookingError(request, response, isJson, "Vui lòng chọn đầy đủ bác sĩ, ngày khám và khung giờ.");
            return;
        }

        // Gọi Tầng Service (No Fat Servlet)
        AppointmentService appointmentService = ServiceFactory.getAppointmentService();
        int appointmentId = appointmentService.bookAppointment(bookingDto, currentUser.getUserId());

        if (appointmentId > 0) {
            if (isJson) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType("application/json;charset=UTF-8");
                Map<String, Object> resData = new HashMap<>();
                resData.put("success", true);
                resData.put("message", "Đặt lịch khám bệnh thành công! Mã cuộc hẹn: #" + appointmentId);
                resData.put("appointment_id", appointmentId);
                response.getWriter().print(gson.toJson(resData));
            } else {
                request.getSession().setAttribute(Constants.SESSION_SUCCESS_MESSAGE,
                        "Đặt lịch khám thành công! Mã cuộc hẹn: #" + appointmentId);
                response.sendRedirect(request.getContextPath() + "/customer/profile");
            }
        } else {
            // Lỗi slot conflict hoặc dữ liệu không hợp lệ
            handleBookingError(request, response, isJson,
                    "Khung giờ này Bác sĩ đã có lịch hẹn trùng lịch hoặc thông tin không hợp lệ. Vui lòng chọn khung giờ khác!");
        }
    }

    private void handleBookingError(HttpServletRequest request, HttpServletResponse response, boolean isJson, String message)
            throws ServletException, IOException {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", message);
            err.put("error_code", "ERR_SLOT_CONFLICT");
            response.getWriter().print(gson.toJson(err));
        } else {
            request.setAttribute("errorMessage", message);
            doGet(request, response);
        }
    }
}
