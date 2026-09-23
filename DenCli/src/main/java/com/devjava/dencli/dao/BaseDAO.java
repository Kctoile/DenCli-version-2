package com.devjava.dencli.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Lớp trừu tượng cơ sở cho toàn bộ các DAO trong hệ thống.
 * Cung cấp kết nối cơ sở dữ liệu từ Connection Pool và các hàm đóng tài nguyên an toàn.
 */
public abstract class BaseDAO {

    /**
     * Mở và trả về kết nối từ DBConnection pool.
     */
    protected Connection getConnection() throws ClassNotFoundException, SQLException {
        return DBConnection.getConnection();
    }

    /**
     * Đóng an toàn ResultSet, Statement, Connection.
     */
    protected void close(Connection conn, Statement stmt, ResultSet rs) {
        DBConnection.closeResultSet(rs);
        DBConnection.closeStatement(stmt);
        DBConnection.closeConnection(conn);
    }

    /**
     * Rollback an toàn cho giao dịch khi phát sinh ngoại lệ.
     */
    protected void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
        }
    }
}
