package com.devjava.dencli.controller.doctor;

import com.devjava.dencli.dao.ExaminationResultDAO;
import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.PrescriptionDAO;
import com.devjava.dencli.dao.impl.AppointmentDAOImpl;
import com.devjava.dencli.dao.impl.ExaminationResultDAOImpl;
import com.devjava.dencli.dao.impl.PrescriptionDAOImpl;
import com.devjava.dencli.model.*;
import com.devjava.dencli.util.Constants;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/doctor/patient-history")
public class DoctorPatientHistoryServlet extends HttpServlet {
    // ponytail: dung DAOImpl truc tiep, Gson khoi tao tai cho de khong them factory
    private final ExaminationResultDAO examDAO = new ExaminationResultDAOImpl();
    private final PrescriptionDAO rxDAO = new PrescriptionDAOImpl();
    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        HttpSession session = req.getSession(false);
        User doctor = session == null ? null : (User) session.getAttribute(Constants.SESSION_USER);
        if (doctor == null || doctor.getRoleId() != Constants.ROLE_DOCTOR_ID) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        int patientId;
        try {
            patientId = Integer.parseInt(req.getParameter("patient_id"));
            if (patientId <= 0) throw new NumberFormatException();
        } catch (NumberFormatException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã bệnh nhân không hợp lệ.");
            return;
        }

        try {
            List<Map<String,Object>> out = new ArrayList<>();
            for (Appointment a : appointmentDAO.getCompletedAppointmentsForDoctorPatient(doctor.getUserId(), patientId)) {
                Map<String,Object> m = new LinkedHashMap<>();
                m.put("appointment_id", a.getAppointmentId());
                m.put("date", String.valueOf(a.getAppointmentDate()));
                m.put("time", String.valueOf(a.getAppointmentTime()));
                m.put("status", a.getStatus());
                m.put("room", a.getRoom());
                ExaminationResult r = examDAO.getExaminationResultByAppointmentId(a.getAppointmentId());
                if (r != null) {
                    m.put("diagnosis", r.getResultDetails());
                    m.put("services", examDAO.getPrescribedServicesByResultId(r.getResultId()));
                    m.put("prescription", rxDAO.getPrescriptionByResultId(r.getResultId()));
                }
                out.add(m);
            }
            resp.getWriter().print(gson.toJson(out));
        } catch (Exception e) {
            throw new ServletException("Không thể tải lịch sử khám bệnh.", e);
        }
    }
}
