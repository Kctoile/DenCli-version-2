/**
 * File: ConsultationRequestDTO.java
 * Mục đích: Đối tượng truyền dữ liệu (DTO) đóng gói toàn bộ yêu cầu của phiên khám bệnh lâm sàng:
 *           Chẩn đoán, lịch tái khám, dịch vụ chỉ định thêm, đơn thuốc và chỉ thị hoàn tất lịch hẹn.
 */
package com.devjava.dencli.model.dto;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.devjava.dencli.model.PrescribedService;
import com.devjava.dencli.model.PrescriptionDetail;

public class ConsultationRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int appointmentId;
    private int doctorId;
    private String diagnosis;
    private Date revisitDate;
    private String revisitNote;
    private List<PrescribedService> additionalServices = new ArrayList<>();
    private List<PrescriptionDetail> prescriptionDetails = new ArrayList<>();
    private String prescriptionInstructions;
    private boolean completeAppointment = true;

    public ConsultationRequestDTO() {
    }

    public ConsultationRequestDTO(int appointmentId, int doctorId, String diagnosis) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Date getRevisitDate() {
        return revisitDate;
    }

    public void setRevisitDate(Date revisitDate) {
        this.revisitDate = revisitDate;
    }

    public String getRevisitNote() {
        return revisitNote;
    }

    public void setRevisitNote(String revisitNote) {
        this.revisitNote = revisitNote;
    }

    public List<PrescribedService> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(List<PrescribedService> additionalServices) {
        this.additionalServices = (additionalServices != null) ? additionalServices : new ArrayList<>();
    }

    public List<PrescriptionDetail> getPrescriptionDetails() {
        return prescriptionDetails;
    }

    public void setPrescriptionDetails(List<PrescriptionDetail> prescriptionDetails) {
        this.prescriptionDetails = (prescriptionDetails != null) ? prescriptionDetails : new ArrayList<>();
    }

    public String getPrescriptionInstructions() {
        return prescriptionInstructions;
    }

    public void setPrescriptionInstructions(String prescriptionInstructions) {
        this.prescriptionInstructions = prescriptionInstructions;
    }

    public boolean isCompleteAppointment() {
        return completeAppointment;
    }

    public void setCompleteAppointment(boolean completeAppointment) {
        this.completeAppointment = completeAppointment;
    }
}

