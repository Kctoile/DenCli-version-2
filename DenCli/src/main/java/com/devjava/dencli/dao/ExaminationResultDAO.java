/**
 * File: ExaminationResultDAO.java
 * Mục đích: Interface định nghĩa các phương thức thao tác dữ liệu với bảng 'examination_results' và 'prescribed_services'.
 */
package com.devjava.dencli.dao;

import com.devjava.dencli.model.ExaminationResult;
import com.devjava.dencli.model.PrescribedService;
import java.sql.Connection;
import java.util.List;

public interface ExaminationResultDAO {

    /**
     * Phương thức thêm mới kết quả khám bệnh, hỗ trợ truyền Connection để tham gia Transaction.
     * @param result Đối tượng kết quả khám
     * @param conn Kết nối JDBC của Transaction (nếu null sẽ tạo kết nối riêng)
     * @return Mã result_id vừa được sinh ra, hoặc -1 nếu thất bại
     */
    int insertExaminationResult(ExaminationResult result, Connection conn);

    /**
     * Phương thức tìm kiếm kết quả khám bệnh theo mã cuộc hẹn (appointment_id).
     * @param appointmentId Mã cuộc hẹn
     * @return Đối tượng ExaminationResult hoặc null nếu chưa có kết quả
     */
    ExaminationResult getExaminationResultByAppointmentId(int appointmentId);

    /**
     * Phương thức thêm dịch vụ được chỉ định thêm trong khi khám bệnh (prescribed_services).
     * @param prescribedService Đối tượng dịch vụ chỉ định
     * @param conn Kết nối JDBC dùng chung của Transaction
     * @return true nếu thêm thành công, ngược lại false
     */
    boolean insertPrescribedService(PrescribedService prescribedService, Connection conn);

    /**
     * Phương thức lấy danh sách các dịch vụ được chỉ định thêm theo mã kết quả khám.
     * @param resultId Mã kết quả khám
     * @return Danh sách dịch vụ chỉ định kèm đơn giá
     */
    List<PrescribedService> getPrescribedServicesByResultId(int resultId);
}
