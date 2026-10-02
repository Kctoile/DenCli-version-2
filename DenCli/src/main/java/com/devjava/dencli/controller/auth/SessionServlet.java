/** SessionServlet: GET /auth/session — trả về trạng thái đăng nhập hiện tại (công khai, không cần auth). */
package com.devjava.dencli.controller.auth;

import com.devjava.dencli.model.User;
import com.devjava.dencli.util.Constants;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "SessionServlet", urlPatterns = {"/auth/session"})
public class SessionServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        Map<String, Object> res = new HashMap<>();
        if (user != null) {
            res.put("success", true);
            res.put("data", Map.of(
                    "user_id", user.getUserId(),
                    "full_name", user.getFullName(),
                    "role", user.getRoleName() != null ? user.getRoleName() : ""));
        } else {
            res.put("success", false);
            res.put("message", "Chưa đăng nhập");
        }
        response.getWriter().print(gson.toJson(res));
    }
}
