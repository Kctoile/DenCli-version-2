# TÀI LIỆU THIẾT KẾ KỸ THUẬT CHI TIẾT (TECHNICAL SPECIFICATION)

## Dự án: Ứng dụng Web Quản lý Phòng khám Nha khoa (DenCli)

| Thuộc tính | Giá trị |
|---|---|
| **Kiến trúc** | Java Servlet/JSP – Mô hình MVC |
| **Cơ sở dữ liệu** | SQL Server (JDBC) |
| **Build Tool** | Apache Maven |
| **Phiên bản tài liệu** | 1.0 |
| **Ngày phát hành** | 12/08/2026 |

---

## 1. TỔNG QUAN KIẾN TRÚC HỆ THỐNG (SYSTEM ARCHITECTURE)

Hệ thống DenCli được phát triển theo kiến trúc 3 lớp (3-tier Architecture) kết hợp mô hình thiết kế MVC (Model-View-Controller) thuần túy bằng công nghệ Java Servlet và JSP, kết nối cơ sở dữ liệu SQL Server thông qua JDBC. Mô hình này giúp phân tách rõ ràng giữa giao diện hiển thị, logic điều hướng nghiệp vụ và dữ liệu hệ thống.

### 1.1 Sơ đồ kiến trúc logic (Architecture Diagram)

```
[Trình duyệt Client]  <--- HTTP/HTTPS (HTML/CSS/JS/Bootstrap) --->  [Tầng Controller: Java Servlets]
                                                                       |
                                                                       v
                                                      [Tầng Model: Business Logic (Services) & DAO]
                                                                       |
                                                                       v
                                                      [Tầng Cơ sở dữ liệu: SQL Server Database (JDBC)]
```

### 1.2 Mô tả chi tiết các tầng (Layer Description)

| Tầng (Layer) | Công nghệ sử dụng | Vai trò và Trách nhiệm |
|---|---|---|
| **Presentation (View)** | JSP, HTML5, CSS3, Bootstrap 5, JavaScript (AJAX) | Hiển thị giao diện người dùng responsive. Tiếp nhận tương tác và gửi yêu cầu HTTP (GET/POST/AJAX) về Controller. |
| **Business Logic & Controller** | Java Servlet, Java Beans, Filters, Service Classes | Servlet tiếp nhận HTTP request, trích xuất tham số, điều phối xử lý nghiệp vụ thông qua Service và điều hướng (forward/redirect) kết quả sang JSP tương ứng. Session-based authentication được cài đặt tại đây. |
| **Data Access Layer (DAO)** | JDBC (PreparedStatement), Connection Pool (HikariCP / Tomcat JDBC) | Thực hiện các truy vấn SQL trực tiếp xuống SQL Server. Sử dụng PreparedStatement để tránh SQL Injection. Đóng gói dữ liệu thành các thực thể Java (POJO). |

---

## 2. THIẾT KẾ CƠ SỞ DỮ LIỆU CHI TIẾT (DATABASE DESIGN)

Cơ sở dữ liệu của hệ thống sử dụng SQL Server. Toàn bộ dữ liệu tiếng Việt được hỗ trợ lưu trữ thông qua các trường kiểu dữ liệu Unicode (`NVARCHAR`, `NVARCHAR(MAX)`). Các bảng khóa ngoại được thiết lập ràng buộc toàn vẹn dữ liệu chặt chẽ.

### 2.1 Bảng dữ liệu: users
Lưu trữ thông tin chi tiết về người dùng của hệ thống (bao gồm Admin, Bác sĩ, Nhân viên tiếp đón và Bệnh nhân).

| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **user_id** | INT | PK | NO | Khóa chính, tự động tăng (IDENTITY) |
| **full_name** | NVARCHAR(100) | | NO | Họ và tên đầy đủ |
| **email** | VARCHAR(100) | | NO | Địa chỉ email (Duy nhất) |
| **password** | VARCHAR(255) | | NO | Mật khẩu băm (BCrypt) |
| **phone** | VARCHAR(20) | | YES | Số điện thoại liên hệ |
| **role_id** | INT | FK | YES | Liên kết với roles.role_id |
| **gender** | NVARCHAR(10) | | YES | Giới tính (Nam, Nữ, Khác) |
| **dob** | DATE | | YES | Ngày sinh |
| **address** | NVARCHAR(255) | | YES | Địa chỉ thường trú |
| **created_at** | DATETIME | | YES | Thời điểm tạo tài khoản (Mặc định `GETDATE()`) |
| **display_order** | INT | | YES | Thứ tự hiển thị |

### 2.2 Bảng dữ liệu: roles
Bảng danh mục định nghĩa các vai trò của người dùng trong hệ thống.

| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **role_id** | INT | PK | NO | Khóa chính, tự tăng |
| **role_name** | NVARCHAR(50) | | NO | Tên vai trò (`ADMIN`, `DOCTOR`, `STAFF`, `CUSTOMER`) |

### 2.3 Bảng dữ liệu: appointments
Lưu trữ thông tin lịch hẹn khám bệnh của bệnh nhân với bác sĩ nha khoa.

| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **appointment_id** | INT | PK | NO | Khóa chính, tự tăng |
| **patient_id** | INT | FK | YES | Người đặt lịch, liên kết với users.user_id |
| **doctor_id** | INT | FK | YES | Bác sĩ phụ trách, liên kết với users.user_id |
| **appointment_date** | DATE | | NO | Ngày hẹn khám |
| **appointment_time** | TIME(7) | | NO | Giờ hẹn khám |
| **status** | NVARCHAR(50) | | YES | Trạng thái lịch hẹn (`Pending`, `Confirmed`, `Checked In`, `Completed`, `Cancelled`) |
| **notes** | NVARCHAR(MAX) | | YES | Ghi chú thêm |
| **room** | NVARCHAR(50) | | YES | Phòng khám được chỉ định lúc check-in |

### 2.4 Bảng dữ liệu: appointment_services
Bảng trung gian lưu danh sách dịch vụ bệnh nhân đặt trước trong lịch hẹn (Quan hệ N-N).

| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **appointment_id** | INT | PK, FK | NO | Liên kết với appointments.appointment_id |
| **service_id** | INT | PK, FK | NO | Liên kết với services.service_id |

### 2.5 Bảng dữ liệu: services
Lưu trữ danh mục các dịch vụ nha khoa mà phòng khám cung cấp.

| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **service_id** | INT | PK | NO | Khóa chính, tự tăng |
| **service_name** | NVARCHAR(100) | | NO | Tên dịch vụ |
| **description** | NVARCHAR(MAX) | | YES | Mô tả chi tiết về dịch vụ |
| **price** | DECIMAL(18,0) | | NO | Đơn giá dịch vụ |
| **duration_minutes** | INT | | YES | Thời gian thực hiện (phút) |

### 2.6 Bảng dữ liệu: examination_results
Lưu trữ kết quả khám bệnh lâm sàng chi tiết của bệnh nhân sau khi hoàn tất cuộc hẹn.

| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **result_id** | INT | PK | NO | Khóa chính, tự tăng |
| **appointment_id** | INT | FK | YES | Liên kết với appointments.appointment_id |
| **result_details** | NVARCHAR(MAX) | | YES | Chi tiết kết quả chẩn đoán và điều trị của bác sĩ |
| **examination_date** | DATETIME | | YES | Thời gian lập hồ sơ khám |

### 2.7 Bảng dữ liệu: prescribed_services
Lưu các dịch vụ phát sinh hoặc được bác sĩ chỉ định thêm trong quá trình khám bệnh thực tế.

| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **result_id** | INT | PK, FK | NO | Liên kết với examination_results.result_id |
| **service_id** | INT | PK, FK | NO | Dịch vụ được chỉ định thêm, liên kết với services.service_id |
| **status** | NVARCHAR(50) | | YES | Trạng thái thực hiện |
| **notes** | NVARCHAR(MAX) | | YES | Ghi chú thực hiện dịch vụ |

### 2.8 Bảng dữ liệu: prescriptions & prescription_details
Bảng `prescriptions` lưu đơn thuốc được kê gắn liền với kết quả khám. Bảng `prescription_details` lưu chi tiết danh sách thuốc và số lượng trong đơn thuốc đó.

**Bảng prescriptions:**
| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **prescription_id** | INT | PK | NO | Khóa chính, tự tăng |
| **result_id** | INT | FK | YES | Kết quả khám tương ứng, liên kết với examination_results.result_id |
| **instructions** | NVARCHAR(MAX) | | YES | Hướng dẫn sử dụng chung cho đơn thuốc |

**Bảng prescription_details:**
| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **prescription_id** | INT | PK, FK | NO | Liên kết với prescriptions.prescription_id |
| **medicine_id** | INT | PK, FK | NO | Liên kết với medicines.medicine_id |
| **prescribed_quantity** | INT | | NO | Số lượng thuốc được kê |
| **purchased_quantity** | INT | | YES | Số lượng thuốc thực tế đã mua |
| **unit_price** | DECIMAL(18,0) | | NO | Đơn giá thuốc tại thời điểm kê đơn |

### 2.9 Bảng dữ liệu: medicines
Lưu trữ danh mục thuốc trong kho của phòng khám.

| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **medicine_id** | INT | PK | NO | Khóa chính, tự tăng |
| **medicine_name** | NVARCHAR(100) | | NO | Tên thuốc |
| **price** | DECIMAL(18,0) | | NO | Giá thuốc |
| **stock_quantity** | INT | | NO | Số lượng tồn kho |

### 2.10 Bảng dữ liệu: clinic_configs
Lưu trữ cấu hình thông tin hoạt động của phòng khám nha khoa hiển thị trên trang chủ.

| Tên trường (Column) | Kiểu dữ liệu | Khóa (Key) | Cho phép Null | Mô tả / Ràng buộc |
|---|---|---|---|---|
| **config_id** | INT | PK | NO | Khóa chính, tự tăng |
| **opening_time** | TIME(7) | | YES | Giờ mở cửa phòng khám |
| **closing_time** | TIME(7) | | YES | Giờ đóng cửa phòng khám |
| **clinic_info** | NVARCHAR(MAX) | | YES | Giới thiệu chi tiết phòng khám |

---

## 3. ĐẶC TẢ API / SERVLET ENDPOINTS

Tầng Controller điều phối dữ liệu qua các Servlet. Dưới đây là thiết kế chi tiết luồng xử lý và tham số đầu vào/đầu ra cho một số Servlet quan trọng.

### 3.1 Đăng ký tài khoản (RegisterServlet)
- **URL Endpoint**: `/register`
- **Phương thức HTTP**: POST
- **Tham số Request (Form Data)**:
  - `name`: Họ và tên người dùng (Bắt buộc)
  - `phone`: Số điện thoại (Bắt buộc, duy nhất)
  - `email`: Địa chỉ email (Tùy chọn, duy nhất nếu có)
  - `password`: Mật khẩu dạng text (Bắt buộc, tối thiểu 6 ký tự)
- **Logic xử lý chi tiết**:
  1. Tiếp nhận request, trích xuất các tham số.
  2. Thực hiện kiểm tra tính hợp lệ dữ liệu (validation): Định dạng email, số điện thoại, mật khẩu.
  3. Gọi `UserDAO.checkPhoneExists(phone)` và `UserDAO.checkEmailExists(email)`. Nếu trùng lặp, chuyển hướng kèm thông báo lỗi.
  4. Sử dụng thuật toán băm một chiều BCrypt để băm mật khẩu.
  5. Bắt đầu một Transaction cơ sở dữ liệu:
     - Thêm bản ghi mới vào bảng `users` với vai trò `CUSTOMER` (lấy role_id tương ứng).
  6. Commit Transaction và redirect người dùng về trang login với thông báo thành công.

