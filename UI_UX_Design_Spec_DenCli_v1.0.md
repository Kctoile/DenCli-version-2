# TÀI LIỆU ĐẶC TẢ GIAO DIỆN UI/UX - HỆ THỐNG DENCLI (v1.0)

Tài liệu này đặc tả cấu trúc màn hình, bố cục (wireframe) và luồng tương tác (user flows) cho hệ thống **DenCli**, đảm bảo tính đồng nhất thiết kế dựa trên Framework **Bootstrap 5** hiển thị Responsive (Desktop, Tablet, Mobile).

---

## 1. QUY CHUẨN THIẾT KẾ CHUNG (DESIGN SYSTEM)

- **Layout Grid:** Sử dụng Bootstrap 5 Grid System (12 cột). Bản dành cho máy tính (Desktop >= 1200px), Máy tính bảng (Tablet >= 768px), và Điện thoại (Mobile < 576px).
- **Màu sắc chủ đạo (Brand Colors):**
  - Primary (Xanh Nha khoa): `#1F3864` (Deep Blue) hoặc `#0d6efd` (Bootstrap Blue).
  - Secondary (Xanh mint y tế): `#198754` (Success/Green) hoặc `#20c997` (Teal).
  - Neutral Light (Nền): `#f8f9fa` (Light Gray).
  - Dark Neutral (Chữ chính): `#212529` (Near Black).
- **Typography:** Font chữ chính `Arial` hoặc `Inter`, sans-serif.
- **Ràng buộc ngôn ngữ:** Toàn bộ giao diện hiển thị 100% bằng Tiếng Việt.

---

## 2. LUỒNG TRANG VÀ BẢN VẼ PHÂN HỆ KHÁCH HÀNG (PATIENT)

### 2.1 Luồng Đăng ký & Đăng nhập (Authentication Flow)
```
[Trang chủ] ──> [Đăng ký] ──> [Kiểm tra SĐT/Email trùng] ──> [Lưu Hash mật khẩu] ──> [Đăng nhập] ──> [Chuyển hướng theo Role]
```

### 2.2 Wireframe 1: Trang chủ & Đặt lịch trực tuyến (UC-01, UC-02, UC-05)
```
+-----------------------------------------------------------------------------------------+
| [Logo DenCli]   Giới thiệu   Dịch vụ & Bảng giá   [Đăng ký] [Đăng nhập] | [Hotline: 1900xxxx]   |
+-----------------------------------------------------------------------------------------+
|                                                                                         |
|  CHĂM SÓC NỤ CƯỜI VIỆT - PHÒNG KHÁM NHA KHOA CÔNG NGHỆ CAO                                |
|  [ Đặt Lịch Hẹn Ngay ] (Cuộn xuống phần Đặt lịch hoặc mở Modal đăng nhập)                |
|                                                                                         |
+-----------------------------------------------------------------------------------------+
| DANH MỤC DỊCH VỤ & BẢNG GIÁ (Hỗ trợ tìm kiếm nhanh: [ Nhập tên dịch vụ... ])            |
| +----------------------------------+--------------------------------------------------+ |
| | Nhóm Dịch Vụ: Nha Khoa Tổng Quát  | Đơn giá: 150.000đ - Khám & tư vấn miễn phí       | |
| | Nhóm Dịch Vụ: Răng Sứ Thẩm Mỹ    | Đơn giá: 2.000.000đ - 8.000.000đ / răng          | |
| | Nhóm Dịch Vụ: Niềng Răng - Chỉnh Nha| Đơn giá: 25.000.000đ - 45.000.000đ / gói         | |
| +----------------------------------+--------------------------------------------------+ |
+-----------------------------------------------------------------------------------------+
| ĐẶT LỊCH HẸN TRỰC TUYẾN (Chỉ dành cho Patient đã đăng nhập)                             |
| 1. Chọn Bác sĩ:     [ Chọn bác sĩ chuyên khoa... (Dropdown chứa danh sách bác sĩ)  v ]     |
| 2. Chọn Ngày khám:  [ DD/MM/YYYY (Chọn từ Lịch Calendar)                         ]     |
| 3. Khung giờ trống: [ 08:00 ] [ 09:00 ] [ 10:00 ] (Vô hiệu hóa khung giờ đã bị trùng)   |
| 4. Chọn Dịch vụ:    [ Khám tổng quát | Nhổ răng khôn | Tẩy trắng răng...           v ]     |
| 5. Ghi chú thêm:    [ Nhập triệu chứng hoặc yêu cầu riêng...                         ]     |
|                                                                                         |
|                     [ XÁC NHẬN ĐẶT LỊCH ] -> Tạo trạng thái Chờ xác nhận                |
+-----------------------------------------------------------------------------------------+
```

