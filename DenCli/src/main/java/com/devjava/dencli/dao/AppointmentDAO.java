/**
 * File: AppointmentDAO.java
 * Mục đích: Interface định nghĩa các phương thức thao tác dữ liệu với bảng 'appointments' và 'appointment_services'.
 */
package com.devjava.dencli.dao;

import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.dto.RevenueDTO;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface AppointmentDAO {

    /**
     * Phương thức kiểm tra xem bác sĩ đã có lịch hẹn nào trùng ngày và giờ hay chưa.
     * @param doctorId Mã bác sĩ phụ trách
     * @param date Ngày hẹn khám
     * @param time Giờ hẹn khám
     * @return true nếu đã có lịch trùng (khác Cancelled), ngược lại false
     */
    boolean checkDuplicateSlot(int doctorId, Date date, Time time);

    /**
     * Phương thức thêm mới một lịch hẹn khám và danh sách dịch vụ đi kèm trong một Transaction an toàn.
     * @param appointment Đối tượng lịch hẹn chứa thông tin bệnh nhân, bác sĩ, ngày giờ
     * @param serviceIds Danh sách mã dịch vụ đặt kèm
     * @return Mã appointment_id vừa tạo nếu thành công, ngược lại trả về -1
     */
    int insertAppointmentWithServices(Appointment appointment, List<Integer> serviceIds);

    /**
     * Phương thức tìm kiếm thông tin chi tiết của lịch hẹn theo mã appointment_id.
     * @param appointmentId Mã lịch hẹn
     * @return Đối tượng Appointment kèm thông tin bệnh nhân, bác sĩ
     */
    Appointment getAppointmentById(int appointmentId);

    /**
     * Phương thức lấy danh sách lịch hẹn của một bệnh nhân theo phân trang.
     * @param patientId Mã bệnh nhân
     * @param offset Số bản ghi bỏ qua
     * @param limit Số bản ghi tối đa
     * @return Danh sách lịch hẹn của bệnh nhân
     */
    List<Appointment> getAppointmentsByPatient(int patientId, int offset, int limit);

    /**
     * Phương thức đếm tổng số lịch hẹn của một bệnh nhân.
     * @param patientId Mã bệnh nhân
     * @return Tổng số cuộc hẹn
     */
    int countAppointmentsByPatient(int patientId);

    /**
     * Phương thức lấy danh sách lịch hẹn phân công cho bác sĩ theo ngày (tùy chọn) có phân trang.
     * @param doctorId Mã bác sĩ
     * @param date Ngày cần xem (có thể null nếu xem tất cả)
     * @param offset Số bản ghi bỏ qua
     * @param limit Số bản ghi tối đa
     * @return Danh sách lịch hẹn của bác sĩ
     */
    List<Appointment> getAppointmentsByDoctor(int doctorId, Date date, int offset, int limit);

    /**
     * Phương thức đếm tổng số lịch hẹn của bác sĩ theo ngày.
     * @param doctorId Mã bác sĩ
     * @param date Ngày hẹn (có thể null)
     * @return Tổng số lịch hẹn
     */
    int countAppointmentsByDoctor(int doctorId, Date date);

    /**
     * Phương thức lấy danh sách lịch hẹn cho quản trị viên và nhân viên lễ tân với bộ lọc trạng thái và ngày.
     * @param status Trạng thái lịch hẹn cần lọc (null nếu xem tất cả)
     * @param date Ngày hẹn cần lọc (null nếu xem tất cả)
     * @param offset Số bản ghi bỏ qua
     * @param limit Số bản ghi tối đa
     * @return Danh sách lịch hẹn thỏa điều kiện lọc
     */
    List<Appointment> getAllAppointmentsWithPagination(String status, Date date, int offset, int limit);

    /**
     * Phương thức đếm tổng số lịch hẹn theo bộ lọc trạng thái và ngày.
     * @param status Trạng thái lọc
     * @param date Ngày lọc
     * @return Tổng số bản ghi thỏa điều kiện
     */
    int countAllAppointments(String status, Date date);

    /**
     * Phương thức cập nhật trạng thái của lịch hẹn (Pending, Confirmed, Checked In, Completed, Cancelled).
     * @param appointmentId Mã lịch hẹn
     * @param status Trạng thái mới
     * @return true nếu cập nhật thành công, ngược lại false
     */
    boolean updateAppointmentStatus(int appointmentId, String status);

    /**
     * Phương thức chỉ định phòng khám cho bệnh nhân khi thực hiện thủ tục tiếp đón (Check In).
     * @param appointmentId Mã lịch hẹn
     * @param room Tên hoặc số phòng khám
     * @return true nếu cập nhật thành công, ngược lại false
     */
    boolean updateAppointmentRoom(int appointmentId, String room);

    /**
     * Phương thức thống kê doanh thu và số lượng ca khám theo từng tháng trong năm phục vụ biểu đồ Chart.js.
     * @param year Năm cần thống kê
     * @return Danh sách 12 tháng với tổng doanh thu và số ca khám
     */
    List<RevenueDTO> getMonthlyRevenueStatistics(int year);
}
