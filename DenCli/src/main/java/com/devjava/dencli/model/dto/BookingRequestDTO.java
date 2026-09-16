/**
 * File: BookingRequestDTO.java
 * Mục đích: Đối tượng truyền dữ liệu (DTO) tiếp nhận thông tin yêu cầu đặt lịch hẹn từ Client.
 */
package com.devjava.dencli.model.dto;

import java.util.ArrayList;
import java.util.List;

public class BookingRequestDTO {

    private Integer doctorId;
    private String appointmentDate;
    private String appointmentTime;
    private String notes;
    private List<Integer> serviceIds = new ArrayList<>();

    public BookingRequestDTO() {
    }

    /**
     * Constructor khởi tạo đối tượng yêu cầu đặt lịch với các tham số chính.
     */
    public BookingRequestDTO(Integer doctorId, String appointmentDate, String appointmentTime, 
                             String notes, List<Integer> serviceIds) {
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.notes = notes;
        this.serviceIds = serviceIds;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Integer> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Integer> serviceIds) {
        this.serviceIds = serviceIds;
    }
}
