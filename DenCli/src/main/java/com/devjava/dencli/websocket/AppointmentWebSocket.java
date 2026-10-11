package com.devjava.dencli.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WebSocket Server Endpoint hỗ trợ cập nhật dữ liệu lịch hẹn thời gian thực (Real-time broadcast).
 */
@ServerEndpoint("/ws/appointment")
public class AppointmentWebSocket {

    private static final Logger LOGGER = Logger.getLogger(AppointmentWebSocket.class.getName());
    private static final Set<Session> SESSIONS = ConcurrentHashMap.newKeySet();

    @OnOpen
    public void onOpen(Session session) {
        SESSIONS.add(session);
        LOGGER.log(Level.INFO, "[WebSocket] New client connected: {0} (Total active: {1})", new Object[]{session.getId(), SESSIONS.size()});
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.log(Level.INFO, "[WebSocket] Received from {0}: {1}", new Object[]{session.getId(), message});
    }

    @OnClose
    public void onClose(Session session) {
        SESSIONS.remove(session);
        LOGGER.log(Level.INFO, "[WebSocket] Client disconnected: {0}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.log(Level.WARNING, () -> "[WebSocket] Error on " + (session != null ? session.getId() : "unknown") + ": " + throwable.getMessage());
    }

    /**
     * Gửi bản tin tới toàn bộ client đang kết nối (Reception desk, Dashboard, etc.).
     */
    public static void broadcast(String message) {
        for (Session s : SESSIONS) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    LOGGER.warning("[WebSocket] Failed to send message to " + s.getId() + ": " + e.getMessage());
                }
            }
        }
    }
}
