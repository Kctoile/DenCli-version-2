# Báo Cáo Hoàn Thành: Phase 1, Phase 2 & Phase 3 — DenCli Dental Clinic Management

Dự án **DenCli-version-2** (Hệ thống quản lý phòng khám nha khoa) đã hoàn thành xuất sắc các giai đoạn cốt lõi:
- **Phase 1**: Models/DTOs, `DBConnection` (Pure JDBC với `try-with-resources`), toàn bộ DAO phân trang SQL Server.
- **Phase 2**: Tầng Service (`UserService`, `AppointmentService`, `PrescriptionService`, `ExaminationService`, `BillingService`), `ServiceFactory`, và Transaction đa bảng thủ công (`setAutoCommit(false)`, `commit()`, `rollback()`).
- **Phase 3**: Tầng Lọc & Bảo mật (Filters & Security): `EncodingFilter`, `AuthenticationFilter`, `AuthorizationFilter` (RBAC) cùng trang `403.jsp` chuẩn Bootstrap 5.

Toàn bộ hệ thống tuân thủ nghiêm ngặt mọi quy chuẩn kỹ thuật: Jakarta EE 10, Servlet API chuẩn, JDBC DriverManager thuần, không Spring Boot, và 100% comment tiếng Việt có dấu.

---

## Chi Tiết Triển Khai Phase 3: Filters & Security (`com.devjava.dencli.filter`)

### 1. `EncodingFilter` ([EncodingFilter.java](file:///c:/Users/ad/Desktop/DenCli/DenCli/src/main/java/com/devjava/dencli/filter/EncodingFilter.java))
- **Mục tiêu**: Đảm bảo 100% Request và Response sử dụng bảng mã `UTF-8`, triệt tiêu hoàn toàn tình trạng lỗi hiển thị tiếng Việt có dấu khi gửi nhận form hoặc trả về JSON/HTML.
- **Cấu hình**: `@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})`.
- **Thực thi**:
  - `request.setCharacterEncoding("UTF-8");`
  - `response.setCharacterEncoding("UTF-8");`
  - `chain.doFilter(request, response);`

### 2. `AuthenticationFilter` ([AuthenticationFilter.java](file:///c:/Users/ad/Desktop/DenCli/DenCli/src/main/java/com/devjava/dencli/filter/AuthenticationFilter.java))
- **Mục tiêu**: Ngăn chặn các truy cập chưa xác thực vào các tài nguyên nội bộ, đồng thời mở đường cho tài nguyên công khai và hỗ trợ trải nghiệm người dùng tối ưu khi đăng nhập.
- **Cấu hình**: `@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})`.
- **Bypass / Tài nguyên công khai**:
  - Trang xác thực & công khai: `/login`, `/login.jsp`, `/register`, `/register.jsp`, `/logout`, `/logout.jsp`, `/403.jsp`, `/error.jsp`, `/`, `/index.jsp`, `/index.html`, `/favicon.ico`.
  - Tài nguyên tĩnh: `/assets/*`, `/css/*`, `/js/*`, `/images/*`, `/vendor/*`, `/auth/*`.
- **Kiểm tra Session**:
  - Đọc `User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;`.
  - **Nếu đã đăng nhập**: Cho phép đi tiếp qua `chain.doFilter(request, response);`.
  - **Nếu chưa đăng nhập**:
    - *Với API/AJAX Request* (`Accept: application/json` hoặc URI `/api/*`): Trả về HTTP 401 Unauthorized kèm JSON `{"success":false,"message":"...","error_code":"ERR_UNAUTHORIZED"}`.
    - *Với Web Page thông thường*: Tự động lưu `targetUrl` vào Session (kèm Query String nếu có) để redirect lại đúng trang sau khi đăng nhập thành công; lưu thông báo lỗi `Constants.SESSION_ERROR_MESSAGE` vào Session và chuyển hướng về `/login.jsp`.

### 3. `AuthorizationFilter` ([AuthorizationFilter.java](file:///c:/Users/ad/Desktop/DenCli/DenCli/src/main/java/com/devjava/dencli/filter/AuthorizationFilter.java))
- **Mục tiêu**: Phân quyền truy cập dựa trên vai trò (Role-Based Access Control - RBAC) chặt chẽ giữa 4 nhóm người dùng trong phòng khám.
- **Cấu hình**: `@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})`.
- **Quy tắc phân hệ RBAC**:
  - `/admin/*` và `/admin.jsp` $\rightarrow$ Chỉ cho phép User có `roleId == Constants.ROLE_ADMIN_ID` (1).
  - `/doctor/*` và `/doctor.jsp` $\rightarrow$ Cho phép Bác sĩ `roleId == Constants.ROLE_DOCTOR_ID` (2) và Quản trị viên (1).
  - `/staff/*` và `/staff.jsp` $\rightarrow$ Cho phép Lễ tân `roleId == Constants.ROLE_STAFF_ID` (4) và Quản trị viên (1).
  - `/customer/*` và `/book.jsp` $\rightarrow$ Cho phép Bệnh nhân `roleId == Constants.ROLE_CUSTOMER_ID` (5) và Quản trị viên (1).
- **Xử lý khi vi phạm (Forbidden / 403)**:
  - *Nếu chưa đăng nhập*: Tự động chuyển hướng về `/login.jsp`.
  - *Nếu là API request*: Trả về HTTP 403 Forbidden kèm JSON `{"success":false,"message":"...","error_code":"ERR_FORBIDDEN"}`.
  - *Nếu là Web Page*: Thiết lập HTTP Status 403 và chuyển tiếp (`forward`) trực tiếp sang `403.jsp`.

