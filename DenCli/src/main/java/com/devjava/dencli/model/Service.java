/**
 * File: Service.java
 * Mục đích: Thực thể đại diện cho bảng 'services' (dịch vụ nha khoa) trong cơ sở dữ liệu.
 */
package com.devjava.dencli.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Service implements Serializable {

    private static final long serialVersionUID = 1L;

    private int serviceId;
    private String serviceName;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;

    public Service() {
    }

    /**
     * Constructor khởi tạo đối tượng Service với đầy đủ thông tin.
     */
    public Service(int serviceId, String serviceName, String description, BigDecimal price, Integer durationMinutes) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
