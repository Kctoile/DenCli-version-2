/**
 * File: Appointment.java
 * Mục đích: Thực thể đại diện cho bảng 'appointments' (lịch hẹn khám) trong cơ sở dữ liệu.
 */
package com.devjava.dencli.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    private int appointmentId;
    private Integer patientId;
    private Integer doctorId;
    private Date appointmentDate;
    private Time appointmentTime;
    private String status;
    private String notes;
    private String room;

    // Các thuộc tính tiện ích phục vụ hiển thị trên giao diện View/JSP
    private String patientName;
    private String patientPhone;
    private String doctorName;
    private List<Service> services = new ArrayList<>();

    public Appointment() {
    }

    /**
     * Constructor khởi tạo đối tượng Appointment với các trường cơ bản từ bảng appointments.
     */
    public Appointment(int appointmentId, Integer patientId, Integer doctorId, 
                       Date appointmentDate, Time appointmentTime, String status, String notes, String room) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.notes = notes;
        this.room = room;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
