<%-- 
 * Purpose: Public homepage & landing page of the clinic (UC-01, UC-02) for Guests and Patients.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.dencli.models.User"%>
<%@page import="com.mycompany.dencli.models.Service"%>
<%@page import="com.mycompany.dencli.dao.ServiceDAO"%>
<%@page import="java.util.List"%>
<%
    // 1. Kiểm tra trạng thái đăng nhập của người dùng để tùy biến hiển thị
    User currentUser = (User) session.getAttribute("user");

    // 2. Lấy danh sách dịch vụ thực tế từ cơ sở dữ liệu để hiển thị bảng giá
    ServiceDAO serviceDAO = new ServiceDAO();
    List<Service> services = serviceDAO.getAllServices(); // Gọi hàm lấy danh sách từ CSDL
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Chào mừng tới Nha khoa Công nghệ cao DenCli - Chăm sóc nụ cười Việt">
    <title>Nha khoa Công nghệ cao DenCli</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        /* Tùy chỉnh thêm các thành phần đặc thù của Trang chủ */
        .hero-section {
            background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
            color: white;
            text-align: center;
            padding: 5rem 2rem;
            border-radius: var(--radius-lg);
            margin-bottom: 2rem;
            box-shadow: var(--shadow-lg);
        }
        
        .hero-section h1 {
            font-size: 2.5rem;
            font-weight: 800;
            margin-bottom: 1rem;
            letter-spacing: -0.02em;
        }

        .hero-section p {
            font-size: 1.125rem;
            margin-bottom: 2rem;
            opacity: 0.9;
        }

        .btn-hero {
            display: inline-flex;
            max-width: 250px;
            font-size: 1rem;
            padding: 0.875rem 2rem;
        }

        .search-box {
            margin-bottom: 1.5rem;
            position: relative;
        }

        .search-icon {
            position: absolute;
            left: 1rem;
            top: 50%;
            transform: translateY(-50%);
            color: var(--text-secondary);
        }

        .search-input {
            padding-left: 2.75rem;
        }

        .service-list-card {
            background: var(--bg-card);
            border-radius: var(--radius-lg);
            box-shadow: var(--shadow-md);
            padding: 2rem;
            margin-bottom: 2rem;
        }

        .service-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1rem;
        }

        .service-table th {
            text-align: left;
            padding: 1rem;
            background: var(--bg-body);
            font-weight: 600;
            color: var(--text-primary);
            border-bottom: 2px solid var(--border);
        }

        .service-table td {
            padding: 1rem;
            border-bottom: 1px solid var(--border);
            color: var(--text-primary);
        }

        .service-table tr:hover {
            background: var(--bg-input);
        }

        .price-tag {
            font-weight: 700;
            color: var(--primary);
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 1.5rem;
            margin-bottom: 3rem;
        }

        .info-card {
            background: var(--bg-card);
            padding: 1.5rem;
            border-radius: var(--radius-md);
            box-shadow: var(--shadow-sm);
            border-left: 4px solid var(--primary);
        }

        .info-card h3 {
            font-size: 1rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
            color: var(--text-primary);
        }
    </style>
