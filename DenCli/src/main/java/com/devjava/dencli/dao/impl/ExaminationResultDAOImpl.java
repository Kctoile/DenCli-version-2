/**
 * File: ExaminationResultDAOImpl.java
 * Mục đích: Triển khai các phương thức truy xuất bảng 'examination_results' và 'prescribed_services' sử dụng JDBC thuần.
 */
package com.devjava.dencli.dao.impl;

import com.devjava.dencli.dao.DBConnection;
import com.devjava.dencli.dao.ExaminationResultDAO;
import com.devjava.dencli.model.ExaminationResult;
import com.devjava.dencli.model.PrescribedService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ExaminationResultDAOImpl implements ExaminationResultDAO {

    /**
     * Phương thức thêm mới một bản ghi kết quả khám lâm sàng vào bảng examination_results.
     * @param result Đối tượng kết quả khám
     * @param conn Kết nối JDBC của Transaction (hoặc null)
     * @return Mã result_id vừa sinh, hoặc -1 nếu thất bại
     */
    @Override // Ghi đè phương thức insertExaminationResult từ interface ExaminationResultDAO
    public int insertExaminationResult(ExaminationResult result, Connection conn) {
        String sql = "INSERT INTO examination_results (appointment_id, result_details, examination_date) VALUES (?, ?, ?)";

        if (conn != null) {
            // Khi có kết nối từ Transaction bên ngoài, sử dụng try-with-resources để quản lý PreparedStatement
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                if (result.getAppointmentId() != null) {
                    ps.setInt(1, result.getAppointmentId());
                } else {
                    ps.setNull(1, java.sql.Types.INTEGER);
                }

                ps.setString(2, result.getResultDetails());

                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                ps.setTimestamp(3, (result.getExaminationDate() != null) ? result.getExaminationDate() : currentTimestamp);

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
            // Khi không có Transaction ngoài, tự mở kết nối bằng try-with-resources
            try (Connection localConn = DBConnection.getConnection();
                 PreparedStatement ps = localConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                if (result.getAppointmentId() != null) {
                    ps.setInt(1, result.getAppointmentId());
                } else {
                    ps.setNull(1, java.sql.Types.INTEGER);
                }

                ps.setString(2, result.getResultDetails());

                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                ps.setTimestamp(3, (result.getExaminationDate() != null) ? result.getExaminationDate() : currentTimestamp);

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
     * Phương thức tìm kiếm kết quả khám bệnh gắn liền với một lịch hẹn.
     * @param appointmentId Mã lịch hẹn
     * @return Đối tượng ExaminationResult hoặc null nếu chưa khám
     */
    @Override // Ghi đè phương thức getExaminationResultByAppointmentId từ interface ExaminationResultDAO
    public ExaminationResult getExaminationResultByAppointmentId(int appointmentId) {
        String sql = "SELECT er.*, p.full_name AS patient_name, d.full_name AS doctor_name "
                   + "FROM examination_results er "
                   + "INNER JOIN appointments a ON er.appointment_id = a.appointment_id "
                   + "LEFT JOIN users p ON a.patient_id = p.user_id "
                   + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                   + "WHERE er.appointment_id = ?";

        // Sử dụng try-with-resources để tự động đóng kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ExaminationResult res = new ExaminationResult();
                    res.setResultId(rs.getInt("result_id"));
                    res.setAppointmentId(rs.getInt("appointment_id"));
                    res.setResultDetails(rs.getString("result_details"));
                    res.setExaminationDate(rs.getTimestamp("examination_date"));
                    res.setPatientName(rs.getString("patient_name"));
                    res.setDoctorName(rs.getString("doctor_name"));

                    return res;
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Phương thức thêm dịch vụ được chỉ định thêm vào bảng prescribed_services.
     * @param prescribedService Đối tượng dịch vụ chỉ định
     * @param conn Kết nối JDBC của Transaction (hoặc null)
     * @return true nếu thêm thành công
     */
    @Override // Ghi đè phương thức insertPrescribedService từ interface ExaminationResultDAO
    public boolean insertPrescribedService(PrescribedService prescribedService, Connection conn) {
        String sql = "INSERT INTO prescribed_services (result_id, service_id, status, notes) VALUES (?, ?, ?, ?)";

        if (conn != null) {
            // Quản lý PreparedStatement trên Connection dùng chung của Transaction
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, prescribedService.getResultId());
                ps.setInt(2, prescribedService.getServiceId());
                ps.setString(3, prescribedService.getStatus());
                ps.setString(4, prescribedService.getNotes());

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // Mở kết nối riêng khi không có Transaction
            try (Connection localConn = DBConnection.getConnection();
                 PreparedStatement ps = localConn.prepareStatement(sql)) {

                ps.setInt(1, prescribedService.getResultId());
                ps.setInt(2, prescribedService.getServiceId());
                ps.setString(3, prescribedService.getStatus());
                ps.setString(4, prescribedService.getNotes());

                return ps.executeUpdate() > 0;

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Phương thức lấy danh sách các dịch vụ chỉ định theo mã kết quả khám.
     * @param resultId Mã kết quả khám
     * @return Danh sách dịch vụ chỉ định kèm đơn giá
     */
    @Override // Ghi đè phương thức getPrescribedServicesByResultId từ interface ExaminationResultDAO
    public List<PrescribedService> getPrescribedServicesByResultId(int resultId) {
        List<PrescribedService> list = new ArrayList<>();
        String sql = "SELECT ps.*, s.service_name, s.price FROM prescribed_services ps "
                   + "INNER JOIN services s ON ps.service_id = s.service_id "
                   + "WHERE ps.result_id = ?";

        // Sử dụng try-with-resources đóng tài nguyên tự động
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, resultId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrescribedService psModel = new PrescribedService();
                    psModel.setResultId(rs.getInt("result_id"));
                    psModel.setServiceId(rs.getInt("service_id"));
                    psModel.setStatus(rs.getString("status"));
                    psModel.setNotes(rs.getString("notes"));
                    psModel.setServiceName(rs.getString("service_name"));
                    psModel.setPrice(rs.getBigDecimal("price"));

                    list.add(psModel);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
