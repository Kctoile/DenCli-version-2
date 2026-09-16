/**
 * Purpose: Model class representing the 'appointments' table in SQL Server.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.models;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;

    private int appointmentId;
    private int patientId;
    private int doctorId;
    private Date appointmentDate;
    private Time appointmentTime;
    private String status;
    private String notes;
    private String room;

    public Appointment() {
    }

    /**
     * Constructor khởi tạo đối tượng Appointment với đầy đủ thuộc tính.
     */
    public Appointment(int appointmentId, int patientId, int doctorId, Date appointmentDate, Time appointmentTime, String status, String notes, String room) {
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

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
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

    /**
     * Hàm ghi đè phương thức toString để in thông tin lịch hẹn.
     */
    @Override // Ghi đè phương thức toString từ lớp Object để xuất dữ liệu dạng chuỗi
    public String toString() {
        return "Appointment{" + "appointmentId=" + appointmentId + ", patientId=" + patientId + ", doctorId=" + doctorId + ", appointmentDate=" + appointmentDate + ", appointmentTime=" + appointmentTime + ", status=" + status + '}';
    }
}
