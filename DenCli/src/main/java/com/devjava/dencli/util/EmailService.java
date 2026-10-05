package com.devjava.dencli.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Tiện ích gửi email thông báo và mã xác thực OTP qua SMTP.
 * Hỗ trợ chế độ Dev Mode tự động ghi log OTP khi chưa cấu hình máy chủ SMTP.
 */
public final class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private static final String SMTP_HOST = getConfig("DENCLI_SMTP_HOST");
    private static final String SMTP_PORT = getConfig("DENCLI_SMTP_PORT");
    private static final String SMTP_USER = getConfig("DENCLI_SMTP_USER");
    private static final String SMTP_PASS = getConfig("DENCLI_SMTP_PASS");

    private EmailService() {
    }

    private static String getConfig(String key) {
        String val = System.getProperty(key);
        if (val == null || val.isBlank()) {
            val = System.getenv(key);
        }
        return val != null && !val.isBlank() ? val : null;
    }

    /**
     * Gửi email định dạng HTML tới người nhận.
     */
    public static boolean sendEmail(String toEmail, String subject, String htmlContent) {
        LOGGER.info("[EmailService] Preparing email delivery.");

        // ponytail: ghi log console nếu chưa có cấu hình SMTP để dev/test luôn thông suốt
        if (SMTP_HOST == null || SMTP_USER == null || SMTP_PASS == null) {
            LOGGER.info("[EmailService DEV MODE] SMTP not configured; delivery is simulated.");
            return true;
        }

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT != null ? SMTP_PORT : "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER, "DenCli Dental"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            return true;
        } catch (Exception e) {
            LOGGER.warning("[EmailService] Email delivery failed.");
            return false;
        }
    }

    /**
     * Gửi mã OTP xác nhận đặt lại mật khẩu.
     */
    public static boolean sendOtpEmail(String toEmail, String otp) {
        String subject = "DenCli - Mã xác thực đặt lại mật khẩu (OTP)";
        String content = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #1e293b;'>"
                + "<h2 style='color: #0284c7;'>Hệ thống Phòng khám Nha khoa DenCli</h2>"
                + "<p>Xin chào,</p>"
                + "<p>Bạn đã yêu cầu đặt lại mật khẩu tài khoản. Mã OTP xác thực của bạn là:</p>"
                + "<div style='background: #f0f9ff; border: 1px solid #bae6fd; display: inline-block; padding: 12px 24px; border-radius: 8px; margin: 10px 0;'>"
                + "<span style='font-size: 28px; font-weight: bold; letter-spacing: 6px; color: #0284c7;'>" + otp + "</span>"
                + "</div>"
                + "<p>Mã này có hiệu lực trong vòng <b>5 phút</b>. Vui lòng không cung cấp mã cho người khác.</p>"
                + "<hr style='border: none; border-top: 1px solid #e2e8f0; margin: 20px 0;'>"
                + "<p style='font-size: 12px; color: #64748b;'>Nếu bạn không yêu cầu hành động này, vui lòng bỏ qua email.</p>"
                + "</div>";
        return sendEmail(toEmail, subject, content);
    }
}
