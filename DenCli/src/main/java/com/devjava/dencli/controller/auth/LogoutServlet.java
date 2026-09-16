/**
 * File: LogoutServlet.java
 * Package: com.devjava.dencli.controller.auth
 * Mục đích: Servlet xử lý đăng xuất người dùng (UC-04), hủy phiên làm việc Session và chuyển hướng về login.jsp.
 */
package com.devjava.dencli.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AuthLogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processLogout(request, response);
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Hủy bỏ phiên làm việc Session nếu tồn tại
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Chuyển hướng người dùng về trang đăng nhập
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
