<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hóa đơn viện phí khám chữa bệnh | DenCli</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: #f8fafc; }
        .card-custom { border: none; border-radius: 0.75rem; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }
        @media print {
            .no-print, header, nav, .btn, footer, .toast-container { display: none !important; }
            body { background-color: #fff !important; }
            .card-custom { box-shadow: none !important; border: none !important; }
            .print-full-width { width: 100% !important; max-width: 100% !important; margin: 0 !important; }
        }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- Header dùng chung -->
    <div class="no-print">
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
    </div>

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <!-- Sidebar điều hướng theo Role -->
            <div class="no-print col-12 col-md-3 col-lg-2 p-0">
                <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
            </div>

            <!-- Nội dung chính -->
            <main class="col-12 col-md-9 col-lg-10 p-4 print-full-width">
                <div class="card card-custom bg-white p-4 p-md-5 mx-auto" style="max-width: 880px;">

                    <!-- Tiêu đề Hóa đơn & Thông tin phòng khám -->
                    <div class="d-flex justify-content-between align-items-start border-bottom pb-4 mb-4">
                        <div>
                            <div class="d-flex align-items-center gap-2 mb-1">
                                <span class="fs-3">🦷</span>
                                <h3 class="fw-bold text-primary mb-0">NHA KHOA DENCLI</h3>
                            </div>
                            <p class="text-muted small mb-1">Hệ thống Nha khoa Công nghệ cao & Thẩm mỹ chuẩn quốc tế</p>
                            <p class="text-muted small mb-0">Hotline: 1900 6868 | Email: contact@dencli.vn | 123 Nguyễn Văn Linh, Đà Nẵng</p>
                        </div>
                        <div class="text-end">
                            <h4 class="fw-bold text-dark mb-1">HÓA ĐƠN VIỆN PHÍ</h4>
                            <div class="badge bg-primary fs-6 px-3 py-2">MÃ HẸN: #<c:out value="${appointmentId}" /></div>
                            <jsp:useBean id="now" class="java.util.Date" scope="page" />
                            <div class="text-muted small mt-2">Ngày in: <fmt:formatDate value="${now}" pattern="dd/MM/yyyy HH:mm" /></div>
                        </div>
                    </div>

                    <!-- Thông tin bệnh nhân & Bác sĩ -->
                    <div class="bg-light rounded p-3 mb-4">
                        <div class="row g-2">
                            <div class="col-sm-6">
                                <span class="text-muted small">Họ tên bệnh nhân:</span>
                                <div class="fw-bold text-dark fs-6"><c:out value="${invoice.patientName}" /></div>
                            </div>
                            <div class="col-sm-6">
                                <span class="text-muted small">Số điện thoại:</span>
                                <div class="fw-bold text-dark fs-6"><c:out value="${invoice.patientPhone != null ? invoice.patientPhone : '—'}" /></div>
                            </div>
                            <div class="col-sm-6">
                                <span class="text-muted small">Bác sĩ điều trị phụ trách:</span>
                                <div class="fw-bold text-primary"><c:out value="${invoice.doctorName}" /></div>
                            </div>
                            <div class="col-sm-6">
                                <span class="text-muted small">Trạng thái thanh toán:</span>
                                <div>
                                    <c:choose>
                                        <c:when test="${invoice.status == 'Completed'}">
                                            <span class="badge bg-success">Đã hoàn tất thanh toán</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-warning text-dark">Chờ thanh toán viện phí</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Bảng 1: Dịch vụ kỹ thuật điều trị -->
                    <div class="mb-4">
                        <h6 class="fw-bold text-uppercase text-secondary mb-2">1. Danh mục Dịch vụ & Thủ thuật nha khoa</h6>
                        <table class="table table-bordered align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th style="width: 10%; text-align: center;">STT</th>
                                    <th style="width: 60%;">Tên dịch vụ</th>
                                    <th style="width: 30%; text-align: right;">Đơn giá</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty invoice.services}">
                                        <tr>
                                            <td colspan="3" class="text-center text-muted py-2">Không có dịch vụ tính phí.</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${invoice.services}" var="s" varStatus="loop">
                                            <tr>
                                                <td class="text-center"><c:out value="${loop.index + 1}" /></td>
                                                <td><c:out value="${s.serviceName}" /></td>
                                                <td class="text-end fw-semibold">
                                                    <fmt:formatNumber value="${s.price}" pattern="#,##0" /> VNĐ
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                            <tfoot>
                                <tr class="table-light">
                                    <td colspan="2" class="text-end fw-semibold">Cộng tiền dịch vụ:</td>
                                    <td class="text-end fw-bold text-primary">
                                        <fmt:formatNumber value="${invoice.servicesTotal}" pattern="#,##0" /> VNĐ
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                    <!-- Bảng 2: Thuốc kê đơn -->
                    <div class="mb-4">
                        <h6 class="fw-bold text-uppercase text-secondary mb-2">2. Đơn thuốc chỉ định điều trị</h6>
                        <table class="table table-bordered align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th style="width: 10%; text-align: center;">STT</th>
                                    <th style="width: 45%;">Tên thuốc</th>
                                    <th style="width: 15%; text-align: center;">Số lượng</th>
                                    <th style="width: 15%; text-align: right;">Đơn giá</th>
                                    <th style="width: 15%; text-align: right;">Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty invoice.medicines}">
                                        <tr>
                                            <td colspan="5" class="text-center text-muted py-2">Không có thuốc chỉ định.</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${invoice.medicines}" var="m" varStatus="loop">
                                            <tr>
                                                <td class="text-center"><c:out value="${loop.index + 1}" /></td>
                                                <td><c:out value="${m.medicineName}" /></td>
                                                <td class="text-center"><c:out value="${m.prescribedQuantity}" /></td>
                                                <td class="text-end">
                                                    <fmt:formatNumber value="${m.unitPrice}" pattern="#,##0" />đ
                                                </td>
                                                <td class="text-end fw-semibold">
                                                    <fmt:formatNumber value="${m.unitPrice * m.prescribedQuantity}" pattern="#,##0" /> VNĐ
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                            <tfoot>
                                <tr class="table-light">
                                    <td colspan="4" class="text-end fw-semibold">Cộng tiền thuốc:</td>
                                    <td class="text-end fw-bold text-primary">
                                        <fmt:formatNumber value="${invoice.medicinesTotal}" pattern="#,##0" /> VNĐ
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                    <!-- Tổng kết hóa đơn -->
                    <div class="border rounded p-3 bg-light mb-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="fs-5 fw-bold text-dark">TỔNG CỘNG THANH TOÁN:</span>
                            <span class="fs-3 fw-bold text-danger">
                                <fmt:formatNumber value="${invoice.grandTotal}" pattern="#,##0" /> VNĐ
                            </span>
                        </div>
                    </div>

                    <!-- Ký tên -->
                    <div class="row text-center mt-5 pt-3">
                        <div class="col-6">
                            <div class="fw-semibold">Người nộp tiền</div>
                            <div class="text-muted small">(Ký và ghi rõ họ tên)</div>
                        </div>
                        <div class="col-6">
                            <div class="fw-semibold">Thu ngân / Người lập phiếu</div>
                            <div class="text-muted small">(Ký và ghi rõ họ tên)</div>
                        </div>
                    </div>

                    <!-- Thao tác nút bấm (Ẩn khi in) -->
                    <div class="no-print d-flex justify-content-between align-items-center mt-5 pt-4 border-top">
                        <a href="${pageContext.request.contextPath}/staff/reception" class="btn btn-outline-secondary">
                            ← Quay lại Bàn tiếp đón
                        </a>
                        <div class="d-flex gap-2">
                            <button type="button" class="btn btn-outline-primary" onclick="window.print()">
                                🖨️ In Hóa Đơn
                            </button>
                            <c:if test="${invoice.status != 'Completed'}">
                                <form action="${pageContext.request.contextPath}/staff/invoice" method="POST" class="d-inline" onsubmit="return confirm('Xác nhận đã thu đủ số tiền và hoàn tất ca khám?');">
                                    <input type="hidden" name="appointment_id" value="${appointmentId}">
                                    <button type="submit" class="btn btn-success fw-bold px-4">
                                        💰 Xác nhận Đã Thu Tiền
                                    </button>
                                </form>
                            </c:if>
                        </div>
                    </div>

                </div>
            </main>
        </div>
    </div>

    <!-- Footer dùng chung -->
    <div class="no-print">
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </div>
</body>
</html>
