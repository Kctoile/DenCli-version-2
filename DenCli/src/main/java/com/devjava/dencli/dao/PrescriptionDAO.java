/**
 * File: PrescriptionDAO.java
 * Mục đích: Interface định nghĩa các phương thức thao tác dữ liệu với bảng 'prescriptions' và 'prescription_details'.
 */
package com.devjava.dencli.dao;

import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.model.PrescriptionDetail;
import java.sql.Connection;
import java.util.List;

public interface PrescriptionDAO {

    /**
     * Phương thức tạo mới một đơn thuốc gắn với kết quả khám, hỗ trợ truyền Connection cho Transaction.
     * @param prescription Đối tượng đơn thuốc
     * @param conn Kết nối JDBC của Transaction (hoặc null)
     * @return Mã prescription_id vừa sinh, hoặc -1 nếu thất bại
     */
    int insertPrescription(Prescription prescription, Connection conn);

    /**
     * Phương thức thêm một dòng chi tiết thuốc vào đơn thuốc, hỗ trợ truyền Connection cho Transaction.
     * @param detail Đối tượng chi tiết đơn thuốc
     * @param conn Kết nối JDBC của Transaction (hoặc null)
     * @return true nếu thêm thành công, ngược lại false
     */
    boolean insertPrescriptionDetail(PrescriptionDetail detail, Connection conn);

    /**
     * Phương thức tìm kiếm đơn thuốc theo mã kết quả khám (result_id).
     * @param resultId Mã kết quả khám
     * @return Đối tượng Prescription kèm danh sách chi tiết thuốc
     */
    Prescription getPrescriptionByResultId(int resultId);

    /**
     * Phương thức lấy danh sách các chi tiết thuốc theo mã đơn thuốc (prescription_id).
     * @param prescriptionId Mã đơn thuốc
     * @return Danh sách chi tiết đơn thuốc kèm tên thuốc
     */
    List<PrescriptionDetail> getPrescriptionDetails(int prescriptionId);
}
