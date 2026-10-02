<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân & Lịch sử khám | DenCli</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- DenCli Design System Theme -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=3">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
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
                <div class="container-fluid p-0" style="max-width: 1080px;">

                    <!-- Thông báo lỗi hoặc thành công -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
                            <strong>⚠️ Thông báo:</strong> <c:out value="${errorMessage}" />
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.successMessage}">
                        <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
                            <strong>✅ Thành công:</strong> <c:out value="${sessionScope.successMessage}" />
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <c:remove var="successMessage" scope="session" />
                    </c:if>

                    <!-- Thông báo nhắc lịch hẹn sắp tới (Phase 3.4) -->
                    <c:forEach items="${appointments}" var="app" begin="0" end="0">
                        <c:if test="${app.status == 'Confirmed' || app.status == 'Pending'}">
                            <div class="alert alert-info border-info d-flex align-items-center gap-3 mb-4 shadow-sm" role="alert">
                                <span class="fs-3">🔔</span>
                                <div>
                                    <h6 class="alert-heading fw-bold mb-1">Nhắc nhở lịch khám nha khoa sắp tới</h6>
                                    <p class="mb-0 small">Bạn có cuộc hẹn <strong>#<c:out value="${app.appointmentId}" /></strong> vào ngày <strong><c:out value="${app.appointmentDate}" /></strong> lúc <strong><c:out value="${app.appointmentTime}" /></strong> (Trạng thái: <em><c:out value="${app.status}" /></em>). Vui lòng có mặt đúng giờ để được phục vụ chu đáo nhất.</p>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>

                    <!-- Khối thông tin tài khoản -->
                    <div class="card card-custom bg-white p-4 mb-4">
                        <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-3">
                            <div>
                                <h4 class="fw-bold text-primary mb-1">👤 Hồ sơ cá nhân</h4>
                                <p class="text-muted small mb-0">Quản lý và cập nhật thông tin liên hệ của bạn tại phòng khám.</p>
                            </div>
                            <span class="badge bg-primary-subtle text-primary border border-primary px-3 py-2 fw-semibold">
                                Mã BN: #<c:out value="${profileUser.userId}" />
                            </span>
                        </div>

                        <form action="${pageContext.request.contextPath}/customer/profile" method="POST">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="profileFullName" class="form-label fw-semibold">Họ và tên <span class="text-danger">*</span></label>
                                    <input type="text" id="profileFullName" name="full_name" class="form-control" value="<c:out value='${profileUser.fullName}' />" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="profilePhone" class="form-label fw-semibold">Số điện thoại <span class="text-danger">*</span></label>
                                    <input type="tel" id="profilePhone" name="phone" class="form-control" value="<c:out value='${profileUser.phone}' />" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="profileEmail" class="form-label fw-semibold">Địa chỉ Email</label>
                                    <input type="email" id="profileEmail" class="form-control bg-light" value="<c:out value='${profileUser.email}' />" readonly>
                                    <div class="form-text">Email dùng làm tên đăng nhập chính, không thể thay đổi.</div>
                                </div>
                                <div class="col-md-3">
                                    <label for="profileGender" class="form-label fw-semibold">Giới tính</label>
                                    <select id="profileGender" name="gender" class="form-select">
                                        <option value="Nam" ${profileUser.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                                        <option value="Nữ" ${profileUser.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
                                        <option value="Khác" ${profileUser.gender == 'Khác' ? 'selected' : ''}>Khác</option>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <label for="profileDob" class="form-label fw-semibold">Ngày sinh</label>
                                    <input type="date" id="profileDob" name="dob" class="form-control" value="<c:out value='${profileUser.dob}' />">
                                </div>
                                <div class="col-12">
                                    <label for="profileAddress" class="form-label fw-semibold">Địa chỉ liên hệ</label>
                                    <input type="text" id="profileAddress" name="address" class="form-control" value="<c:out value='${profileUser.address}' />" placeholder="Số nhà, tên đường, phường/xã, quận/huyện...">
                                </div>
                            </div>
                            <div class="text-end mt-3">
                                <button type="submit" class="btn btn-primary px-4 fw-semibold">Lưu thay đổi</button>
                            </div>
                        </form>
                    </div>

                    <!-- Khối lịch sử khám bệnh -->
                    <div class="card card-custom bg-white p-4">
                        <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-3">
                            <div>
                                <h4 class="fw-bold text-primary mb-1">📋 Lịch sử & Trạng thái Đặt khám</h4>
                                <p class="text-muted small mb-0">Theo dõi tiến trình các cuộc hẹn từ khi đặt lịch đến khi hoàn tất.</p>
                            </div>
                            <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-sm btn-primary fw-semibold px-3">
                                + Đặt lịch mới
                            </a>
                        </div>

                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Mã hẹn</th>
                                        <th scope="col">Ngày khám</th>
                                        <th scope="col">Giờ khám</th>
                                        <th scope="col">Bác sĩ phụ trách</th>
                                        <th scope="col">Phòng khám</th>
                                        <th scope="col">Trạng thái</th>
                                        <th scope="col">Ghi chú</th>
                                        <th scope="col" class="text-end">Chi tiết</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty appointments}">
                                            <tr>
                                                <td colspan="8" class="text-center py-5">
                                                    <div class="empty-state-box">
                                                        <div class="empty-state-icon">📅</div>
                                                        <h6 class="fw-bold text-navy mb-1">Chưa có lịch hẹn khám nào</h6>
                                                        <p class="text-muted small mb-3">Bạn chưa đăng ký lịch khám nha khoa nào tại DenCli.</p>
                                                        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-sm btn-primary px-3">
                                                            + Đặt lịch khám ngay
                                                        </a>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach items="${appointments}" var="app">
                                                <tr>
                                                    <td class="fw-bold text-primary">#<c:out value="${app.appointmentId}" /></td>
                                                    <td><c:out value="${app.appointmentDate}" /></td>
                                                    <td><span class="badge bg-light text-dark border"><c:out value="${app.appointmentTime}" /></span></td>
                                                    <td>BS. #<c:out value="${app.doctorId}" /></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty app.room}">
                                                                <c:out value="${app.room}" />
                                                            </c:when>
                                                            <c:otherwise><span class="text-muted">—</span></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${app.status == 'Pending'}">
                                                                <span class="badge bg-warning text-dark">Chờ xác nhận</span>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Confirmed'}">
                                                                <span class="badge bg-info text-dark">Đã xác nhận</span>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Checked In'}">
                                                                <span class="badge bg-primary">Đã tiếp đón</span>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Completed'}">
                                                                <span class="badge bg-success">Đã hoàn thành</span>
                                                            </c:when>
                                                            <c:when test="${app.status == 'Cancelled'}">
                                                                <span class="badge bg-danger">Đã hủy</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary"><c:out value="${app.status}" /></span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="text-muted small">
                                                        <c:choose>
                                                            <c:when test="${not empty app.notes}"><c:out value="${app.notes}" /></c:when>
                                                            <c:otherwise>—</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="text-end">
                                                        <c:choose>
                                                            <c:when test="${app.status == 'Completed' || app.status == 'Checked In'}">
                                                                <a href="${pageContext.request.contextPath}/customer/invoice?appointment_id=${app.appointmentId}" class="btn btn-sm btn-outline-primary fw-semibold">
                                                                    📄 Xem Hóa đơn & Thuốc
                                                                </a>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-muted small">—</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
            </main>
        </div>
    </div>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
