/**
 * Purpose: Unit tests for LoginServlet using JUnit 5 and Mockito.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServletTest {

    /**
     * Test case kiểm thử đăng nhập thất bại khi thông tin tài khoản bị để trống.
     */
    @Test // JUnit 5 annotation khai báo hàm kiểm thử
    public void testLoginEmptyCredentials() throws Exception {
        // Khởi tạo các đối tượng giả lập bằng Mockito
        HttpServletRequest request = mock(HttpServletRequest.class); // Giả lập HttpServletRequest từ Mockito
        HttpServletResponse response = mock(HttpServletResponse.class); // Giả lập HttpServletResponse từ Mockito

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        // Thiết lập cấu hình hành vi cho các mock objects
        when(request.getContentType()).thenReturn("application/json");

        // Giả lập body JSON với thông tin trống
        BufferedReader reader = new BufferedReader(new StringReader("{\"email_or_phone\":\"\",\"password\":\"\"}"));
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);

        // Thực thi hàm xử lý POST trực tiếp (cùng package nên truy cập được protected)
        LoginServlet loginServlet = new LoginServlet();
        loginServlet.doPost(request, response);

        // Đảm bảo dữ liệu đã flush hoàn toàn
        writer.flush();
        String jsonResult = stringWriter.toString();

        // Kiểm tra kết quả mong đợi sử dụng Assertions của JUnit 5
        assertTrue(jsonResult.contains("\"success\":false")); // Phản hồi phải báo thất bại
    }
}
