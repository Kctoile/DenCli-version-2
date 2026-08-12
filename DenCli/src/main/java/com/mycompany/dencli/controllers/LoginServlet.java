/**
 * Purpose: Servlet controller to handle user login (UC-04) and failure locks.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.controllers;

import com.mycompany.dencli.dao.UserDAO;
import com.mycompany.dencli.models.User;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"}) // Khai báo Servlet xử lý đường dẫn /login
public class LoginServlet extends HttpServlet {

    // ponytail: Lưu trữ số lần đăng nhập sai của từng tài khoản trong bộ nhớ để phòng chống dò mật khẩu
    private static final ConcurrentHashMap<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private static final int MAX_FAILED_ATTEMPTS = 5;

    /**
     * Phương thức tiếp nhận yêu cầu POST để xác thực thông tin đăng nhập.
     */
    @Override // Ghi đè phương thức doPost từ HttpServlet để xử lý request POST
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Cấu hình phản hồi dạng JSON với bộ mã UTF-8
        response.setContentType("application/json;charset=UTF-8"); // Đặt kiểu nội dung JSON cho response

        PrintWriter out = response.getWriter(); // Lấy luồng ghi dữ liệu của Servlet
        String emailOrPhone = null;
        String password = null;

        String contentType = request.getContentType(); // Kiểm tra định dạng dữ liệu gửi lên

        // Đọc dữ liệu đăng nhập tùy theo định dạng JSON hay Form
        if (contentType != null && contentType.contains("application/json")) {
            StringBuilder sb = new StringBuilder();
            
            try (BufferedReader reader = request.getReader()) {
                String line;
                
                while ((line = reader.readLine()) != null) { // Đọc từng dòng từ request body
                    sb.append(line);
                }
            }
            
            String json = sb.toString();
            emailOrPhone = getJsonValue(json, "email_or_phone");
            password = getJsonValue(json, "password");

        } else {
            emailOrPhone = request.getParameter("email_or_phone");
            password = request.getParameter("password");
        }

        // Kiểm tra dữ liệu đầu vào bắt buộc
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            
            out.print("{\"success\":false,\"message\":\"Vui lòng cung cấp đầy đủ thông tin đăng nhập.\",\"error_code\":\"ERR_REQUIRED_FIELDS\"}");
            return;
        }

        emailOrPhone = emailOrPhone.trim();

        // Kiểm tra xem tài khoản này có đang bị tạm khóa do đăng nhập sai nhiều lần hay không
        Integer currentFailures = failedAttempts.get(emailOrPhone); // Lấy số lần đăng nhập sai từ Map

        if (currentFailures != null && currentFailures >= MAX_FAILED_ATTEMPTS) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Trả về HTTP 401 Unauthorized
            
            out.print("{\"success\":false,\"message\":\"Tài khoản của bạn đã bị tạm khóa do nhập sai mật khẩu quá 5 lần liên tiếp.\",\"error_code\":\"ERR_ACCOUNT_LOCKED\"}");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail(emailOrPhone); // Tìm kiếm người dùng bằng email hoặc số điện thoại

        boolean isValid = false;

        if (user != null) {
            // Kiểm tra mật khẩu băm thông qua thư viện jbcrypt
            isValid = BCrypt.checkpw(password, user.getPassword()); // Gọi hàm checkpw của thư viện BCrypt
        }

        if (isValid) {
            // Đăng nhập thành công, xóa vết số lần đăng nhập sai
            failedAttempts.remove(emailOrPhone); // Xóa khỏi danh sách theo dõi sai mật khẩu

            // Tạo và cấu hình Session cho người dùng
            HttpSession session = request.getSession(true); // Tạo Session mới của Servlet
            session.setAttribute("user", user); // Lưu đối tượng người dùng vào Session

            // Xác định vai trò dạng chuỗi dựa trên role_id
            String roleName = "CUSTOMER";
            
            if (user.getRoleId() == 1) {
                roleName = "ADMIN";
            } else if (user.getRoleId() == 2) {
                roleName = "DOCTOR";
            } else if (user.getRoleId() == 4) {
                roleName = "STAFF";
            }

            response.setStatus(HttpServletResponse.SC_OK); // Trả về HTTP 200 OK
            
            out.print("{\"success\":true,\"message\":\"Đăng nhập thành công!\",\"data\":{\"user_id\":" + user.getUserId() + ",\"full_name\":\"" + user.getFullName() + "\",\"role\":\"" + roleName + "\"}}");

        } else {
            // Đăng nhập thất bại, tăng số lần đăng nhập sai
            int attempts = (currentFailures == null) ? 1 : currentFailures + 1;
            failedAttempts.put(emailOrPhone, attempts); // Cập nhật lại số lần sai vào Map

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                out.print("{\"success\":false,\"message\":\"Tài khoản của bạn đã bị tạm khóa do nhập sai mật khẩu quá 5 lần liên tiếp.\",\"error_code\":\"ERR_ACCOUNT_LOCKED\"}");
            } else {
                int remains = MAX_FAILED_ATTEMPTS - attempts;
                out.print("{\"success\":false,\"message\":\"Tài khoản hoặc mật khẩu không chính xác. Bạn còn " + remains + " lần thử.\",\"error_code\":\"ERR_INVALID_CREDENTIALS\"}");
            }
        }
    }

    /**
     * ponytail: Hàm Regex tiện ích trích xuất giá trị từ chuỗi JSON phẳng.
     */
    private String getJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\""); // Nạp biểu thức Regex
        Matcher matcher = pattern.matcher(json); // Khớp mẫu Regex
        
        if (matcher.find()) {
            return matcher.group(1); // Trả về chuỗi khớp
        }
        
        return null;
    }
}
