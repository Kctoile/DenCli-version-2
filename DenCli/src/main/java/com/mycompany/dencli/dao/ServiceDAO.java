/**
 * Purpose: DAO class for managing 'services' table queries in SQL Server.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.dao;

import com.mycompany.dencli.models.Service;
import com.mycompany.dencli.utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    /**
     * Hàm lấy toàn bộ danh sách dịch vụ nha khoa hiện có trong cơ sở dữ liệu.
     */
    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>(); // Khởi tạo ArrayList từ thư viện Java
        String sql = "SELECT * FROM services ORDER BY service_name ASC";

        // Thực hiện truy vấn kết nối CSDL và lấy dữ liệu dịch vụ
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); // Chuẩn bị câu lệnh JDBC
             ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn của JDBC

            while (rs.next()) { // Lặp qua ResultSet thu được từ JDBC
                Service svc = new Service(
                    rs.getInt("service_id"),
                    rs.getString("service_name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("duration_minutes")
                );

                list.add(svc); // Thêm phần tử dịch vụ vào danh sách ArrayList
            }

        } catch (Exception e) {
            e.printStackTrace(); // In vết lỗi ngoại lệ
        }

        return list;
    }
}
