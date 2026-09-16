<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Người dùng | DenCli</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: #f8fafc; }
        .card-custom { border: none; border-radius: 0.75rem; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- Header dùng chung -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <!-- Sidebar điều hướng theo Role -->
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

            <!-- Nội dung chính -->
            <main class="col-12 col-md-9 col-lg-10 p-4">
                <div class="container-fluid p-0">

                    <div class="card card-custom bg-white p-4">
                        <div class="d-flex flex-wrap justify-content-between align-items-center border-bottom pb-3 mb-3 gap-2">
                            <div>
                                <h4 class="fw-bold text-primary mb-1">👥 Danh sách Tài khoản & Người dùng</h4>
                                <p class="text-muted small mb-0">Quản lý toàn bộ nhân sự và bệnh nhân trên hệ thống phòng khám.</p>
                            </div>

                            <!-- Bộ lọc vai trò -->
                            <form action="${pageContext.request.contextPath}/admin/users" method="GET" class="d-flex align-items-center gap-2">
                                <label for="roleFilter" class="small fw-semibold text-muted text-nowrap">Vai trò:</label>
                                <select id="roleFilter" name="role_id" class="form-select form-select-sm" onchange="this.form.submit()">
                                    <option value="0" ${selectedRoleId == 0 || empty selectedRoleId ? 'selected' : ''}>-- Tất cả vai trò --</option>
                                    <option value="1" ${selectedRoleId == 1 ? 'selected' : ''}>Quản trị viên (ADMIN)</option>
                                    <option value="2" ${selectedRoleId == 2 ? 'selected' : ''}>Bác sĩ nha khoa (DOCTOR)</option>
                                    <option value="4" ${selectedRoleId == 4 ? 'selected' : ''}>Nhân viên Lễ tân (STAFF)</option>
                                    <option value="5" ${selectedRoleId == 5 ? 'selected' : ''}>Bệnh nhân (CUSTOMER)</option>
                                </select>
                                <span class="badge bg-light text-muted border py-2 px-3">
                                    Tổng: <c:out value="${totalRecords}" /> người
                                </span>
                            </form>
                        </div>

                        <!-- Bảng danh sách người dùng -->
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col" style="width: 8%;">ID</th>
                                        <th scope="col" style="width: 22%;">Họ và tên</th>
                                        <th scope="col" style="width: 22%;">Email</th>
                                        <th scope="col" style="width: 15%;">Số điện thoại</th>
                                        <th scope="col" style="width: 15%;">Vai trò</th>
                                        <th scope="col" style="width: 8%;">Giới tính</th>
                                        <th scope="col" style="width: 10%;">Địa chỉ</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty userList}">
                                            <tr>
                                                <td colspan="7" class="text-center py-4 text-muted">
                                                    Không tìm thấy người dùng nào phù hợp với bộ lọc.
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach items="${userList}" var="u">
                                                <tr>
                                                    <td class="fw-bold text-muted">#<c:out value="${u.userId}" /></td>
                                                    <td class="fw-semibold text-dark"><c:out value="${u.fullName}" /></td>
                                                    <td><c:out value="${u.email}" /></td>
                                                    <td><c:out value="${u.phone}" /></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${u.roleId == 1}">
                                                                <span class="badge bg-warning text-dark">⚙️ Quản trị viên</span>
                                                            </c:when>
                                                            <c:when test="${u.roleId == 2}">
                                                                <span class="badge bg-info text-dark">🩺 Bác sĩ</span>
                                                            </c:when>
                                                            <c:when test="${u.roleId == 4}">
                                                                <span class="badge bg-primary">💁 Lễ tân</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">👤 Bệnh nhân</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty u.gender}"><c:out value="${u.gender}" /></c:when>
                                                            <c:otherwise><span class="text-muted">—</span></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="small text-muted" style="max-width: 150px;">
                                                        <c:choose>
                                                            <c:when test="${not empty u.address}"><c:out value="${u.address}" /></c:when>
                                                            <c:otherwise>—</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>

                        <!-- Bộ thanh phân trang Pagination UI -->
                        <c:if test="${totalPages > 1}">
                            <div class="d-flex justify-content-between align-items-center mt-4 pt-3 border-top flex-wrap gap-2">
                                <div class="text-muted small">
                                    Đang hiển thị trang <strong>${currentPage}</strong> / <strong>${totalPages}</strong>
                                </div>
                                <nav aria-label="Phân trang người dùng">
                                    <ul class="pagination pagination-sm mb-0">
                                        <!-- Nút Previous -->
                                        <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                                            <a class="page-link" href="${pageContext.request.contextPath}/admin/users?role_id=${selectedRoleId}&page=${currentPage - 1}&page_size=${pageSize}" aria-label="Previous">
                                                <span aria-hidden="true">&laquo; Trang trước</span>
                                            </a>
                                        </li>

                                        <!-- Các số trang -->
                                        <c:forEach begin="1" end="${totalPages}" var="p">
                                            <li class="page-item ${p == currentPage ? 'active' : ''}">
                                                <a class="page-link" href="${pageContext.request.contextPath}/admin/users?role_id=${selectedRoleId}&page=${p}&page_size=${pageSize}">
                                                    ${p}
                                                </a>
                                            </li>
                                        </c:forEach>

                                        <!-- Nút Next -->
                                        <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                                            <a class="page-link" href="${pageContext.request.contextPath}/admin/users?role_id=${selectedRoleId}&page=${currentPage + 1}&page_size=${pageSize}" aria-label="Next">
                                                <span aria-hidden="true">Trang sau &raquo;</span>
                                            </a>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </c:if>

                    </div>

                </div>
            </main>
        </div>
    </div>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
