/**
 * File: PrescriptionDAOImpl.java
 * Mục đích: Triển khai các phương thức truy xuất bảng 'prescriptions' và 'prescription_details' sử dụng JDBC thuần.
 */
package com.devjava.dencli.dao.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.PrescriptionDAO;
import com.devjava.dencli.model.Prescription;
import com.devjava.dencli.model.PrescriptionDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrescriptionDAOImpl implements PrescriptionDAO {

    private static final Logger LOGGER = Logger.getLogger(PrescriptionDAOImpl.class.getName());

    @Override
    public boolean isResultAssignedToDoctor(int resultId, int doctorId, Connection conn) {
        String sql = "SELECT 1 FROM examination_results er "
                   + "JOIN appointments a WITH (UPDLOCK, HOLDLOCK) ON a.appointment_id = er.appointment_id "
                   + "WHERE er.result_id = ? AND a.doctor_id = ? AND a.status = 'Checked In'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resultId);
            ps.setInt(2, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Phương thức thêm mới một bản ghi đơn thuốc vào bảng prescriptions.
     * @param prescription Đối tượng đơn thuốc
     * @param conn Kết nối JDBC của Transaction (hoặc null)
     * @return Mã prescription_id vừa được sinh ra, hoặc -1 nếu thất bại
     */
    // ponytail: helper method executes insert with provided Connection (no duplicate PreparedStatement binding)
    private int executeInsertPrescription(Connection conn, Prescription prescription) throws SQLException {
        String sql = "INSERT INTO prescriptions (result_id, instructions) VALUES (?, ?)";
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
        }
        return -1;
    }

    @Override
    public int insertPrescription(Prescription prescription, Connection conn) {
        if (conn != null) {
            try {
                return executeInsertPrescription(conn, prescription);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                return -1;
            }
        }
        try (Connection localConn = DBConnection.getConnection()) {
            return executeInsertPrescription(localConn, prescription);
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }

    // ponytail: helper method executes detail insert with provided Connection
    private boolean executeInsertPrescriptionDetail(Connection conn, PrescriptionDetail detail) throws SQLException {
        String sql = "INSERT INTO prescription_details (prescription_id, medicine_id, prescribed_quantity, purchased_quantity, unit_price) "
               + "SELECT ?, medicine_id, ?, ?, price FROM medicines WHERE medicine_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getPrescriptionId());
            ps.setInt(2, detail.getPrescribedQuantity());
            ps.setInt(3, detail.getPurchasedQuantity() != null ? detail.getPurchasedQuantity() : detail.getPrescribedQuantity());
            ps.setInt(4, detail.getMedicineId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean insertPrescriptionDetail(PrescriptionDetail detail, Connection conn) {
        if (conn != null) {
            try {
                return executeInsertPrescriptionDetail(conn, detail);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                return false;
            }
        }
        try (Connection localConn = DBConnection.getConnection()) {
            return executeInsertPrescriptionDetail(localConn, detail);
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Phương thức lấy đơn thuốc theo mã kết quả khám (result_id).
     * @param resultId Mã kết quả khám
     * @return Đối tượng Prescription kèm danh sách chi tiết thuốc
     */
    @Override // Ghi đè phương thức getPrescriptionByResultId từ interface PrescriptionDAO
    public Prescription getPrescriptionByResultId(int resultId) {
        String sql = "SELECT prescription_id, result_id, instructions FROM prescriptions WHERE result_id = ?";

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
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
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
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        return list;
    }
}
