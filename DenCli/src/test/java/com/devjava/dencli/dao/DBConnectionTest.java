/**
 * File: DBConnectionTest.java
 * Mục đích: Unit test kiểm thử cấu hình DBConnection, Driver SQL Server và các phương thức tiện ích giải phóng tài nguyên.
 */
package com.devjava.dencli.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionTest {

    /**
     * Phương thức kiểm thử việc nạp Driver com.microsoft.sqlserver.jdbc.SQLServerDriver thành công.
     */
    @Test
    public void testDriverClassAvailable() {
        assertDoesNotThrow(() -> {
            // Nạp class Driver kiểm tra xem thư viện mssql-jdbc đã sẵn sàng trong classpath chưa
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        });
    }

    /**
     * Phương thức kiểm thử các hàm đóng tài nguyên tiện ích khi truyền giá trị null không gây ra lỗi ngoại lệ.
     */
    @Test
    public void testSafeCloseWithNull() {
        assertDoesNotThrow(() -> {
            DBConnection.closeResultSet(null);
            DBConnection.closeStatement(null);
            DBConnection.closeConnection(null);
        });
    }

    /**
     * Phương thức kiểm thử kết nối thực tế tới cơ sở dữ liệu SQL Server thành công.
     * Bỏ qua tự động trong môi trường CI/CD (GitHub Actions) do không có SQL Server.
     * ponytail: @DisabledIfEnvironmentVariable — zero deps, JUnit 5 built-in
     */
    @Test
    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    public void testGetConnectionSuccess() {
        assertDoesNotThrow(() -> {
            try (java.sql.Connection conn = DBConnection.getConnection()) {
                assertNotNull(conn);
                assertFalse(conn.isClosed());
            }
        });
    }
}
