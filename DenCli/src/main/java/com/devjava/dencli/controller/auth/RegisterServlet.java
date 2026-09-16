/**
 * File: RegisterServlet.java
 * Package: com.devjava.dencli.controller.auth
 * Mục đích: Servlet xử lý đăng ký tài khoản bệnh nhân mới (UC-03), gọi UserService băm mật khẩu BCrypt.
 */
package com.devjava.dencli.controller.auth;

import com.devjava.dencli.model.User;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.service.UserService;
import com.devjava.dencli.util.Constants;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AuthRegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();

    /**
     * GET /register: Chuyển tiếp (forward) hiển thị trang đăng ký register.jsp.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    /**
     * POST /register: Tiếp nhận dữ liệu đăng ký bệnh nhân và gọi UserService.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        boolean isJson = (contentType != null && contentType.contains(Constants.CONTENT_TYPE_JSON))
                || Constants.CONTENT_TYPE_JSON.equalsIgnoreCase(request.getHeader("Accept"));

        String fullName = null;
        String email = null;
        String password = null;
        String phone = null;
        String gender = null;
        String dobStr = null;
        String address = null;

        if (contentType != null && contentType.contains(Constants.CONTENT_TYPE_JSON)) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            Map<?, ?> jsonMap = gson.fromJson(sb.toString(), Map.class);
            if (jsonMap != null) {
                fullName = (String) jsonMap.get("full_name");
                email = (String) jsonMap.get("email");
                password = (String) jsonMap.get("password");
                phone = (String) jsonMap.get("phone");
                gender = (String) jsonMap.get("gender");
                dobStr = (String) jsonMap.get("dob");
                address = (String) jsonMap.get("address");
            }
        } else {
            fullName = request.getParameter("full_name");
            email = request.getParameter("email");
            password = request.getParameter("password");
            phone = request.getParameter("phone");
            gender = request.getParameter("gender");
            dobStr = request.getParameter("dob");
            address = request.getParameter("address");
        }

        // Validate cơ bản
        if (fullName == null || fullName.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()
                || password == null || password.trim().length() < 6) {
            handleFailure(request, response, isJson, "Vui lòng nhập đầy đủ họ tên, số điện thoại và mật khẩu (tối thiểu 6 ký tự).");
            return;
        }

        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail((email != null && !email.trim().isEmpty()) ? email.trim() : null);
        user.setPhone(phone.trim());
        user.setGender(gender);
        user.setAddress((address != null && !address.trim().isEmpty()) ? address.trim() : null);
        user.setDisplayOrder(99);

        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                user.setDob(Date.valueOf(dobStr.trim()));
            } catch (IllegalArgumentException e) {
                // Định dạng ngày không hợp lệ thì để null
            }
        }

        // Gọi Tầng Service (No Fat Servlet)
        UserService userService = ServiceFactory.getUserService();
        boolean isSuccess = userService.registerCustomer(user, password.trim());

        if (isSuccess) {
            if (isJson) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
                Map<String, Object> resData = new HashMap<>();
                resData.put("success", true);
                resData.put("message", "Đăng ký tài khoản thành công! Bạn có thể đăng nhập ngay.");
                response.getWriter().print(gson.toJson(resData));
            } else {
                request.getSession().setAttribute(Constants.SESSION_SUCCESS_MESSAGE, "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        } else {
            handleFailure(request, response, isJson, "Số điện thoại hoặc email đã được đăng ký trước đó.");
        }
    }

    private void handleFailure(HttpServletRequest request, HttpServletResponse response, boolean isJson, String message)
            throws ServletException, IOException {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
            Map<String, Object> errData = new HashMap<>();
            errData.put("success", false);
            errData.put("message", message);
            errData.put("error_code", "ERR_REGISTRATION_FAILED");
            response.getWriter().print(gson.toJson(errData));
        } else {
            request.setAttribute("errorMessage", message);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
