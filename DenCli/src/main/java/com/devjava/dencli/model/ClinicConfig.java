/**
 * File: ClinicConfig.java
 * Mục đích: Thực thể đại diện cho bảng 'clinic_configs' (cấu hình giờ mở cửa và thông tin phòng khám).
 */
package com.devjava.dencli.model;

import java.sql.Time;

public class ClinicConfig {

    private int configId;
    private Time openingTime;
    private Time closingTime;
    private String clinicInfo;

    public ClinicConfig() {
    }

    /**
     * Constructor khởi tạo đối tượng cấu hình phòng khám.
     */
    public ClinicConfig(int configId, Time openingTime, Time closingTime, String clinicInfo) {
        this.configId = configId;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.clinicInfo = clinicInfo;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Time openingTime) {
        this.openingTime = openingTime;
    }

    public Time getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Time closingTime) {
        this.closingTime = closingTime;
    }

    public String getClinicInfo() {
        return clinicInfo;
    }

    public void setClinicInfo(String clinicInfo) {
        this.clinicInfo = clinicInfo;
    }
}
