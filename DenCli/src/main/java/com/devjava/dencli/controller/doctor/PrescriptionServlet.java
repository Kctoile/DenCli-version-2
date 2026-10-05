/**
 * File: PrescriptionServlet.java
 * Package: com.devjava.dencli.controller.doctor
 * Mục đích: Servlet xử lý kê đơn thuốc cho bệnh nhân và trừ kho thuốc nguyên tử qua Transaction (UC-12).
 */
package com.devjava.dencli.controller.doctor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devjava.dencli.model.Medicine;

import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.model.PrescriptionDetail;
import com.devjava.dencli.model.User;
import com.devjava.dencli.service.PrescriptionService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DoctorPrescriptionServlet", urlPatterns = {"/doctor/prescription"})
public class PrescriptionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();

    /**
     * GET /doctor/prescription: Lấy danh mục thuốc trong kho để bác sĩ chọn kê đơn.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isDoctor(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        PrescriptionService prescriptionService = ServiceFactory.getPrescriptionService();
        List<Medicine> medicines = prescriptionService.getAllMedicines();

        request.setAttribute("medicines", medicines);
        request.getRequestDispatcher("/doctor/prescription.jsp").forward(request, response);
    }

    /**
     * POST /doctor/prescription: Tiếp nhận đơn thuốc và gọi Transaction trừ tồn kho.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User doctor = getDoctor(request);
        if (doctor == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        boolean isJson = (contentType != null && contentType.contains(Constants.CONTENT_TYPE_JSON))
                || Constants.CONTENT_TYPE_JSON.equalsIgnoreCase(request.getHeader("Accept"));

        int resultId = 0;
        String instructions = null;
        List<PrescriptionDetail> details = new ArrayList<>();

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
                Object resObj = reqMap.get("result_id");
                if (resObj != null) {
                    resultId = ((Number) resObj).intValue();
                }
                instructions = (String) reqMap.get("instructions");
                if (instructions == null) {
                    instructions = (String) reqMap.get("notes");
                }
                List<?> items = (List<?>) reqMap.get("details");
                if (items != null) {
                    for (Object it : items) {
                        if (it instanceof Map) {
                            Map<?, ?> m = (Map<?, ?>) it;
                            PrescriptionDetail pd = new PrescriptionDetail();
                            if (m.get("medicine_id") != null) {
                                pd.setMedicineId(((Number) m.get("medicine_id")).intValue());
                            }
                            if (m.get("quantity") != null) {
                                pd.setPrescribedQuantity(((Number) m.get("quantity")).intValue());
                            } else if (m.get("prescribed_quantity") != null) {
                                pd.setPrescribedQuantity(((Number) m.get("prescribed_quantity")).intValue());
                            }
                            details.add(pd);
                        }
                    }
                }
            }
        } else {
            String resStr = request.getParameter("result_id");
            if (resStr != null && !resStr.trim().isEmpty()) {
                try {
                    resultId = Integer.parseInt(resStr.trim());
                } catch (NumberFormatException ignored) {
                    // ResultId không hợp lệ sẽ được kiểm tra ở validate
                }
            }
            instructions = request.getParameter("instructions");
            if (instructions == null) {
                instructions = request.getParameter("notes");
            }
            String[] medIds = request.getParameterValues("medicine_id");
            String[] quantities = request.getParameterValues("quantity");

            if (medIds != null && quantities != null) {
                for (int i = 0; i < medIds.length; i++) {
                    try {
                        PrescriptionDetail pd = new PrescriptionDetail();
                        pd.setMedicineId(Integer.parseInt(medIds[i].trim()));
                        pd.setPrescribedQuantity(Integer.parseInt(quantities[i].trim()));
                        details.add(pd);
                    } catch (NumberFormatException ignored) {
                        // Bỏ qua chi tiết thuốc không hợp lệ
                    }
                }
            }
        }

        // Validate cơ bản
        if (resultId <= 0 || details.isEmpty()) {
            handleError(response, isJson, request, "Vui lòng chọn kết quả khám và ít nhất 1 loại thuốc cần kê đơn.");
            return;
        }

        Prescription prescription = new Prescription();
        prescription.setResultId(resultId);
        prescription.setInstructions(instructions);
        prescription.setDetails(details);

        // Gọi Tầng Service thực hiện Transaction trừ tồn kho đa bảng
        PrescriptionService prescriptionService = ServiceFactory.getPrescriptionService();
        boolean success = prescriptionService.createPrescriptionWithStockDeduction(prescription, doctor.getUserId());

        if (success) {
            if (isJson) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
                Map<String, Object> resData = new HashMap<>();
                resData.put("success", true);
                resData.put("message", "Kê đơn thuốc và trừ tồn kho thành công!");
                response.getWriter().print(gson.toJson(resData));
            } else {
                request.getSession().setAttribute(Constants.SESSION_SUCCESS_MESSAGE, "Kê đơn thuốc thành công!");
                response.sendRedirect(request.getContextPath() + "/doctor/examination");
            }
        } else {
            handleError(response, isJson, request, "Kê đơn thuốc thất bại: Thuốc trong kho không đủ số lượng hoặc có lỗi.");
        }
    }

    private boolean isDoctor(HttpServletRequest request) {
        return getDoctor(request) != null;
    }

    private User getDoctor(HttpServletRequest request) {
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute(Constants.SESSION_USER);
        return user != null && user.getRoleId() == Constants.ROLE_DOCTOR_ID ? user : null;
    }

    private void handleError(HttpServletResponse response, boolean isJson, HttpServletRequest request, String message)
            throws ServletException, IOException {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(Constants.CONTENT_TYPE_JSON + ";charset=UTF-8");
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", message);
            err.put("error_code", "ERR_PRESCRIPTION_FAILED");
            response.getWriter().print(gson.toJson(err));
        } else {
            request.setAttribute("errorMessage", message);
            doGet(request, response);
        }
    }
}
