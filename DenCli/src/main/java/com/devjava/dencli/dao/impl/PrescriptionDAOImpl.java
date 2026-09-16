/**
 * File: PrescriptionDAOImpl.java
 * Mục đích: Triển khai các phương thức truy xuất bảng 'prescriptions' và 'prescription_details' sử dụng JDBC thuần.
 */
package com.devjava.dencli.dao.impl;

import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.PrescriptionDAO;
import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.model.PrescriptionDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAOImpl implements PrescriptionDAO {

    /**
     * Phương thức thêm mới một bản ghi đơn thuốc vào bảng prescriptions.
     * @param prescription Đối tượng đơn thuốc
     * @param conn Kết nối JDBC của Transaction (hoặc null)
     * @return Mã prescription_id vừa được sinh ra, hoặc -1 nếu thất bại
     */
    @Override // Ghi đè phương thức insertPrescription từ interface PrescriptionDAO
    public int insertPrescription(Prescription prescription, Connection conn) {
        String sql = "INSERT INTO prescriptions (result_id, instructions) VALUES (?, ?)";

        if (conn != null) {
            // Khi có Connection từ Transaction bên ngoài, sử dụng try-with-resources cho PreparedStatement
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                if (prescription.getResultId() != null) {
                    ps.setInt(1, prescription.getResultId());
                } else {
                    ps.setNull(1, java.sql.Types.INTEGER);
                }

                ps.setString(2, prescription.getInstructions());

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            return rsKeys.getInt(1);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1;
        } else {
            // Tự mở Connection bằng try-with-resources khi không có Transaction ngoài
            try (Connection localConn = DBConnection.getConnection();
                 PreparedStatement ps = localConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                if (prescription.getResultId() != null) {
                    ps.setInt(1, prescription.getResultId());
                } else {
                    ps.setNull(1, java.sql.Types.INTEGER);
                }

                ps.setString(2, prescription.getInstructions());

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            return rsKeys.getInt(1);
                        }
                    }
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            return -1;
        }
    }

    /**
     * Phương thức thêm một bản ghi chi tiết đơn thuốc vào bảng prescription_details.
     * @param detail Đối tượng chi tiết thuốc
     * @param conn Kết nối JDBC của Transaction (hoặc null)
     * @return true nếu thêm thành công
     */
    @Override // Ghi đè phương thức insertPrescriptionDetail từ interface PrescriptionDAO
    public boolean insertPrescriptionDetail(PrescriptionDetail detail, Connection conn) {
        String sql = "INSERT INTO prescription_details (prescription_id, medicine_id, prescribed_quantity, purchased_quantity, unit_price) "
                   + "VALUES (?, ?, ?, ?, ?)";

        if (conn != null) {
            // Quản lý PreparedStatement trên Connection chung của Transaction
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, detail.getPrescriptionId());
                ps.setInt(2, detail.getMedicineId());
                ps.setInt(3, detail.getPrescribedQuantity());

                if (detail.getPurchasedQuantity() != null) {
                    ps.setInt(4, detail.getPurchasedQuantity());
                } else {
                    ps.setInt(4, detail.getPrescribedQuantity());
                }

                ps.setBigDecimal(5, detail.getUnitPrice());

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // Mở kết nối riêng khi không có Transaction
            try (Connection localConn = DBConnection.getConnection();
                 PreparedStatement ps = localConn.prepareStatement(sql)) {

                ps.setInt(1, detail.getPrescriptionId());
                ps.setInt(2, detail.getMedicineId());
                ps.setInt(3, detail.getPrescribedQuantity());

                if (detail.getPurchasedQuantity() != null) {
                    ps.setInt(4, detail.getPurchasedQuantity());
                } else {
                    ps.setInt(4, detail.getPrescribedQuantity());
                }

                ps.setBigDecimal(5, detail.getUnitPrice());

                return ps.executeUpdate() > 0;

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Phương thức lấy đơn thuốc theo mã kết quả khám (result_id).
     * @param resultId Mã kết quả khám
     * @return Đối tượng Prescription kèm danh sách chi tiết thuốc
     */
    @Override // Ghi đè phương thức getPrescriptionByResultId từ interface PrescriptionDAO
    public Prescription getPrescriptionByResultId(int resultId) {
        String sql = "SELECT * FROM prescriptions WHERE result_id = ?";

        // Sử dụng try-with-resources để tự động giải phóng Connection
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, resultId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Prescription pres = new Prescription();
                    pres.setPrescriptionId(rs.getInt("prescription_id"));
                    pres.setResultId(rs.getInt("result_id"));
                    pres.setInstructions(rs.getString("instructions"));

                    // Lấy luôn danh sách chi tiết đơn thuốc
                    pres.setDetails(getPrescriptionDetails(pres.getPrescriptionId()));

                    return pres;
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Phương thức lấy danh sách chi tiết thuốc trong một đơn thuốc kèm tên thuốc.
     * @param prescriptionId Mã đơn thuốc
     * @return Danh sách các chi tiết thuốc
     */
    @Override // Ghi đè phương thức getPrescriptionDetails từ interface PrescriptionDAO
    public List<PrescriptionDetail> getPrescriptionDetails(int prescriptionId) {
        List<PrescriptionDetail> list = new ArrayList<>();
        String sql = "SELECT pd.*, m.medicine_name "
                   + "FROM prescription_details pd "
                   + "INNER JOIN medicines m ON pd.medicine_id = m.medicine_id "
                   + "WHERE pd.prescription_id = ?";

        // Sử dụng try-with-resources đóng tài nguyên tự động
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, prescriptionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrescriptionDetail pd = new PrescriptionDetail();
                    pd.setPrescriptionId(rs.getInt("prescription_id"));
                    pd.setMedicineId(rs.getInt("medicine_id"));
                    pd.setPrescribedQuantity(rs.getInt("prescribed_quantity"));

                    int purchased = rs.getInt("purchased_quantity");
                    if (!rs.wasNull()) {
                        pd.setPurchasedQuantity(purchased);
                    }

                    pd.setUnitPrice(rs.getBigDecimal("unit_price"));
                    pd.setMedicineName(rs.getString("medicine_name"));

                    list.add(pd);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
