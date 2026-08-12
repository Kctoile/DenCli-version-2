/**
 * Purpose: Test runner class to verify DBContext, Models, and DAOs database operations.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli;

import com.mycompany.dencli.dao.AppointmentDAO;
import com.mycompany.dencli.dao.UserDAO;
import com.mycompany.dencli.models.Appointment;
import com.mycompany.dencli.models.User;
import com.mycompany.dencli.utils.DBContext;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TestDatabaseFlow {

    /**
     * Hàm main chạy độc lập trên console để thực hiện các bài kiểm tra CSDL.
     */
    public static void main(String[] args) {
        // Cấu hình in Unicode tiếng Việt ra console để tránh lỗi font hiển thị
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8")); // Cài đặt luồng out UTF-8
        } catch (Exception e) {
            System.err.println("Không thể cài đặt mã hóa console UTF-8.");
        }

        System.out.println("=== BẮT ĐẦU KIỂM TRA LUỒNG CƠ SỞ DỮ LIỆU ===");

        // 1. Kiểm tra kết nối CSDL cơ bản
        try (Connection conn = DBContext.getConnection()) { // Gọi hàm getConnection của JDBC
            if (conn != null) {
                System.out.println("[OK] Kết nối tới SQL Server [Dental] thành công.");
            }
        } catch (Exception e) {
            System.err.println("[FAIL] Lỗi kết nối CSDL: " + e.getMessage());
            return;
        }

        UserDAO userDAO = new UserDAO();
        AppointmentDAO appDAO = new AppointmentDAO();

        // 2. Tạo một email ngẫu nhiên để kiểm tra đăng ký tránh trùng lặp
        String testEmail = "test_patient_" + System.currentTimeMillis() + "@gmail.com";
        String testPhone = "098" + (System.currentTimeMillis() % 10000000);

        System.out.println("\n--- Bước 1: Kiểm tra Đăng ký (UC-03) ---");
        System.out.println("Thử kiểm tra số điện thoại tồn tại: " + userDAO.checkPhoneExists(testPhone));

        User newPatient = new User();
        newPatient.setFullName("Bệnh Nhân Thử Nghiệm");
        newPatient.setEmail(testEmail);
        newPatient.setPassword("hashed_password_xyz"); // Trạng thái mật khẩu đã băm
        newPatient.setPhone(testPhone);
        newPatient.setRoleId(5); // CUSTOMER role
        newPatient.setGender("Nam");
        newPatient.setDob(Date.valueOf("1990-05-10")); // Chuyển chuỗi sang Date
        newPatient.setAddress("Hà Nội, Việt Nam");
        newPatient.setDisplayOrder(99);

        boolean registerResult = userDAO.insertUser(newPatient);
        System.out.println("Kết quả insertUser: " + (registerResult ? "THÀNH CÔNG" : "THẤT BẠI"));

        // 3. Kiểm tra đăng nhập (Lấy thông tin người dùng bằng email)
        System.out.println("\n--- Bước 2: Kiểm tra Đăng nhập (UC-04) ---");
        User dbUser = userDAO.getUserByEmail(testEmail);
        
        if (dbUser != null) {
            System.out.println("[OK] Lấy thông tin user từ CSDL thành công: ID = " + dbUser.getUserId() + ", Họ tên = " + dbUser.getFullName());
        } else {
            System.err.println("[FAIL] Không tìm thấy user có email: " + testEmail);
            return;
        }

        // 4. Kiểm tra đặt lịch hẹn khám (UC-05)
        System.out.println("\n--- Bước 3: Kiểm tra Đặt lịch hẹn (UC-05) ---");
        int doctorId = 2; // BS. Nguyễn Văn A (mặc định trong SQL)
        Date appDate = Date.valueOf("2026-08-20");
        Time appTime = Time.valueOf("09:00:00");

        // Kiểm tra xem giờ này bác sĩ đã có lịch hẹn chưa
        boolean isConflict = appDAO.checkDuplicateSlot(doctorId, appDate, appTime);
        System.out.println("Khung giờ có bị trùng không?: " + isConflict);

        if (!isConflict) {
            Appointment app = new Appointment();
            app.setPatientId(dbUser.getUserId());
            app.setDoctorId(doctorId);
            app.setAppointmentDate(appDate);
            app.setAppointmentTime(appTime);
            app.setStatus("Pending");
            app.setNotes("Lịch hẹn thử nghiệm hệ thống");
            app.setRoom("Phòng số 01");

            // Chọn 2 dịch vụ mẫu đặt cùng lịch hẹn (ID 1: Khám tổng quát, ID 2: Nhổ răng khôn)
            List<Integer> selectedServices = new ArrayList<>();
            selectedServices.add(1);
            selectedServices.add(2);

            boolean bookingResult = appDAO.insertAppointment(app, selectedServices);
            System.out.println("Kết quả insertAppointment: " + (bookingResult ? "THÀNH CÔNG" : "THẤT BẠI"));
            System.out.println("Mã appointment_id tự sinh: " + app.getAppointmentId());

            // Thử kiểm tra lại trùng lịch (Lần này phải báo trùng)
            boolean checkAgain = appDAO.checkDuplicateSlot(doctorId, appDate, appTime);
            System.out.println("Kiểm tra lại trùng sau khi đặt: " + (checkAgain ? "TRÙNG (Đúng)" : "KHOÔNG TRÙNG (Sai)"));
        } else {
            System.out.println("Bác sĩ đã bị trùng lịch, bỏ qua bước đặt thử lịch.");
        }

        System.out.println("\n=== HOÀN TẤT KIỂM TRA LUỒNG CSDL ===");
    }
}
