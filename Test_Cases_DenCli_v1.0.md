# TÀI LIỆU KỊCH BẢN KIỂM THỬ (TEST CASES) - HỆ THỐNG DENCLI (v1.0)

Tài liệu này cung cấp các kịch bản kiểm thử (Test Cases) chi tiết cho các tính năng cốt lõi của hệ thống **DenCli**, bao gồm các ca kiểm thử tích cực (Positive), tiêu cực (Negative), và kiểm thử biên (Boundary), nhằm đảm bảo chất lượng hệ thống trước khi đưa vào vận hành.

---

## 1. PHẠM VI KIỂM THỬ (TESTING SCOPE)

- **Kiểm thử chức năng:** Đăng ký, Đăng nhập, Đặt lịch hẹn, Check-in, Khám bệnh, Kê đơn, Mua thuốc, Thanh toán và Thống kê.
- **Ràng buộc an toàn:** Khóa tài khoản sau 5 lần đăng nhập sai, không cho đặt trùng lịch, không cho hủy lịch trễ hạn, không cho mua thuốc vượt số lượng kê.

---

## 2. CHI TIẾT CÁC KỊCH BẢN KIỂM THỬ (TEST CASES)

### 2.1 Nhóm Đăng ký & Đăng nhập (UC-03, UC-04)

#### TC-AUTH-01: Đăng ký tài khoản mới hợp lệ (Positive)
- **Mô tả:** Đảm bảo khách vãng lai có thể đăng ký tài khoản Bệnh nhân thành công khi nhập đầy đủ thông tin hợp lệ.
- **Các bước thực hiện:**
  1. Truy cập trang Đăng ký.
  2. Nhập: Họ tên = "Nguyễn Văn A", Số ĐT = "0905111222", Email = "bena@gmail.com", Mật khẩu = "Pass12345", Ngày sinh = "20/10/1995", Giới tính = "Nam".
  3. Nhấp nút "Đăng ký".
- **Kết quả mong đợi:**
  - Hệ thống báo: "Đăng ký thành công".
  - Chuyển hướng người dùng sang trang Đăng nhập.
  - Kiểm tra database: Mật khẩu của tài khoản mới được mã hóa (băm) an toàn, không lưu text thô.

#### TC-AUTH-02: Đăng ký trùng Số điện thoại hoặc Email (Negative)
- **Mô tả:** Kiểm tra hệ thống từ chối đăng ký và báo lỗi nếu Số điện thoại hoặc Email đã tồn tại.
- **Các bước thực hiện:**
  1. Thực hiện lại các bước đăng ký với Số ĐT "0905111222" hoặc Email "bena@gmail.com" đã đăng ký ở TC-AUTH-01.
  2. Nhấp nút "Đăng ký".
- **Kết quả mong đợi:**
  - Hệ thống hiển thị cảnh báo: "Số điện thoại hoặc Email đã tồn tại trên hệ thống."
  - Không có dữ liệu trùng lặp nào được thêm vào bảng `users`.

#### TC-AUTH-03: Khóa tài khoản do nhập sai mật khẩu 5 lần liên tiếp (Security/Negative)
- **Mô tả:** Kiểm tra cơ chế tự động khóa tài khoản sau 5 lần thử sai để chống brute-force.
- **Các bước thực hiện:**
  1. Truy cập trang Đăng nhập.
  2. Nhập Email "bena@gmail.com" và mật khẩu sai "SaiPass1". Thực hiện đăng nhập. (Lần 1)
  3. Lặp lại việc nhập sai mật khẩu thêm 4 lần tiếp theo liên tiếp.
- **Kết quả mong đợi:**
  - Ở lần thứ 5, hệ thống hiển thị thông báo: "Tài khoản của bạn đã bị tạm khóa do nhập sai mật khẩu quá 5 lần liên tiếp."
  - Trạng thái tài khoản chuyển sang tạm khóa. Thử đăng nhập lại bằng mật khẩu ĐÚNG vẫn không thành công cho đến khi hết thời gian khóa hoặc được Admin mở khóa.

---

### 2.2 Nhóm Đặt lịch & Quản lý lịch hẹn trực tuyến (UC-05, UC-06)

#### TC-APP-01: Đặt lịch hẹn trực tuyến thành công (Positive)
- **Mô tả:** Bệnh nhân đã đăng nhập đặt lịch hẹn thành công với một khung giờ trống.
- **Các bước thực hiện:**
  1. Đăng nhập tài khoản Bệnh nhân.
  2. Chọn Bác sĩ = "BS. Nguyễn Văn A", Ngày khám = "20/08/2026", Khung giờ = "09:00", Dịch vụ = "Khám tổng quát".
  3. Nhấp "Xác nhận đặt lịch".
- **Kết quả mong đợi:**
  - Hệ thống báo: "Đăng ký lịch hẹn thành công! Vui lòng chờ cuộc gọi xác nhận."
  - Một dòng ghi nhận mới được tạo trong bảng `appointments` với trạng thái mặc định là `Pending` (Chờ xác nhận).

#### TC-APP-02: Đặt lịch trùng khung giờ đã được đặt của cùng một Bác sĩ (Boundary/Negative)
- **Mô tả:** Đảm bảo hệ thống chặn không cho phép đặt trùng một khung giờ đã có lịch hẹn đã xác nhận (`CONFIRMED` hoặc `Pending`) của cùng một bác sĩ.
- **Các bước thực hiện:**
  1. Sử dụng tài khoản bệnh nhân khác đăng nhập vào hệ thống.
  2. Chọn cùng Bác sĩ = "BS. Nguyễn Văn A", Ngày khám = "20/08/2026", Khung giờ = "09:00" (khung giờ đã được đặt thành công ở TC-APP-01).
  3. Nhấp "Xác nhận đặt lịch".
