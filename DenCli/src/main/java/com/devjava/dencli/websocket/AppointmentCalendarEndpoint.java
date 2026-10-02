package com.devjava.dencli.websocket;

import com.google.gson.Gson;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * WebSocket Server Endpoint hỗ trợ đồng bộ trạng thái khung giờ khám thời gian thực (Real-time Appointment Calendar).
 * Khi một ca khám được đặt thành công, endpoint sẽ phát broadcast tới tất cả các client đang xem cùng Bác sĩ và Ngày khám.
 */
@ServerEndpoint("/ws/appointment-calendar/{doctorId}/{date}")
public class AppointmentCalendarEndpoint {

    private static final Logger LOGGER = Logger.getLogger(AppointmentCalendarEndpoint.class.getName());
    private static final Gson GSON = new Gson();

    // Map lưu trữ topic: key = "{doctorId}_{date}", value = Set các WebSocket Session đang mở
    private static final Map<String, Set<Session>> TOPIC_SESSIONS = new ConcurrentHashMap<>();

    private String topicKey;

    @OnOpen
    public void onOpen(Session session, @PathParam("doctorId") String doctorId, @PathParam("date") String date) {
        this.topicKey = buildKey(doctorId, date);
        TOPIC_SESSIONS.computeIfAbsent(this.topicKey, k -> Collections.newSetFromMap(new ConcurrentHashMap<>())).add(session);
        LOGGER.info("[WebSocket Calendar] Client " + session.getId() + " subscribed to: " + this.topicKey);
    }

    @OnClose
    public void onClose(Session session) {
        if (this.topicKey != null) {
            Set<Session> sessions = TOPIC_SESSIONS.get(this.topicKey);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    TOPIC_SESSIONS.remove(this.topicKey);
                }
            }
        }
        LOGGER.info("[WebSocket Calendar] Client disconnected: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.warning("[WebSocket Calendar] Error on " + (session != null ? session.getId() : "unknown") + ": " + throwable.getMessage());
        onClose(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if ("ping".equalsIgnoreCase(message)) {
            try {
                session.getBasicRemote().sendText("pong");
            } catch (IOException ignored) {}
        }
    }

    /**
     * Phát broadcast thông báo khung giờ vừa được đặt tới toàn bộ client đang theo dõi cùng Bác sĩ và Ngày hẹn.
     */
    public static void broadcastSlotBooked(int doctorId, String date, String timeSlot) {
        String key = buildKey(String.valueOf(doctorId), date);
        Set<Session> sessions = TOPIC_SESSIONS.get(key);
        if (sessions == null || sessions.isEmpty()) {
            return;
        }

        String formattedSlot = (timeSlot != null && timeSlot.length() >= 5) ? timeSlot.substring(0, 5) : timeSlot;

        Map<String, Object> payload = new HashMap<>();
        payload.put("event", "SLOT_BOOKED");
        payload.put("doctorId", doctorId);
        payload.put("date", date);
        payload.put("timeSlot", formattedSlot);
        payload.put("message", "Khung giờ " + formattedSlot + " vừa được đặt thành công!");

        String json = GSON.toJson(payload);

        for (Session s : sessions) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(json);
                } catch (IOException e) {
                    LOGGER.warning("[WebSocket Calendar] Failed to send slot update to " + s.getId() + ": " + e.getMessage());
                    try {
                        s.close();
                    } catch (IOException ignored) {}
                }
            }
        }
    }

    public static String buildKey(String doctorId, String date) {
        return (doctorId != null ? doctorId.trim() : "") + "_" + (date != null ? date.trim() : "");
    }

    public static Map<String, Set<Session>> getTopicSessions() {
        return TOPIC_SESSIONS;
    }
}
