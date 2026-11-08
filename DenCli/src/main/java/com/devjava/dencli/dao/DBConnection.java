package com.devjava.dencli.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.devjava.dencli.util.Constants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static HikariDataSource dataSource;

    private DBConnection() {
    }

    private static synchronized HikariDataSource getDataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            String url = String.format(
                "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;characterEncoding=UTF-8;",
                Constants.DB_HOST,
                Constants.DB_PORT,
                Constants.DB_NAME
            );
            config.setJdbcUrl(url);
            config.setUsername(Constants.DB_USER);
            config.setPassword(Constants.DB_PASSWORD);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(5000);
            config.setPoolName("DenCliHikariPool");
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        if (Constants.DB_PASSWORD.isBlank()) {
            throw new SQLException("DENCLI_DB_PASSWORD is not configured for SQL Server user "
                    + Constants.DB_USER + ".");
        }

        try {
            return getDataSource().getConnection();
        } catch (Exception poolEx) {
            LOGGER.warning("HikariCP pool unavailable, fallback to DriverManager: " + poolEx.getMessage());
            String url = String.format(
                "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;characterEncoding=UTF-8;",
                Constants.DB_HOST,
                Constants.DB_PORT,
                Constants.DB_NAME
            );
            return DriverManager.getConnection(url, Constants.DB_USER, Constants.DB_PASSWORD);
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }
}
