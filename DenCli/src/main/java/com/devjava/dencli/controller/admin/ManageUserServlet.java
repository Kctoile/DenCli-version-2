/**
 * File: ManageUserServlet.java
 * Package: com.devjava.dencli.controller.admin
 * Mục đích: Servlet cho Quản trị viên quản lý danh sách người dùng (Bác sĩ, Lễ tân, Bệnh nhân) có phân trang (UC-15).
 */
package com.devjava.dencli.controller.admin;

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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminManageUserServlet", urlPatterns = {"/admin/users"})
public class ManageUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();

    /**
     * GET /admin/users: Lấy danh sách người dùng theo vai trò có phân trang.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String roleStr = request.getParameter("role_id");
        String pageStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("page_size");

        int roleId = 0;
        int page = 1;
        int pageSize = Constants.DEFAULT_PAGE_SIZE;

        if (roleStr != null && !roleStr.trim().isEmpty()) {
            try { 
                roleId = Integer.parseInt(roleStr.trim()); 
            } catch (NumberFormatException ignored) {
                // Giữ roleId = 0 (tất cả vai trò)
            }
        }
        if (pageStr != null && !pageStr.trim().isEmpty()) {
            try { 
                page = Integer.parseInt(pageStr.trim()); 
            } catch (NumberFormatException ignored) {
                // Giữ page mặc định = 1
            }
        }
        if (pageSizeStr != null && !pageSizeStr.trim().isEmpty()) {
            try { 
                pageSize = Integer.parseInt(pageSizeStr.trim()); 
            } catch (NumberFormatException ignored) {
                // Giữ pageSize mặc định
            }
        }

        UserService userService = ServiceFactory.getUserService();
        List<User> userList = userService.getUsersByRole(roleId, page, pageSize);
        int totalRecords = userService.countUsersByRole(roleId);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        boolean isJson = Constants.CONTENT_TYPE_JSON.equalsIgnoreCase(request.getHeader("Accept"));
        if (isJson) {
            response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
            Map<String, Object> data = new HashMap<>();
            data.put("users", userList);
            data.put("total_records", totalRecords);
            data.put("page", page);
            data.put("page_size", pageSize);
            data.put("total_pages", totalPages);
            response.getWriter().print(gson.toJson(data));
        } else {
            request.setAttribute("userList", userList);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("selectedRoleId", roleId);

            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        }
    }
}
