/**
 * File: ServiceFactory.java
 * Mục đích: Cung cấp các thể hiện (instances) của tầng Service cho Controller và hỗ trợ injection cho Unit Tests.
 */
package com.devjava.dencli.service;

public final class ServiceFactory {

    private static UserService userService = new UserService();
    private static AppointmentService appointmentService = new AppointmentService();
    private static PrescriptionService prescriptionService = new PrescriptionService();
    private static ExaminationService examinationService = new ExaminationService();
    private static BillingService billingService = new BillingService();

    private ServiceFactory() {
    }

    public static UserService getUserService() {
        return userService;
    }

    public static void setUserService(UserService service) {
        userService = service;
    }

    public static AppointmentService getAppointmentService() {
        return appointmentService;
    }

    public static void setAppointmentService(AppointmentService service) {
        appointmentService = service;
    }

    public static PrescriptionService getPrescriptionService() {
        return prescriptionService;
    }

    public static void setPrescriptionService(PrescriptionService service) {
        prescriptionService = service;
    }

    public static ExaminationService getExaminationService() {
        return examinationService;
    }

    public static void setExaminationService(ExaminationService service) {
        examinationService = service;
    }

    public static BillingService getBillingService() {
        return billingService;
    }

    public static void setBillingService(BillingService service) {
        billingService = service;
    }
}
