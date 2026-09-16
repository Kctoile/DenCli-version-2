/**
 * File: InvoiceServlet.java
 * Package: com.devjava.dencli.controller.staff
 * Mục đích: Servlet lập hóa đơn viện phí đa nguồn (dịch vụ đặt trước, chỉ định thêm, đơn thuốc)
 *           và xử lý thu ngân, xác nhận thanh toán (UC-13).
 */
package com.devjava.dencli.controller.staff;

import com.devjava.dencli.model.dto.InvoiceResponseDTO;
import com.devjava.dencli.service.BillingService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "StaffInvoiceServlet", urlPatterns = {"/staff/invoice"})
public class InvoiceServlet extends HttpServlet {

    private final Gson gson = new Gson();

    /**
     * GET /staff/invoice: Tính toán và hiển thị chi tiết hóa đơn viện phí cho một cuộc hẹn.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appStr = request.getParameter("appointment_id");
        int appointmentId = 0;
        if (appStr != null && !appStr.trim().isEmpty()) {
            try {
                appointmentId = Integer.parseInt(appStr.trim());
            } catch (NumberFormatException ignored) {}
        }

        if (appointmentId <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai mã cuộc hẹn.");
            return;
        }

        BillingService billingService = ServiceFactory.getBillingService();
        InvoiceResponseDTO invoice = billingService.calculateInvoice(appointmentId);

        boolean isJson = "application/json".equalsIgnoreCase(request.getHeader("Accept"));
        if (isJson) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(gson.toJson(invoice));
        } else {
            request.setAttribute("invoice", invoice);
            request.setAttribute("appointmentId", appointmentId);
            request.getRequestDispatcher("/staff/invoice.jsp").forward(request, response);
        }
    }

    /**
     * POST /staff/invoice: Xác nhận thu tiền viện phí và hoàn tất lịch hẹn.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        boolean isJson = (contentType != null && contentType.contains("application/json"))
                || "application/json".equalsIgnoreCase(request.getHeader("Accept"));

        int appointmentId = 0;
        if (isJson) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            Map<?, ?> map = gson.fromJson(sb.toString(), Map.class);
            if (map != null && map.get("appointment_id") != null) {
                appointmentId = ((Number) map.get("appointment_id")).intValue();
            }
        } else {
            String appStr = request.getParameter("appointment_id");
            if (appStr != null && !appStr.trim().isEmpty()) {
                try {
                    appointmentId = Integer.parseInt(appStr.trim());
                } catch (NumberFormatException ignored) {}
            }
        }

        if (appointmentId <= 0) {
            handleError(response, isJson, request, "Mã cuộc hẹn không hợp lệ.");
            return;
        }

        BillingService billingService = ServiceFactory.getBillingService();
        boolean success = billingService.payInvoice(appointmentId);

        if (success) {
            if (isJson) {
                response.setContentType("application/json;charset=UTF-8");
                Map<String, Object> resData = new HashMap<>();
                resData.put("success", true);
                resData.put("message", "Thanh toán viện phí thành công! Cuộc hẹn đã chuyển trạng thái Hoàn tất.");
                response.getWriter().print(gson.toJson(resData));
            } else {
                request.getSession().setAttribute(Constants.SESSION_SUCCESS_MESSAGE,
                        "Thanh toán hóa đơn #" + appointmentId + " thành công!");
                response.sendRedirect(request.getContextPath() + "/staff/reception");
            }
        } else {
            handleError(response, isJson, request, "Thanh toán thất bại hoặc cuộc hẹn không tồn tại.");
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
            response.sendRedirect(request.getContextPath() + "/staff/reception");
        }
    }
}
