<%-- 
 * Purpose: Online appointment booking page for patients (UC-05).
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.dencli.models.User"%>
<%@page import="com.mycompany.dencli.models.Service"%>
<%@page import="com.mycompany.dencli.dao.UserDAO"%>
<%@page import="com.mycompany.dencli.dao.ServiceDAO"%>
<%@page import="java.util.List"%>
<%
    // 1. Kiểm tra xác thực Session của Bệnh nhân trước khi hiển thị trang
    User currentUser = (User) session.getAttribute("user");
    
    // Nếu chưa đăng nhập hoặc không phải bệnh nhân (role_id = 5), chuyển hướng ngay về trang đăng nhập
    if (currentUser == null || currentUser.getRoleId() != 5) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 2. Lấy dữ liệu Bác sĩ và Dịch vụ nha khoa để hiển thị lên form
    UserDAO userDAO = new UserDAO();
    ServiceDAO serviceDAO = new ServiceDAO();
    
    List<User> doctors = userDAO.getDoctors(); // Gọi DAO lấy danh sách bác sĩ nha khoa
    List<Service> services = serviceDAO.getAllServices(); // Gọi DAO lấy danh sách dịch vụ hiện có
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Đặt lịch hẹn khám trực tuyến - Phòng khám Nha khoa DenCli">
    <title>Đặt lịch hẹn khám | DenCli</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <!-- Thanh điều hướng (Navbar) phía trên -->
    <nav class="navbar">
        <a href="#" class="navbar-brand">
            <span style="font-size: 1.5rem; margin-right: 0.25rem;">🦷</span> DenCli
        </a>
        <div class="navbar-links">
            <a href="#" class="active">Đặt lịch khám</a>
            <div class="user-badge">
                👤 <%= currentUser.getFullName() %>
            </div>
            <a href="${pageContext.request.contextPath}/logout.jsp" style="color: var(--danger);">Đăng xuất</a>
        </div>
    </nav>

    <div class="page-content">
        <div class="content-card">
            <h2>Đăng ký đặt lịch hẹn khám bệnh</h2>

            <!-- Khu vực hiển thị thông báo -->
            <div id="alertSuccess" class="alert alert-success"></div>
            <div id="alertError" class="alert alert-error"></div>

            <form id="bookForm" onsubmit="return handleBooking(event)">
                <!-- Lựa chọn Bác sĩ -->
                <div class="form-group">
                    <label>Bác sĩ điều trị <span class="required">*</span></label>
                    <select id="doctorId" name="doctor_id" class="form-control" required>
                        <option value="">-- Chọn Bác sĩ nha khoa --</option>
                        <% for (User doc : doctors) { %>
                            <option value="<%= doc.getUserId() %>">BS. <%= doc.getFullName() %></option>
                        <% } %>
                    </select>
                </div>

                <!-- Ngày và Giờ khám -->
                <div class="form-row">
                    <div class="form-group">
                        <label>Ngày hẹn <span class="required">*</span></label>
                        <input type="date" id="appointmentDate" name="appointment_date" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Giờ hẹn <span class="required">*</span></label>
                        <select id="appointmentTime" name="appointment_time" class="form-control" required>
                            <option value="">-- Chọn khung giờ --</option>
                            <option value="08:00">08:00 sáng</option>
                            <option value="09:00">09:00 sáng</option>
                            <option value="10:00">10:00 sáng</option>
                            <option value="11:00">11:00 sáng</option>
                            <option value="14:00">14:00 chiều</option>
                            <option value="15:00">15:00 chiều</option>
                            <option value="16:00">16:00 chiều</option>
                        </select>
                    </div>
                </div>

                <!-- Lựa chọn Dịch vụ kèm theo -->
                <div class="form-group" style="margin-top: 0.5rem; margin-bottom: 1.5rem;">
                    <label style="margin-bottom: 0.5rem;">Dịch vụ nha khoa mong muốn</label>
                    <div class="checkbox-grid">
                        <% for (Service svc : services) { %>
                            <label class="checkbox-item" id="labelSvc<%= svc.getServiceId() %>">
                                <input type="checkbox" name="service_ids" value="<%= svc.getServiceId() %>" onchange="toggleCheckbox(this)">
                                <div>
                                    <div style="font-weight: 600;"><%= svc.getServiceName() %></div>
                                    <div style="color: var(--text-secondary); font-size: 0.75rem;"><%= String.format("%,.0fđ", svc.getPrice()) %></div>
                                </div>
                            </label>
                        <% } %>
                    </div>
                </div>

                <!-- Ghi chú triệu chứng -->
                <div class="form-group">
                    <label>Triệu chứng hoặc yêu cầu đặc biệt</label>
                    <textarea id="notes" name="notes" class="form-control" placeholder="Mô tả cụ thể tình trạng răng miệng của bạn..."></textarea>
                </div>

                <button type="submit" id="btnSubmit" class="btn btn-success">Xác nhận đặt lịch khám</button>
            </form>
        </div>
    </div>

    <script>
        // Thiết lập thuộc tính min cho input date để ngăn người dùng chọn ngày trong quá khứ
        var today = new Date().toISOString().split('T')[0]; // Lấy ngày hôm nay định dạng yyyy-mm-dd từ hệ thống JS
        document.getElementById('appointmentDate').min = today; // Cập nhật thuộc tính min

        // Hàm thay đổi trạng thái giao diện của Checkbox dịch vụ khi người dùng click
        function toggleCheckbox(checkbox) {
            var label = document.getElementById('labelSvc' + checkbox.value);
            
            if (checkbox.checked) {
                label.classList.add('checked'); // Thêm class CSS checked để đổi màu viền và nền
            } else {
                label.classList.remove('checked'); // Bỏ class CSS checked
            }
        }

        // Hàm gửi dữ liệu đặt lịch hẹn qua API /api/appointments/book dạng JSON
        function handleBooking(event) {
            event.preventDefault(); // Ngăn trình duyệt submit form mặc định

            var alertSuccess = document.getElementById('alertSuccess');
            var alertError = document.getElementById('alertError');
            alertSuccess.style.display = 'none';
            alertError.style.display = 'none';

            var btn = document.getElementById('btnSubmit');
            btn.disabled = true;
            btn.textContent = 'Đang đăng ký lịch...';

            // Lấy danh sách ID dịch vụ đã chọn
            var selectedServices = [];
            var checkedBoxes = document.querySelectorAll('input[name="service_ids"]:checked'); // Tìm tất cả checkbox đã chọn
            
            checkedBoxes.forEach(function(box) {
                selectedServices.push(parseInt(box.value)); // Ép kiểu số nguyên và thêm vào mảng
            });

            // Tạo đối tượng JSON chứa thông tin đặt lịch
            var payload = {
                doctor_id: parseInt(document.getElementById('doctorId').value),
                appointment_date: document.getElementById('appointmentDate').value,
                appointment_time: document.getElementById('appointmentTime').value,
                service_ids: selectedServices,
                notes: document.getElementById('notes').value.trim()
            };

            // Gọi API bằng Fetch gửi POST request
            fetch('${pageContext.request.contextPath}/api/appointments/book', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload) // Convert dữ liệu sang chuỗi JSON bằng hàm stringify
            })
            .then(function(response) {
                return response.json(); // Nhận phân tích JSON từ server
            })
            .then(function(data) {
                btn.disabled = false;
                btn.textContent = 'Xác nhận đặt lịch khám';

                if (data.success) {
                    alertSuccess.textContent = data.message;
                    alertSuccess.style.display = 'block';
                    document.getElementById('bookForm').reset(); // Reset form

                    // Bỏ chọn tất cả nhãn checked dịch vụ
                    var labels = document.querySelectorAll('.checkbox-item');
                    labels.forEach(function(lbl) {
                        lbl.classList.remove('checked');
                    });

                } else {
                    alertError.textContent = data.message;
                    alertError.style.display = 'block';
                }
            })
            .catch(function(err) {
                btn.disabled = false;
                btn.textContent = 'Xác nhận đặt lịch khám';
                alertError.textContent = 'Lỗi kết nối cơ sở dữ liệu hoặc hệ thống quá tải.';
                alertError.style.display = 'block';
            });

            return false;
        }
    </script>
</body>
</html>
