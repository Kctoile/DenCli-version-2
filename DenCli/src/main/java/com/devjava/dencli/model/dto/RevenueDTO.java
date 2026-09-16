/**
 * File: RevenueDTO.java
 * Mục đích: Đối tượng truyền dữ liệu (DTO) biểu diễn số liệu doanh thu theo tháng phục vụ biểu đồ Chart.js.
 */
package com.devjava.dencli.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RevenueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int year;
    private int month;
    private String monthLabel;
    private BigDecimal revenue = BigDecimal.ZERO;
    private int totalAppointments;

    public RevenueDTO() {
    }

    /**
     * Constructor khởi tạo đối tượng thống kê doanh thu.
     */
    public RevenueDTO(int year, int month, String monthLabel, BigDecimal revenue, int totalAppointments) {
        this.year = year;
        this.month = month;
        this.monthLabel = monthLabel;
        this.revenue = revenue;
        this.totalAppointments = totalAppointments;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMonthLabel() {
        return monthLabel;
    }

    public void setMonthLabel(String monthLabel) {
        this.monthLabel = monthLabel;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }
}
