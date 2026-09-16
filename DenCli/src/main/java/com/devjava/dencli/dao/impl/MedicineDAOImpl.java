/**
 * File: MedicineDAOImpl.java
 * Mục đích: Triển khai các phương thức truy xuất bảng 'medicines' sử dụng JDBC thuần và PreparedStatement.
 */
package com.devjava.dencli.dao.impl;

import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.MedicineDAO;
import com.devjava.dencli.model.Medicine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAOImpl implements MedicineDAO {

    /**
     * Phương thức lấy toàn bộ danh sách thuốc hiện có trong kho sắp xếp theo tên thuốc.
     * @return Danh sách các loại thuốc
     */
    @Override // Ghi đè phương thức getAllMedicines từ interface MedicineDAO
    public List<Medicine> getAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines ORDER BY medicine_name ASC";

        // Sử dụng try-with-resources để tự động giải phóng tài nguyên kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); // Chuẩn bị truy vấn qua JDBC
             ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn SELECT dữ liệu

            while (rs.next()) { // Duyệt qua tập kết quả
                medicines.add(mapResultSetToMedicine(rs)); // Thêm thuốc vào danh sách
            }

        } catch (ClassNotFoundException | SQLException e) {
            // Ghi nhận ngoại lệ khi truy vấn danh sách thuốc
            e.printStackTrace();
        }

        return medicines;
    }

    /**
     * Phương thức tìm kiếm thông tin chi tiết một loại thuốc theo mã medicine_id.
     * @param medicineId Mã thuốc
     * @return Đối tượng Medicine hoặc null nếu không tồn tại
     */
    @Override // Ghi đè phương thức getMedicineById từ interface MedicineDAO
    public Medicine getMedicineById(int medicineId) {
        String sql = "SELECT * FROM medicines WHERE medicine_id = ?";

        // Sử dụng try-with-resources để tự động giải phóng kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, medicineId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedicine(rs);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Phương thức lấy danh sách thuốc có tìm kiếm và phân trang bằng cú pháp SQL Server OFFSET ... FETCH.
     * @param searchKeyword Từ khóa tìm kiếm theo tên thuốc
     * @param offset Số bản ghi bỏ qua
     * @param limit Số bản ghi tối đa
     * @return Danh sách thuốc của trang
     */
    @Override // Ghi đè phương thức getMedicinesWithPagination từ interface MedicineDAO
    public List<Medicine> getMedicinesWithPagination(String searchKeyword, int offset, int limit) {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines "
                   + "WHERE (? IS NULL OR ? = '' OR medicine_name LIKE ?) "
                   + "ORDER BY medicine_id ASC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // Sử dụng try-with-resources đóng tài nguyên an toàn
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String keywordParam = (searchKeyword != null && !searchKeyword.trim().isEmpty()) 
                                  ? "%" + searchKeyword.trim() + "%" : "";

            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, keywordParam);
            ps.setInt(4, offset);
            ps.setInt(5, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToMedicine(rs));
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Phương thức đếm tổng số loại thuốc phù hợp với từ khóa tìm kiếm.
     * @param searchKeyword Từ khóa tìm kiếm
     * @return Tổng số loại thuốc
     */
    @Override // Ghi đè phương thức countMedicines từ interface MedicineDAO
    public int countMedicines(String searchKeyword) {
        String sql = "SELECT COUNT(*) FROM medicines "
                   + "WHERE (? IS NULL OR ? = '' OR medicine_name LIKE ?)";

        // Sử dụng try-with-resources đóng tài nguyên an toàn
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String keywordParam = (searchKeyword != null && !searchKeyword.trim().isEmpty()) 
                                  ? "%" + searchKeyword.trim() + "%" : "";

            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, keywordParam);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Phương thức thêm mới một loại thuốc vào cơ sở dữ liệu.
     * @param medicine Đối tượng thuốc cần thêm
     * @return true nếu thêm thành công
     */
    @Override // Ghi đè phương thức insertMedicine từ interface MedicineDAO
    public boolean insertMedicine(Medicine medicine) {
        String sql = "INSERT INTO medicines (medicine_name, price, stock_quantity) VALUES (?, ?, ?)";

        // Sử dụng try-with-resources đóng kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, medicine.getMedicineName());
            ps.setBigDecimal(2, medicine.getPrice());
            ps.setInt(3, medicine.getStockQuantity());

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức cập nhật thông tin loại thuốc theo medicine_id.
     * @param medicine Đối tượng thuốc với thông tin mới
     * @return true nếu cập nhật thành công
     */
    @Override // Ghi đè phương thức updateMedicine từ interface MedicineDAO
    public boolean updateMedicine(Medicine medicine) {
        String sql = "UPDATE medicines SET medicine_name = ?, price = ?, stock_quantity = ? WHERE medicine_id = ?";

        // Sử dụng try-with-resources đóng kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, medicine.getMedicineName());
            ps.setBigDecimal(2, medicine.getPrice());
            ps.setInt(3, medicine.getStockQuantity());
            ps.setInt(4, medicine.getMedicineId());

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức trừ số lượng tồn kho của thuốc khi kê đơn, đảm bảo tồn kho đủ và hỗ trợ Transaction.
     * @param medicineId Mã thuốc
     * @param quantityDeducted Số lượng trừ
     * @param conn Kết nối JDBC của Transaction (nếu null sẽ tạo kết nối riêng)
     * @return true nếu trừ kho thành công
     */
    @Override // Ghi đè phương thức deductStockQuantity từ interface MedicineDAO
    public boolean deductStockQuantity(int medicineId, int quantityDeducted, Connection conn) {
        String sql = "UPDATE medicines SET stock_quantity = stock_quantity - ? "
                   + "WHERE medicine_id = ? AND stock_quantity >= ?";

        if (conn != null) {
            // Khi có Connection từ Transaction bên ngoài, chỉ quản lý PreparedStatement bằng try-with-resources
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, quantityDeducted);
                ps.setInt(2, medicineId);
                ps.setInt(3, quantityDeducted); // Ràng buộc tồn kho hiện tại phải lớn hơn hoặc bằng số lượng trừ

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // Khi không có Transaction bên ngoài, mở Connection riêng bằng try-with-resources
            try (Connection localConn = DBConnection.getConnection();
                 PreparedStatement ps = localConn.prepareStatement(sql)) {

                ps.setInt(1, quantityDeducted);
                ps.setInt(2, medicineId);
                ps.setInt(3, quantityDeducted);

                return ps.executeUpdate() > 0;

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Phương thức xóa một loại thuốc khỏi kho.
     * @param medicineId Mã thuốc cần xóa
     * @return true nếu xóa thành công
     */
    @Override // Ghi đè phương thức deleteMedicine từ interface MedicineDAO
    public boolean deleteMedicine(int medicineId) {
        String sql = "DELETE FROM medicines WHERE medicine_id = ?";

        // Sử dụng try-with-resources đóng tài nguyên
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, medicineId);

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức phụ trợ ánh xạ một dòng ResultSet sang đối tượng Medicine.
     * @param rs Tập kết quả ResultSet
     * @return Đối tượng Medicine
     * @throws SQLException khi đọc cột lỗi
     */
    private Medicine mapResultSetToMedicine(ResultSet rs) throws SQLException {
        Medicine med = new Medicine();

        med.setMedicineId(rs.getInt("medicine_id"));
        med.setMedicineName(rs.getString("medicine_name"));
        med.setPrice(rs.getBigDecimal("price"));
        med.setStockQuantity(rs.getInt("stock_quantity"));

        return med;
    }
}
