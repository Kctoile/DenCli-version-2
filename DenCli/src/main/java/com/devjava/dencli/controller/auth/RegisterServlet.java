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
@SuppressWarnings({"java:S1989", "squid:S1989", "java:S2143", "squid:S2143"})
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

        RegisterRequestData data = parseRegisterData(request, isJson);

        if (isInvalidRegisterData(data)) {
            handleFailure(request, response, isJson, "Vui lòng nhập đầy đủ họ tên, số điện thoại và mật khẩu (tối thiểu 6 ký tự).");
            return;
        }

        User user = buildUser(data);

        // Gọi Tầng Service (No Fat Servlet)
        UserService userService = ServiceFactory.getUserService();
        boolean isSuccess = userService.registerCustomer(user, data.password.trim());

        if (isSuccess) {
            handleSuccess(request, response, isJson);
        } else {
            handleFailure(request, response, isJson, "Số điện thoại hoặc email đã được đăng ký trước đó.");
        }
    }

    private boolean isInvalidRegisterData(RegisterRequestData data) {
        return data.fullName == null || data.fullName.trim().isEmpty()
                || data.phone == null || data.phone.trim().isEmpty()
                || data.password == null || data.password.trim().length() < 6;
    }

    private RegisterRequestData parseRegisterData(HttpServletRequest request, boolean isJson) throws IOException {
        RegisterRequestData data = new RegisterRequestData();
        if (isJson) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            Map<?, ?> jsonMap = gson.fromJson(sb.toString(), Map.class);
            if (jsonMap != null) {
                data.fullName = (String) jsonMap.get("full_name");
                data.email = (String) jsonMap.get("email");
                data.password = (String) jsonMap.get("password");
                data.phone = (String) jsonMap.get("phone");
                data.gender = (String) jsonMap.get("gender");
                data.dobStr = (String) jsonMap.get("dob");
                data.address = (String) jsonMap.get("address");
            }
        } else {
            data.fullName = request.getParameter("full_name");
            data.email = request.getParameter("email");
            data.password = request.getParameter("password");
            data.phone = request.getParameter("phone");
            data.gender = request.getParameter("gender");
            data.dobStr = request.getParameter("dob");
            data.address = request.getParameter("address");
        }
        return data;
    }

    private User buildUser(RegisterRequestData data) {
        User user = new User();
        user.setFullName(data.fullName.trim());
        user.setEmail((data.email != null && !data.email.trim().isEmpty()) ? data.email.trim() : null);
        user.setPhone(data.phone.trim());
        user.setGender(data.gender);
        user.setAddress((data.address != null && !data.address.trim().isEmpty()) ? data.address.trim() : null);
        user.setDisplayOrder(99);

        if (data.dobStr != null && !data.dobStr.trim().isEmpty()) {
            try {
                user.setDob(Date.valueOf(data.dobStr.trim()));
            } catch (IllegalArgumentException e) {
                // Định dạng ngày không hợp lệ thì để null
            }
        }
        return user;
    }

    private void handleSuccess(HttpServletRequest request, HttpServletResponse response, boolean isJson) throws IOException {
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
    }

    private static class RegisterRequestData {
        String fullName;
        String email;
        String password;
        String phone;
        String gender;
        String dobStr;
        String address;
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
