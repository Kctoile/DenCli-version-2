/**
 * File: DBConnection.java
 * Mục đích: Cung cấp kết nối cơ sở dữ liệu Microsoft SQL Server sử dụng JDBC DriverManager thuần.
 */
package com.devjava.dencli.dao;

import com.devjava.dencli.util.Constants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public final class DBConnection {

    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

    // Khởi tạo constructor private để ngăn chặn khởi tạo đối tượng từ bên ngoài
    private DBConnection() {
    }

    /**
     * Phương thức mở và trả về một kết nối cơ sở dữ liệu mới tới SQL Server thông qua DriverManager.
     * @return Đối tượng java.sql.Connection đang mở
     * @throws ClassNotFoundException khi không tìm thấy driver SQL Server
     * @throws SQLException khi kết nối thất bại
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if (Constants.DB_PASSWORD.isBlank()) {
            throw new SQLException("DENCLI_DB_PASSWORD is not configured for SQL Server user "
                    + Constants.DB_USER + ".");
        }

        // Nạp Driver của Microsoft SQL Server vào bộ nhớ máy ảo Java
        Class.forName(DRIVER_CLASS);

        // Xây dựng chuỗi URL kết nối với tham số bỏ qua kiểm tra chứng chỉ SSL cục bộ
        String url = String.format(
            "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;characterEncoding=UTF-8;",
            Constants.DB_HOST,
            Constants.DB_PORT,
            Constants.DB_NAME
        );

        // Gọi phương thức getConnection từ thư viện DriverManager của JDBC để tạo kết nối
        try {
            return DriverManager.getConnection(url, Constants.DB_USER, Constants.DB_PASSWORD);
        } catch (SQLException e) {
            LOGGER.severe("SQL Server connection failed for " + Constants.DB_HOST + ":"
                    + Constants.DB_PORT + "/" + Constants.DB_NAME + " as " + Constants.DB_USER
                    + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Phương thức tiện ích hỗ trợ đóng đối tượng ResultSet an toàn.
     * @param rs Đối tượng ResultSet cần đóng
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                // Gọi hàm close từ thư viện JDBC để đóng ResultSet
                rs.close();

            } catch (SQLException e) {
                // Ghi nhận lỗi khi đóng ResultSet ra log
                e.printStackTrace();
            }
        }
    }

    /**
     * Phương thức tiện ích hỗ trợ đóng đối tượng Statement hoặc PreparedStatement an toàn.
     * @param stmt Đối tượng Statement cần đóng
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                // Gọi hàm close từ thư viện JDBC để giải phóng Statement
                stmt.close();

            } catch (SQLException e) {
                // Ghi nhận lỗi khi đóng Statement ra log
                e.printStackTrace();
            }
        }
    }

    /**
     * Phương thức tiện ích hỗ trợ đóng đối tượng Connection an toàn.
     * @param conn Đối tượng Connection cần đóng
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                // Gọi hàm close từ thư viện JDBC để giải phóng kết nối
                conn.close();

            } catch (SQLException e) {
                // Ghi nhận lỗi khi đóng Connection ra log
                e.printStackTrace();
            }
        }
    }
}
