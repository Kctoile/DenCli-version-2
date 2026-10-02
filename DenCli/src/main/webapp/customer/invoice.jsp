<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hóa đơn & Chi tiết khám bệnh #${appointmentId} | DenCli</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- DenCli Design System Theme -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=3">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: #f8fafc; }
        .card-custom { border: none; border-radius: 0.75rem; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }
        @media print {
            .no-print, header, nav, .btn, footer, .sidebar { display: none !important; }
            body { background-color: #fff !important; }
            .card-custom { box-shadow: none !important; border: none !important; }
            .print-full-width { width: 100% !important; max-width: 100% !important; margin: 0 !important; }
        }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <div class="no-print">
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
    </div>

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <div class="no-print col-12 col-md-3 col-lg-2 p-0">
                <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
            </div>

            <main class="col-12 col-md-9 col-lg-10 p-4 print-full-width">
                <div class="card card-custom bg-white p-4 p-md-5 mx-auto" style="max-width: 880px;">

                    <!-- Tiêu đề Hóa đơn -->
                    <div class="d-flex justify-content-between align-items-start border-bottom pb-4 mb-4">
                        <div>
                            <div class="d-flex align-items-center gap-2 mb-1">
                                <span class="fs-3">🦷</span>
                                <h3 class="fw-bold text-primary mb-0">NHA KHOA DENCLI</h3>
                            </div>
                            <p class="text-muted small mb-0">Địa chỉ: 123 Đường Nguyễn Tri Phương, Quận 10, TP.HCM</p>
                            <p class="text-muted small mb-0">Hotline: 1900 6868 | Email: contact@dencli.com</p>
                        </div>
                        <div class="text-end">
                            <h4 class="fw-bold text-uppercase mb-1">HÓA ĐƠN KHÁM BỆNH</h4>
                            <div class="text-muted small">Mã cuộc hẹn: <strong>#<c:out value="${appointmentId}" /></strong></div>
                            <div class="mt-2">
                                <c:choose>
                                    <c:when test="${invoice.status == 'Completed'}">
                                        <span class="badge bg-success px-3 py-2 fs-6">ĐÃ THANH TOÁN</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark px-3 py-2 fs-6">CHỜ THANH TOÁN</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <!-- Thông tin Bệnh nhân & Bác sĩ -->
                    <div class="row g-3 mb-4 pb-3 border-bottom">
                        <div class="col-sm-6">
                            <h6 class="fw-bold text-dark text-uppercase small mb-2">Thông tin Bệnh nhân:</h6>
                            <p class="mb-1"><strong>Họ và tên:</strong> <c:out value="${invoice.patientName}" /></p>
                            <p class="mb-1"><strong>Mã hồ sơ:</strong> #<c:out value="${invoice.patientId}" /></p>
                        </div>
                        <div class="col-sm-6 text-sm-end">
                            <h6 class="fw-bold text-dark text-uppercase small mb-2">Bác sĩ phụ trách:</h6>
                            <p class="mb-1"><strong>Bác sĩ:</strong> <c:out value="${invoice.doctorName}" /></p>
                            <p class="mb-1 text-muted small">Thời gian khám: <c:out value="${appointment.appointmentDate}" /> (<c:out value="${appointment.appointmentTime}" />)</p>
                        </div>
                    </div>

                    <!-- 1. Bảng Dịch vụ khám chữa bệnh -->
                    <h5 class="fw-bold text-primary mb-3">1. Dịch vụ Kỹ thuật & Điều trị</h5>
                    <div class="table-responsive mb-4">
                        <table class="table table-bordered align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th scope="col" style="width: 5%;">#</th>
                                    <th scope="col">Tên dịch vụ</th>
                                    <th scope="col" style="width: 25%;" class="text-end">Đơn giá</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty invoice.services}">
                                        <tr><td colspan="3" class="text-center text-muted py-3">Không có dịch vụ phát sinh.</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${invoice.services}" var="s" varStatus="loop">
                                            <tr>
                                                <td><c:out value="${loop.count}" /></td>
                                                <td class="fw-semibold"><c:out value="${s.serviceName}" /></td>
                                                <td class="text-end fw-semibold">
                                                    <fmt:formatNumber value="${s.price}" type="currency" currencySymbol="₫" maxFractionDigits="0" />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>

                    <!-- 2. Bảng Thuốc điều trị (Đơn thuốc) -->
                    <h5 class="fw-bold text-primary mb-3">2. Đơn thuốc & Vật tư Y tế</h5>
                    <div class="table-responsive mb-4">
                        <table class="table table-bordered align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th scope="col" style="width: 5%;">#</th>
                                    <th scope="col">Tên thuốc</th>
                                    <th scope="col" style="width: 15%;" class="text-center">Số lượng</th>
                                    <th scope="col" style="width: 20%;" class="text-end">Đơn giá</th>
                                    <th scope="col" style="width: 25%;" class="text-end">Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty invoice.medicines}">
                                        <tr><td colspan="5" class="text-center text-muted py-3">Không có thuốc được kê trong ca này.</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${invoice.medicines}" var="m" varStatus="loop">
                                            <tr>
                                                <td><c:out value="${loop.count}" /></td>
                                                <td class="fw-semibold"><c:out value="${m.medicineName}" /></td>
                                                <td class="text-center"><c:out value="${m.quantity}" /></td>
                                                <td class="text-end">
                                                    <fmt:formatNumber value="${m.unitPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0" />
                                                </td>
                                                <td class="text-end fw-semibold">
                                                    <fmt:formatNumber value="${m.subtotal}" type="currency" currencySymbol="₫" maxFractionDigits="0" />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>

                    <!-- Tổng kết chi phí -->
                    <div class="row justify-content-end mb-4">
                        <div class="col-md-6 col-lg-5">
                            <div class="border rounded p-3 bg-light">
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Tiền dịch vụ:</span>
                                    <span class="fw-semibold">
                                        <fmt:formatNumber value="${invoice.totalServiceFee}" type="currency" currencySymbol="₫" maxFractionDigits="0" />
                                    </span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Tiền thuốc:</span>
                                    <span class="fw-semibold">
                                        <fmt:formatNumber value="${invoice.totalMedicineFee}" type="currency" currencySymbol="₫" maxFractionDigits="0" />
                                    </span>
                                </div>
                                <hr class="my-2">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="fs-5 fw-bold text-dark">TỔNG CỘNG:</span>
                                    <span class="fs-4 fw-bold text-danger">
                                        <fmt:formatNumber value="${invoice.totalAmount}" type="currency" currencySymbol="₫" maxFractionDigits="0" />
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Nút thao tác -->
                    <div class="d-flex justify-content-between align-items-center pt-3 border-top no-print">
                        <a href="${pageContext.request.contextPath}/customer/profile" class="btn btn-outline-secondary">
                            ← Quay lại Hồ sơ của tôi
                        </a>
                        <div class="d-flex gap-2">
                            <c:if test="${invoice.status != 'Completed'}">
                                <a href="${pageContext.request.contextPath}/payment/vnpay?appointment_id=${appointmentId}" class="btn btn-success fw-bold">
                                    💳 Thanh toán trực tuyến VNPAY
                                </a>
                            </c:if>
                            <button type="button" class="btn btn-outline-primary fw-semibold" onclick="window.print()">
                                🖨️ In / Xuất PDF Hóa đơn
                            </button>
                        </div>
                    </div>

                </div>
            </main>
        </div>
    </div>

    <div class="no-print">
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </div>
</body>
</html>
