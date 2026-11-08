/**
 * File: ConsultationResultDTO.java
 * Mục đích: Đối tượng truyền dữ liệu (DTO) phản hồi kết quả xử lý phiên khám bệnh lâm sàng nguyên tử:
 *           Trạng thái thành công, mã bệnh án (result_id), mã đơn thuốc (prescription_id) và cờ hoàn thành lịch hẹn.
 */
package com.devjava.dencli.model.dto;

import java.io.Serializable;

public class ConsultationResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private int resultId;
    private int prescriptionId;
    private int appointmentId;
    private boolean appointmentCompleted;

    public ConsultationResultDTO() {
    }

    public ConsultationResultDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ConsultationResultDTO failure(String message) {
        return new ConsultationResultDTO(false, message);
    }

    public static ConsultationResultDTO success(int resultId, int prescriptionId, int appointmentId, boolean appointmentCompleted, String message) {
        ConsultationResultDTO dto = new ConsultationResultDTO(true, message);
        dto.setResultId(resultId);
        dto.setPrescriptionId(prescriptionId);
        dto.setAppointmentId(appointmentId);
        dto.setAppointmentCompleted(appointmentCompleted);
        return dto;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public boolean isAppointmentCompleted() {
        return appointmentCompleted;
    }

    public void setAppointmentCompleted(boolean appointmentCompleted) {
        this.appointmentCompleted = appointmentCompleted;
    }
}

