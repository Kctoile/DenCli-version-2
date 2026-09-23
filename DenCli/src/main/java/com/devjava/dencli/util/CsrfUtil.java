package com.devjava.dencli.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Tiện ích quản lý và xác thực CSRF Token.
 */
public final class CsrfUtil {

    public static final String CSRF_PARAM = "_csrf";
    public static final String CSRF_HEADER = "X-CSRF-TOKEN";
    public static final String CSRF_SESSION_ATTR = "csrfToken";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private CsrfUtil() {
    }

    /**
     * Lấy token hiện tại trong session hoặc sinh mới nếu chưa tồn tại.
     */
    public static String getToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        String token = (String) session.getAttribute(CSRF_SESSION_ATTR);
        if (token == null || token.isBlank()) {
            byte[] bytes = new byte[32];
            SECURE_RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute(CSRF_SESSION_ATTR, token);
        }
        return token;
    }

    /**
     * Xác thực token từ request (header hoặc form param) với token trong session.
     */
    public static boolean isValid(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String sessionToken = (String) session.getAttribute(CSRF_SESSION_ATTR);
        if (sessionToken == null || sessionToken.isBlank()) {
            return false;
        }

        String requestToken = request.getHeader(CSRF_HEADER);
        if (requestToken == null || requestToken.isBlank()) {
            requestToken = request.getParameter(CSRF_PARAM);
        }

        return sessionToken.equals(requestToken);
    }
}
