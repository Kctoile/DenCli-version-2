/**
 * File: AppointmentServiceImpl.java
 * Mục đích: Triển khai các phương thức nghiệp vụ lịch hẹn, bao gồm kiểm tra xung đột lịch và điều phối trạng thái cuộc hẹn.
 */
package com.devjava.dencli.service.impl;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.ServiceDAO;
import com.devjava.dencli.dao.impl.AppointmentDAOImpl;
import com.devjava.dencli.dao.impl.ServiceDAOImpl;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.Service;
import com.devjava.dencli.model.dto.BookingRequestDTO;
import com.devjava.dencli.model.dto.RevenueDTO;
import com.devjava.dencli.service.AppointmentService;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.DateUtil;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final ServiceDAO serviceDAO;

    // Constructor mặc định khởi tạo các DAO tương ứng
    public AppointmentServiceImpl() {
        this.appointmentDAO = new AppointmentDAOImpl();
        this.serviceDAO = new ServiceDAOImpl();
    }

    // Constructor hỗ trợ tiêm phụ thuộc cho kiểm thử tự động
    public AppointmentServiceImpl(AppointmentDAO appointmentDAO, ServiceDAO serviceDAO) {
        this.appointmentDAO = appointmentDAO;
        this.serviceDAO = serviceDAO;
    }

    /**
     * Phương thức thực hiện đặt lịch hẹn khám sau khi kiểm tra tính hợp lệ và xung đột thời gian của bác sĩ.
     * @param request DTO chứa thông tin đặt lịch
     * @param patientId Mã bệnh nhân
     * @return Mã appointment_id hoặc -1 nếu thất bại
     */
    @Override // Ghi đè phương thức bookAppointment từ interface AppointmentService
    public int bookAppointment(BookingRequestDTO request, int patientId) {
        if (request == null || patientId <= 0 || request.getDoctorId() == null || request.getDoctorId() <= 0) {
            return -1;
        }

        // Chuyển đổi ngày và giờ từ chuỗi sang kiểu java.sql.Date và java.sql.Time
        Date appDate = DateUtil.parseDate(request.getAppointmentDate());
        Time appTime = DateUtil.parseTime(request.getAppointmentTime());

        if (appDate == null || appTime == null) {
            return -1;
        }

        // Kiểm tra xung đột lịch: Bác sĩ đã có lịch hẹn vào ngày và giờ này hay chưa
        boolean isDuplicate = appointmentDAO.checkDuplicateSlot(request.getDoctorId(), appDate, appTime);

        if (isDuplicate) {
            // Đã có lịch trùng, từ chối tạo mới để tránh đặt chồng chéo
            return -1;
        }

        // Khởi tạo đối tượng Appointment với trạng thái ban đầu là Pending
        Appointment app = new Appointment();
        app.setPatientId(patientId);
        app.setDoctorId(request.getDoctorId());
        app.setAppointmentDate(appDate);
        app.setAppointmentTime(appTime);
        app.setStatus(Constants.APPOINTMENT_PENDING);
        app.setNotes(request.getNotes());

        // Gọi hàm của tầng DAO để thêm mới lịch hẹn và danh sách dịch vụ đi kèm trong một Transaction
        return appointmentDAO.insertAppointmentWithServices(app, request.getServiceIds());
    }

    /**
     * Phương thức lấy thông tin chi tiết của lịch hẹn kèm danh sách dịch vụ bệnh nhân đã đặt.
     * @param appointmentId Mã lịch hẹn
     * @return Đối tượng Appointment hoàn chỉnh
     */
    @Override // Ghi đè phương thức getAppointmentDetails từ interface AppointmentService
    public Appointment getAppointmentDetails(int appointmentId) {
        if (appointmentId <= 0) {
            return null;
        }

        Appointment app = appointmentDAO.getAppointmentById(appointmentId);

        if (app != null) {
            // Lấy danh sách dịch vụ đặt trước đính kèm lịch hẹn
            List<Service> services = serviceDAO.getServicesByAppointmentId(appointmentId);
            app.setServices(services);
        }

        return app;
    }

    /**
     * Phương thức lấy danh sách lịch hẹn của một bệnh nhân có phân trang.
     * @param patientId Mã bệnh nhân
     * @param page Số trang
     * @param pageSize Số lượng bản ghi trên một trang
     * @return Danh sách lịch hẹn
     */
    @Override // Ghi đè phương thức getPatientAppointments từ interface AppointmentService
    public List<Appointment> getPatientAppointments(int patientId, int page, int pageSize) {
        int validPage = (page <= 0) ? Constants.DEFAULT_PAGE_NUMBER : page;
        int validPageSize = (pageSize <= 0) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        int offset = (validPage - 1) * validPageSize;

        return appointmentDAO.getAppointmentsByPatient(patientId, offset, validPageSize);
    }

    /**
     * Phương thức đếm tổng số lịch hẹn của một bệnh nhân.
     * @param patientId Mã bệnh nhân
     * @return Số lượng lịch hẹn
     */
    @Override // Ghi đè phương thức countPatientAppointments từ interface AppointmentService
    public int countPatientAppointments(int patientId) {
        return appointmentDAO.countAppointmentsByPatient(patientId);
    }

    /**
     * Phương thức lấy danh sách lịch hẹn của bác sĩ theo ngày có phân trang.
     * @param doctorId Mã bác sĩ
     * @param date Ngày hẹn
     * @param page Số trang
     * @param pageSize Số bản ghi mỗi trang
     * @return Danh sách lịch hẹn
     */
    @Override // Ghi đè phương thức getDoctorAppointments từ interface AppointmentService
    public List<Appointment> getDoctorAppointments(int doctorId, Date date, int page, int pageSize) {
        int validPage = (page <= 0) ? Constants.DEFAULT_PAGE_NUMBER : page;
        int validPageSize = (pageSize <= 0) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        int offset = (validPage - 1) * validPageSize;

        return appointmentDAO.getAppointmentsByDoctor(doctorId, date, offset, validPageSize);
    }

    /**
     * Phương thức đếm số lượng lịch hẹn của bác sĩ theo ngày.
     * @param doctorId Mã bác sĩ
     * @param date Ngày hẹn
     * @return Tổng số cuộc hẹn
     */
    @Override // Ghi đè phương thức countDoctorAppointments từ interface AppointmentService
    public int countDoctorAppointments(int doctorId, Date date) {
        return appointmentDAO.countAppointmentsByDoctor(doctorId, date);
    }

    /**
     * Phương thức lấy danh sách toàn bộ lịch hẹn hệ thống theo tiêu chí lọc và phân trang.
     * @param status Trạng thái lọc
     * @param date Ngày lọc
     * @param page Trang
     * @param pageSize Kích thước trang
     * @return Danh sách lịch hẹn
     */
    @Override // Ghi đè phương thức getAllAppointments từ interface AppointmentService
    public List<Appointment> getAllAppointments(String status, Date date, int page, int pageSize) {
        int validPage = (page <= 0) ? Constants.DEFAULT_PAGE_NUMBER : page;
        int validPageSize = (pageSize <= 0) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        int offset = (validPage - 1) * validPageSize;

        return appointmentDAO.getAllAppointmentsWithPagination(status, date, offset, validPageSize);
    }

    /**
     * Phương thức đếm tổng số cuộc hẹn theo trạng thái và ngày.
     * @param status Trạng thái
     * @param date Ngày
     * @return Tổng số bản ghi
     */
    @Override // Ghi đè phương thức countAllAppointments từ interface AppointmentService
    public int countAllAppointments(String status, Date date) {
        return appointmentDAO.countAllAppointments(status, date);
    }

    /**
     * Phương thức thực hiện tiếp đón bệnh nhân và phân bổ phòng khám.
     * @param appointmentId Mã lịch hẹn
     * @param room Số phòng
     * @return true nếu tiếp đón thành công
     */
    @Override // Ghi đè phương thức checkInAppointment từ interface AppointmentService
    public boolean checkInAppointment(int appointmentId, String room) {
        if (appointmentId <= 0) {
            return false;
        }

        // Cập nhật số phòng khám cho cuộc hẹn
        boolean roomUpdated = appointmentDAO.updateAppointmentRoom(appointmentId, room);

        if (!roomUpdated) {
            return false;
        }

        // Cập nhật trạng thái cuộc hẹn thành Checked In
        return appointmentDAO.updateAppointmentStatus(appointmentId, Constants.APPOINTMENT_CHECKED_IN);
    }

    /**
     * Phương thức hủy lịch hẹn có kiểm tra thẩm quyền của người yêu cầu hủy.
     * @param appointmentId Mã lịch hẹn
     * @param userId Mã người yêu cầu
     * @param roleId Vai trò của người yêu cầu
     * @return true nếu hủy thành công
     */
    @Override // Ghi đè phương thức cancelAppointment từ interface AppointmentService
    public boolean cancelAppointment(int appointmentId, int userId, int roleId) {
        Appointment app = appointmentDAO.getAppointmentById(appointmentId);

        if (app == null) {
            return false;
        }

        // Nếu là bệnh nhân, chỉ được phép hủy lịch hẹn do chính mình đặt
        if (roleId == Constants.ROLE_CUSTOMER_ID) {
            if (app.getPatientId() == null || app.getPatientId() != userId) {
                return false;
            }
        }

        // Không cho phép hủy lịch hẹn đã hoàn thành
        if (Constants.APPOINTMENT_COMPLETED.equalsIgnoreCase(app.getStatus())) {
            return false;
        }

        return appointmentDAO.updateAppointmentStatus(appointmentId, Constants.APPOINTMENT_CANCELLED);
    }

    /**
     * Phương thức xác nhận lịch hẹn bởi nhân viên lễ tân.
     * @param appointmentId Mã lịch hẹn
     * @return true nếu xác nhận thành công
     */
    @Override // Ghi đè phương thức confirmAppointment từ interface AppointmentService
    public boolean confirmAppointment(int appointmentId) {
        return appointmentDAO.updateAppointmentStatus(appointmentId, Constants.APPOINTMENT_CONFIRMED);
    }

    /**
     * Phương thức chuyển trạng thái cuộc hẹn sang Hoàn thành (Completed).
     * @param appointmentId Mã lịch hẹn
     * @return true nếu cập nhật thành công
     */
    @Override // Ghi đè phương thức completeAppointment từ interface AppointmentService
    public boolean completeAppointment(int appointmentId) {
        return appointmentDAO.updateAppointmentStatus(appointmentId, Constants.APPOINTMENT_COMPLETED);
    }

    /**
     * Phương thức lấy báo cáo thống kê doanh thu 12 tháng phục vụ Chart.js.
     * @param year Năm cần thống kê
     * @return Danh sách số liệu doanh thu 12 tháng
     */
    @Override // Ghi đè phương thức getMonthlyRevenueReport từ interface AppointmentService
    public List<RevenueDTO> getMonthlyRevenueReport(int year) {
        int validYear = (year <= 0) ? 2026 : year;

        return appointmentDAO.getMonthlyRevenueStatistics(validYear);
    }
}
