/**
 * File: ExaminationServlet.java
 * Package: com.devjava.dencli.controller.doctor
 * Mục đích: Servlet dành cho Bác sĩ xem danh sách bệnh nhân chờ khám và ghi nhận kết quả khám lâm sàng & kê đơn thuốc nguyên tử (UC-11).
 */
package com.devjava.dencli.controller.doctor;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devjava.dencli.dao.ServiceDAO;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.PrescribedService;
import com.devjava.dencli.model.PrescriptionDetail;
import com.devjava.dencli.model.User;
import com.devjava.dencli.model.dto.ConsultationRequestDTO;
import com.devjava.dencli.model.dto.ConsultationResultDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ExaminationService;
import com.devjava.dencli.service.PrescriptionService;
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

@WebServlet(name = "DoctorExaminationServlet", urlPatterns = {"/doctor/examination", "/doctor/dashboard"})
@SuppressWarnings({"java:S1989", "squid:S1989"})
public class ExaminationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();

    /**
     * GET /doctor/examination: Lấy danh sách bệnh nhân được chỉ định cho Bác sĩ hiện tại và danh mục dịch vụ, thuốc.
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
        PrescriptionService prescriptionService = ServiceFactory.getPrescriptionService();

        String dateParam = request.getParameter("date");
        Date filterDate = (dateParam != null && !dateParam.trim().isEmpty()) ? DateUtil.parseDate(dateParam) : null;

        List<Appointment> appointments = appointmentService.getDoctorAppointments(currentUser.getUserId(), filterDate, 1, 50);
        request.setAttribute("appointments", appointments);
        request.setAttribute("services", new ServiceDAO().getAllServices());
        request.setAttribute("medicines", prescriptionService.getAllMedicines());

        request.getRequestDispatcher("/doctor/examination.jsp").forward(request, response);
    }

    /**
     * POST /doctor/examination: Tiếp nhận và xử lý phiên khám lâm sàng (chẩn đoán, dịch vụ, đơn thuốc và hoàn tất).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        User currentUser = session == null ? null : (User) session.getAttribute(Constants.SESSION_USER);
        if (currentUser == null || currentUser.getRoleId() != Constants.ROLE_DOCTOR_ID) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String contentType = request.getContentType();
        boolean isJson = (contentType != null && contentType.contains(Constants.CONTENT_TYPE_JSON))
                || Constants.CONTENT_TYPE_JSON.equalsIgnoreCase(request.getHeader("Accept"));

        ConsultationRequestDTO consultationRequest = isJson ? parseJsonBody(request) : parseFormBody(request);
        consultationRequest.setDoctorId(currentUser.getUserId());

        // Validate cơ bản
        if (consultationRequest.getAppointmentId() <= 0 || consultationRequest.getDiagnosis() == null
                || consultationRequest.getDiagnosis().trim().isEmpty()) {
            handleError(response, isJson, request, "Vui lòng cung cấp mã lịch hẹn và nội dung chẩn đoán.");
            return;
        }

        if (consultationRequest.getRevisitDate() != null
                && consultationRequest.getRevisitDate().before(Date.valueOf(java.time.LocalDate.now(java.time.ZoneId.systemDefault())))) {
            handleError(response, isJson, request, "Ngày tái khám không thể ở trong quá khứ.");
            return;
        }

        // Gọi Tầng Service xử lý Seam nguyên tử (No Fat Servlet)
        ExaminationService examinationService = ServiceFactory.getExaminationService();
        ConsultationResultDTO result = examinationService.recordConsultation(consultationRequest);

        if (result.isSuccess()) {
            if (isJson) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
                Map<String, Object> resData = new HashMap<>();
                resData.put("success", true);
                resData.put("message", result.getMessage());
                resData.put("result_id", result.getResultId());
                resData.put("prescription_id", result.getPrescriptionId());
                resData.put("appointment_completed", result.isAppointmentCompleted());
                response.getWriter().print(gson.toJson(resData));
            } else {
                StringBuilder msg = new StringBuilder("Ghi nhận hồ sơ bệnh án thành công! Mã bệnh án: #")
                        .append(result.getResultId());
                if (result.getPrescriptionId() > 0) {
                    msg.append(" (Đơn thuốc #").append(result.getPrescriptionId()).append(")");
                }
                if (result.isAppointmentCompleted()) {
                    msg.append(" - Lịch hẹn đã được hoàn tất.");
                }
                request.getSession().setAttribute(Constants.SESSION_SUCCESS_MESSAGE, msg.toString());
                response.sendRedirect(request.getContextPath() + "/doctor/examination");
            }
        } else {
            handleError(response, isJson, request, result.getMessage());
        }
    }

    private ConsultationRequestDTO parseJsonBody(HttpServletRequest request) throws IOException {
        ConsultationRequestDTO dto = new ConsultationRequestDTO();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        Map<?, ?> reqMap = gson.fromJson(sb.toString(), Map.class);
        if (reqMap == null) {
            return dto;
        }

        Object appObj = reqMap.get("appointment_id");
        if (appObj instanceof Number) {
            dto.setAppointmentId(((Number) appObj).intValue());
        }
        dto.setDiagnosis((String) reqMap.get("diagnosis"));

        Object rvObj = reqMap.get("revisit_date");
        if (rvObj != null && !String.valueOf(rvObj).isBlank()) {
            try {
                dto.setRevisitDate(Date.valueOf(String.valueOf(rvObj).trim()));
            } catch (IllegalArgumentException ignored) {
                // Sẽ được validate sau
            }
        }
        dto.setRevisitNote((String) reqMap.get("revisit_note"));

        parseJsonServices(reqMap.get("services"), dto);
        parseJsonPrescription(reqMap, dto);

        Object compObj = reqMap.get("complete_appointment");
        if (compObj != null) {
            dto.setCompleteAppointment(Boolean.parseBoolean(String.valueOf(compObj)));
        } else {
            dto.setCompleteAppointment(true);
        }

        return dto;
    }

    private void parseJsonServices(Object svcsObj, ConsultationRequestDTO dto) {
        if (!(svcsObj instanceof List)) return;
        List<PrescribedService> list = new ArrayList<>();
        for (Object s : (List<?>) svcsObj) {
            if (s instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) s;
                if (m.get("service_id") instanceof Number) {
                    PrescribedService ps = new PrescribedService();
                    ps.setServiceId(((Number) m.get("service_id")).intValue());
                    list.add(ps);
                }
            }
        }
        dto.setAdditionalServices(list);
    }

    private void parseJsonPrescription(Map<?, ?> reqMap, ConsultationRequestDTO dto) {
        dto.setPrescriptionInstructions((String) reqMap.get("instructions"));
        if (dto.getPrescriptionInstructions() == null) {
            dto.setPrescriptionInstructions((String) reqMap.get("notes"));
        }

        Object detailsObj = reqMap.get("details");
        if (detailsObj == null) {
            detailsObj = reqMap.get("medicines");
        }
        if (!(detailsObj instanceof List)) return;

        List<PrescriptionDetail> list = new ArrayList<>();
        for (Object item : (List<?>) detailsObj) {
            if (item instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) item;
                PrescriptionDetail pd = new PrescriptionDetail();
                if (m.get("medicine_id") instanceof Number) {
                    pd.setMedicineId(((Number) m.get("medicine_id")).intValue());
                }
                if (m.get("quantity") instanceof Number) {
                    pd.setPrescribedQuantity(((Number) m.get("quantity")).intValue());
                } else if (m.get("prescribed_quantity") instanceof Number) {
                    pd.setPrescribedQuantity(((Number) m.get("prescribed_quantity")).intValue());
                }
                if (m.get("dosage") != null) {
                    pd.setDosage(String.valueOf(m.get("dosage")).trim());
                }
                list.add(pd);
            }
        }
        dto.setPrescriptionDetails(list);
    }

    private ConsultationRequestDTO parseFormBody(HttpServletRequest request) {
        ConsultationRequestDTO dto = new ConsultationRequestDTO();
        String appStr = request.getParameter("appointment_id");
        if (appStr != null && !appStr.trim().isEmpty()) {
            try {
                dto.setAppointmentId(Integer.parseInt(appStr.trim()));
            } catch (NumberFormatException ignored) {
                // validate sau
            }
        }
        dto.setDiagnosis(request.getParameter("diagnosis"));

        String revisitDateStr = request.getParameter("revisit_date");
        if (revisitDateStr != null && !revisitDateStr.isBlank()) {
            try {
                dto.setRevisitDate(Date.valueOf(revisitDateStr.trim()));
            } catch (IllegalArgumentException ignored) {
                // validate sau
            }
        }
        dto.setRevisitNote(request.getParameter("revisit_note"));

        // Dịch vụ chỉ định thêm
        String[] svcIds = request.getParameterValues("service_ids");
        if (svcIds != null) {
            List<PrescribedService> list = new ArrayList<>();
            for (String sid : svcIds) {
                try {
                    PrescribedService ps = new PrescribedService();
                    ps.setServiceId(Integer.parseInt(sid.trim()));
                    list.add(ps);
                } catch (NumberFormatException ignored) {
                    // ignore invalid
                }
            }
            dto.setAdditionalServices(list);
        }

        // Thuốc kê đơn đi kèm (nếu có)
        String[] medIds = request.getParameterValues("medicine_id");
        String[] quantities = request.getParameterValues("quantity");
        String[] dosages = request.getParameterValues("dosage");
        if (medIds != null && quantities != null) {
            List<PrescriptionDetail> list = new ArrayList<>();
            for (int i = 0; i < medIds.length; i++) {
                try {
                    int mid = Integer.parseInt(medIds[i].trim());
                    int qty = Integer.parseInt(quantities[i].trim());
                    if (mid > 0 && qty > 0) {
                        PrescriptionDetail pd = new PrescriptionDetail();
                        pd.setMedicineId(mid);
                        pd.setPrescribedQuantity(qty);
                        if (dosages != null && i < dosages.length && dosages[i] != null && !dosages[i].trim().isEmpty()) {
                            pd.setDosage(dosages[i].trim());
                        }
                        list.add(pd);
                    }
                } catch (NumberFormatException ignored) {
                    // ignore invalid
                }
            }
            dto.setPrescriptionDetails(list);
        }

        dto.setPrescriptionInstructions(request.getParameter("instructions"));

        String compParam = request.getParameter("complete_appointment");
        dto.setCompleteAppointment(compParam == null || !"false".equalsIgnoreCase(compParam));

        return dto;
    }

    private void handleError(HttpServletResponse response, boolean isJson, HttpServletRequest request, String message)
            throws ServletException, IOException {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
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
