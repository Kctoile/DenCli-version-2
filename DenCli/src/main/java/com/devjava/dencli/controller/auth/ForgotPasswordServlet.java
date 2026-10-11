package com.devjava.dencli.controller.auth;

import com.devjava.dencli.dao.UserDAO;
import com.devjava.dencli.dao.impl.UserDAOImpl;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.service.UserService;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.EmailService;
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
import java.security.SecureRandom;

/**
 * Servlet xử lý yêu cầu Quên mật khẩu qua mã OTP gửi về Email.
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
@SuppressWarnings({"java:S1989", "squid:S1989"})
public class ForgotPasswordServlet extends HttpServlet {

    private static final String PARAM_ACTION = "action";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_NEW_PASSWORD = "new_password";
    private static final String ATTR_FP_OTP = "fp_otp";
    private static final String ATTR_FP_EMAIL = "fp_email";
    private static final String ATTR_FP_TIME = "fp_time";

    private final transient UserService userService = ServiceFactory.getUserService();
    private final transient UserDAO userDAO = new UserDAOImpl();
    private final transient SecureRandom secureRandom = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");

        String action = "";
        String email = "";
        String otp = "";
        String newPassword = "";

        // Kiểm tra request JSON hoặc form param
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains(Constants.CONTENT_TYPE_JSON)) {
            try (BufferedReader reader = request.getReader()) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                if (json.has(PARAM_ACTION)) action = json.get(PARAM_ACTION).getAsString();
                if (json.has(PARAM_EMAIL)) email = json.get(PARAM_EMAIL).getAsString().trim();
                if (json.has("otp")) otp = json.get("otp").getAsString().trim();
                if (json.has(PARAM_NEW_PASSWORD)) newPassword = json.get(PARAM_NEW_PASSWORD).getAsString();
            } catch (Exception e) {
                sendJsonResponse(response, false, "Dữ liệu JSON không hợp lệ.");
                return;
            }
        } else {
            action = request.getParameter(PARAM_ACTION);
            email = request.getParameter(PARAM_EMAIL);
            otp = request.getParameter("otp");
            newPassword = request.getParameter(PARAM_NEW_PASSWORD);
            if (action == null) action = "";
            if (email == null) email = "";
            if (otp == null) otp = "";
            if (newPassword == null) newPassword = "";
        }

        HttpSession session = request.getSession(true);

        if ("send-otp".equalsIgnoreCase(action)) {
            handleSendOtp(response, session, email);
        } else if ("verify-reset".equalsIgnoreCase(action)) {
            handleVerifyAndReset(response, session, email, otp, newPassword);
        } else {
            sendJsonResponse(response, false, "Thao tác không được hỗ trợ.");
        }
    }

    private void handleSendOtp(HttpServletResponse response, HttpSession session, String email) throws IOException {
        if (email.isBlank()) {
            sendJsonResponse(response, false, "Vui lòng nhập địa chỉ email.");
            return;
        }

        if (!userDAO.checkEmailExists(email)) {
            sendJsonResponse(response, false, "Địa chỉ email này chưa được đăng ký trong hệ thống.");
            return;
        }

        // Sinh mã OTP 6 chữ số
        String generatedOtp = String.format("%06d", secureRandom.nextInt(1000000));
        session.setAttribute(ATTR_FP_OTP, generatedOtp);
        session.setAttribute(ATTR_FP_EMAIL, email);
        session.setAttribute(ATTR_FP_TIME, System.currentTimeMillis());

        boolean sent = EmailService.sendOtpEmail(email, generatedOtp);
        if (sent) {
            sendJsonResponse(response, true, "Mã OTP đã được gửi tới email của bạn. Vui lòng kiểm tra hộp thư!");
        } else {
            sendJsonResponse(response, false, "Không thể gửi email OTP lúc này. Vui lòng thử lại sau.");
        }
    }

    private void handleVerifyAndReset(HttpServletResponse response, HttpSession session,
                                     String email, String otp, String newPassword) throws IOException {
        String sessionOtp = (String) session.getAttribute(ATTR_FP_OTP);
        String sessionEmail = (String) session.getAttribute(ATTR_FP_EMAIL);
        Long sessionTime = (Long) session.getAttribute(ATTR_FP_TIME);

        if (sessionOtp == null || sessionEmail == null || sessionTime == null) {
            sendJsonResponse(response, false, "Chưa yêu cầu mã OTP hoặc phiên đã hết hạn. Vui lòng thử lại.");
            return;
        }

        // Kiểm tra thời hạn 5 phút (300,000 ms)
        if (System.currentTimeMillis() - sessionTime > 300000) {
            session.removeAttribute(ATTR_FP_OTP);
            session.removeAttribute(ATTR_FP_EMAIL);
            session.removeAttribute(ATTR_FP_TIME);
            sendJsonResponse(response, false, "Mã OTP đã hết hiệu lực (quá 5 phút). Vui lòng lấy mã mới.");
            return;
        }

        if (!sessionEmail.equalsIgnoreCase(email) || !sessionOtp.equals(otp)) {
            sendJsonResponse(response, false, "Mã xác thực OTP không chính xác.");
            return;
        }

        if (newPassword.trim().length() < 6) {
            sendJsonResponse(response, false, "Mật khẩu mới phải có tối thiểu 6 ký tự.");
            return;
        }

        boolean updated = userService.resetPassword(email, newPassword);
        if (updated) {
            // Xóa dữ liệu OTP sau khi reset thành công
            session.removeAttribute(ATTR_FP_OTP);
            session.removeAttribute(ATTR_FP_EMAIL);
            session.removeAttribute(ATTR_FP_TIME);
            sendJsonResponse(response, true, "Đặt lại mật khẩu thành công! Bạn có thể đăng nhập ngay.");
        } else {
            sendJsonResponse(response, false, "Cập nhật mật khẩu thất bại. Vui lòng thử lại.");
        }
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("success", success);
        json.addProperty("message", message);
        response.getWriter().print(json.toString());
    }
}
