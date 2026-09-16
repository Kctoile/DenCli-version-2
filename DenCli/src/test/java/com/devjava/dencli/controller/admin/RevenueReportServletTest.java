/**
 * File: RevenueReportServletTest.java
 * Package: com.devjava.dencli.controller.admin
 * Mục đích: Unit test kiểm thử API endpoint trả về JSON báo cáo doanh thu 12 tháng bằng JUnit 5 và Mockito.
 */
package com.devjava.dencli.controller.admin;

import com.devjava.dencli.model.dto.RevenueDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RevenueReportServletTest {

    private RevenueReportServlet revenueReportServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AppointmentService appointmentService;

    @BeforeEach
    public void setUp() {
        revenueReportServlet = new RevenueReportServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        appointmentService = mock(AppointmentService.class);

        ServiceFactory.setAppointmentService(appointmentService);
    }

    /**
     * Kiểm thử GET /admin/api/revenue trả về đúng định dạng JSON và dữ liệu doanh thu các tháng.
     */
    @Test
    public void testDoGetReturnsJsonRevenueReport() throws Exception {
        when(request.getParameter("year")).thenReturn("2026");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        List<RevenueDTO> mockList = new ArrayList<>();
        RevenueDTO r1 = new RevenueDTO();
        r1.setMonth(1);
        r1.setRevenue(new BigDecimal("15000000"));
        r1.setTotalAppointments(25);
        mockList.add(r1);

        when(appointmentService.getMonthlyRevenueReport(2026)).thenReturn(mockList);

        revenueReportServlet.doGet(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");

        pw.flush();
        String jsonResult = sw.toString();
        assertTrue(jsonResult.contains("\"success\":true"));
        assertTrue(jsonResult.contains("\"year\":2026"));
        assertTrue(jsonResult.contains("15000000"));
        assertTrue(jsonResult.contains("\"totalAppointments\":25"));
    }
}
