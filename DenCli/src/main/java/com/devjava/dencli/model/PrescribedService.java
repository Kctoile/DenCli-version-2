/**
 * File: PrescribedService.java
 * Mục đích: Thực thể đại diện cho bảng 'prescribed_services' (dịch vụ chỉ định trong khi khám) trong cơ sở dữ liệu.
 */
package com.devjava.dencli.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PrescribedService implements Serializable {

    private static final long serialVersionUID = 1L;

    private int resultId;
    private int serviceId;
    private String status;
    private String notes;

    // Các trường tiện ích hiển thị
    private String serviceName;
    private BigDecimal price;

    public PrescribedService() {
    }

    /**
     * Constructor khởi tạo đối tượng PrescribedService với các khóa và thuộc tính chính.
     */
    public PrescribedService(int resultId, int serviceId, String status, String notes) {
        this.resultId = resultId;
        this.serviceId = serviceId;
        this.status = status;
        this.notes = notes;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
