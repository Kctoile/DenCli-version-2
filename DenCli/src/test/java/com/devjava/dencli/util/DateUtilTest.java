/**
 * File: DateUtilTest.java
 * Mục đích: Unit test kiểm thử các phương thức phân tích và chuyển đổi ngày giờ của DateUtil.
 */
package com.devjava.dencli.util;

import java.sql.Date;
import java.sql.Time;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateUtilTest {

    /**
     * Phương thức kiểm thử việc parse chuỗi ngày hợp lệ.
     */
    @Test
    public void testParseDateValid() {
        Date d = DateUtil.parseDate("2026-08-15");
        assertNotNull(d);
        assertEquals("2026-08-15", DateUtil.formatDate(d));
    }

    /**
     * Phương thức kiểm thử việc parse chuỗi ngày không hợp lệ.
     */
    @Test
    public void testParseDateInvalid() {
        assertNull(DateUtil.parseDate("invalid-date"));
        assertNull(DateUtil.parseDate(null));
        assertNull(DateUtil.parseDate("   "));
    }

    /**
     * Phương thức kiểm thử việc parse chuỗi giờ hợp lệ cả 5 ký tự và 8 ký tự.
     */
    @Test
    public void testParseTimeValid() {
        Time t1 = DateUtil.parseTime("08:30");
        assertNotNull(t1);

        Time t2 = DateUtil.parseTime("14:45:00");
        assertNotNull(t2);
    }
}
