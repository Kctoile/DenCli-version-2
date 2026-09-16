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

    private static volatile UserService userService;
    private static volatile AppointmentService appointmentService;
    private static volatile PrescriptionService prescriptionService;
    private static volatile ExaminationService examinationService;
    private static volatile BillingService billingService;

    // Khởi tạo constructor private để ngăn tạo đối tượng từ bên ngoài
    private ServiceFactory() {
    }

    /**
     * Phương thức lấy thể hiện duy nhất (Singleton) của UserService.
     * @return Đối tượng UserService
     */
    public static UserService getUserService() {
        if (userService == null) {
            synchronized (ServiceFactory.class) {
                if (userService == null) {
                    userService = new UserServiceImpl();
                }
            }
        }

        return userService;
    }

    /**
     * Phương thức lấy thể hiện duy nhất của AppointmentService.
     * @return Đối tượng AppointmentService
     */
    public static AppointmentService getAppointmentService() {
        if (appointmentService == null) {
            synchronized (ServiceFactory.class) {
                if (appointmentService == null) {
                    appointmentService = new AppointmentServiceImpl();
                }
            }
        }

        return appointmentService;
    }

    /**
     * Phương thức lấy thể hiện duy nhất của PrescriptionService.
     * @return Đối tượng PrescriptionService
     */
    public static PrescriptionService getPrescriptionService() {
        if (prescriptionService == null) {
            synchronized (ServiceFactory.class) {
                if (prescriptionService == null) {
                    prescriptionService = new PrescriptionServiceImpl();
                }
            }
        }

        return prescriptionService;
    }

    /**
     * Phương thức lấy thể hiện duy nhất của ExaminationService.
     * @return Đối tượng ExaminationService
     */
    public static ExaminationService getExaminationService() {
        if (examinationService == null) {
            synchronized (ServiceFactory.class) {
                if (examinationService == null) {
                    examinationService = new ExaminationServiceImpl();
                }
            }
        }

        return examinationService;
    }

    /**
     * Phương thức lấy thể hiện duy nhất của BillingService.
     * @return Đối tượng BillingService
     */
    public static BillingService getBillingService() {
        if (billingService == null) {
            synchronized (ServiceFactory.class) {
                if (billingService == null) {
                    billingService = new BillingServiceImpl();
                }
            }
        }

        return billingService;
    }

    /**
     * Phương thức thiết lập UserService giả lập hỗ trợ chạy Unit Test.
     * @param service Mock hoặc Custom UserService
     */
    public static void setUserService(UserService service) {
        userService = service;
    }

    /**
     * Phương thức thiết lập AppointmentService giả lập hỗ trợ chạy Unit Test.
     * @param service Mock hoặc Custom AppointmentService
     */
    public static void setAppointmentService(AppointmentService service) {
        appointmentService = service;
    }

    /**
     * Phương thức thiết lập PrescriptionService giả lập hỗ trợ chạy Unit Test.
     * @param service Mock hoặc Custom PrescriptionService
     */
    public static void setPrescriptionService(PrescriptionService service) {
        prescriptionService = service;
    }

    /**
     * Phương thức thiết lập ExaminationService giả lập hỗ trợ chạy Unit Test.
     * @param service Mock hoặc Custom ExaminationService
     */
    public static void setExaminationService(ExaminationService service) {
        examinationService = service;
    }

    /**
     * Phương thức thiết lập BillingService giả lập hỗ trợ chạy Unit Test.
     * @param service Mock hoặc Custom BillingService
     */
    public static void setBillingService(BillingService service) {
        billingService = service;
    }
}
