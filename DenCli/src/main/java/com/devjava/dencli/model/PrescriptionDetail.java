/**
 * File: PrescriptionDetail.java
 * Mục đích: Thực thể đại diện cho bảng 'prescription_details' (chi tiết đơn thuốc) trong cơ sở dữ liệu.
 */
package com.devjava.dencli.model;

import java.math.BigDecimal;

public class PrescriptionDetail {

    private int prescriptionId;
    private int medicineId;
    private int prescribedQuantity;
    private Integer purchasedQuantity;
    private BigDecimal unitPrice;

    // Thuộc tính tiện ích phục vụ hiển thị
    private String medicineName;

    public PrescriptionDetail() {
    }

    /**
     * Constructor khởi tạo đối tượng PrescriptionDetail với đầy đủ thông tin chi tiết.
     */
    public PrescriptionDetail(int prescriptionId, int medicineId, int prescribedQuantity, 
                              Integer purchasedQuantity, BigDecimal unitPrice) {
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.prescribedQuantity = prescribedQuantity;
        this.purchasedQuantity = purchasedQuantity;
        this.unitPrice = unitPrice;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getPrescribedQuantity() {
        return prescribedQuantity;
    }

    public void setPrescribedQuantity(int prescribedQuantity) {
        this.prescribedQuantity = prescribedQuantity;
    }

    public Integer getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public void setPurchasedQuantity(Integer purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
}