### 4. Trang Lỗi Phân Quyền ([403.jsp](file:///c:/Users/ad/Desktop/DenCli/DenCli/src/main/webapp/403.jsp))
- Thiết kế giao diện hiện đại với **Bootstrap 5**, đồng bộ màu sắc nhận diện thương hiệu DenCli.
- Hiển thị thông điệp rõ ràng: **"Bạn không có quyền truy cập trang này!"**.
- Hiển thị ngữ cảnh tài khoản: Họ tên, Email/SĐT và Vai trò hiện tại của người dùng.
- Nút bấm điều hướng thông minh: "Bàn làm việc của tôi" (tự động link về dashboard tương ứng của vai trò), "Về Trang chủ", và "Đăng xuất".

---

## Kết Quả Kiểm Thử Toàn Diện (Verification)

Hệ thống đã bổ sung đầy đủ **15 Unit Test Cases** cho Tầng Filter, nâng tổng số test cases lên **39/39 tests PASS 100%** qua `mvn clean test` và đóng gói WAR thành công:

```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.devjava.dencli.dao.DBConnectionTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.devjava.dencli.filter.AuthenticationFilterTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.devjava.dencli.filter.AuthorizationFilterTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.devjava.dencli.filter.EncodingFilterTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.devjava.dencli.service.AppointmentServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.devjava.dencli.service.BillingServiceTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.devjava.dencli.service.PrescriptionServiceTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.devjava.dencli.service.UserServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.devjava.dencli.util.DateUtilTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.devjava.dencli.util.PasswordUtilTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.mycompany.dencli.controllers.LoginServletTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.mycompany.dencli.controllers.RegisterServletTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] Tests run: 39, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] --- maven-war-plugin:3.4.0:war (default-war) @ DenCli ---
[INFO] Packaging webapp
[INFO] Assembling webapp [DenCli] in [C:\Users\ad\Desktop\DenCli\DenCli\target\DenCli-1.0-SNAPSHOT]
[INFO] Building war: C:\Users\ad\Desktop\DenCli\DenCli\target\DenCli-1.0-SNAPSHOT.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### Danh Sách Các Kịch Bản Test Cho Phase 3:
1. **[EncodingFilterTest.java](file:///c:/Users/ad/Desktop/DenCli/DenCli/src/test/java/com/devjava/dencli/filter/EncodingFilterTest.java)**:
   - Kiểm tra thiết lập UTF-8 cho Request và Response, xác minh tiếp tục FilterChain.
2. **[AuthenticationFilterTest.java](file:///c:/Users/ad/Desktop/DenCli/DenCli/src/test/java/com/devjava/dencli/filter/AuthenticationFilterTest.java)**:
   - Kiểm tra bypass cho trang login/register/public.
   - Kiểm tra bypass cho tài nguyên tĩnh `/css/style.css`.
   - Kiểm tra người dùng đã đăng nhập được truy cập bình thường.
   - Kiểm tra người dùng chưa đăng nhập bị chuyển hướng về `/login.jsp` kèm lưu `targetUrl` vào Session.
   - Kiểm tra API request chưa đăng nhập nhận mã HTTP 401 Unauthorized kèm mã lỗi `ERR_UNAUTHORIZED`.
3. **[AuthorizationFilterTest.java](file:///c:/Users/ad/Desktop/DenCli/DenCli/src/test/java/com/devjava/dencli/filter/AuthorizationFilterTest.java)**:
   - Quản trị viên (ADMIN - 1) truy cập `/admin.jsp` thành công.
   - Bác sĩ (DOCTOR - 2) cố tình truy cập `/admin.jsp` bị chặn mã 403 và forward sang `403.jsp`.
   - Bác sĩ (DOCTOR - 2) truy cập `/doctor.jsp` thành công.
   - Bệnh nhân (CUSTOMER - 5) cố tình truy cập `/doctor.jsp` bị chặn mã 403.
   - Bệnh nhân (CUSTOMER - 5) truy cập `/book.jsp` thành công.
   - Lễ tân (STAFF - 4) truy cập `/staff.jsp` thành công.
   - Truy cập phân hệ bảo vệ khi chưa đăng nhập bị redirect về `login.jsp`.
   - Đường dẫn công khai thông thường `/index.jsp` không bị cản trở.
   - Gọi API phân hệ cấm trả về HTTP 403 JSON với `ERR_FORBIDDEN`.

---

## Bảng Tổng Hợp Điều Phối Quyền & Phân Hệ

| Phân hệ / URL | ADMIN (1) | DOCTOR (2) | STAFF (4) | CUSTOMER (5) | Khách chưa đăng nhập |
|---|:---:|:---:|:---:|:---:|:---:|
| `/admin/*`, `admin.jsp` |  Cho phép | 🚫 403 | 🚫 403 | 🚫 403 | 🔒 Redirect `/login.jsp` |
| `/doctor/*`, `doctor.jsp` |  Cho phép |  Cho phép | 🚫 403 | 🚫 403 | 🔒 Redirect `/login.jsp` |
| `/staff/*`, `staff.jsp` |  Cho phép | 🚫 403 |  Cho phép | 🚫 403 | 🔒 Redirect `/login.jsp` |
| `/customer/*`, `book.jsp` |  Cho phép | 🚫 403 | 🚫 403 |  Cho phép | 🔒 Redirect `/login.jsp` |
| `index.jsp`, `css/*`, `login.jsp` |  Public |  Public |  Public |  Public |  Public |
