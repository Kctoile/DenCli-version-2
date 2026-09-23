package com.devjava.dencli.controller.auth;

import com.devjava.dencli.dao.UserDAO;
import com.devjava.dencli.dao.impl.UserDAOImpl;
import com.devjava.dencli.model.User;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.PasswordUtil;
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Servlet điều hướng và xử lý xác thực Google OAuth 2.0 (UC-04 mở rộng).
 */
@WebServlet(name = "GoogleLoginServlet", urlPatterns = {"/auth/google"})
public class GoogleLoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(GoogleLoginServlet.class.getName());

    private static final String CLIENT_ID = getConfig("GOOGLE_CLIENT_ID");
    private static final String CLIENT_SECRET = getConfig("GOOGLE_CLIENT_SECRET");
    private static final String REDIRECT_URI = getConfig("GOOGLE_REDIRECT_URI", "http://localhost:8080/DenCli/auth/google");

    private UserDAO userDAO = new UserDAOImpl();

    private static String getConfig(String key) {
        String val = System.getProperty(key);
        if (val == null || val.isBlank()) val = System.getenv(key);
        return val != null && !val.isBlank() ? val : null;
    }

    private static String getConfig(String key, String def) {
        String val = getConfig(key);
        return val != null ? val : def;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");

        // Bước 1: Người dùng nhấn nút Đăng nhập Google -> Chuyển hướng sang Google Auth
        if (code == null || code.isBlank()) {
            if (CLIENT_ID == null || CLIENT_SECRET == null) {
                LOGGER.info("[Google OAuth] CLIENT_ID/SECRET not configured. Dev simulation mode.");
                request.getSession().setAttribute(Constants.SESSION_ERROR_MESSAGE,
                        "Google OAuth chưa được cấu hình Client ID. Vui lòng thiết lập biến môi trường GOOGLE_CLIENT_ID.");
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            String authUrl = "https://accounts.google.com/o/oauth2/auth?"
                    + "scope=" + URLEncoder.encode("email profile", StandardCharsets.UTF_8)
                    + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8)
                    + "&response_type=code"
                    + "&client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8)
                    + "&approval_prompt=force";

            response.sendRedirect(authUrl);
            return;
        }

        // Bước 2: Google trả về callback kèm tham số 'code' -> Đổi lấy Token & Thông tin User
        try {
            String accessToken = exchangeCodeForToken(code);
            if (accessToken == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=token_failed");
                return;
            }

            JsonObject userInfo = fetchGoogleUserInfo(accessToken);
            String email = userInfo.has("email") ? userInfo.get("email").getAsString() : null;
            String name = userInfo.has("name") ? userInfo.get("name").getAsString() : "Google User";

            if (email == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=no_email");
                return;
            }

            User user = userDAO.getUserByEmailOrPhone(email);
            if (user == null) {
                // Tự động khởi tạo tài khoản Bệnh nhân mới liên kết với Google
                user = new User();
                user.setEmail(email);
                user.setFullName(name);
                user.setRoleId(Constants.ROLE_CUSTOMER_ID);
                user.setRoleName(Constants.ROLE_CUSTOMER_NAME);
                user.setPassword(PasswordUtil.hashPassword("OAUTH_GOOGLE_" + System.currentTimeMillis()));
                userDAO.insertUser(user);
                user = userDAO.getUserByEmailOrPhone(email);
            }

            HttpSession session = request.getSession(true);
            session.setAttribute(Constants.SESSION_USER, user);
            session.setAttribute(Constants.SESSION_ROLE, user.getRoleId());

            // Chuyển hướng theo vai trò người dùng
            if (user.getRoleId() == Constants.ROLE_ADMIN_ID) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else if (user.getRoleId() == Constants.ROLE_DOCTOR_ID) {
                response.sendRedirect(request.getContextPath() + "/doctor/examination");
            } else if (user.getRoleId() == Constants.ROLE_STAFF_ID) {
                response.sendRedirect(request.getContextPath() + "/staff/reception");
            } else {
                response.sendRedirect(request.getContextPath() + "/customer/book");
            }

        } catch (Exception e) {
            LOGGER.warning("[Google OAuth] Authentication failed: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=oauth_error");
        }
    }

    private String exchangeCodeForToken(String code) throws IOException {
        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String params = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                + "&client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8)
                + "&grant_type=authorization_code";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
        }

        if (conn.getResponseCode() != 200) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            return json.has("access_token") ? json.get("access_token").getAsString() : null;
        }
    }

    private JsonObject fetchGoogleUserInfo(String accessToken) throws IOException {
        URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }
}
