/**
 * File: AppointmentService.java
 * Mục đích: Interface định nghĩa các phương thức xử lý nghiệp vụ lịch hẹn (đặt lịch, kiểm tra trùng lịch, tiếp đón, cập nhật trạng thái).
 */
package com.devjava.dencli.service;

import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.dto.BookingRequestDTO;
import com.devjava.dencli.model.dto.RevenueDTO;
import java.sql.Date;
import java.util.List;

public interface AppointmentService {

    /**
     * Phương thức thực hiện nghiệp vụ đặt lịch hẹn khám của bệnh nhân, kiểm tra xung đột lịch của bác sĩ.
     * @param request DTO chứa thông tin bác sĩ, ngày hẹn, giờ hẹn, ghi chú và danh sách dịch vụ
     * @param patientId Mã bệnh nhân thực hiện đặt lịch
     * @return Mã appointment_id vừa tạo, hoặc -1 nếu có lỗi (ví dụ: trùng lịch, dữ liệu sai)
     */
    int bookAppointment(BookingRequestDTO request, int patientId);

    /**
     * Phương thức lấy thông tin chi tiết lịch hẹn kèm danh sách dịch vụ đã đặt trước.
     * @param appointmentId Mã lịch hẹn
     * @return Đối tượng Appointment
     */
    Appointment getAppointmentDetails(int appointmentId);

    /**
     * Phương thức lấy danh sách lịch hẹn của một bệnh nhân có phân trang.
     * @param patientId Mã bệnh nhân
     * @param page Trang hiện tại
     * @param pageSize Số lượng bản ghi trên một trang
     * @return Danh sách lịch hẹn
     */
    List<Appointment> getPatientAppointments(int patientId, int page, int pageSize);

    /**
     * Phương thức đếm tổng số lịch hẹn của bệnh nhân phục vụ tính số trang.
     * @param patientId Mã bệnh nhân
     * @return Tổng số cuộc hẹn
     */
    int countPatientAppointments(int patientId);

    /**
     * Phương thức lấy danh sách lịch hẹn của bác sĩ theo ngày (tùy chọn) có phân trang.
     * @param doctorId Mã bác sĩ
     * @param date Ngày hẹn (có thể null để lấy tất cả)
     * @param page Trang hiện tại
     * @param pageSize Số lượng bản ghi trên một trang
     * @return Danh sách lịch hẹn
     */
    List<Appointment> getDoctorAppointments(int doctorId, Date date, int page, int pageSize);

    /**
     * Phương thức đếm số lượng lịch hẹn của bác sĩ.
     * @param doctorId Mã bác sĩ
     * @param date Ngày hẹn
     * @return Tổng số cuộc hẹn
     */
    int countDoctorAppointments(int doctorId, Date date);

    /**
     * Phương thức lấy danh sách toàn bộ lịch hẹn hệ thống theo trạng thái và ngày có phân trang.
     * @param status Trạng thái lọc (Pending, Confirmed, Checked In, Completed, Cancelled hoặc null)
     * @param date Ngày lọc (hoặc null)
     * @param page Trang hiện tại
     * @param pageSize Số lượng bản ghi trên một trang
     * @return Danh sách lịch hẹn
     */
    List<Appointment> getAllAppointments(String status, Date date, int page, int pageSize);

    /**
     * Phương thức đếm tổng số lịch hẹn theo tiêu chí lọc.
     * @param status Trạng thái lọc
     * @param date Ngày lọc
     * @return Tổng số lượng bản ghi
     */
    int countAllAppointments(String status, Date date);

    /**
     * Phương thức thực hiện thủ tục tiếp đón bệnh nhân (Check-In) và chỉ định phòng khám.
     * @param appointmentId Mã lịch hẹn
     * @param room Tên hoặc số phòng khám
     * @return true nếu tiếp đón thành công
     */
    boolean checkInAppointment(int appointmentId, String room);

    /**
     * Phương thức hủy lịch hẹn có kiểm tra phân quyền người thực hiện (chính bệnh nhân hoặc nhân viên/admin).
     * @param appointmentId Mã lịch hẹn
     * @param userId Mã người dùng yêu cầu hủy
     * @param roleId Mã vai trò của người dùng
     * @return true nếu hủy thành công, false nếu không đủ quyền hoặc lịch hẹn không tồn tại
     */
    boolean cancelAppointment(int appointmentId, int userId, int roleId);

    /**
     * Phương thức xác nhận lịch hẹn của nhân viên lễ tân.
     * @param appointmentId Mã lịch hẹn
     * @return true nếu xác nhận thành công
     */
    boolean confirmAppointment(int appointmentId);

    /**
     * Phương thức hoàn thành lịch hẹn khám.
     * @param appointmentId Mã lịch hẹn
     * @return true nếu cập nhật thành công
     */
    boolean completeAppointment(int appointmentId);

    /**
     * Phương thức lấy báo cáo doanh thu và số lượng ca khám 12 tháng phục vụ vẽ biểu đồ Chart.js.
     * @param year Năm cần báo cáo
     * @return Danh sách 12 tháng thống kê doanh thu
     */
    List<RevenueDTO> getMonthlyRevenueReport(int year);
}
