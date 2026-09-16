/**
 * Purpose: Model class representing the 'medicines' table in SQL Server.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.models;

import java.io.Serializable;

public class Medicine implements Serializable {
    private static final long serialVersionUID = 1L;

    private int medicineId;
    private String medicineName;
    private double price;
    private int stockQuantity;

    public Medicine() {
    }

    /**
     * Constructor khởi tạo đối tượng Medicine với đầy đủ các thuộc tính.
     */
    public Medicine(int medicineId, String medicineName, double price, int stockQuantity) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Hàm ghi đè phương thức toString để in thông tin thuốc.
     */
    @Override // Ghi đè phương thức toString từ lớp Object để xuất dữ liệu dạng chuỗi
    public String toString() {
        return "Medicine{" + "medicineId=" + medicineId + ", medicineName=" + medicineName + ", price=" + price + '}';
    }
}
