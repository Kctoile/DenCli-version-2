package com.devjava.dencli.util;

import com.devjava.dencli.dao.DBConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Bộ lập lịch nền (Background Scheduler) tự động quét và hủy các lịch hẹn Pending quá hạn 24 giờ.
 * Chạy định kỳ mỗi 60 phút bằng ScheduledExecutorService chuẩn Java (stdlib).
 */
@WebListener
public class AppointmentSchedulerListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppointmentSchedulerListener.class.getName());
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("[Scheduler] Starting background scheduler daemon...");

        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "DenCli-Scheduler-Thread");
            t.setDaemon(true);
            return t;
        });

        // Chạy lần đầu sau 1 phút, định kỳ mỗi 60 phút
        scheduler.scheduleAtFixedRate(this::cleanExpiredPendingAppointments, 1, 60, TimeUnit.MINUTES);
    }

    private void cleanExpiredPendingAppointments() {
        String sql = "UPDATE appointments SET status = 'Cancelled', notes = CONCAT(ISNULL(notes, ''), ' [Hệ thống tự động hủy do quá hạn xác nhận 24h]') "
                   + "WHERE status = 'Pending' AND created_at < DATEADD(hour, -24, GETDATE())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int updatedRows = ps.executeUpdate();
            if (updatedRows > 0) {
                LOGGER.info("[Scheduler] Auto-cancelled " + updatedRows + " expired pending appointment(s).");
            }
        } catch (Exception e) {
            LOGGER.warning("[Scheduler] Scheduled task error: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("[Scheduler] Shutting down background scheduler...");
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }
}
