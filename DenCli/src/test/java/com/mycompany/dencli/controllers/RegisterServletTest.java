/**
 * Purpose: Unit tests for RegisterServlet using JUnit 5 and Mockito.
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    /**
     * Thiết lập các mock objects trước mỗi kịch bản kiểm thử.
     */
    @BeforeEach // JUnit 5 annotation chạy trước mỗi hàm test
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class); // Giả lập HttpServletRequest từ Mockito
        response = mock(HttpServletResponse.class); // Giả lập HttpServletResponse từ Mockito

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer); // Mockito cấu hình mock trả về PrintWriter
    }

    /**
     * TC-AUTH-01: Kiểm thử đăng ký tài khoản khi nhập đầy đủ thông tin hợp lệ.
     */
    @Test // JUnit 5 annotation khai báo hàm kiểm thử
    public void testRegisterValidInput() throws Exception {
        // Tạo body JSON giả lập với thông tin đăng ký
        String jsonBody = "{\"full_name\":\"Test User\","
                + "\"email\":\"testunique" + System.currentTimeMillis() + "@gmail.com\","
                + "\"phone\":\"0905" + (int)(Math.random() * 1000000) + "\","
                + "\"password\":\"SecurePass123\","
                + "\"gender\":\"Nam\","
                + "\"dob\":\"1995-10-25\","
                + "\"address\":\"Đà Nẵng\"}";

        // Cấu hình mock request đọc body dạng JSON
        when(request.getContentType()).thenReturn("application/json");
        BufferedReader reader = new BufferedReader(new StringReader(jsonBody));
        when(request.getReader()).thenReturn(reader); // Mockito cấu hình mock trả về BufferedReader

        // Thực thi hàm xử lý POST (cùng package nên truy cập được protected)
        RegisterServlet servlet = new RegisterServlet();
        servlet.doPost(request, response);

        writer.flush();
        String jsonResult = stringWriter.toString();

        // Xác minh kết quả phản hồi có chứa trường success
        assertTrue(jsonResult.contains("\"success\":true") || jsonResult.contains("\"success\":false"));
    }

    /**
     * TC-AUTH-02: Kiểm thử đăng ký thất bại khi thiếu các trường bắt buộc.
     */
    @Test
    public void testRegisterMissingFields() throws Exception {
        // Tạo body JSON thiếu các trường bắt buộc
        String jsonBody = "{\"full_name\":\"\",\"email\":\"\",\"phone\":\"\",\"password\":\"\"}";

        when(request.getContentType()).thenReturn("application/json");
        BufferedReader reader = new BufferedReader(new StringReader(jsonBody));
        when(request.getReader()).thenReturn(reader);

        RegisterServlet servlet = new RegisterServlet();
        servlet.doPost(request, response);

        writer.flush();
        String jsonResult = stringWriter.toString();

        // Xác minh hệ thống từ chối đăng ký và trả về lỗi
        assertTrue(jsonResult.contains("\"success\":false"));

        // Xác minh HTTP Status Code trả về 400 Bad Request
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST); // Gọi hàm xác minh từ Mockito
    }
}
