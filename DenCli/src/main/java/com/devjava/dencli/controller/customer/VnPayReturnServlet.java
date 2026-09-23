package com.devjava.dencli.controller.customer;

import com.devjava.dencli.service.BillingService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet tiếp nhận callback kết quả giao dịch thanh toán từ VNPAY.
 */
@WebServlet(name = "VnPayReturnServlet", urlPatterns = {"/payment/vnpay-return"})
public class VnPayReturnServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String responseCode = request.getParameter("vnp_ResponseCode");
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
                    "Giao dịch VNPAY không thành công hoặc đã bị hủy (Mã phản hồi: " + responseCode + ").");
        }

        response.sendRedirect(request.getContextPath() + "/customer/invoice?appointment_id=" + appointmentId);
    }
}
