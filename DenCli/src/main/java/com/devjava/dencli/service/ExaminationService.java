/**
 * File: ExaminationService.java
 * Mục đích: Interface định nghĩa các phương thức xử lý nghiệp vụ khám bệnh, lập hồ sơ bệnh án và chỉ định dịch vụ phát sinh.
 */
package com.devjava.dencli.service;

import com.devjava.dencli.model.ExaminationResult;
import com.devjava.dencli.model.PrescribedService;
import java.util.List;

public interface ExaminationService {

    /**
     * Phương thức ghi nhận kết quả khám bệnh lâm sàng và các dịch vụ chỉ định thêm trong một Transaction duy nhất.
     * @param appointmentId Mã lịch hẹn khám
     * @param diagnosis Chi tiết chẩn đoán và hướng điều trị của bác sĩ
     * @param additionalServices Danh sách dịch vụ chỉ định thêm (có thể rỗng nếu không chỉ định)
     * @return Mã result_id vừa tạo, hoặc -1 nếu có lỗi
     */
    int recordExamination(int appointmentId, String diagnosis, List<PrescribedService> additionalServices);

    /**
     * Phương thức lấy thông tin kết quả khám của một lịch hẹn.
     * @param appointmentId Mã lịch hẹn
     * @return Đối tượng ExaminationResult hoặc null nếu chưa có
     */
    ExaminationResult getExaminationByAppointmentId(int appointmentId);

    /**
     * Phương thức lấy danh sách các dịch vụ chỉ định thêm theo mã kết quả khám.
     * @param resultId Mã kết quả khám
     * @return Danh sách các dịch vụ chỉ định
     */
    List<PrescribedService> getPrescribedServices(int resultId);
}