- **Kết quả mong đợi:**
  - Khung giờ "09:00" hiển thị trạng thái mờ (Disabled) không thể chọn, hoặc nếu cố tình gửi request trực tiếp, hệ thống sẽ trả về lỗi: "Khung giờ này đã được đặt. Vui lòng chọn khung giờ khác."
  - Database không ghi nhận bản ghi trùng lắp (ràng buộc bởi UNIQUE index trên `doctor_id, appointment_date, appointment_time`).

#### TC-APP-03: Hủy lịch hẹn sát giờ khám < 2 tiếng (Negative/Boundary)
- **Mô tả:** Đảm bảo bệnh nhân không thể tự ý hủy lịch hẹn nếu thời gian hiện tại cách thời điểm khám ít hơn 2 tiếng.
- **Các bước thực hiện:**
  1. Tạo lịch hẹn vào lúc "10:00" ngày hiện tại.
  2. Đến lúc "08:30" cùng ngày (cách giờ hẹn 1.5 tiếng), Bệnh nhân truy cập mục lịch hẹn và nhấp "Hủy lịch".
- **Kết quả mong đợi:**
  - Hệ thống hiển thị cảnh báo: "Không thể hủy lịch! Bạn chỉ có thể hủy lịch trước giờ khám ít nhất 2 tiếng."
  - Trạng thái lịch hẹn trong database giữ nguyên, không thay đổi thành `Cancelled`.

---

### 2.3 Nhóm Tiếp đón & Khám bệnh (UC-09, UC-12, UC-13)

#### TC-CLINIC-01: Check-in bệnh nhân và gán phòng khám (Positive)
- **Mô tả:** Tiếp đón thực hiện tiếp nhận bệnh nhân có mặt tại quầy và điều phối phòng.
- **Các bước thực hiện:**
  1. Nhân viên Tiếp đón tìm kiếm lịch hẹn của bệnh nhân "Nguyễn Văn A" ngày hôm nay.
  2. Nhấp chọn "Check-in", chọn Phòng khám = "Phòng số 01".
- **Kết quả mong đợi:**
  - Trạng thái lịch hẹn chuyển sang `Checked In`. Cột `room` được cập nhật giá trị là "Phòng số 01".

#### TC-CLINIC-02: Bác sĩ ghi kết quả khám, chỉ định dịch vụ phát sinh và kê đơn thuốc (Positive)
- **Mô tả:** Bác sĩ hoàn thành khám cho bệnh nhân, ghi kết quả, chỉ định thêm dịch vụ và kê toa thuốc.
- **Các bước thực hiện:**
  1. Bác sĩ đăng nhập, chọn bệnh nhân "Nguyễn Văn A" trong danh sách phòng khám của mình.
  2. Nhập Chẩn đoán = "Viêm tủy răng hàm số 5".
  3. Chỉ định dịch vụ phát sinh = "Lấy tủy răng" và "Hàn ống tủy".
  4. Kê đơn thuốc = "Paracetamol (10 viên)", "Amoxicillin (14 viên)".
  5. Nhấp "Hoàn tất khám".
- **Kết quả mong đợi:**
  - Trạng thái lịch hẹn chuyển sang `Completed`.
  - Kết quả chẩn đoán được lưu vào bảng `examination_results`.
  - Đơn thuốc được tạo thành công trong bảng `prescriptions` và `prescription_details`.

---

### 2.4 Nhóm Bán thuốc & Thanh toán (UC-07, UC-08)

#### TC-PAY-01: Bệnh nhân mua một phần số lượng thuốc trong đơn (Boundary/Positive)
- **Mô tả:** Đảm bảo bệnh nhân có thể mua số lượng thuốc ít hơn hoặc bằng lượng bác sĩ kê toa, tự động cập nhật đúng số tiền và trạng thái đơn thuốc.
- **Các bước thực hiện:**
  1. Bệnh nhân xem đơn thuốc được kê ở TC-CLINIC-02 (gồm 10 viên Paracetamol, đơn giá 2.000đ/viên).
  2. Điều chỉnh số lượng muốn mua thực tế cho Paracetamol từ "10" xuống "6" viên.
  3. Nhấp "Xác nhận mua thuốc & Thanh toán".
- **Kết quả mong đợi:**
  - Tổng số tiền thanh toán hiển thị chính xác: 6 * 2.000đ = 12.000đ.
  - Sau khi xác nhận thanh toán thành công, trạng thái đơn thuốc cập nhật thành "Đã mua một phần" (đã bán 6 viên, còn lại 4 viên được phép mua sau).

#### TC-PAY-02: Điều chỉnh số lượng mua vượt mức bác sĩ kê toa (Negative)
- **Mô tả:** Đảm bảo hệ thống chặn không cho phép nhập số lượng thuốc mua thực tế vượt quá mức tối đa bác sĩ đã chỉ định trong đơn thuốc.
- **Các bước thực hiện:**
  1. Bệnh nhân mở đơn thuốc gồm 10 viên Paracetamol.
  2. Cố tình thay đổi số lượng mua thực tế thành "12" viên.
  3. Nhấp "Xác nhận mua thuốc".
- **Kết quả mong đợi:**
  - Ô nhập liệu báo lỗi đỏ và không cho phép tăng quá số lượng "10".
  - Nếu gửi request trực tiếp bypass giao diện, hệ thống trả lỗi: "Số lượng thuốc mua không được vượt quá số lượng kê trong đơn thuốc."

---
*Hết tài liệu Kịch bản kiểm thử.*