</head>
<body>
    <!-- Thanh điều hướng đầu trang -->
    <nav class="navbar">
        <a href="#" class="navbar-brand">
            <span style="font-size: 1.5rem; margin-right: 0.25rem;">🦷</span> DenCli
        </a>
        <div class="navbar-links">
            <a href="#" class="active">Trang chủ</a>
            
            <% if (currentUser != null) { %>
                <!-- Giao diện hiển thị cho bệnh nhân đã đăng nhập -->
                <a href="${pageContext.request.contextPath}/book.jsp">Đặt lịch khám</a>
                <div class="user-badge">
                    👤 <%= currentUser.getFullName() %>
                </div>
                <a href="${pageContext.request.contextPath}/logout.jsp" style="color: var(--danger);">Đăng xuất</a>
            <% } else { %>
                <!-- Giao diện hiển thị cho khách vãng lai (Guest) -->
                <a href="${pageContext.request.contextPath}/login.jsp">Đăng nhập</a>
                <a href="${pageContext.request.contextPath}/register.jsp" style="background: var(--primary); color: white; border-radius: var(--radius-sm);">Đăng ký</a>
            <% } %>
        </div>
    </nav>

    <div class="page-content" style="max-width: 1000px; margin: 0 auto;">
        
        <!-- Khu vực Hero chào mừng -->
        <div class="hero-section">
            <h1>NHA KHOA CÔNG NGHỆ CAO DENCLI</h1>
            <p>Kiến tạo nụ cười rạng rỡ - Đồng hành cùng sức khỏe răng miệng của gia đình bạn</p>
            
            <% if (currentUser != null) { %>
                <a href="${pageContext.request.contextPath}/book.jsp" class="btn btn-success btn-hero">Đặt lịch khám ngay</a>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-success btn-hero">Đăng nhập để đặt lịch</a>
            <% } %>
        </div>

        <!-- Thông tin hoạt động của phòng khám -->
        <div class="info-grid">
            <div class="info-card">
                <h3>🕒 Giờ mở cửa</h3>
                <p>Thứ Hai - Chủ Nhật: 08:00 sáng - 18:00 chiều (Không nghỉ trưa)</p>
            </div>
            <div class="info-card">
                <h3>📍 Địa chỉ liên hệ</h3>
                <p>123 Nguyễn Trãi, Hải Châu, Đà Nẵng</p>
            </div>
            <div class="info-card">
                <h3>📞 Hotline khẩn cấp</h3>
                <p style="font-weight: 700; color: var(--primary);">1900 6088 (Hỗ trợ 24/7)</p>
            </div>
        </div>

        <!-- Danh mục dịch vụ và bảng giá khám bệnh -->
        <div class="service-list-card">
            <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem; margin-bottom: 1.5rem;">
                <h2 style="font-size: 1.5rem; font-weight: 700;">Danh mục Dịch vụ & Bảng giá công khai</h2>
                
                <!-- Thanh tìm kiếm dịch vụ nhanh -->
                <div class="search-box" style="margin-bottom: 0; min-width: 250px;">
                    <span class="search-icon">🔍</span>
                    <input type="text" id="searchInput" onkeyup="filterServices()" class="form-control search-input" placeholder="Tìm kiếm dịch vụ...">
                </div>
            </div>

            <table class="service-table" id="serviceTable">
                <thead>
                    <tr>
                        <th style="width: 40%;">Tên dịch vụ</th>
                        <th style="width: 45%;">Mô tả dịch vụ</th>
                        <th style="width: 15%; text-align: right;">Đơn giá</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (services != null && !services.isEmpty()) { %>
                        <% for (Service svc : services) { %>
                            <tr class="service-item">
                                <td class="service-name" style="font-weight: 600;"><%= svc.getServiceName() %></td>
                                <td style="color: var(--text-secondary); font-size: 0.875rem;"><%= svc.getDescription() != null ? svc.getDescription() : "Không có mô tả" %></td>
                                <td class="price-tag" style="text-align: right;"><%= String.format("%,.0fđ", svc.getPrice()) %></td>
                            </tr>
                        <% } %>
                    <% } else { %>
                        <tr>
                            <td colspan="3" style="text-align: center; color: var(--text-secondary); padding: 2rem;">Hiện phòng khám đang cập nhật danh mục dịch vụ.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

    <script>
        // Hàm lọc tìm kiếm dịch vụ theo từ khóa nhập vào ô input
        function filterServices() {
            var input = document.getElementById("searchInput");
            var filter = input.value.toLowerCase().trim(); // Chuyển chữ thường và cắt khoảng trắng
            var table = document.getElementById("serviceTable");
            var rows = table.getElementsByClassName("service-item"); // Lấy danh sách dòng dịch vụ

            // Lặp qua từng dòng để ẩn/hiển thị dựa trên tên dịch vụ
            for (var i = 0; i < rows.length; i++) {
                var nameCol = rows[i].getElementsByClassName("service-name")[0];
                
                if (nameCol) {
                    var txtValue = nameCol.textContent || nameCol.innerText;
                    
                    if (txtValue.toLowerCase().indexOf(filter) > -1) { // Sử dụng hàm indexOf tìm kiếm chuỗi
                        rows[i].style.display = ""; // Hiển thị dòng
                    } else {
                        rows[i].style.display = "none"; // Ẩn dòng
                    }
                }
            }
        }
    </script>
</body>
</html>
