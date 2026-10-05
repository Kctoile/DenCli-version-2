package com.devjava.dencli.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Tiện ích tạo URL thanh toán và kiểm tra chữ ký số cổng thanh toán VNPAY (UC mở rộng).
 * Sử dụng thuật toán mã hóa tiêu chuẩn HMAC-SHA512 từ thư viện Java chuẩn (javax.crypto).
 */
public final class VnPayUtil {

    public static final String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String VNP_TMN_CODE = getConfig("VNP_TMN_CODE", "");
    public static final String VNP_HASH_SECRET = getConfig("VNP_HASH_SECRET", "");

    private VnPayUtil() {
    }

    private static String getConfig(String key, String def) {
        String val = System.getProperty(key);
        if (val == null || val.isBlank()) val = System.getenv(key);
        return val != null && !val.isBlank() ? val : def;
    }

    /**
     * Tạo URL chuyển hướng tới cổng thanh toán VNPAY.
     */
    public static String createPaymentUrl(long amountVnd, String orderInfo, String returnUrl, String ipAddress, String txnRef) {
        if (VNP_TMN_CODE.isBlank() || VNP_HASH_SECRET.isBlank()) {
            throw new IllegalStateException("VNPAY credentials are not configured.");
        }
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderType = "other";
        long vnp_Amount = amountVnd * 100; // VNPAY tính số tiền nhân với 100

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNP_TMN_CODE);
        vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", txnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpAddr", ipAddress);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(new Date());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Sắp xếp các tham số theo thứ tự bảng chữ cái A-Z
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (i < fieldNames.size() - 1) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = hmacSHA512(VNP_HASH_SECRET, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        return VNP_PAY_URL + "?" + query.toString();
    }

    /**
     * Xác thực chữ ký số phản hồi từ VNPAY.
     */
    public static boolean verifySignature(Map<String, String> fields, String secureHash) {
        if (fields == null || secureHash == null || secureHash.isBlank() || VNP_HASH_SECRET.isBlank()) {
            return false;
        }
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            if ("vnp_SecureHash".equals(fieldName) || "vnp_SecureHashType".equals(fieldName)) {
                continue;
            }
            String fieldValue = fields.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                if (!hashData.isEmpty()) {
                    hashData.append('&');
                }
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            }
        }

        String expectedHash = hmacSHA512(VNP_HASH_SECRET, hashData.toString());
        return MessageDigest.isEqual(expectedHash.toLowerCase().getBytes(StandardCharsets.US_ASCII),
                secureHash.toLowerCase().getBytes(StandardCharsets.US_ASCII));
    }

    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte aByte : bytes) {
                String hex = Integer.toHexString(0xff & aByte);
                if (hex.length() == 1) hash.append('0');
                hash.append(hex);
            }
            return hash.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
