/**
 * File: BillingService.java
 * Mục đích: Triển khai các phương thức nghiệp vụ tính viện phí và xử lý thanh toán hóa đơn.
 */
package com.devjava.dencli.service;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.ExaminationResultDAO;
import com.devjava.dencli.dao.PrescriptionDAO;
import com.devjava.dencli.dao.ServiceDAO;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.ExaminationResult;
import com.devjava.dencli.model.PrescribedService;
import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.model.PrescriptionDetail;
import com.devjava.dencli.model.Service;
import com.devjava.dencli.model.dto.InvoiceResponseDTO;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.DateUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BillingService {

    private final AppointmentDAO appointmentDAO;
    private final ServiceDAO serviceDAO;
    private final ExaminationResultDAO examinationResultDAO;
    private final PrescriptionDAO prescriptionDAO;

    // Constructor mặc định khởi tạo các DAO cần thiết
    public BillingService() {
        this.appointmentDAO = new AppointmentDAO();
        this.serviceDAO = new ServiceDAO();
        this.examinationResultDAO = new ExaminationResultDAO();
        this.prescriptionDAO = new PrescriptionDAO();
    }

    // Constructor hỗ trợ tiêm phụ thuộc phục vụ kiểm thử đơn vị
    public BillingService(AppointmentDAO appointmentDAO, ServiceDAO serviceDAO,
                              ExaminationResultDAO examinationResultDAO, PrescriptionDAO prescriptionDAO) {
        this.appointmentDAO = appointmentDAO;
        this.serviceDAO = serviceDAO;
        this.examinationResultDAO = examinationResultDAO;
        this.prescriptionDAO = prescriptionDAO;
    }

    /**
     * Phương thức tổng hợp chi phí các dịch vụ đã làm và đơn thuốc để xuất hóa đơn viện phí.
     * @param appointmentId Mã cuộc hẹn
     * @return Đối tượng DTO chứa toàn bộ thông tin chi phí viện phí
     */
    public InvoiceResponseDTO calculateInvoice(int appointmentId) {
        if (appointmentId <= 0) {
            return null;
        }

        // 1. Lấy thông tin cuộc hẹn
        Appointment app = appointmentDAO.getAppointmentById(appointmentId);
        if (app == null) {
            return null;
        }

        InvoiceResponseDTO invoice = new InvoiceResponseDTO();
        invoice.setAppointmentId(appointmentId);
        invoice.setPatientId(app.getPatientId());
        invoice.setPatientName(app.getPatientName());
        invoice.setPatientPhone(app.getPatientPhone());
        invoice.setDoctorName(app.getDoctorName());
        invoice.setAppointmentDate(DateUtil.formatDate(app.getAppointmentDate()));
        invoice.setStatus(app.getStatus());

        BigDecimal servicesTotal = BigDecimal.ZERO;
        BigDecimal medicinesTotal = BigDecimal.ZERO;
        List<Service> allServices = new ArrayList<>();

        // 2. Lấy các dịch vụ đặt trước từ lịch hẹn ban đầu
        List<Service> bookedServices = serviceDAO.getServicesByAppointmentId(appointmentId);
        if (bookedServices != null) {
            for (Service s : bookedServices) {
                allServices.add(s);

                if (s.getPrice() != null) {
                    servicesTotal = servicesTotal.add(s.getPrice());
                }
            }
        }

        // 3. Kiểm tra xem đã có kết quả khám lâm sàng chưa
        ExaminationResult examResult = examinationResultDAO.getExaminationResultByAppointmentId(appointmentId);

        if (examResult != null) {
            // Lấy các dịch vụ bác sĩ chỉ định thêm trong khi khám
            List<PrescribedService> prescribedServices = examinationResultDAO.getPrescribedServicesByResultId(examResult.getResultId());

            if (prescribedServices != null) {
                for (PrescribedService ps : prescribedServices) {
                    Service s = new Service();
                    s.setServiceId(ps.getServiceId());
                    s.setServiceName(ps.getServiceName() != null ? ps.getServiceName() : ("Dịch vụ chỉ định #" + ps.getServiceId()));
                    s.setPrice(ps.getPrice() != null ? ps.getPrice() : BigDecimal.ZERO);

                    allServices.add(s);

                    if (ps.getPrice() != null) {
                        servicesTotal = servicesTotal.add(ps.getPrice());
                    }
                }
            }

            // 4. Lấy đơn thuốc gắn với kết quả khám
            Prescription prescription = prescriptionDAO.getPrescriptionByResultId(examResult.getResultId());

            if (prescription != null && prescription.getDetails() != null) {
                for (PrescriptionDetail pd : prescription.getDetails()) {
                    int qty = (pd.getPurchasedQuantity() != null) ? pd.getPurchasedQuantity() : pd.getPrescribedQuantity();
                    BigDecimal unitPrice = (pd.getUnitPrice() != null) ? pd.getUnitPrice() : BigDecimal.ZERO;
                    BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(qty));

                    medicinesTotal = medicinesTotal.add(itemTotal);
                }

                invoice.setMedicines(prescription.getDetails());
            }
        }

        invoice.setServices(allServices);
        invoice.setServicesTotal(servicesTotal);
        invoice.setMedicinesTotal(medicinesTotal);
        invoice.setGrandTotal(servicesTotal.add(medicinesTotal));

        return invoice;
    }

    /**
     * Phương thức thanh toán hóa đơn và đánh dấu lịch hẹn thành Completed.
     * @param appointmentId Mã cuộc hẹn
     * @return true nếu cập nhật thành công
     */
    public boolean payInvoice(int appointmentId) {
        if (appointmentId <= 0) {
            return false;
        }

        return appointmentDAO.updateAppointmentStatusIfCurrent(appointmentId,
            Constants.APPOINTMENT_CHECKED_IN, Constants.APPOINTMENT_COMPLETED);
    }
}
