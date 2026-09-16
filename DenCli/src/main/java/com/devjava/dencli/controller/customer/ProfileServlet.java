/**
 * File: ProfileServlet.java
 * Package: com.devjava.dencli.controller.customer
 * Mục đích: Servlet xem/cập nhật hồ sơ cá nhân và theo dõi lịch sử khám bệnh của bệnh nhân (UC-06).
 */
package com.devjava.dencli.controller.customer;

import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.User;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.service.UserService;
import com.devjava.dencli.util.Constants;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CustomerProfileServlet", urlPatterns = {"/customer/profile"})
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * GET /customer/profile: Lấy hồ sơ cá nhân và danh sách lịch sử khám bệnh của bệnh nhân.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        UserService userService = ServiceFactory.getUserService();
        AppointmentService appointmentService = ServiceFactory.getAppointmentService();

        User profileUser = userService.getUserById(currentUser.getUserId());
        List<Appointment> appointments = appointmentService.getPatientAppointments(currentUser.getUserId(), 1, 20);

        request.setAttribute("profileUser", (profileUser != null) ? profileUser : currentUser);
        request.setAttribute("appointments", appointments);

        request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
    }

    /**
     * POST /customer/profile: Cập nhật thông tin cá nhân của bệnh nhân.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String fullName = request.getParameter("full_name");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String dobStr = request.getParameter("dob");
        String address = request.getParameter("address");

        if (fullName == null || fullName.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Họ tên và số điện thoại không được để trống.");
            doGet(request, response);
            return;
        }

        User updatedUser = new User();
        updatedUser.setUserId(currentUser.getUserId());
        updatedUser.setFullName(fullName.trim());
        updatedUser.setPhone(phone.trim());
        updatedUser.setEmail(currentUser.getEmail());
        updatedUser.setGender(gender);
        updatedUser.setAddress(address != null ? address.trim() : null);

        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                updatedUser.setDob(Date.valueOf(dobStr.trim()));
            } catch (IllegalArgumentException ignored) {
                // Bỏ qua định dạng ngày sinh không hợp lệ
            }
        }

        UserService userService = ServiceFactory.getUserService();
        boolean success = userService.updateProfile(updatedUser);

        if (success) {
            currentUser.setFullName(updatedUser.getFullName());
            currentUser.setPhone(updatedUser.getPhone());
            currentUser.setGender(updatedUser.getGender());
            currentUser.setAddress(updatedUser.getAddress());
            currentUser.setDob(updatedUser.getDob());
            session.setAttribute(Constants.SESSION_USER, currentUser);

            request.getSession().setAttribute(Constants.SESSION_SUCCESS_MESSAGE, "Cập nhật hồ sơ thành công!");
            response.sendRedirect(request.getContextPath() + "/customer/profile");
        } else {
            request.setAttribute("errorMessage", "Không thể cập nhật hồ sơ. Vui lòng thử lại.");
            doGet(request, response);
        }
    }
}
