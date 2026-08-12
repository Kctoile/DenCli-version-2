# TÀI LIỆU ĐẶC TẢ CHI TIẾT API CONTRACT - HỆ THỐNG DENCLI (v1.0)

Tài liệu này định nghĩa cấu trúc Request và Response cho các API/Servlet Endpoints cốt lõi của hệ thống **DenCli**, tuân thủ kiến trúc MVC (Model-View-Controller) và mô hình trao đổi dữ liệu JSON/HTTP Status Code.

---

## 1. QUY CHUẨN THÔNG ĐIỆP CHUNG (COMMON MESSAGES)

Tất cả các API trả về JSON đều sử dụng chung một cấu trúc chuẩn khi phản hồi trạng thái:

### 1.1 Phản hồi thành công (Standard Success Response)
- **HTTP Status Code:** `200 OK` hoặc `201 Created`
```json
{
  "success": true,
  "message": "Thao tác thực hiện thành công",
  "data": {} // Có thể là Object hoặc Array dữ liệu cụ thể
}
```

### 1.2 Phản hồi lỗi nghiệp vụ / Hệ thống (Standard Error Response)
- **HTTP Status Code:** `400 Bad Request` hoặc `500 Internal Server Error`
```json
{
  "success": false,
  "message": "Mô tả chi tiết lỗi tiếng Việt (ví dụ: Trùng lịch hẹn của bác sĩ)",
  "error_code": "ERR_DUPLICATE_APPOINTMENT"
}
```

---

## 2. CHI TIẾT ENDPOINTS CHO PHÂN HỆ AUTHENTICATION & GUEST

### 2.1 [POST] `/register` - Đăng ký tài khoản Bệnh nhân (UC-03)
Cho phép khách vãng lai đăng ký tài khoản với vai trò mặc định là `CUSTOMER`.

- **Content-Type:** `application/x-www-form-urlencoded` hoặc `application/json`
- **Request Body:**
```json
{
  "full_name": "Nguyễn Văn A",
  "email": "nguyenvana@gmail.com",
  "phone": "0905123456",
  "password": "Mật khẩu thô chưa mã hóa",
  "gender": "Nam",
  "dob": "1995-10-25",
  "address": "123 Nguyễn Trãi, Đà Nẵng"
}
```
- **Response (201 Created):**
```json
{
  "success": true,
  "message": "Đăng ký tài khoản thành công!",
  "data": {
    "user_id": 1024,
    "email": "nguyenvana@gmail.com"
  }
}
```
- **Response (400 Bad Request - Trùng SĐT hoặc Email):**
```json
{
  "success": false,
  "message": "Số điện thoại hoặc Email đã tồn tại trên hệ thống.",
  "error_code": "ERR_DUPLICATE_USER"
}
```

### 2.2 [POST] `/login` - Đăng nhập tài khoản (UC-04)
Thực hiện xác thực thông tin và tạo Session cho người dùng.

