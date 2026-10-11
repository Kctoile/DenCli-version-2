/**
 * File: ServiceFactory.java
 * Mục đích: Triển khai Factory Pattern cung cấp các thể hiện (instances) của tầng Service cho Controller.
 */
package com.devjava.dencli.service;

import com.devjava.dencli.service.impl.AppointmentServiceImpl;
import com.devjava.dencli.service.impl.BillingServiceImpl;
import com.devjava.dencli.service.impl.ExaminationServiceImpl;
import com.devjava.dencli.service.impl.PrescriptionServiceImpl;
import com.devjava.dencli.service.impl.UserServiceImpl;

public final class ServiceFactory {

    // ponytail: synchronized methods over volatile compound objects (Sonar S3077)
    private static UserService userService;
    private static AppointmentService appointmentService;
    private static PrescriptionService prescriptionService;
    private static ExaminationService examinationService;
    private static BillingService billingService;

    // Khởi tạo constructor private để ngăn tạo đối tượng từ bên ngoài
    private ServiceFactory() {
    }

    /**
     * Phương thức lấy thể hiện duy nhất (Singleton) của UserService.
     * @return Đối tượng UserService
     */
    public static synchronized UserService getUserService() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        return userService;
    }

    /**
     * Phương thức lấy thể hiện duy nhất của AppointmentService.
     * @return Đối tượng AppointmentService
     */
    public static synchronized AppointmentService getAppointmentService() {
        if (appointmentService == null) {
            appointmentService = new AppointmentServiceImpl();
        }
        return appointmentService;
    }

    /**
     * Phương thức lấy thể hiện duy nhất của PrescriptionService.
     * @return Đối tượng PrescriptionService
     */
    public static synchronized PrescriptionService getPrescriptionService() {
        if (prescriptionService == null) {
            prescriptionService = new PrescriptionServiceImpl();
        }
        return prescriptionService;
    }

    /**
     * Phương thức lấy thể hiện duy nhất của ExaminationService.
     * @return Đối tượng ExaminationService
     */
    public static synchronized ExaminationService getExaminationService() {
        if (examinationService == null) {
            examinationService = new ExaminationServiceImpl();
        }
        return examinationService;
    }

    /**
     * Phương thức lấy thể hiện duy nhất của BillingService.
     * @return Đối tượng BillingService
     */
    public static synchronized BillingService getBillingService() {
        if (billingService == null) {
            billingService = new BillingServiceImpl();
        }
        return billingService;
    }

    /**
     * Phương thức thiết lập UserService giả lập hỗ trợ chạy Unit Test.
     * @param service Mock hoặc Custom UserService
     */
    public static synchronized void setUserService(UserService service) {
        userService = service;
    }

    /**
     * Phương thức thiết lập AppointmentService giả lập hỗ trợ chạy Unit Test.
     * @param service Mock hoặc Custom AppointmentService
     */
    public static synchronized void setAppointmentService(AppointmentService service) {
        appointmentService = service;
    }

    /**
     * Phương thức thiết lập PrescriptionService giả lập hỗ trợ chạy Unit Test.
     * @param service Mock hoặc Custom PrescriptionService
     */
    public static synchronized void setPrescriptionService(PrescriptionService service) {
        prescriptionService = service;
    }

    /**
     * Phương thức thiết lập ExaminationService giả lập hỗ trợ chạy Unit Test.
     * @param service Mock hoặc Custom ExaminationService
     */
    public static synchronized void setExaminationService(ExaminationService service) {
        examinationService = service;
    }

    /**
     * Phương thức thiết lập BillingService giả lập hỗ trợ chạy Unit Test.
     * @param service Mock hoặc Custom BillingService
     */
    public static synchronized void setBillingService(BillingService service) {
        billingService = service;
    }
}
