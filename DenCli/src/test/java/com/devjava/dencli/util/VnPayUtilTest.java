package com.devjava.dencli.util;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VnPayUtilTest {

    @Test
    void testCreatePaymentUrlContainsRequiredParams() {
        String url = VnPayUtil.createPaymentUrl(150000, "Thanh toan don thuoc", "http://localhost/return", "127.0.0.1", "101");

        assertNotNull(url);
        assertTrue(url.startsWith(VnPayUtil.VNP_PAY_URL));
        assertTrue(url.contains("vnp_Amount=15000000")); // Multiplied by 100
        assertTrue(url.contains("vnp_Command=pay"));
        assertTrue(url.contains("vnp_SecureHash="));
    }

    @Test
    void testHmacSHA512Consistency() {
        String key = "secret-key-123";
        String data = "vnp_Amount=10000000&vnp_Command=pay";

        String hash1 = VnPayUtil.hmacSHA512(key, data);
        String hash2 = VnPayUtil.hmacSHA512(key, data);

        assertNotNull(hash1);
        assertEquals(128, hash1.length()); // SHA512 produces 128 hex chars
        assertEquals(hash1, hash2);
    }
}