- **Request Body:**
```json
{
  "email_or_phone": "nguyenvana@gmail.com",
  "password": "Password123"
}
```
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Đăng nhập thành công!",
  "data": {
    "user_id": 1024,
    "full_name": "Nguyễn Văn A",
    "role": "CUSTOMER" // Hoặc ADMIN, DOCTOR, STAFF
  }
}
```
- **Response (401 Unauthorized - Khóa tài khoản sau 5 lần nhập sai):**
```json
{
  "success": false,
  "message": "Tài khoản của bạn đã bị tạm khóa do nhập sai mật khẩu quá 5 lần liên tiếp.",
  "error_code": "ERR_ACCOUNT_LOCKED"
}
```

---

## 3. PHÂN HỆ BỆNH NHÂN (PATIENT)

### 3.1 [POST] `/api/appointments/book` - Đặt lịch hẹn trực tuyến (UC-05)
Yêu cầu Session hợp lệ có Role `CUSTOMER`.

- **Headers:** `Authorization: SessionID`
- **Request Body:**
```json
{
  "doctor_id": 5,
  "appointment_date": "2026-08-20",
  "appointment_time": "09:00:00",
  "service_ids": [1, 3], // Mảng ID dịch vụ đã chọn (ví dụ: Khám tổng quát, Tẩy trắng răng)
  "notes": "Răng hàm dưới của tôi bị đau nhức nhiều ngày qua"
}
```
- **Response (201 Created):**
```json
{
  "success": true,
  "message": "Đăng ký lịch hẹn thành công! Vui lòng chờ cuộc gọi xác nhận.",
  "data": {
    "appointment_id": 450,
    "status": "Pending"
  }
}
```
- **Response (409 Conflict - Trùng lịch khám của Bác sĩ):**
```json
{
  "success": false,
  "message": "Rất tiếc, khung giờ 09:00:00 ngày 2026-08-20 của Bác sĩ này đã bị trùng. Vui lòng chọn khung giờ khác.",
  "error_code": "ERR_SLOT_TAKEN"
}
```

### 3.2 [POST] `/api/appointments/cancel` - Hủy lịch hẹn (UC-06)
- **Request Body:**
```json
{
  "appointment_id": 450
}
```
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Đã hủy lịch hẹn thành công!"
}
```
- **Response (400 Bad Request - Trễ giờ quy định):**
```json
{
  "success": false,
  "message": "Không thể hủy lịch! Bạn chỉ có thể hủy lịch trước giờ khám ít nhất 2 tiếng.",
  "error_code": "ERR_CANCEL_TIME_EXCEEDED"
}
```

---

## 4. PHÂN HỆ BÁC SĨ (DENTIST)

### 4.1 [POST] `/api/dentist/complete-exam` - Lưu chẩn đoán & Chỉ định phát sinh & Kê đơn thuốc (UC-12, UC-13)
Yêu cầu Session có Role `DOCTOR`.

- **Request Body:**
```json
{
  "appointment_id": 450,
  "result_details": "Phát hiện sâu răng hàm số 6. Đã tiến hành mài và trám composite.",
  "prescribed_services": [
    {
      "service_id": 2, // Trám răng composite
      "notes": "Thực hiện ở răng hàm dưới"
    }
  ],
  "prescription": {
    "instructions": "Uống sau khi ăn no, sáng tối 2 lần.",
    "medicines": [
      {
        "medicine_id": 101, // Paracetamol
        "prescribed_quantity": 10
      },
      {
        "medicine_id": 104, // Amoxicillin
        "prescribed_quantity": 15
      }
    ]
  }
}
```
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Ghi nhận kết quả khám và kê đơn thuốc thành công!",
  "data": {
    "result_id": 89,
    "prescription_id": 45
  }
}
```

---

## 5. PHÂN HỆ TIẾP Đ đón & THANH TOÁN (RECEPTIONIST & PAYMENT)

### 5.1 [POST] `/api/receptionist/checkin` - Check-in bệnh nhân (UC-09)
Nhân viên tiếp đón cập nhật trạng thái lịch hẹn khi bệnh nhân có mặt tại phòng khám.

- **Request Body:**
```json
{
  "appointment_id": 450,
  "room": "Phòng số 02"
}
```
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Check-in bệnh nhân thành công. Đã gán vào Phòng số 02."
}
```

### 5.2 [POST] `/api/payment/confirm` - Xác nhận thanh toán hóa đơn (UC-08)
Xác nhận bệnh nhân đã thanh toán chi phí tại quầy (tiền mặt / chuyển khoản).

- **Request Body:**
```json
{
  "appointment_id": 450,
  "payment_method": "CASH" // Hoặc BANK_TRANSFER
}
```
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Đã xác nhận thanh toán thành công hóa đơn!",
  "data": {
    "invoice_id": 310,
    "total_amount": 650000.00,
    "status": "COMPLETED"
  }
}
```

---
*Hết tài liệu Đặc tả API Contract.*