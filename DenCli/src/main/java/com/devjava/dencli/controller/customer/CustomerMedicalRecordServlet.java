package com.devjava.dencli.controller.customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.ExaminationResultDAO;
import com.devjava.dencli.dao.PrescriptionDAO;
import com.devjava.dencli.dao.impl.AppointmentDAOImpl;
import com.devjava.dencli.dao.impl.ExaminationResultDAOImpl;
import com.devjava.dencli.dao.impl.PrescriptionDAOImpl;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.ExaminationResult;
import com.devjava.dencli.model.User;
import com.devjava.dencli.service.BillingService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/customer/medical-records")
public class CustomerMedicalRecordServlet extends HttpServlet {
    // ponytail: dung DAOImpl truc tiep thay vi them DAO vao ServiceFactory
    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private final ExaminationResultDAO examDAO = new ExaminationResultDAOImpl();
    private final PrescriptionDAO rxDAO = new PrescriptionDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User u = session == null ? null : (User) session.getAttribute(Constants.SESSION_USER);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }

        BillingService billingService = ServiceFactory.getBillingService();
        List<Map<String,Object>> records = new ArrayList<>();
        for (Appointment a : appointmentDAO.getCompletedAppointmentsByPatient(u.getUserId())) {
            Map<String,Object> r = new LinkedHashMap<>();
            r.put("appointment", a);
            ExaminationResult er = examDAO.getExaminationResultByAppointmentId(a.getAppointmentId());
            if (er != null) {
                r.put("diagnosis", er.getResultDetails());
                r.put("services", examDAO.getPrescribedServicesByResultId(er.getResultId()));
                r.put("prescription", rxDAO.getPrescriptionByResultId(er.getResultId()));
                r.put("invoice", billingService.calculateInvoice(a.getAppointmentId()));
            }
            records.add(r);
        }
        req.setAttribute("records", records);
        req.getRequestDispatcher("/customer/medical_records.jsp").forward(req, resp);
    }
}
