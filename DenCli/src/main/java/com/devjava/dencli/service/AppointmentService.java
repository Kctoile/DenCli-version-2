/**
 * File: AppointmentService.java
 * Mục đích: Triển khai các phương thức nghiệp vụ lịch hẹn, bao gồm kiểm tra xung đột lịch và điều phối trạng thái cuộc hẹn.
 */
package com.devjava.dencli.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.ServiceDAO;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.Service;
import com.devjava.dencli.model.dto.BookingRequestDTO;
import com.devjava.dencli.util.Constants;
import com.devjava.dencli.util.DateUtil;
import com.devjava.dencli.websocket.AppointmentCalendarEndpoint;
import com.devjava.dencli.websocket.AppointmentWebSocket;
import java.sql.Date;
import java.sql.Time;
import java.util.Collections;
import java.util.List;

public class AppointmentService {

    private static final Logger LOGGER = Logger.getLogger(AppointmentService.class.getName());

    private final AppointmentDAO appointmentDAO;
    private final ServiceDAO serviceDAO;

    // Constructor mặc định khởi tạo các DAO tương ứng
    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
        this.serviceDAO = new ServiceDAO();
    }

    // Constructor hỗ trợ tiêm phụ thuộc cho kiểm thử tự động
    public AppointmentService(AppointmentDAO appointmentDAO, ServiceDAO serviceDAO) {
        this.appointmentDAO = appointmentDAO;
        this.serviceDAO = serviceDAO;
    }

    /**
     * Phương thức thực hiện đặt lịch hẹn khám sau khi kiểm tra tính hợp lệ và xung đột thời gian của bác sĩ.
     * @param request DTO chứa thông tin đặt lịch
     * @param patientId Mã bệnh nhân
     * @return Mã appointment_id hoặc -1 nếu thất bại
     */
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

        // Khởi tạo đối tượng Appointment với trạng thái ban đầu là Pending
        Appointment app = new Appointment();
        app.setPatientId(patientId);
        app.setDoctorId(request.getDoctorId());
        app.setAppointmentDate(appDate);
        app.setAppointmentTime(appTime);
        app.setStatus(Constants.APPOINTMENT_PENDING);
        app.setNotes(request.getNotes());

        // Gọi hàm của tầng DAO để thêm mới lịch hẹn và danh sách dịch vụ đi kèm trong một Transaction
        int appointmentId = appointmentDAO.insertAppointmentWithServices(app, request.getServiceIds());

        if (appointmentId > 0) {
            broadcastSlot(true, request.getDoctorId(), appDate, appTime);
            broadcastEvent("NEW_APPOINTMENT", appointmentId, Constants.APPOINTMENT_PENDING, null);
        }

        return appointmentId;
    }

    /**
     * Phương thức lấy thông tin chi tiết của lịch hẹn kèm danh sách dịch vụ bệnh nhân đã đặt.
     * @param appointmentId Mã lịch hẹn
     * @return Đối tượng Appointment hoàn chỉnh
     */
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
    public List<Appointment> getDoctorAppointments(int doctorId, Date date, int page, int pageSize) {
        int validPage = (page <= 0) ? Constants.DEFAULT_PAGE_NUMBER : page;
        int validPageSize = (pageSize <= 0) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        int offset = (validPage - 1) * validPageSize;

        return appointmentDAO.getAppointmentsByDoctor(doctorId, date, offset, validPageSize);
    }

    /**
     * Phương thức lấy danh sách toàn bộ lịch hẹn hệ thống theo tiêu chí lọc và phân trang.
     * @param status Trạng thái lọc
     * @param date Ngày lọc
     * @param page Trang
     * @param pageSize Kích thước trang
     * @return Danh sách lịch hẹn
     */
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
    public int countAllAppointments(String status, Date date) {
        return appointmentDAO.countAllAppointments(status, date);
    }

    /**
     * Phương thức thực hiện tiếp đón bệnh nhân và phân bổ phòng khám.
     * @param appointmentId Mã lịch hẹn
     * @param room Số phòng
     * @return true nếu tiếp đón thành công
     */
    public boolean checkInAppointment(int appointmentId, String room) {
        boolean success = appointmentId > 0 && room != null && !room.isBlank()
                && appointmentDAO.checkInAppointmentIfCurrent(appointmentId, Constants.APPOINTMENT_CONFIRMED,
                        room.trim(), Constants.APPOINTMENT_CHECKED_IN);
        if (success) {
            broadcastEvent("APPOINTMENT_UPDATED", appointmentId, Constants.APPOINTMENT_CHECKED_IN, room.trim());
        }
        return success;
    }

    /**
     * Phương thức hủy lịch hẹn có kiểm tra thẩm quyền của người yêu cầu hủy.
     * @param appointmentId Mã lịch hẹn
     * @param userId Mã người yêu cầu
     * @param roleId Vai trò của người yêu cầu
     * @return true nếu hủy thành công
     */
    public boolean cancelAppointment(int appointmentId, int userId, int roleId) {
        if (appointmentId <= 0 || userId <= 0) {
            return false;
        }

        Appointment app = null;
        try {
            app = appointmentDAO.getAppointmentById(appointmentId);
        } catch (Exception ignored) {
            // Best-effort retrieval for realtime notification
        }

        boolean success;
        if (roleId == Constants.ROLE_CUSTOMER_ID) {
            success = appointmentDAO.cancelAppointmentForPatient(appointmentId, userId);
        } else if (roleId == Constants.ROLE_ADMIN_ID || roleId == Constants.ROLE_STAFF_ID) {
            success = updateStatusIfAllowed(appointmentId, Constants.APPOINTMENT_CANCELLED);
        } else {
            return false;
        }

        if (success) {
            if (app != null) {
                broadcastSlot(false, app.getDoctorId(), app.getAppointmentDate(), app.getAppointmentTime());
            }
            broadcastEvent("APPOINTMENT_CANCELLED", appointmentId, Constants.APPOINTMENT_CANCELLED, null);
        }

        return success;
    }

    /**
     * Phương thức xác nhận lịch hẹn bởi nhân viên lễ tân.
     * @param appointmentId Mã lịch hẹn
     * @return true nếu xác nhận thành công
     */
    public boolean confirmAppointment(int appointmentId) {
        boolean success = updateStatusIfAllowed(appointmentId, Constants.APPOINTMENT_CONFIRMED);
        if (success) {
            broadcastEvent("APPOINTMENT_UPDATED", appointmentId, Constants.APPOINTMENT_CONFIRMED, null);
        }
        return success;
    }

    /**
     * Phương thức chuyển trạng thái cuộc hẹn sang Hoàn thành (Completed).
     * @param appointmentId Mã lịch hẹn
     * @return true nếu cập nhật thành công
     */
    public boolean completeAppointment(int appointmentId) {
        boolean success = updateStatusIfAllowed(appointmentId, Constants.APPOINTMENT_COMPLETED);
        if (success) {
            broadcastEvent("APPOINTMENT_UPDATED", appointmentId, Constants.APPOINTMENT_COMPLETED, null);
        }
        return success;
    }

    public boolean completeAppointmentForDoctor(int appointmentId, int doctorId) {
        boolean success = appointmentId > 0 && doctorId > 0
                && appointmentDAO.completeAppointmentForDoctorIfCurrent(appointmentId, doctorId,
                        Constants.APPOINTMENT_CHECKED_IN, Constants.APPOINTMENT_COMPLETED);
        if (success) {
            broadcastEvent("APPOINTMENT_UPDATED", appointmentId, Constants.APPOINTMENT_COMPLETED, null);
        }
        return success;
    }

    public boolean isValidStatusTransition(String currentStatus, String targetStatus) {
        if (currentStatus == null || targetStatus == null) {
            return false;
        }

        if (Constants.APPOINTMENT_CONFIRMED.equalsIgnoreCase(targetStatus)) {
            return Constants.APPOINTMENT_PENDING.equalsIgnoreCase(currentStatus);
        }
        if (Constants.APPOINTMENT_CHECKED_IN.equalsIgnoreCase(targetStatus)) {
            return Constants.APPOINTMENT_CONFIRMED.equalsIgnoreCase(currentStatus);
        }
        if (Constants.APPOINTMENT_COMPLETED.equalsIgnoreCase(targetStatus)) {
            return Constants.APPOINTMENT_CHECKED_IN.equalsIgnoreCase(currentStatus);
        }
        if (Constants.APPOINTMENT_CANCELLED.equalsIgnoreCase(targetStatus)) {
            return Constants.APPOINTMENT_PENDING.equalsIgnoreCase(currentStatus)
                    || Constants.APPOINTMENT_CONFIRMED.equalsIgnoreCase(currentStatus)
                    || Constants.APPOINTMENT_CHECKED_IN.equalsIgnoreCase(currentStatus);
        }
        return false;
    }

    private boolean updateStatusIfAllowed(int appointmentId, String targetStatus) {
        if (appointmentId <= 0 || targetStatus == null) {
            return false;
        }
        for (String currentStatus : List.of(Constants.APPOINTMENT_PENDING,
                Constants.APPOINTMENT_CONFIRMED, Constants.APPOINTMENT_CHECKED_IN)) {
            if (isValidStatusTransition(currentStatus, targetStatus)
                    && appointmentDAO.updateAppointmentStatusIfCurrent(appointmentId, currentStatus, targetStatus)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lấy danh sách các khung giờ đã có người đặt của bác sĩ trong ngày (định dạng YYYY-MM-DD).
     */
    public List<String> getBookedTimeSlots(int doctorId, String dateStr) {
        if (doctorId <= 0 || dateStr == null || dateStr.trim().isEmpty()) {
            return Collections.emptyList();
        }
        Date date = DateUtil.parseDate(dateStr);
        if (date == null) {
            return Collections.emptyList();
        }
        return appointmentDAO.getBookedTimeSlots(doctorId, date);
    }

    private void broadcastEvent(String event, int appointmentId, String status, String room) {
        try {
            StringBuilder json = new StringBuilder(128);
            json.append("{\"event\":\"").append(event).append("\",\"appointmentId\":").append(appointmentId);
            if (status != null) {
                json.append(",\"status\":\"").append(status).append("\"");
            }
            if (room != null) {
                json.append(",\"room\":\"").append(room).append("\"");
            }
            json.append("}");
            AppointmentWebSocket.broadcast(json.toString());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to broadcast appointment event: " + event, e);
        }
    }

    private void broadcastSlot(boolean booked, Integer doctorId, Date date, Time time) {
        if (doctorId == null || date == null || time == null) {
            return;
        }
        try {
            String dateStr = date.toString();
            String timeStr = time.toString();
            if (booked) {
                AppointmentCalendarEndpoint.broadcastSlotBooked(doctorId, dateStr, timeStr);
            } else {
                AppointmentCalendarEndpoint.broadcastSlotReleased(doctorId, dateStr, timeStr);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to broadcast slot update", e);
        }
    }
}
