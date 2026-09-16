/**
 * File: BillingService.java
 * Mục đích: Interface định nghĩa các phương thức xử lý nghiệp vụ thanh toán viện phí, tính tổng tiền dịch vụ và tiền thuốc.
 */
package com.devjava.dencli.service;

import com.devjava.dencli.model.dto.InvoiceResponseDTO;

public interface BillingService {

    /**
     * Phương thức tính toán toàn bộ chi phí khám chữa bệnh (tiền dịch vụ + tiền thuốc) để lập hóa đơn viện phí.
     * @param appointmentId Mã lịch hẹn
     * @return DTO chứa thông tin bệnh nhân, bác sĩ, các dịch vụ, các loại thuốc và tổng số tiền
     */
    InvoiceResponseDTO calculateInvoice(int appointmentId);

    /**
     * Phương thức xác nhận thanh toán hóa đơn và đánh dấu lịch hẹn hoàn tất (Completed).
     * @param appointmentId Mã lịch hẹn
     * @return true nếu thanh toán và cập nhật trạng thái thành công
     */
    boolean payInvoice(int appointmentId);
}
