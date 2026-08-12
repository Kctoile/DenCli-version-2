/**
 * Purpose: Servlet controller to handle user registration (UC-03).
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"}) // Khai báo Servlet ánh xạ tới url /register
public class RegisterServlet extends HttpServlet {

    /**
     * Phương thức xử lý yêu cầu POST gửi dữ liệu đăng ký người dùng mới.
     */
    @Override // Ghi đè phương thức doPost từ HttpServlet để xử lý request POST
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Thiết lập mã hóa UTF-8 cho dữ liệu đầu ra để hiển thị tiếng Việt chính xác
        response.setContentType("application/json;charset=UTF-8"); // Đặt kiểu nội dung JSON cho response

        PrintWriter out = response.getWriter(); // Lấy luồng xuất dữ liệu của JDBC/Servlet
        String fullName = null;
        String email = null;
        String password = null;
        String phone = null;
        String gender = null;
        String dobStr = null;
        String address = null;

        String contentType = request.getContentType(); // Lấy Content-Type của request gửi lên

        // Phân biệt xử lý request JSON hay Form URL-encoded
        if (contentType != null && contentType.contains("application/json")) {
            // Đọc chuỗi JSON từ body của request
            StringBuilder sb = new StringBuilder();
            
            try (BufferedReader reader = request.getReader()) {
                String line;
                
                while ((line = reader.readLine()) != null) { // Đọc từng dòng từ luồng dữ liệu đầu vào
                    sb.append(line);
                }
            }
            
            String json = sb.toString();

            // Trích xuất các thuộc tính từ chuỗi JSON sử dụng regex
            fullName = getJsonValue(json, "full_name");
            
            if (fullName == null) {
                fullName = getJsonValue(json, "name"); // Hỗ trợ cả trường name
            }
            
            email = getJsonValue(json, "email");
            password = getJsonValue(json, "password");
            phone = getJsonValue(json, "phone");
            gender = getJsonValue(json, "gender");
            dobStr = getJsonValue(json, "dob");
            address = getJsonValue(json, "address");

        } else {
            // Lấy tham số thông thường từ biểu mẫu form của Servlet
            fullName = request.getParameter("name");
            
            if (fullName == null) {
                fullName = request.getParameter("full_name");
            }
            
            email = request.getParameter("email");
            password = request.getParameter("password");
            phone = request.getParameter("phone");
            gender = request.getParameter("gender");
            dobStr = request.getParameter("dob");
            address = request.getParameter("address");
        }

        // Kiểm tra dữ liệu đầu vào bắt buộc
        if (fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty()) {
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Trả về mã lỗi 400
            
            out.print("{\"success\":false,\"message\":\"Vui lòng điền đầy đủ các thông tin bắt buộc (họ tên, email, mật khẩu, số điện thoại).\",\"error_code\":\"ERR_REQUIRED_FIELDS\"}");
            return;
        }

        UserDAO userDAO = new UserDAO(); // Khởi tạo thực thể UserDAO để thao tác database

        // Kiểm tra xem số điện thoại đã tồn tại trong hệ thống chưa
        if (userDAO.checkPhoneExists(phone)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            
            out.print("{\"success\":false,\"message\":\"Số điện thoại đã tồn tại trên hệ thống.\",\"error_code\":\"ERR_DUPLICATE_USER\"}");
            return;
        }

        // Kiểm tra xem địa chỉ email đã tồn tại chưa
        if (userDAO.checkEmailExists(email)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            
            out.print("{\"success\":false,\"message\":\"Email đã tồn tại trên hệ thống.\",\"error_code\":\"ERR_DUPLICATE_USER\"}");
            return;
        }

        // Băm mật khẩu người dùng sử dụng thuật toán mã hóa BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // Gọi thư viện BCrypt băm mật khẩu

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setPhone(phone);
        user.setRoleId(5); // Thiết lập mặc định role_id = 5 đại diện cho CUSTOMER (Bệnh nhân)
        user.setGender(gender);
        user.setAddress(address);
        user.setDisplayOrder(99);

        // Chuyển đổi định dạng ngày sinh từ chuỗi sang kiểu java.sql.Date
        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                user.setDob(Date.valueOf(dobStr)); // Gọi hàm chuyển đổi java.sql.Date.valueOf
            } catch (IllegalArgumentException e) {
                // Định dạng ngày không hợp lệ, bỏ qua để lưu NULL hoặc xử lý lỗi
            }
        }

        boolean isSuccess = userDAO.insertUser(user); // Thao tác lưu tài khoản mới vào cơ sở dữ liệu

        if (isSuccess) {
            response.setStatus(HttpServletResponse.SC_CREATED); // Trả về HTTP Status 201 Created
            
            out.print("{\"success\":true,\"message\":\"Đăng ký tài khoản thành công!\",\"data\":{\"email\":\"" + email + "\"}}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Trả về HTTP Status 500
            
            out.print("{\"success\":false,\"message\":\"Lỗi hệ thống trong quá trình tạo tài khoản.\",\"error_code\":\"ERR_SYSTEM_ERROR\"}");
        }
    }

    /**
     * ponytail: Hàm tiện ích sử dụng Regex để trích xuất giá trị từ chuỗi JSON phẳng mà không cần thư viện lớn.
     */
    private String getJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\""); // Nạp biểu thức chính quy Regex
        Matcher matcher = pattern.matcher(json); // Thực hiện khớp chuỗi tìm kiếm trong Regex
        
        if (matcher.find()) {
            return matcher.group(1); // Trả về giá trị của trường tìm được
        }
        
        return null;
    }
}
