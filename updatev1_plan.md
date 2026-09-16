# Kế Hoạch Tái Cấu Trúc & Nâng Cấp Hệ Thống DenCli-version-2

Dự án **DenCli-version-2** (Hệ thống quản lý phòng khám nha khoa) được tái cấu trúc thành kiến trúc 3 lớp chuẩn mực (3-Tier MVC: Controller -> Service -> DAO -> Database) phục vụ đồ án môn học Java Web.
Dự án tuân thủ nghiêm ngặt các ràng buộc kỹ thuật: **KHÔNG DÙNG SPRING BOOT**, chỉ dùng **Pure Java Servlet (Jakarta EE 10), JSP/JSTL, JDBC, HikariCP, jBCrypt, Bootstrap 5, Fetch API/AJAX, Chart.js**.

---

## User Review Required

> [!IMPORTANT]
> - Hệ thống chuyển toàn bộ package gốc từ `com.mycompany.dencli` sang `com.devjava.dencli` theo đúng tài liệu đặc tả kiến trúc.
> - Bổ sung thư viện Connection Pooling **HikariCP** và **Gson** (hỗ trợ chuyển đổi JSON cho Fetch API / Chart.js thay thế việc dùng Regex thô sơ).
> - Chuẩn hóa toàn bộ comment mã nguồn sang Tiếng Việt có dấu, có header file, ghi chú phương thức, cách dòng giữa các block và ghi chú các lệnh gọi thư viện/override.

---

## Phân Kỳ Triển Khai (Roadmap)

### Phase 1: Quản lý Thư viện & Tầng Truy Xuất Dữ Liệu (Data Access Layer) - *Trọng tâm khởi đầu*
1. **Cập nhật `pom.xml`**:
   - Thêm HikariCP (`com.zaxxer:HikariCP:5.1.0`)
   - Thêm JSTL GlassFish implementation cho Jakarta EE 10 (`org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1`)
   - Thêm Gson (`com.google.code.gson:gson:2.10.1`) phục vụ REST/AJAX sạch sẽ
   - Duy trì `mssql-jdbc:12.2.0.jre11`, `jbcrypt:0.4`, JUnit 5 & Mockito
2. **Cấu hình & Tiện ích chung (`com.devjava.dencli.util`)**:
   - `Constants.java`: Định nghĩa các hằng số hệ thống (vai trò người dùng: ADMIN=1, DOCTOR=2, STAFF=4, CUSTOMER=5; trạng thái cuộc hẹn: PENDING, CONFIRMED, CHECKED_IN, COMPLETED, CANCELLED; cấu hình DB; Session key).
   - `PasswordUtil.java`: Băm và kiểm tra mật khẩu bằng `jBCrypt`.
   - `DateUtil.java`: Định dạng và chuyển đổi Date/Time/Timestamp an toàn.
3. **Quản lý kết nối cơ sở dữ liệu (`com.devjava.dencli.dao.DBConnection`)**:
   - Hiện thực Singleton Pattern với HikariDataSource quản lý connection pool.
   - Cơ chế đóng tài nguyên, cấp phát Connection an toàn.
4. **Các Interface & Lớp DAO Chuẩn (`com.devjava.dencli.dao` & `impl`)**:
   - Áp dụng 100% `PreparedStatement` chống SQL Injection.
   - Tích hợp phân trang chuẩn SQL Server: `OFFSET ? ROWS FETCH NEXT ? ROWS ONLY`.
   - `UserDAO` / `UserDAOImpl`: Quản lý người dùng, phân trang danh sách, tìm kiếm theo email/phone/role.
   - `AppointmentDAO` / `AppointmentDAOImpl`: Kiểm tra trùng slot khám, phân trang lịch hẹn theo vai trò/ngày.
   - `ServiceDAO` / `ServiceDAOImpl`: Danh mục dịch vụ nha khoa, dịch vụ kèm lịch hẹn.
   - `MedicineDAO` / `MedicineDAOImpl`: Danh mục thuốc, kiểm tra số lượng tồn kho, cập nhật số lượng khi kê đơn.
   - `PrescriptionDAO` / `PrescriptionDAOImpl`: Quản lý đơn thuốc và chi tiết đơn thuốc.
   - `ExaminationResultDAO` / `ExaminationResultDAOImpl`: Lưu trữ kết quả khám và dịch vụ chỉ định thêm.

---

### Phase 2: Tầng Nghiệp vụ (Service Layer) & Quản Lý Transaction
1. Xây dựng các Interface và Lớp triển khai Service:
   - `UserService` / `UserServiceImpl`: Đăng ký, đăng nhập, xác thực, quản lý tài khoản người dùng.
   - `AppointmentService` / `AppointmentServiceImpl`: Kiểm tra xung đột lịch khám (slot conflict) của bác sĩ và giờ làm việc, điều phối đặt lịch.
   - `PrescriptionService` / `PrescriptionServiceImpl`: Kê đơn thuốc, trừ tồn kho (`stock_quantity`) áp dụng Transaction thủ công (`setAutoCommit(false)`, `commit()`, `rollback()`).
   - `ExaminationService` / `ExaminationServiceImpl`: Ghi nhận chẩn đoán kết quả khám, chỉ định dịch vụ phát sinh.
   - `BillingService` / `BillingServiceImpl`: Tính tổng viện phí (dịch vụ khám + tiền thuốc), xuất hóa đơn và thanh toán.
