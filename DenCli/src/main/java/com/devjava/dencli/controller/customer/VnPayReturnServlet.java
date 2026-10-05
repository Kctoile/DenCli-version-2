package com.devjava.dencli.controller.customer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.devjava.dencli.service.BillingService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.VnPayUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet tiếp nhận callback kết quả giao dịch thanh toán từ VNPAY.
 */
@WebServlet(name = "VnPayReturnServlet", urlPatterns = {"/payment/vnpay-return"})
public class VnPayReturnServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> fields = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                fields.put(key, values[0]);
            }
        });
        String secureHash = fields.get("vnp_SecureHash");
        if (!VnPayUtil.verifySignature(fields, secureHash)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Chữ ký phản hồi VNPAY không hợp lệ.");
            return;
        }

        String responseCode = fields.get("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");

        int appointmentId = 0;
        if (txnRef != null && txnRef.contains("_")) {
            try {
                appointmentId = Integer.parseInt(txnRef.split("_")[0]);
            } catch (NumberFormatException ignored) {
            }
        }

        if (appointmentId <= 0) {
            response.sendRedirect(request.getContextPath() + "/customer/profile");
            return;
        }

        BillingService billingService = ServiceFactory.getBillingService();

        // Mã phản hồi "00" đại diện cho giao dịch thành công trên cổng VNPAY
        if ("00".equals(responseCode)) {
            boolean paid = billingService.payInvoice(appointmentId);
            if (paid) {
                request.getSession().setAttribute(Constants.SESSION_SUCCESS_MESSAGE,
                        "Thanh toán trực tuyến VNPAY thành công cho cuộc hẹn #" + appointmentId + "!");
            }
        } else {
                request.getSession().setAttribute(Constants.SESSION_ERROR_MESSAGE,
                    "Giao dịch VNPAY không thành công hoặc đã bị hủy.");
        }

        response.sendRedirect(request.getContextPath() + "/customer/invoice?appointment_id=" + appointmentId);
    }
}
