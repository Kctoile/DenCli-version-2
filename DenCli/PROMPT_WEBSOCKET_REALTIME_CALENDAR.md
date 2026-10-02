# 🚀 PROMPT SPECIFICATION: TRIỂN KHAI TÍNH NĂNG REAL-TIME APPOINTMENT CALENDAR QUA WEBSOCKET CHO DENCLI

> **Dành cho**: AI Coding Assistant / Senior Java Fullstack Developer  
> **Dự án**: DenCli - Dental Clinic Management Platform  
> **Công nghệ**: Jakarta EE 10 (Servlet 6.0, WebSocket 2.1), Java 17+, Vanilla ES6 JS, Bootstrap 5  
> **Mục tiêu**: Bổ sung cơ chế đồng bộ thời gian thực (Real-time Slot Synchronization) cho lịch hẹn khám nha khoa giữa Khách hàng và Lễ tân.

---

## 📌 1. BỐI CẢNH VÀ BÀI TOÁN KỸ THUẬT (PROBLEM STATEMENT)

Hiện tại trên hệ thống DenCli, việc kiểm tra trùng lịch (Slot Conflict) chỉ diễn ra khi người dùng bấm gửi form đặt lịch (`POST /customer/book` hoặc tại quầy tiếp đón `POST /staff/reception`).  
Nếu hai khách hàng (hoặc một khách hàng và lễ tân) cùng mở form chọn khung giờ **09:00 - 10:00** của **BS. Hoàng** vào ngày mai:
* Người bấm xác nhận trước sẽ thành công.
* Người bấm sau sẽ bị báo lỗi trùng slot, gây ức chế trải nghiệm người dùng (UX friction).

### 🎯 Mục tiêu yêu cầu:
1. Khi có bất kỳ ai đặt lịch thành công một khung giờ của Bác sĩ X vào Ngày Y:
   * **Ngay lập tức**, tất cả các thiết bị/trình duyệt khác đang mở trang đặt lịch (`customer/book.jsp`) hoặc quầy lễ tân (`staff/reception.jsp`) xem cùng Bác sĩ X và Ngày Y sẽ **tự động cập nhật ô giờ đó thành màu xám ("Đã có người đặt")**, đồng thời `disabled` không cho chọn nữa mà **không cần tải lại trang (Zero page reload)**.
2. Hiển thị thông báo Toast nhẹ nhàng: *"Bác sĩ vừa có lịch hẹn mới vào lúc [Giờ] - [Ngày]"*.
3. Nếu khách hàng đang chọn dở chính khung giờ vừa bị người khác chốt: hệ thống hiển thị cảnh báo đỏ và tự động bỏ chọn ô đó.

---

## 🏗️ 2. THIẾT KẾ KIẾN TRÚC HỆ THỐNG (SYSTEM ARCHITECTURE)

```
[ Khách hàng A / Lễ tân ]
         │ (1) POST /customer/book hoặc /staff/reception
         ▼
 ┌──────────────────────┐
 │  AppointmentService  │ ──► Ghi DB (MySQL) thành công
 └──────────┬───────────┘
            │ (2) Kích hoạt broadcast event
            ▼
 ┌────────────────────────────────────────────────────────┐
 │   AppointmentCalendarEndpoint (@ServerEndpoint)        │
 │   - Quản lý tập hợp Sessions đang online                │
 │   - Lọc theo doctorId và appointmentDate                │
 └──────────────────────────┬─────────────────────────────┘
                            │ (3) WebSocket Push (JSON Payload)
                            ▼
          ┌───────────────────────────────────┐
          │  Các trình duyệt Khách hàng B, C  │
          │  - Nhận WS message qua ES6        │
          │  - Đổi màu slot sang "Đã đặt"     │
          │  - Toast thông báo tức thì        │
          └───────────────────────────────────┘
```

---

## 📝 3. ĐẶC TẢ CHI TIẾT CÁC BƯỚC THỰC HIỆN

### Bước 1: Cấu hình Maven Dependencies (Nếu chưa có)
Kiểm tra trong `pom.xml`, đảm bảo dependency của Jakarta WebSocket API đã sẵn sàng:
```xml
<dependency>
    <groupId>jakarta.websocket</groupId>
    <artifactId>jakarta.websocket-api</artifactId>
    <version>2.1.1</version>
    <scope>provided</scope>
</dependency>
```

---

### Bước 2: Xây dựng WebSocket Server Endpoint (`AppointmentCalendarEndpoint.java`)
Tạo lớp Java tại package: `com.devjava.dencli.websocket.AppointmentCalendarEndpoint`
* **Annotation**: `@ServerEndpoint(value = "/ws/appointment-calendar/{doctorId}/{date}")`
* **Session Registry**: Sử dụng `ConcurrentHashMap<String, Set<Session>>` luồng an toàn (Thread-safe) để gom nhóm các kết nối theo key: `"{doctorId}_{date}"`.
* **Vòng đời Endpoint**:
  * `@OnOpen`: Lưu `Session` vào nhóm tương ứng.
  * `@OnClose`: Xóa `Session` khỏi nhóm khi người dùng rời trang hoặc đóng tab.
  * `@OnError`: Log lỗi và dọn dẹp session chết.
  * `public static void broadcastSlotBooked(int doctorId, String date, String timeSlot, String patientName)`:
    * Duyệt qua danh sách `Session` đang theo dõi cặp `(doctorId, date)`.
    * Gửi gói tin JSON broadcast:
    ```json
    {
      "event": "SLOT_BOOKED",
      "doctorId": 2,
      "date": "2026-10-05",
      "timeSlot": "09:00",
      "message": "Khung giờ 09:00 đã vừa được đặt thành công."
    }
    ```

