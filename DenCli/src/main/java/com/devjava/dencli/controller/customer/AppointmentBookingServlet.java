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

    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();
    private static final ServiceDAO serviceDAO = new ServiceDAOImpl();
    private static final String JSON_CONTENT_TYPE = Constants.CONTENT_TYPE_JSON + ";charset=UTF-8";

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
        try {
            String contentType = request.getContentType();
            boolean isJson = (contentType != null && contentType.contains(Constants.CONTENT_TYPE_JSON))
                    || Constants.CONTENT_TYPE_JSON.equalsIgnoreCase(request.getHeader("Accept"));

            HttpSession session = request.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

            if (currentUser == null) {
                handleUnauthorized(request, response, isJson);
                return;
            }

            BookingRequestDTO bookingDto = parseBookingData(request, isJson);

            if (bookingDto.getDoctorId() == null || bookingDto.getAppointmentDate() == null
                    || bookingDto.getAppointmentTime() == null) {
                handleBookingError(request, response, isJson, "Vui lòng chọn đầy đủ bác sĩ, ngày khám và khung giờ.");
                return;
            }

            AppointmentService appointmentService = ServiceFactory.getAppointmentService();
            int appointmentId = appointmentService.bookAppointment(bookingDto, currentUser.getUserId());

            if (appointmentId > 0) {
                handleBookingSuccess(request, response, appointmentId, isJson);
            } else {
                handleBookingError(request, response, isJson,
                        "Khung giờ này Bác sĩ đã có lịch hẹn trùng lịch hoặc thông tin không hợp lệ. Vui lòng chọn khung giờ khác!");
            }
        } catch (IOException e) {
            throw new ServletException("Failed to process customer booking request.", e);
        }
    }

    private void handleUnauthorized(HttpServletRequest request, HttpServletResponse response, boolean isJson)
            throws IOException {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(JSON_CONTENT_TYPE);
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Vui lòng đăng nhập để thực hiện đặt lịch.");
            response.getWriter().print(gson.toJson(err));
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    private BookingRequestDTO parseBookingData(HttpServletRequest request, boolean isJson) throws IOException {
        if (isJson) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            BookingRequestDTO dto = gson.fromJson(sb.toString(), BookingRequestDTO.class);
            return dto != null ? dto : new BookingRequestDTO();
        }

        BookingRequestDTO dto = new BookingRequestDTO();
        String doctorIdStr = request.getParameter("doctor_id");
        if (doctorIdStr != null && !doctorIdStr.trim().isEmpty()) {
            try {
                dto.setDoctorId(Integer.parseInt(doctorIdStr.trim()));
            } catch (NumberFormatException ignored) {
                // Giữ null để kiểm tra ở bước validate
            }
        }
        dto.setAppointmentDate(request.getParameter("appointment_date"));
        dto.setAppointmentTime(request.getParameter("appointment_time"));
        dto.setNotes(request.getParameter("notes"));

        String[] serviceIdStrs = request.getParameterValues("service_ids");
        if (serviceIdStrs != null) {
            List<Integer> svcIds = new ArrayList<>();
            for (String s : serviceIdStrs) {
                try {
                    svcIds.add(Integer.parseInt(s.trim()));
                } catch (NumberFormatException ignored) {
                    // Bỏ qua ID không hợp lệ
                }
            }
            dto.setServiceIds(svcIds);
        }
        return dto;
    }

    private void handleBookingSuccess(HttpServletRequest request, HttpServletResponse response, int appointmentId, boolean isJson)
            throws IOException {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType(JSON_CONTENT_TYPE);
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
    }

    private void handleBookingError(HttpServletRequest request, HttpServletResponse response, boolean isJson, String message)
            throws ServletException, IOException {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(JSON_CONTENT_TYPE);
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
