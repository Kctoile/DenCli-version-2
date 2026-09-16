/**
 * File: Medicine.java
 * Mục đích: Thực thể đại diện cho bảng 'medicines' (kho thuốc) trong cơ sở dữ liệu.
 */
package com.devjava.dencli.model;

import java.math.BigDecimal;

public class Medicine {

    private int medicineId;
    private String medicineName;
    private BigDecimal price;
    private int stockQuantity;

    public Medicine() {
    }

    /**
     * Constructor khởi tạo đối tượng Medicine với đầy đủ các thuộc tính.
     */
    public Medicine(int medicineId, String medicineName, BigDecimal price, int stockQuantity) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
