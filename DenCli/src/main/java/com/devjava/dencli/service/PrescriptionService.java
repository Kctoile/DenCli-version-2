/**
 * File: PrescriptionService.java
 * Mục đích: Interface định nghĩa các phương thức xử lý nghiệp vụ kê đơn thuốc, quản lý kho thuốc và Transaction trừ kho.
 */
package com.devjava.dencli.service;

import com.devjava.dencli.model.Medicine;
import com.devjava.dencli.model.Prescription;
import java.util.List;

public interface PrescriptionService {

    /**
     * Phương thức kê đơn thuốc và trừ số lượng kho thuốc tương ứng trong một Transaction cơ sở dữ liệu duy nhất.
     * Tầng Service mở 1 Connection, tắt autoCommit, truyền Connection vào các DAO và gọi commit/rollback trong try-catch-finally.
     * @param prescription Đối tượng đơn thuốc chứa danh sách các chi tiết thuốc
     * @return true nếu kê đơn và trừ kho thành công, false nếu thuốc không đủ tồn kho hoặc có lỗi
     */
    boolean createPrescriptionWithStockDeduction(Prescription prescription);

    /**
     * Phương thức lấy thông tin đơn thuốc theo mã kết quả khám.
     * @param resultId Mã kết quả khám
     * @return Đối tượng Prescription kèm danh sách chi tiết thuốc
     */
    Prescription getPrescriptionByResultId(int resultId);

    /**
     * Phương thức lấy toàn bộ danh mục thuốc trong kho.
     * @return Danh sách thuốc
     */
    List<Medicine> getAllMedicines();

    /**
     * Phương thức tìm kiếm danh mục thuốc có phân trang.
     * @param keyword Từ khóa tìm kiếm tên thuốc
     * @param page Trang hiện tại
     * @param pageSize Số bản ghi mỗi trang
     * @return Danh sách thuốc
     */
    List<Medicine> searchMedicines(String keyword, int page, int pageSize);

    /**
     * Phương thức đếm số lượng loại thuốc theo từ khóa.
     * @param keyword Từ khóa
     * @return Tổng số loại thuốc
     */
    int countMedicines(String keyword);

    /**
     * Phương thức thêm mới một loại thuốc vào kho.
     * @param medicine Đối tượng thuốc cần thêm
     * @return true nếu thêm thành công
     */
    boolean addMedicine(Medicine medicine);

    /**
     * Phương thức cập nhật thông tin thuốc (tên, giá, tồn kho).
     * @param medicine Đối tượng thuốc chứa thông tin mới
     * @return true nếu cập nhật thành công
     */
    boolean updateMedicine(Medicine medicine);
}
