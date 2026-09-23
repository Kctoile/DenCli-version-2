package com.devjava.dencli.controller.customer;

import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.User;
import com.devjava.dencli.model.dto.InvoiceResponseDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.BillingService;
import com.devjava.dencli.service.ServiceFactory;
import com.devjava.dencli.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet cho phép Bệnh nhân xem chi tiết hóa đơn viện phí, đơn thuốc và in/xuất PDF hóa đơn của mình.
 */
@WebServlet(name = "CustomerInvoiceServlet", urlPatterns = {"/customer/invoice"})
public class CustomerInvoiceServlet extends HttpServlet {

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

        // Bảo mật: Đảm bảo bệnh nhân chỉ được xem hóa đơn của chính mình
        if (appointment == null || appointment.getPatientId() != currentUser.getUserId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập hóa đơn của lịch hẹn này.");
            return;
        }

        BillingService billingService = ServiceFactory.getBillingService();
        InvoiceResponseDTO invoice = billingService.calculateInvoice(appointmentId);

        request.setAttribute("invoice", invoice);
        request.setAttribute("appointment", appointment);
        request.setAttribute("appointmentId", appointmentId);

        request.getRequestDispatcher("/customer/invoice.jsp").forward(request, response);
    }
}
