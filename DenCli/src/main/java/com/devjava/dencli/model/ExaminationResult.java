/**
 * File: ExaminationResult.java
 * Mục đích: Thực thể đại diện cho bảng 'examination_results' (kết quả khám bệnh) trong cơ sở dữ liệu.
 */
package com.devjava.dencli.model;

import java.sql.Timestamp;

public class ExaminationResult {

    private int resultId;
    private Integer appointmentId;
    private String resultDetails;
    private Timestamp examinationDate;

    // Các trường hỗ trợ hiển thị
    private String patientName;
    private String doctorName;

    public ExaminationResult() {
    }

    /**
     * Constructor khởi tạo đối tượng ExaminationResult với đầy đủ các trường cơ bản.
     */
    public ExaminationResult(int resultId, Integer appointmentId, String resultDetails, Timestamp examinationDate) {
        this.resultId = resultId;
        this.appointmentId = appointmentId;
        this.resultDetails = resultDetails;
        this.examinationDate = examinationDate;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getResultDetails() {
        return resultDetails;
    }

    public void setResultDetails(String resultDetails) {
        this.resultDetails = resultDetails;
    }

    public Timestamp getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(Timestamp examinationDate) {
        this.examinationDate = examinationDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
