/**
 * File: InvoiceResponseDTO.java
 * Mục đích: Đối tượng truyền dữ liệu (DTO) biểu diễn chi tiết hóa đơn thanh toán viện phí của bệnh nhân.
 */
package com.devjava.dencli.model.dto;

import com.devjava.dencli.model.PrescriptionDetail;
import com.devjava.dencli.model.Service;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InvoiceResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int appointmentId;
    private String patientName;
    private String patientPhone;
    private String doctorName;
    private String appointmentDate;
    private String status;

    private BigDecimal servicesTotal = BigDecimal.ZERO;
    private BigDecimal medicinesTotal = BigDecimal.ZERO;
    private BigDecimal grandTotal = BigDecimal.ZERO;

    private List<Service> services = new ArrayList<>();
    private List<PrescriptionDetail> medicines = new ArrayList<>();

    public InvoiceResponseDTO() {
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getServicesTotal() {
        return servicesTotal;
    }

    public void setServicesTotal(BigDecimal servicesTotal) {
        this.servicesTotal = servicesTotal;
    }

    public BigDecimal getMedicinesTotal() {
        return medicinesTotal;
    }

    public void setMedicinesTotal(BigDecimal medicinesTotal) {
        this.medicinesTotal = medicinesTotal;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<PrescriptionDetail> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<PrescriptionDetail> medicines) {
        this.medicines = medicines;
    }
}
