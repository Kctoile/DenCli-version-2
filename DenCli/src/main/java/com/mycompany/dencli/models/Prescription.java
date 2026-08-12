/**
 * Purpose: Model class representing the 'prescriptions' table in SQL Server.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.models;

public class Prescription {
    private int prescriptionId;
    private int resultId;
    private String instructions;

    public Prescription() {
    }

    /**
     * Constructor khởi tạo đối tượng Prescription với đầy đủ thuộc tính.
     */
    public Prescription(int prescriptionId, int resultId, String instructions) {
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

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * Hàm ghi đè phương thức toString để in thông tin đơn thuốc.
     */
    @Override // Ghi đè phương thức toString từ lớp Object để xuất dữ liệu dạng chuỗi
    public String toString() {
        return "Prescription{" + "prescriptionId=" + prescriptionId + ", resultId=" + resultId + '}';
    }
}
