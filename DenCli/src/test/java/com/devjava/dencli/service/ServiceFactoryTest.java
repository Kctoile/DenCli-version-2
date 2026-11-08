package com.devjava.dencli.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ServiceFactoryTest {

    @Test
    void testServiceInstancesNotNull() {
        assertNotNull(ServiceFactory.getUserService());
        assertNotNull(ServiceFactory.getAppointmentService());
        assertNotNull(ServiceFactory.getPrescriptionService());
        assertNotNull(ServiceFactory.getExaminationService());
        assertNotNull(ServiceFactory.getBillingService());
    }
}
