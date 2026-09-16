/**
 * File: Prescription.java
 * Mục đích: Thực thể đại diện cho bảng 'prescriptions' (đơn thuốc) trong cơ sở dữ liệu.
 */
package com.devjava.dencli.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    private int prescriptionId;
    private Integer resultId;
    private String instructions;

    // Danh sách các loại thuốc chi tiết trong đơn
    private List<PrescriptionDetail> details = new ArrayList<>();

    public Prescription() {
    }

    /**
     * Constructor khởi tạo đơn thuốc với mã đơn, mã kết quả khám và hướng dẫn sử dụng.
     */
    public Prescription(int prescriptionId, Integer resultId, String instructions) {
        this.prescriptionId = prescriptionId;
        this.resultId = resultId;
        this.instructions = instructions;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<PrescriptionDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PrescriptionDetail> details) {
        this.details = details;
    }
}