### 3.2 Đặt lịch hẹn khám (BookAppointmentServlet)
- **URL Endpoint**: `/appointments/book`
- **Phương thức HTTP**: POST
- **Tham số Request (JSON / Form Data)**:
  - `doctorId`: ID bác sĩ được chọn (user_id của bác sĩ)
  - `date`: Ngày khám mong muốn (yyyy-MM-dd)
  - `slotTime`: Giờ khám mong muốn (hh:mm:ss)
  - `serviceIds`: Mảng ID các dịch vụ được chọn
- **Logic xử lý chi tiết**:
  1. Kiểm tra session để lấy `user_id` của bệnh nhân (đảm bảo vai trò `CUSTOMER` đã đăng nhập).
  2. Thực hiện cơ chế khóa đồng thời (locking) để tránh trùng lịch:
     - Sử dụng truy vấn SQL với khóa gợi ý như `SELECT ... WITH (UPDLOCK, HOLDLOCK)` trên bảng `appointments` hoặc đặt mức cô lập giao dịch Transaction ở mức `SERIALIZABLE`.
     - Kiểm tra xem khung giờ tại ngày đã chọn của bác sĩ đó có bị người khác đặt trước chưa.
  3. Nếu trống:
     - Tạo bản ghi lịch hẹn mới trong bảng `appointments` với trạng thái 'Pending'.
     - Thêm các dịch vụ được lựa chọn vào bảng `appointment_services`.
  4. Lưu vết hoạt động và gửi email xác nhận đặt lịch hẹn thông qua EmailService chạy nền (Asynchronous thread) để tránh chặn luồng UI chính.

---

## 4. CHUẨN MÃ HÓA BẢO MẬT & AN TOÀN THÔNG TIN (OWASP)

Để đảm bảo an toàn cho hệ thống và tuân thủ các nguyên tắc thiết kế bảo mật, dự án cam kết áp dụng các giải pháp kỹ thuật cụ thể đối với các lỗ hổng thuộc OWASP Top 10.

### 4.1 Phòng chống tấn công SQL Injection
- Tuyệt đối không sử dụng cộng chuỗi String để tạo truy vấn SQL động.
- Bắt buộc sử dụng `java.sql.PreparedStatement` cho mọi câu truy vấn chứa tham số đầu vào từ người dùng.
- Ví dụ đoạn mã tiêu chuẩn trong DAO:
  ```java
  String sql = "SELECT * FROM users WHERE email = ?";
  try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) { ... }
  }
  ```

### 4.2 Phòng chống tấn công Cross-Site Scripting (XSS)
- Khi hiển thị dữ liệu do người dùng nhập lên trang JSP, bắt buộc sử dụng thẻ JSTL `<c:out value="${...}" />` để tự động escape các thẻ HTML đặc biệt (`<`, `>`, `&`, `"`, `'`).
- Đối với các trường nhập liệu dạng văn bản tự do, áp dụng bộ lọc thư viện OWASP Java HTML Sanitizer trên Controller trước khi lưu vào cơ sở dữ liệu.

### 4.3 Cơ chế phân quyền và quản lý Session
- Sử dụng HTTP Session tiêu chuẩn của Tomcat để quản lý trạng thái đăng nhập.
- Cài đặt một Servlet Filter chặn mọi request đến các thư mục bảo mật (như `/admin/*`, `/doctor/*`, `/receptionist/*`).
- Kiểm tra thuộc tính vai trò (Role) được lưu trong Session. Nếu người dùng truy cập trái phép, lập tức trả về lỗi HTTP 403 Forbidden hoặc redirect về trang login.

