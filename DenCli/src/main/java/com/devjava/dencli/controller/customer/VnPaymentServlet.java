package com.devjava.dencli.controller.customer;

import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.User;
import com.devjava.dencli.model.dto.InvoiceResponseDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.BillingService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.VnPayUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet khởi tạo thanh toán viện phí trực tuyến qua cổng VNPAY.
 */
@WebServlet(name = "VnPaymentServlet", urlPatterns = {"/payment/vnpay"})
public class VnPaymentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String appStr = request.getParameter("appointment_id");
        int appointmentId = 0;
        try {
            if (appStr != null && !appStr.isBlank()) {
                appointmentId = Integer.parseInt(appStr.trim());
            }
        } catch (NumberFormatException ignored) {
        }

        if (appointmentId <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã lịch hẹn không hợp lệ.");
            return;
        }

        AppointmentService appointmentService = ServiceFactory.getAppointmentService();
        Appointment appointment = appointmentService.getAppointmentDetails(appointmentId);
        if (appointment == null || (appointment.getPatientId() != currentUser.getUserId() && currentUser.getRoleId() != Constants.ROLE_STAFF_ID && currentUser.getRoleId() != Constants.ROLE_ADMIN_ID)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thực hiện thanh toán cho lịch hẹn này.");
            return;
        }

        BillingService billingService = ServiceFactory.getBillingService();
        InvoiceResponseDTO invoice = billingService.calculateInvoice(appointmentId);
        long amount = (invoice.getGrandTotal() != null) ? invoice.getGrandTotal().longValue() : 0L;
        if (amount <= 0) {
            response.sendRedirect(request.getContextPath() + "/customer/invoice?appointment_id=" + appointmentId);
            return;
        }

        String orderInfo = "Thanh toan vien phi DenCli cuoc hen #" + appointmentId;
        String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/payment/vnpay-return";
        String ipAddress = request.getRemoteAddr();
        String txnRef = appointmentId + "_" + System.currentTimeMillis();

        String paymentUrl = VnPayUtil.createPaymentUrl(amount, orderInfo, returnUrl, ipAddress, txnRef);
        response.sendRedirect(paymentUrl);
    }
}