### 2.3 Wireframe 2: Dashboard Bệnh nhân - Quản lý lịch & Mua thuốc (UC-06, UC-07, UC-08)
```
+-----------------------------------------------------------------------------------------+
| [Logo DenCli]   [ Đặt lịch mới ]   [ Lịch hẹn của tôi ]   [ Đơn thuốc & Thanh toán ] | [Tài khoản] |
+-----------------------------------------------------------------------------------------+
| Xin chào, Nguyễn Văn A (Mã BN: BN-1002)                                                 |
|                                                                                         |
| DANH SÁCH LỊCH HẸN ĐÃ ĐẶT                                                               |
| +--------------+--------------+---------------+-------------------+--------------------+ |
| | Ngày & Giờ   | Bác sĩ       | Dịch vụ       | Trạng thái        | Thao tác           | |
| +--------------+--------------+---------------+-------------------+--------------------+ |
| | 15/08/2026   | BS. Trần C   | Niềng răng    | Chờ xác nhận      | [ HỦY LỊCH ]       | |
| | 10/08/2026   | BS. Lê D     | Khám tổng quát| Đã hoàn tất       | [ Xem bệnh án ]    | |
| +--------------+--------------+---------------+-------------------+--------------------+ |
| *Lưu ý: Bạn chỉ được phép hủy lịch hẹn trước thời gian khám ít nhất 2 giờ.              |
|                                                                                         |
| ĐƠN THUỐC CỦA TÔI                                                                       |
| Đơn thuốc ngày 10/08/2026 - Bác sĩ kê: BS. Lê D (Chẩn đoán: Sâu răng hàm dưới)           |
| +-----------------+---------------+---------------+-----------------+------------------+ |
| | Tên Thuốc       | Số lượng kê   | Số lượng mua  | Đơn giá         | Thành tiền       | |
| +-----------------+---------------+---------------+-----------------+------------------+ |
| | Paracetamol     | 10 Viên       | [ 10 ] (Max10)| 2.000đ          | 20.000đ          | |
| | Kháng viêm Alph | 15 Viên       | [  5 ] (Max15)| 5.000đ          | 25.000đ          | |
| +-----------------+---------------+---------------+-----------------+------------------+ |
| Tổng tiền tạm tính: 45.000đ                                                             |
| [ XÁC NHẬN MUA THUỐC & THANH TOÁN ONLINE/TẠI QUẦY ]                                      |
+-----------------------------------------------------------------------------------------+
```

---

## 3. BẢN VẼ PHÂN HỆ TIẾP ĐÓN (RECEPTIONIST)

### 3.1 Wireframe 3: Tra cứu & Check-in tại quầy (UC-09, UC-10)
```
+-----------------------------------------------------------------------------------------+
| [DenCli Staff]  [ Danh sách tiếp đón ]   [ Đăng ký lịch tại quầy (Walk-in) ]   [ Đăng xuất] |
+-----------------------------------------------------------------------------------------+
| DANH SÁCH TIẾP ĐÓN TRONG NGÀY (12/08/2026)                                              |
| Tìm kiếm nhanh: [ Nhập SĐT hoặc Tên bệnh nhân... ]  [ Lọc theo Bác sĩ: Tất cả  v ]      |
|                                                                                         |
| +------------+---------------+------------+---------------+---------------+-----------+ |
| | Thời gian  | Tên bệnh nhân | Số ĐT      | Bác sĩ khám   | Trạng thái    | Thao tác  | |
| +------------+---------------+------------+---------------+---------------+-----------+ |
| | 09:00      | Trần Văn B    | 0905123456 | BS. Nguyễn A  | CONFIRMED     | [CHECKIN] | |
| | 10:00      | Lê Thị C      | 0987654321 | BS. Trần C    | Checked In    | Phòng:01  | |
| +------------+---------------+------------+---------------+---------------+-----------+ |
|                                                                                         |
| [BUTTON: ĐĂNG KÝ WALK-IN TẠI QUẦY] (Mở form nhập nhanh: Họ tên, SĐT, Bác sĩ, Khung giờ) |
+-----------------------------------------------------------------------------------------+
```

---

## 4. BẢN VẼ PHÂN HỆ BÁC SĨ (DENTIST)

