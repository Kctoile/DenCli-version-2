package com.devjava.dencli.controller.customer;

import com.devjava.dencli.model.*;
import com.devjava.dencli.model.User;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/customer/appointments")
public class CustomerAppointmentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User u = session == null ? null : (User) session.getAttribute(Constants.SESSION_USER);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        AppointmentService svc = ServiceFactory.getAppointmentService();
        int page = 1; try { page = Math.max(1,Integer.parseInt(req.getParameter("page"))); } catch (Exception ignored) {}
        int pageSize = 10;
        List<Appointment> list = svc.getPatientAppointments(u.getUserId(), page, pageSize);
        req.setAttribute("appointments", list);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", (svc.countPatientAppointments(u.getUserId())+pageSize-1)/pageSize);
        req.getRequestDispatcher("/customer/appointment_history.jsp").forward(req, resp);
    }
}
