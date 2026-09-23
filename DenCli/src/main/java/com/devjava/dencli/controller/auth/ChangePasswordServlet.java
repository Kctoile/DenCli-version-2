package com.devjava.dencli.controller.auth;

import com.devjava.dencli.model.User;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.service.UserService;
import com.devjava.dencli.util.Constants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Servlet xử lý đổi mật khẩu cho người dùng đã đăng nhập (Admin, Bác sĩ, Nhân viên, Bệnh nhân).
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    private final UserService userService = ServiceFactory.getUserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constants.SESSION_USER) == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.getRequestDispatcher("/change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;
        if (currentUser == null) {
            sendJsonResponse(response, false, "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");
            return;
        }

        String oldPassword = "";
        String newPassword = "";
        String confirmPassword = "";

        String contentType = request.getContentType();
        if (contentType != null && contentType.contains(Constants.CONTENT_TYPE_JSON)) {
            try (BufferedReader reader = request.getReader()) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                if (json.has("old_password")) oldPassword = json.get("old_password").getAsString();
                if (json.has("new_password")) newPassword = json.get("new_password").getAsString();
                if (json.has("confirm_password")) confirmPassword = json.get("confirm_password").getAsString();
            } catch (Exception e) {
                sendJsonResponse(response, false, "Dữ liệu JSON không hợp lệ.");
                return;
            }
        } else {
            oldPassword = request.getParameter("old_password");
            newPassword = request.getParameter("new_password");
            confirmPassword = request.getParameter("confirm_password");
            if (oldPassword == null) oldPassword = "";
            if (newPassword == null) newPassword = "";
            if (confirmPassword == null) confirmPassword = "";
        }

        if (oldPassword.isBlank() || newPassword.isBlank()) {
            sendJsonResponse(response, false, "Vui lòng nhập đầy đủ thông tin mật khẩu.");
            return;
        }

        if (newPassword.trim().length() < 6) {
            sendJsonResponse(response, false, "Mật khẩu mới phải có tối thiểu 6 ký tự.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            sendJsonResponse(response, false, "Mật khẩu xác nhận không khớp.");
            return;
        }

        boolean success = userService.changePassword(currentUser.getUserId(), oldPassword, newPassword);
        if (success) {
            sendJsonResponse(response, true, "Đổi mật khẩu thành công! Hãy ghi nhớ mật khẩu mới.");
        } else {
            sendJsonResponse(response, false, "Mật khẩu cũ không chính xác. Vui lòng kiểm tra lại.");
        }
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("success", success);
        json.addProperty("message", message);
        response.getWriter().print(json.toString());
    }
}