### 4.1 Wireframe 4: Buồng khám & Kê đơn thuốc (UC-11, UC-12, UC-13)
```
+-----------------------------------------------------------------------------------------+
| [DenCli Doctor]  Bác sĩ: BS. Nguyễn Văn A  |  Phòng khám: Phòng số 01 | [ Đăng xuất ]   |
+-----------------------------------------------------------------------------------------+
| DANH SÁCH BỆNH NHÂN CHỜ KHÁM TRONG NGÀY (12/08/2026)                                    |
| +---------+------------------+------------+-----------------+-------------------------+ |
| | Giờ     | Tên bệnh nhân    | Mã BN      | Trạng thái      | Hành động               | |
| +---------+------------------+------------+-----------------+-------------------------+ |
| | 09:00   | Trần Văn B       | BN-1052    | Checked In      | [TIẾN HÀNH KHÁM]        | |
| +---------+------------------+------------+-----------------+-------------------------+ |
|                                                                                         |
| KHU VỰC THAO TÁC KHÁM (Bệnh nhân đang khám: Trần Văn B)                                 |
| - Lịch sử bệnh án: [ Xem lịch sử khám cũ của bệnh nhân này ] (Popup)                     |
|                                                                                         |
| Chẩn đoán & Kết quả điều trị:                                                            |
| [ Sâu răng hàm số 6, cần chỉ định trám răng composite và vệ sinh cao răng...         ]   |
|                                                                                         |
| Chỉ định dịch vụ phát sinh:                                                             |
| [x] Trám răng sứ thẩm mỹ (Đơn giá: 500.000đ)                                            |
| [ ] Nhổ răng khôn (Đơn giá: 1.200.000đ)                                                 |
|                                                                                         |
| Kê đơn thuốc kèm theo:                                                                   |
| +-----------------+---------------------+---------------------+-------------------------+ |
| | Tên thuốc       | Số lượng kê         | Liều dùng & HDSD    | Thao tác                | |
| +-----------------+---------------------+---------------------+-------------------------+ |
| | [ Amoxicillin v]| [ 10 ] (Tồn kho:500)| Ngày uống 2 lần...  | [ Xóa ]                 | |
| | [ Paracetamol v]| [  5 ] (Tồn kho:210)| Khi đau nhức uống...| [ Xóa ]                 | |
| +-----------------+---------------------+---------------------+-------------------------+ |
| [ + Thêm thuốc ]                                                                        |
|                                                                                         |
|                     [ LƯU KẾT QUẢ KHÁM & KÊ ĐƠN THÀNH CÔNG ]                            |
+-----------------------------------------------------------------------------------------+
```

---

## 5. BẢN VẼ PHÂN HỆ QUẢN TRỊ VIÊN (ADMIN)

### 5.1 Wireframe 5: Dashboard Quản lý Danh mục & Báo cáo Thống kê (UC-14, UC-15, UC-16, UC-17)
```
+-----------------------------------------------------------------------------------------+
| [DenCli Admin]  [ QL Người dùng ]  [ QL Dịch vụ ]  [ QL Thuốc ]  [ Báo cáo doanh thu ] |
+-----------------------------------------------------------------------------------------+
| BÁO CÁO THỐNG KÊ DOANH THU & HIỆU SUẤT                                                   |
| Khoảng thời gian: Từ ngày [ DD/MM/YYYY ] Đến ngày [ DD/MM/YYYY ]  [ Xem thống kê ]      |
|                                                                                         |
| +----------------------------------+--------------------------------------------------+ |
| | THỐNG KÊ CHUNG                   | BIỂU ĐỒ TĂNG TRƯỞNG DOANH THU (Cột / Đường)       | |
| | - Tổng số lịch hẹn: 1,250        |                                                  | |
| | - Số lịch đã hoàn tất: 1,120     | [Doanh thu]                                      | |
| | - Doanh thu dịch vụ: 450.000.000đ|   |   |                                              | |
| | - Doanh thu bán thuốc: 82.000.000|   |   |     |                                        | |
| | - Tổng Doanh Thu: 532.000.000đ  |   +---+-----+-----+----------> [Tháng]             | |
| |                                  |  T5  T6    T7    T8                               | |
| +----------------------------------+--------------------------------------------------+ |
|                                                                                         |
| QUẢN LÝ DANH MỤC THUỐC                                                                  |
| [ Thêm thuốc mới ]  [ Xuất Excel ]                                                       |
| +----------+--------------------+------------+---------------+---------------+----------+ |
| | Mã Thuốc | Tên thuốc          | Đơn vị     | Đơn giá bán   | Số lượng tồn  | Thao tác | |
| +----------+--------------------+------------+---------------+---------------+----------+ |
| | MED-001  | Paracetamol 500mg  | Viên       | 2.000đ        | 450           | [Sửa][Xóa| |
| | MED-002  | Amoxicillin 500mg  | Viên       | 3.500đ        | 120 (Sắp hết) | [Sửa][Xóa| |
| +----------+--------------------+------------+---------------+---------------+----------+ |
+-----------------------------------------------------------------------------------------+
```

---
*Hết tài liệu Thiết kế UI/UX.*