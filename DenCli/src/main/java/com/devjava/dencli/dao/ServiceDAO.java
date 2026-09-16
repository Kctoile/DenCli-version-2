/**
 * File: ServiceDAO.java
 * Mục đích: Interface định nghĩa các phương thức thao tác dữ liệu với bảng 'services' (dịch vụ nha khoa).
 */
package com.devjava.dencli.dao;

import com.devjava.dencli.model.Service;
import java.util.List;

public interface ServiceDAO {

    /**
     * Phương thức lấy toàn bộ danh sách dịch vụ nha khoa hiện có.
     * @return Danh sách các dịch vụ
     */
    List<Service> getAllServices();

    /**
     * Phương thức tìm kiếm thông tin dịch vụ nha khoa theo mã dịch vụ (service_id).
     * @param serviceId Mã dịch vụ
     * @return Đối tượng Service, hoặc null nếu không tồn tại
     */
    Service getServiceById(int serviceId);

    /**
     * Phương thức lấy danh sách các dịch vụ được chọn cho một lịch hẹn cụ thể.
     * @param appointmentId Mã lịch hẹn
     * @return Danh sách dịch vụ đính kèm lịch hẹn
     */
    List<Service> getServicesByAppointmentId(int appointmentId);

    /**
     * Phương thức lấy danh sách dịch vụ có hỗ trợ phân trang SQL Server.
     * @param offset Số lượng bản ghi bỏ qua
     * @param limit Số lượng bản ghi cần lấy
     * @return Danh sách dịch vụ của trang
     */
    List<Service> getServicesWithPagination(int offset, int limit);

    /**
     * Phương thức đếm tổng số dịch vụ trong hệ thống.
     * @return Tổng số lượng dịch vụ
     */
    int countServices();

    /**
     * Phương thức thêm mới một dịch vụ nha khoa.
     * @param service Đối tượng dịch vụ cần thêm
     * @return true nếu thêm thành công, ngược lại false
     */
    boolean insertService(Service service);

    /**
     * Phương thức cập nhật thông tin dịch vụ nha khoa.
     * @param service Đối tượng dịch vụ chứa thông tin mới
     * @return true nếu cập nhật thành công, ngược lại false
     */
    boolean updateService(Service service);

    /**
     * Phương thức xóa một dịch vụ nha khoa theo mã định danh.
     * @param serviceId Mã dịch vụ cần xóa
     * @return true nếu xóa thành công, ngược lại false
     */
    boolean deleteService(int serviceId);
}
