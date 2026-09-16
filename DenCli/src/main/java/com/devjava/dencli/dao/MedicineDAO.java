/**
 * File: MedicineDAO.java
 * Mục đích: Interface định nghĩa các phương thức thao tác dữ liệu với bảng 'medicines' (kho thuốc).
 */
package com.devjava.dencli.dao;

import com.devjava.dencli.model.Medicine;
import java.sql.Connection;
import java.util.List;

public interface MedicineDAO {

    /**
     * Phương thức lấy toàn bộ danh sách thuốc hiện có trong kho.
     * @return Danh sách thuốc
     */
    List<Medicine> getAllMedicines();

    /**
     * Phương thức tìm kiếm thông tin thuốc theo mã định danh (medicine_id).
     * @param medicineId Mã thuốc
     * @return Đối tượng Medicine hoặc null nếu không tồn tại
     */
    Medicine getMedicineById(int medicineId);

    /**
     * Phương thức tìm kiếm và lấy danh sách thuốc có hỗ trợ phân trang SQL Server.
     * @param searchKeyword Từ khóa tìm kiếm theo tên thuốc (có thể null hoặc rỗng)
     * @param offset Số bản ghi bỏ qua
     * @param limit Số bản ghi tối đa
     * @return Danh sách thuốc của trang
     */
    List<Medicine> getMedicinesWithPagination(String searchKeyword, int offset, int limit);

    /**
     * Phương thức đếm tổng số lượng loại thuốc theo từ khóa tìm kiếm.
     * @param searchKeyword Từ khóa tìm kiếm
     * @return Tổng số loại thuốc
     */
    int countMedicines(String searchKeyword);

    /**
     * Phương thức thêm mới một loại thuốc vào kho.
     * @param medicine Đối tượng thuốc cần thêm
     * @return true nếu thêm thành công, ngược lại false
     */
    boolean insertMedicine(Medicine medicine);

    /**
     * Phương thức cập nhật thông tin loại thuốc (tên, đơn giá, số lượng tồn kho).
     * @param medicine Đối tượng thuốc với thông tin mới
     * @return true nếu cập nhật thành công, ngược lại false
     */
    boolean updateMedicine(Medicine medicine);

    /**
     * Phương thức trừ số lượng tồn kho của thuốc khi được kê đơn, hỗ trợ nhận Connection để tham gia Transaction.
     * @param medicineId Mã thuốc
     * @param quantityDeducted Số lượng thuốc cần trừ
     * @param conn Kết nối JDBC dùng chung của Transaction (nếu null sẽ tự tạo kết nối riêng)
     * @return true nếu trừ tồn kho thành công (và số lượng tồn kho đủ để trừ)
     */
    boolean deductStockQuantity(int medicineId, int quantityDeducted, Connection conn);

    /**
     * Phương thức xóa một loại thuốc khỏi kho theo mã định danh.
     * @param medicineId Mã thuốc cần xóa
     * @return true nếu xóa thành công, ngược lại false
     */
    boolean deleteMedicine(int medicineId);
}
