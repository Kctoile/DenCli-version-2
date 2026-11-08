/**
 * File: ServiceFactoryTest.java
 * Mục đích: Unit test kiểm thử các thể hiện Service do ServiceFactory cung cấp.
 */
package com.devjava.dencli.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ServiceFactoryTest {

    /**
     * Kiểm thử các phương thức getter trả về thể hiện hợp lệ khác null.
     */
    @Test
    void testServiceInstancesNotNull() {
        assertNotNull(ServiceFactory.getUserService());
        assertNotNull(ServiceFactory.getAppointmentService());
        assertNotNull(ServiceFactory.getPrescriptionService());
        assertNotNull(ServiceFactory.getExaminationService());
        assertNotNull(ServiceFactory.getBillingService());
    }

    /**
     * Kiểm thử các phương thức setter tiêm phụ thuộc (Dependency Injection) cho tầng Service.
     */
    @Test
    void testServiceSetters() {
        UserService mockUser = new UserService();
        ServiceFactory.setUserService(mockUser);
        assertSame(mockUser, ServiceFactory.getUserService());

        AppointmentService mockApp = new AppointmentService();
        ServiceFactory.setAppointmentService(mockApp);
        assertSame(mockApp, ServiceFactory.getAppointmentService());

        PrescriptionService mockPres = new PrescriptionService();
        ServiceFactory.setPrescriptionService(mockPres);
        assertSame(mockPres, ServiceFactory.getPrescriptionService());

        ExaminationService mockExam = new ExaminationService();
        ServiceFactory.setExaminationService(mockExam);
        assertSame(mockExam, ServiceFactory.getExaminationService());

        BillingService mockBill = new BillingService();
        ServiceFactory.setBillingService(mockBill);
        assertSame(mockBill, ServiceFactory.getBillingService());
    }
}