---

### Bước 3: Tích hợp Broadcast vào Service Layer
Tại phương thức tạo lịch hẹn thành công trong `AppointmentService.java` (hoặc sau khi giao dịch commit tại `AppointmentBookingServlet` / `ReceptionServlet`):
```java
// Sau khi insert DB thành công:
AppointmentCalendarEndpoint.broadcastSlotBooked(
    appointment.getDoctorId(),
    appointment.getAppointmentDate().toString(),
    appointment.getAppointmentTime().toString(),
    appointment.getPatientName()
);
```

---

### Bước 4: Xây dựng Client-Side JavaScript (`appointment-realtime.js`)
Tạo file tại: `src/main/webapp/assets/js/appointment-realtime.js`
* **Khởi tạo kết nối**:
  * Lắng nghe sự kiện thay đổi trên `#doctorId` và `#appointmentDate`.
  * Nếu cả hai trường đã có giá trị, mở kết nối WebSocket:
    `ws = new WebSocket((location.protocol === 'https:' ? 'wss://' : 'ws://') + location.host + contextPath + '/ws/appointment-calendar/' + doctorId + '/' + date);`
  * Tự động đóng kết nối cũ và tạo kết nối mới nếu người dùng đổi bác sĩ hoặc chọn ngày khác.
* **Xử lý sự kiện `onmessage`**:
  * Đọc `event.data` JSON.
  * Nếu `data.event === 'SLOT_BOOKED'`:
    * Tìm thẻ `<option value="${data.timeSlot}">` hoặc ô chọn giờ tương ứng.
    * Gán thuộc tính `disabled = true`, bổ sung nhãn `"(Đã có người đặt)"`.
    * Đổi màu nền sang xám mờ / gạch ngang.
    * Nếu khung giờ đó đang được người dùng hiện tại chọn (`select.value === data.timeSlot`):
      * Đặt lại `select.value = ""`.
      * Kích hoạt Alert cảnh báo đỏ: *"Khung giờ bạn vừa chọn đã được người khác đặt trước vài giây. Vui lòng chọn khung giờ khác."*
    * Gọi hàm `showToast('info', data.message)` để hiển thị thông báo góc màn hình.
* **Reconnection & Fallback**:
  * Tự động thử kết nối lại sau 3 giây nếu mạng chập chờn (`ws.onclose`).

---

### Bước 5: Cập nhật Giao diện Đặt Lịch
Tích hợp `appointment-realtime.js` vào:
1. `src/main/webapp/customer/book.jsp` (Giao diện Khách hàng đặt lịch online)
2. `src/main/webapp/staff/reception.jsp` (Giao diện Lễ tân tiếp đón & xếp lịch tại quầy)

---

### Bước 6: Viết Bộ Test Kiểm Thử Tự Động (JUnit 5 + Mockito)
1. **Unit Test Endpoint**: Kiểm tra hàm `broadcastSlotBooked` gửi đúng JSON payload và không gây ngoại lệ khi session đóng bất ngờ.
2. **State Validation Test**: Kiểm tra trạng thái slot chuyển từ `AVAILABLE` sang `CONFLICT` khi có 2 request đồng thời.
3. Đảm bảo toàn bộ bộ test `mvn test` (hiện có 82 bài test) tiếp tục vượt qua 100%.

---

## ✅ 4. TIÊU CHÍ NGHIỆM THU (ACCEPTANCE CRITERIA)

| STT | Kịch bản kiểm thử | Kết quả mong đợi |
| :---: | :--- | :--- |
| 1 | Mở 2 trình duyệt A và B (Chrome & Edge) cùng vào trang `/customer/book`. Chọn cùng Bác sĩ và Ngày khám. | Cả 2 cùng hiển thị đầy đủ các khung giờ còn trống. |
| 2 | Trình duyệt A bấm chọn `09:00` và gửi form thành công. | Tại Trình duyệt B, **ngay tức khắc** khung giờ `09:00` bị đổi sang trạng thái disabled kèm chữ `(Đã đặt)`. |
| 3 | Trình duyệt B đang nhấn giữ `09:00` mà chưa kịp submit thì A submit thành công. | Trình duyệt B nhận WebSocket event, tự động bỏ chọn `09:00` và hiển thị cảnh báo đỏ yêu cầu chọn slot khác. |
| 4 | Trình duyệt B đổi sang chọn Bác sĩ khác hoặc Ngày khác. | WebSocket cũ tự động đóng (`disconnect`), mở WebSocket mới lắng nghe đúng kênh bác sĩ mới. |
| 5 | Bộ kiểm thử tự động | Chạy `mvn test` đạt `BUILD SUCCESS`, không làm gãy bất kỳ tính năng hiện hữu nào. |
