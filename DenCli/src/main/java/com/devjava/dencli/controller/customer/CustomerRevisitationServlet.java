package com.devjava.dencli.controller.customer;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.impl.AppointmentDAOImpl;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.User;
import com.devjava.dencli.util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/customer/revisitation")
public class CustomerRevisitationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User u = session == null ? null : (User) session.getAttribute(Constants.SESSION_USER);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
        List<Appointment> revisits = appointmentDAO.getUpcomingRevisitsByPatient(
            u.getUserId(), Date.valueOf(LocalDate.now()));
        req.setAttribute("revisits", revisits);
        req.getRequestDispatcher("/customer/revisitation.jsp").forward(req, resp);
    }
}