2. `ServiceFactory.java`: Cung cấp các thể hiện Service tập trung (Factory Pattern).

---

### Phase 3: Bộ Lọc Bảo Mật & Xác Thực (Filters & Security)
1. `EncodingFilter`: Đảm bảo toàn bộ Request/Response sử dụng bảng mã `UTF-8`.
2. `AuthenticationFilter`: Chặn truy cập các tài nguyên được bảo vệ nếu chưa có Session đăng nhập.
3. `AuthorizationFilter`: Phân quyền dựa trên Role (RBAC) cho 4 phân hệ: `/admin/*`, `/doctor/*`, `/staff/*`, `/customer/*`.

---

### Phase 4: Các Luồng Nghiệp Vụ Chính (Servlets Controller)
1. **Khách hàng (`customer/`)**:
   - `AppointmentBookingServlet`: Tiếp nhận đặt lịch, hiển thị danh sách bác sĩ/dịch vụ, kiểm tra trùng slot qua Service.
   - `ProfileServlet`: Cập nhật thông tin cá nhân và lịch sử khám của bệnh nhân.
2. **Bác sĩ (`doctor/`)**:
   - `ExaminationServlet`: Danh sách bệnh nhân chờ khám, tạo kết quả chẩn đoán lâm sàng.
   - `PrescriptionServlet`: Kê đơn thuốc theo danh mục, tính giá, trừ kho an toàn.
3. **Nhân viên lễ tân/thu ngân (`staff/`)**:
   - `ReceptionServlet`: Tiếp đón, check-in phòng khám, xác nhận lịch hẹn.
   - `InvoiceServlet`: Lập hóa đơn viện phí, thu tiền và đổi trạng thái hoàn tất.
4. **Quản trị viên (`admin/`)**:
   - `DashboardServlet`: Thống kê tổng quan số lịch hẹn, bệnh nhân, doanh thu.
   - `ManageUserServlet`: Quản lý danh sách bác sĩ, nhân viên, khách hàng (phân trang).
   - `RevenueReportServlet`: Cung cấp API dữ liệu doanh thu dạng JSON cho Chart.js.

---

### Phase 5: Giao Diện JSP/JSTL, Ajax & Dashboard
1. Tinh chỉnh các trang JSP: Loại bỏ 100% scriptlet `<% %>`, thay thế bằng thẻ JSTL (`<c:forEach>`, `<c:if>`, `<fmt:formatNumber>`).
2. Giao diện Bootstrap 5 hiện đại, responsive, đồng bộ layout Header/Sidebar/Footer.
3. Admin Dashboard tích hợp biểu đồ Chart.js trực quan tải dữ liệu qua Fetch API.

---

## Kế Hoạch Triển Khai Ngay (Phase 1 Execution Steps)

1. [MODIFY] `DenCli/pom.xml`: Bổ sung HikariCP, Gson, JSTL API & Implementation.
2. [NEW] `com/devjava/dencli/util/Constants.java`: Các hằng số cấu hình hệ thống, DB, Roles, Status.
3. [NEW] `com/devjava/dencli/util/PasswordUtil.java`: Mã hóa và kiểm tra mật khẩu bằng BCrypt.
4. [NEW] `com/devjava/dencli/util/DateUtil.java`: Chuyển đổi định dạng ngày giờ chuẩn.
5. [NEW] `com/devjava/dencli/dao/DBConnection.java`: Singleton HikariCP Connection Pool kết nối SQL Server `Dental`.
6. [NEW] `com/devjava/dencli/model/*`: Chuyển đổi và bổ sung đầy đủ Entity Models & DTOs vào package `com.devjava.dencli.model`.
7. [NEW] `com/devjava/dencli/dao/*` & `impl/*`: Các DAO Interfaces & Classes hiện thực truy vấn phân trang SQL Server và PreparedStatement.
8. Kiểm tra kết nối DB và biên dịch Maven (`mvn clean compile`).

---

## Kế Hoạch Kiểm Thử (Verification Plan)

### Kiểm Thử Tự Động
- Chạy lệnh `mvn clean test-compile` và `mvn test` để đảm bảo code biên dịch sạch và không có lỗi xung đột thư viện.
- Viết Unit Test cho `DBConnection` và `PasswordUtil`.

### Kiểm Thử Bằng Tay
- Kiểm tra kết nối pool HikariCP lấy kết nối thành công và đóng trả về pool chuẩn xác.
- Kiểm tra các truy vấn phân trang (`OFFSET ... ROWS FETCH NEXT ... ROWS ONLY`).
