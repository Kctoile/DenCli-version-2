package com.devjava.dencli.controller.auth;

import com.devjava.dencli.dao.UserDAO;
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

    private static final long serialVersionUID = 1L;

    private static final String PARAM_ACTION = "action";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_NEW_PASSWORD = "new_password";
    private static final String ATTR_FP_OTP = "fp_otp";
    private static final String ATTR_FP_EMAIL = "fp_email";
    private static final String ATTR_FP_TIME = "fp_time";

    private final transient UserService userService = ServiceFactory.getUserService();
    private final transient UserDAO userDAO = new UserDAO();
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

        ForgotPasswordParams params;
        try {
            params = extractParams(request);
        } catch (Exception e) {
            sendJsonResponse(response, false, "Dữ liệu JSON không hợp lệ.");
            return;
        }

        HttpSession session = request.getSession(true);

        if ("send-otp".equalsIgnoreCase(params.action)) {
            handleSendOtp(response, session, params.email);
        } else if ("verify-reset".equalsIgnoreCase(params.action)) {
            handleVerifyAndReset(response, session, params.email, params.otp, params.newPassword);
        } else {
            sendJsonResponse(response, false, "Thao tác không được hỗ trợ.");
        }
    }

    @SuppressWarnings({"java:S3776", "squid:S3776"})
    private ForgotPasswordParams extractParams(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains(Constants.CONTENT_TYPE_JSON)) {
            return extractParamsFromJson(request);
        }
        return extractParamsFromForm(request);
    }

    private ForgotPasswordParams extractParamsFromJson(HttpServletRequest request) throws IOException {
        ForgotPasswordParams p = new ForgotPasswordParams();
        try (BufferedReader reader = request.getReader()) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            p.action = getJsonString(json, PARAM_ACTION);
            p.email = getJsonString(json, PARAM_EMAIL).trim();
            p.otp = getJsonString(json, "otp").trim();
            p.newPassword = getJsonString(json, PARAM_NEW_PASSWORD);
        }
        return p;
    }

    private static String getJsonString(JsonObject json, String key) {
        return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : "";
    }

    private ForgotPasswordParams extractParamsFromForm(HttpServletRequest request) {
        ForgotPasswordParams p = new ForgotPasswordParams();
        p.action = getParam(request, PARAM_ACTION);
        p.email = getParam(request, PARAM_EMAIL).trim();
        p.otp = getParam(request, "otp").trim();
        p.newPassword = getParam(request, PARAM_NEW_PASSWORD);
        return p;
    }

    private static String getParam(HttpServletRequest req, String name) {
        String val = req.getParameter(name);
        return val != null ? val : "";
    }

    private static class ForgotPasswordParams {
        String action = "";
        String email = "";
        String otp = "";
        String newPassword = "";
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
