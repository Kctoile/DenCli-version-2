package com.devjava.dencli.util;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class VnPayUtilTest {

    @Test
    void testCreatePaymentUrlContainsRequiredParams() {
        if (VnPayUtil.VNP_TMN_CODE.isBlank() || VnPayUtil.VNP_HASH_SECRET.isBlank()) {
            assertThrows(IllegalStateException.class, () -> VnPayUtil.createPaymentUrl(
                    150000, "Thanh toan don thuoc", "http://localhost/return", "127.0.0.1", "101"));
        } else {
            String url = VnPayUtil.createPaymentUrl(150000, "Thanh toan don thuoc", "http://localhost/return", "127.0.0.1", "101");
            assertTrue(url.startsWith(VnPayUtil.VNP_PAY_URL));
            assertTrue(url.contains("vnp_Amount=15000000"));
            assertTrue(url.contains("vnp_Command=pay"));
            assertTrue(url.contains("vnp_SecureHash="));
        }
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

    @Test
    void testVerifySignatureExcludesSignatureFieldsAndRejectsTampering() {
        Map<String, String> fields = new HashMap<>();
        fields.put("vnp_Amount", "10000000");
        fields.put("vnp_ResponseCode", "00");
        fields.put("vnp_SecureHashType", "SHA512");
        String signature = VnPayUtil.hmacSHA512(VnPayUtil.VNP_HASH_SECRET,
                "vnp_Amount=10000000&vnp_ResponseCode=00");
        fields.put("vnp_SecureHash", signature);

        if (VnPayUtil.VNP_HASH_SECRET.isBlank()) {
            assertFalse(VnPayUtil.verifySignature(fields, signature));
        } else {
            assertTrue(VnPayUtil.verifySignature(fields, signature));
            fields.put("vnp_Amount", "1");
            assertFalse(VnPayUtil.verifySignature(fields, signature));
        }
    }
}
