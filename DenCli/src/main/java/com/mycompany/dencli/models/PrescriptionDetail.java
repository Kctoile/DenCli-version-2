/**
 * Purpose: Model class representing the 'prescription_details' table in SQL Server.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.models;

import java.io.Serializable;

public class PrescriptionDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private int prescriptionId;
    private int medicineId;
    private int prescribedQuantity;
    private int purchasedQuantity;
    private double unitPrice;

    public PrescriptionDetail() {
    }

    /**
     * Constructor khởi tạo đối tượng PrescriptionDetail với đầy đủ các thuộc tính.
     */
    public PrescriptionDetail(int prescriptionId, int medicineId, int prescribedQuantity, int purchasedQuantity, double unitPrice) {
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

    public int getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public void setPurchasedQuantity(int purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * Hàm ghi đè phương thức toString để in thông tin chi tiết đơn thuốc.
     */
    @Override // Ghi đè phương thức toString từ lớp Object để xuất dữ liệu dạng chuỗi
    public String toString() {
        return "PrescriptionDetail{" + "prescriptionId=" + prescriptionId + ", medicineId=" + medicineId + ", prescribedQuantity=" + prescribedQuantity + ", unitPrice=" + unitPrice + '}';
    }
}
