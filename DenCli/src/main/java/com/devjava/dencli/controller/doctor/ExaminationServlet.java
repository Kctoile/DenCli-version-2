/**
 * File: ExaminationServlet.java
 * Package: com.devjava.dencli.controller.doctor
 * Mục đích: Servlet dành cho Bác sĩ xem danh sách bệnh nhân chờ khám và ghi nhận kết quả khám lâm sàng (UC-11).
 */
package com.devjava.dencli.controller.doctor;

import com.devjava.dencli.dao.impl.ServiceDAOImpl;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.PrescribedService;
import com.devjava.dencli.model.User;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ExaminationService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.DateUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DoctorExaminationServlet", urlPatterns = {"/doctor/examination"})
public class ExaminationServlet extends HttpServlet {

    private final Gson gson = new Gson();

    /**
     * GET /doctor/examination: Lấy danh sách bệnh nhân được chỉ định cho Bác sĩ hiện tại.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        AppointmentService appointmentService = ServiceFactory.getAppointmentService();
        String dateParam = request.getParameter("date");
        Date filterDate = (dateParam != null && !dateParam.trim().isEmpty()) ? DateUtil.parseDate(dateParam) : null;

        List<Appointment> appointments = appointmentService.getDoctorAppointments(currentUser.getUserId(), filterDate, 1, 50);
        request.setAttribute("appointments", appointments);
        request.setAttribute("services", new ServiceDAOImpl().getAllServices());

        request.getRequestDispatcher("/doctor/examination.jsp").forward(request, response);
    }

    /**
     * POST /doctor/examination: Tạo kết quả chẩn đoán lâm sàng và dịch vụ chỉ định thêm.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        boolean isJson = (contentType != null && contentType.contains("application/json"))
                || "application/json".equalsIgnoreCase(request.getHeader("Accept"));

        int appointmentId = 0;
        String diagnosis = null;
        List<PrescribedService> additionalServices = new ArrayList<>();

        if (isJson) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            Map<?, ?> reqMap = gson.fromJson(sb.toString(), Map.class);
            if (reqMap != null) {
                Object appObj = reqMap.get("appointment_id");
                if (appObj != null) {
                    appointmentId = ((Number) appObj).intValue();
                }
                diagnosis = (String) reqMap.get("diagnosis");
                List<?> svcs = (List<?>) reqMap.get("services");
                if (svcs != null) {
                    for (Object s : svcs) {
                        if (s instanceof Map) {
                            Map<?, ?> m = (Map<?, ?>) s;
                            PrescribedService ps = new PrescribedService();
                            if (m.get("service_id") != null) {
                                ps.setServiceId(((Number) m.get("service_id")).intValue());
                            }
                            if (m.get("price") != null) {
                                ps.setPrice(BigDecimal.valueOf(((Number) m.get("price")).doubleValue()));
                            }
                            additionalServices.add(ps);
                        }
                    }
                }
            }
        } else {
            String appStr = request.getParameter("appointment_id");
            if (appStr != null && !appStr.trim().isEmpty()) {
                try {
                    appointmentId = Integer.parseInt(appStr.trim());
                } catch (NumberFormatException ignored) {}
            }
            diagnosis = request.getParameter("diagnosis");
            String[] svcIds = request.getParameterValues("service_ids");
            if (svcIds != null) {
                for (String sid : svcIds) {
                    try {
                        PrescribedService ps = new PrescribedService();
                        ps.setServiceId(Integer.parseInt(sid.trim()));
                        additionalServices.add(ps);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        // Validate
        if (appointmentId <= 0 || diagnosis == null || diagnosis.trim().isEmpty()) {
            handleError(response, isJson, request, "Vui lòng cung cấp mã lịch hẹn và nội dung chẩn đoán.");
            return;
        }

        // Gọi Tầng Service (No Fat Servlet)
        ExaminationService examinationService = ServiceFactory.getExaminationService();
        int resultId = examinationService.recordExamination(appointmentId, diagnosis.trim(), additionalServices);

        if (resultId > 0) {
            if (isJson) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType("application/json;charset=UTF-8");
                Map<String, Object> resData = new HashMap<>();
                resData.put("success", true);
                resData.put("message", "Ghi nhận kết quả khám thành công!");
                resData.put("result_id", resultId);
                response.getWriter().print(gson.toJson(resData));
            } else {
                request.getSession().setAttribute(Constants.SESSION_SUCCESS_MESSAGE, "Ghi nhận hồ sơ bệnh án thành công!");
                response.sendRedirect(request.getContextPath() + "/doctor/examination");
            }
        } else {
            handleError(response, isJson, request, "Không thể lưu kết quả khám bệnh. Vui lòng kiểm tra lại trạng thái lịch hẹn.");
        }
    }

    private void handleError(HttpServletResponse response, boolean isJson, HttpServletRequest request, String message)
            throws ServletException, IOException {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", message);
            response.getWriter().print(gson.toJson(err));
        } else {
            request.setAttribute("errorMessage", message);
            doGet(request, response);
        }
    }
}
