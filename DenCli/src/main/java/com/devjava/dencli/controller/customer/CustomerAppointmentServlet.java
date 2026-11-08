package com.devjava.dencli.controller.customer;

import java.io.IOException;
import java.util.List;

import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.User;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/customer/appointments")
@SuppressWarnings({"java:S1989", "squid:S1989"})
public class CustomerAppointmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        AppointmentService svc = ServiceFactory.getAppointmentService();
        int page = 1;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                page = Math.max(1, Integer.parseInt(pageParam.trim()));
            }
        } catch (NumberFormatException ignored) {
            // Fallback to default first page
        }

        int pageSize = 10;
        List<Appointment> list = svc.getPatientAppointments(user.getUserId(), page, pageSize);
        int totalAppointments = svc.countPatientAppointments(user.getUserId());
        int totalPages = (totalAppointments + pageSize - 1) / pageSize;
        if (totalPages < 1) {
            totalPages = 1;
        }

        req.setAttribute("appointments", list);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/customer/appointment_history.jsp").forward(req, resp);
    }
}
