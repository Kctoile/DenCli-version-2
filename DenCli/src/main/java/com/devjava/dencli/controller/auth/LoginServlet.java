/**
 * File: LoginServlet.java
 * Package: com.devjava.dencli.controller.auth
 * Mục đích: Servlet xử lý xác thực đăng nhập người dùng (UC-04), điều hướng thông minh theo vai trò
 *           hoặc quay lại URL trước đó bị chặn bởi AuthenticationFilter.
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
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AuthLoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final Gson gson = new Gson();

    /**
     * GET /login: Chuyển tiếp (forward) hiển thị trang đăng nhập login.jsp.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * POST /login: Tiếp nhận thông tin đăng nhập, gọi UserService xác thực và chuyển hướng phù hợp.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        boolean isJson = (contentType != null && contentType.contains("application/json"))
                || "application/json".equalsIgnoreCase(request.getHeader("Accept"));

        String emailOrPhone = null;
        String password = null;

        if (contentType != null && contentType.contains("application/json")) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            Map<?, ?> jsonMap = gson.fromJson(sb.toString(), Map.class);
            if (jsonMap != null) {
                emailOrPhone = (String) jsonMap.get("email_or_phone");
                password = (String) jsonMap.get("password");
            }
        } else {
            emailOrPhone = request.getParameter("email_or_phone");
            password = request.getParameter("password");
        }

        // Validate cơ bản
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            handleFailure(request, response, isJson, "Vui lòng nhập đầy đủ tài khoản và mật khẩu.");
            return;
        }

        // Gọi Tầng Service (No Fat Servlet)
        UserService userService = ServiceFactory.getUserService();
        User user = userService.login(emailOrPhone.trim(), password.trim());

        if (user != null) {
            HttpSession session = request.getSession(true);
            String roleName = resolveRoleName(user.getRoleId());
            user.setRoleName(roleName);

            session.setAttribute(Constants.SESSION_USER, user);
            session.setAttribute(Constants.SESSION_ROLE, roleName);

            // Xác định URL đích đến: Ưu tiên targetUrl nếu có từ AuthenticationFilter
            String targetUrl = (String) session.getAttribute("targetUrl");
            if (targetUrl != null) {
                session.removeAttribute("targetUrl");
            }

            String dashboardUrl = resolveDashboardUrl(request.getContextPath(), user.getRoleId());
            String redirectUrl = (targetUrl != null && !targetUrl.isEmpty()) ? targetUrl : dashboardUrl;

            if (isJson) {
                response.setContentType("application/json;charset=UTF-8");
                Map<String, Object> resData = new HashMap<>();
                resData.put("success", true);
                resData.put("message", "Đăng nhập thành công!");
                Map<String, Object> userData = new HashMap<>();
                userData.put("user_id", user.getUserId());
                userData.put("full_name", user.getFullName());
                userData.put("role", roleName);
                userData.put("role_id", user.getRoleId());
                userData.put("redirect_url", redirectUrl);
                userData.put("target_url", redirectUrl);
                resData.put("data", userData);
                response.getWriter().print(gson.toJson(resData));
            } else {
                response.sendRedirect(redirectUrl);
            }
        } else {
            handleFailure(request, response, isJson, "Email/Số điện thoại hoặc mật khẩu không chính xác.");
        }
    }

    private void handleFailure(HttpServletRequest request, HttpServletResponse response, boolean isJson, String message)
            throws ServletException, IOException {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> errData = new HashMap<>();
            errData.put("success", false);
            errData.put("message", message);
            errData.put("error_code", "ERR_INVALID_CREDENTIALS");
            response.getWriter().print(gson.toJson(errData));
        } else {
            request.setAttribute("errorMessage", message);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private String resolveRoleName(int roleId) {
        if (roleId == Constants.ROLE_ADMIN_ID) return Constants.ROLE_ADMIN_NAME;
        if (roleId == Constants.ROLE_DOCTOR_ID) return Constants.ROLE_DOCTOR_NAME;
        if (roleId == Constants.ROLE_STAFF_ID) return Constants.ROLE_STAFF_NAME;
        return Constants.ROLE_CUSTOMER_NAME;
    }

    private String resolveDashboardUrl(String contextPath, int roleId) {
        if (roleId == Constants.ROLE_ADMIN_ID) return contextPath + "/admin/dashboard";
        if (roleId == Constants.ROLE_DOCTOR_ID) return contextPath + "/doctor/examination";
        if (roleId == Constants.ROLE_STAFF_ID) return contextPath + "/staff/reception";
        return contextPath + "/customer/book";
    }
}
