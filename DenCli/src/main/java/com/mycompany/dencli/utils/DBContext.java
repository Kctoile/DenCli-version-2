package com.mycompany.dencli.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    private static final String HOST = "localhost";
    private static final String PORT = "1433"; // Cổng mặc định của SQL Server
    private static final String DB_NAME = "Dental";
    private static final String USER = "sa"; // Tài khoản SQL Server của bạn (vui lòng đổi nếu cần)
    private static final String PASSWORD = "123"; // Mật khẩu SQL Server của bạn

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        // Khai báo Driver kết nối của Microsoft SQL Server
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        // Chuỗi kết nối JDBC bypass SSL certificate cho môi trường localhost
        String url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;", 
                HOST, PORT, DB_NAME);
        
        return DriverManager.getConnection(url, USER, PASSWORD);
    }

    // Đoạn code kiểm tra kết nối nhanh tại console
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("Chúc mừng! Kết nối cơ sở dữ liệu [Dental] thành công.");
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }
}
