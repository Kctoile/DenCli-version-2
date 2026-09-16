/**
 * Purpose: Model class representing the 'examination_results' table in SQL Server.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class ExaminationResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private int resultId;
    private int appointmentId;
    private String resultDetails;
    private Timestamp examinationDate;

    public ExaminationResult() {
    }

    /**
     * Constructor khởi tạo đối tượng ExaminationResult với đầy đủ các thuộc tính.
     */
    public ExaminationResult(int resultId, int appointmentId, String resultDetails, Timestamp examinationDate) {
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

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
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

    /**
     * Hàm ghi đè phương thức toString để in thông tin kết quả khám.
     */
    @Override // Ghi đè phương thức toString từ lớp Object để xuất dữ liệu dạng chuỗi
    public String toString() {
        return "ExaminationResult{" + "resultId=" + resultId + ", appointmentId=" + appointmentId + ", examinationDate=" + examinationDate + '}';
    }
}
