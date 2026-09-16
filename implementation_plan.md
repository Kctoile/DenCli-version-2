# Kế hoạch Triển khai Phase 5: Giao diện JSP/JSTL, Bootstrap 5, AJAX & Dashboard (Chart.js)

## Tổng quan
Phase 5 là pha hoàn thiện tầng View và trải nghiệm người dùng của dự án **DenCli-version-2** (Hệ thống Quản lý Phòng khám Nha khoa). Toàn bộ mã nhúng Java thuần (`<% ... %>`) trong các trang JSP sẽ được thay thế bằng JSTL chuẩn (`core` và `fmt`) và EL (`${...}`). Giao diện được nâng cấp đồng bộ với Bootstrap 5 responsive, layout phân chia module qua các tệp include (`header.jsp`, `sidebar.jsp`, `footer.jsp`), và tích hợp biểu đồ thống kê Chart.js qua Fetch API (`/admin/api/revenue`).

---

## Các yêu cầu chi tiết

### 1. Chuẩn hóa Layout & Loại bỏ 100% Java Scriptlets (`<% ... %>`)
- Nhập taglib chuẩn Jakarta EE 10 trong tất cả JSP:
  ```jsp
  <%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
  ```
- Sử dụng thẻ JSTL `<c:if>`, `<c:choose>`, `<c:when>`, `<c:otherwise>`, `<c:forEach>`, `<c:out>`, `<c:set>`, `<c:remove>` thay cho scriptlet logic.
- Định dạng tiền tệ và ngày tháng qua `<fmt:formatNumber>` và `<fmt:formatDate>`.
- Tách và sử dụng layout dùng chung qua `<jsp:include page="/WEB-INF/views/common/header.jsp" />`, `sidebar.jsp`, `footer.jsp`.

### 2. Nâng cấp View theo từng Phân hệ
- **Khách hàng**:
  - `src/main/webapp/customer/book.jsp`: Dropdown bác sĩ, chọn ngày/giờ, danh sách dịch vụ kèm giá định dạng VND, thông báo alert/toast Bootstrap 5.
  - `src/main/webapp/customer/profile.jsp`: Form thông tin cá nhân và bảng lịch sử khám bệnh, badge trạng thái Bootstrap màu sắc (Pending, Confirmed, Checked In, Completed, Cancelled).
- **Bác sĩ**:
  - `src/main/webapp/doctor/examination.jsp`: Danh sách ca khám phân công trong ngày, thao tác khám/chẩn đoán, chuyển trạng thái.
  - `src/main/webapp/doctor/prescription.jsp`: Form kê đơn thuốc, chọn thuốc trong kho, số lượng, liều dùng, tính tạm tính tự động bằng Vanilla JavaScript trước khi submit.
- **Lễ tân & Thu ngân**:
  - `src/main/webapp/staff/reception.jsp`: Quản lý tiếp đón, check-in, hủy hẹn, chuyển sang lập hóa đơn.
  - `src/main/webapp/staff/invoice.jsp`: Hóa đơn viện phí chuyên nghiệp, in ấn, bảng chi tiết dịch vụ + thuốc và nút xác nhận thanh toán.
- **Quản trị viên**:
  - `src/main/webapp/admin/dashboard.jsp`: Thẻ thống kê (Cards), Canvas biểu đồ doanh thu Chart.js nạp dữ liệu động từ `fetch('/admin/api/revenue')`.
  - `src/main/webapp/admin/users.jsp`: Danh sách người dùng, lọc vai trò, bộ phân trang (Pagination UI) tương ứng `page` & `pageSize`.
- **Public & Authentication**:
  - `src/main/webapp/login.jsp`: Loại bỏ scriptlet session error, Bootstrap 5 alert, chuyển hướng đúng dashboard theo vai trò.
  - `src/main/webapp/register.jsp`: Form đăng ký bệnh nhân, validate, toast phản hồi.
  - `src/main/webapp/403.jsp`: Loại bỏ scriptlet lấy URL dashboard theo role, dùng JSTL `<c:choose>`.
  - `src/main/webapp/index.jsp`: Trang chủ với bảng giá dịch vụ dùng `<jsp:useBean>` và `<c:forEach>`, không còn scriptlet.
  - `src/main/webapp/logout.jsp`, `admin.jsp`, `doctor.jsp`, `staff.jsp`, `book.jsp`: Chuyển hướng an toàn bằng `<c:redirect>` sang servlet controllers tương ứng.

---

## Kế hoạch Kiểm thử & Xác minh
1. **Kiểm tra Scriptlets**: Quét grep toàn bộ thư mục `src/main/webapp/` để đảm bảo không còn bất kỳ ký tự `<%` nào (ngoại trừ chỉ thị JSP `<%@` và chú thích `<%--`).
2. **Kiểm thử tự động**: Chạy `mvn clean test` đảm bảo 54/54 test cases đều PASS.
3. **Đóng gói WAR**: Chạy `mvn package` đảm bảo biên dịch thành công và tạo file `DenCli-1.0-SNAPSHOT.war`.
4. **Cập nhật tài liệu**: Ghi nhận toàn bộ tiến độ vào `walkthrough.md`.
