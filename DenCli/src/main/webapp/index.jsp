<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="serviceDAO" class="com.devjava.dencli.dao.impl.ServiceDAOImpl" scope="page" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Chào mừng tới Nha khoa Công nghệ cao DenCli - Chăm sóc nụ cười Việt">
    <title>Nha khoa Công nghệ cao DenCli</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: #f8fafc; }
        .hero-banner {
            background: linear-gradient(135deg, #0284c7 0%, #0369a1 50%, #075985 100%);
            border-radius: 1rem;
            color: white;
            padding: 4rem 2rem;
            box-shadow: 0 10px 25px -5px rgba(2, 132, 199, 0.3);
        }
        .info-card {
            border: none;
            border-radius: 0.75rem;
            box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
            transition: transform 0.2s ease;
        }
        .info-card:hover { transform: translateY(-3px); }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- Header dùng chung -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="container py-4 flex-grow-1" style="max-width: 1080px;">

        <!-- Khu vực Hero chào mừng -->
        <div class="hero-banner text-center mb-5">
            <h1 class="display-5 fw-bold mb-3">🦷 NHA KHOA CÔNG NGHỆ CAO DENCLI</h1>
            <p class="lead mb-4 opacity-90 mx-auto" style="max-width: 680px;">
                Kiến tạo nụ cười rạng rỡ - Đồng hành cùng sức khỏe răng miệng toàn diện cho cả gia đình bạn với công nghệ điều trị tiên tiến chuẩn quốc tế.
            </p>

            <div>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <c:choose>
                            <c:when test="${sessionScope.user.roleId == 1}">
                                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-warning btn-lg fw-bold px-4 shadow">
                                    ⚙️ Vào Bảng điều khiển Quản trị
                                </a>
                            </c:when>
                            <c:when test="${sessionScope.user.roleId == 2}">
                                <a href="${pageContext.request.contextPath}/doctor/examination" class="btn btn-info text-white btn-lg fw-bold px-4 shadow">
                                    🩺 Vào Buồng khám Bác sĩ
                                </a>
                            </c:when>
                            <c:when test="${sessionScope.user.roleId == 4}">
                                <a href="${pageContext.request.contextPath}/staff/reception" class="btn btn-light text-primary btn-lg fw-bold px-4 shadow">
                                    💁 Vào Bàn Tiếp đón & Check-in
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-warning btn-lg fw-bold px-5 shadow">
                                    📅 Đặt lịch khám ngay
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-light text-primary btn-lg fw-bold px-4 shadow me-2">
                            Đăng nhập
                        </a>
                        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-warning btn-lg fw-bold px-4 shadow">
                            Đặt lịch khám
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Thông tin hoạt động phòng khám -->
        <div class="row g-3 mb-5">
            <div class="col-md-4">
                <div class="card info-card bg-white p-4 h-100 text-center">
                    <div class="fs-1 mb-2">🕒</div>
                    <h5 class="fw-bold text-dark mb-1">Giờ mở cửa</h5>
                    <p class="text-muted small mb-0">Thứ Hai - Chủ Nhật: 08:00 - 18:00 (Không nghỉ trưa, tiếp đón liên tục)</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card info-card bg-white p-4 h-100 text-center">
                    <div class="fs-1 mb-2">📍</div>
                    <h5 class="fw-bold text-dark mb-1">Địa chỉ phòng khám</h5>
                    <p class="text-muted small mb-0">123 Nguyễn Văn Linh, Quận Hải Châu, TP. Đà Nẵng</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card info-card bg-white p-4 h-100 text-center">
                    <div class="fs-1 mb-2">📞</div>
                    <h5 class="fw-bold text-dark mb-1">Hotline tư vấn 24/7</h5>
                    <p class="text-primary fw-bold fs-5 mb-0">1900 6868</p>
                </div>
            </div>
        </div>

        <!-- Danh mục dịch vụ và bảng giá niêm yết -->
        <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
            <div class="d-flex flex-wrap justify-content-between align-items-center border-bottom pb-3 mb-3 gap-2">
                <div>
                    <h4 class="fw-bold text-primary mb-1">📋 Danh mục Dịch vụ & Bảng giá Công khai</h4>
                    <p class="text-muted small mb-0">Bảng giá niêm yết minh bạch theo quy chuẩn của Bộ Y tế.</p>
                </div>
                <!-- Tìm kiếm dịch vụ -->
                <div class="input-group" style="max-width: 280px;">
                    <span class="input-group-text bg-white border-end-0">🔍</span>
                    <label for="serviceSearch" class="visually-hidden">Tìm dịch vụ</label>
                    <input type="text" id="serviceSearch" class="form-control border-start-0" placeholder="Tìm tên dịch vụ..." onkeyup="filterServices()">
                </div>
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" id="servicesTable">
                    <thead class="table-light">
                        <tr>
                            <th scope="col" style="width: 35%;">Tên dịch vụ nha khoa</th>
                            <th scope="col" style="width: 45%;">Mô tả chi tiết</th>
                            <th scope="col" style="width: 20%; text-align: right;">Đơn giá niêm yết</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty serviceDAO.allServices}">
                                <tr>
                                    <td colspan="3" class="text-center py-4 text-muted">Đang cập nhật danh mục dịch vụ...</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${serviceDAO.allServices}" var="s">
                                    <tr class="service-row">
                                        <td class="fw-bold text-dark service-name">
                                            <c:out value="${s.serviceName}" />
                                        </td>
                                        <td class="small text-muted">
                                            <c:out value="${not empty s.description ? s.description : 'Dịch vụ nha khoa chuẩn quốc tế'}" />
                                        </td>
                                        <td class="text-end fw-bold text-primary">
                                            <fmt:formatNumber value="${s.price}" pattern="#,##0" /> VNĐ
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

    </main>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <script>
        function filterServices() {
            var input = document.getElementById('serviceSearch');
            var filter = input.value.toLowerCase().trim();
            var rows = document.querySelectorAll('#servicesTable .service-row');

            rows.forEach(function(row) {
                var nameCol = row.querySelector('.service-name');
                if (nameCol) {
                    var txt = nameCol.textContent.toLowerCase();
                    row.style.display = txt.indexOf(filter) > -1 ? '' : 'none';
                }
            });
        }
    </script>
</body>
</html>
