<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ Sơ Bệnh Án Điện Tử | DenCli Dental Clinic</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Google Fonts: Plus Jakarta Sans & Figtree -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Figtree:wght@400;500;600;700&family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=6">

    <style>
        .record-timeline {
            position: relative;
            padding-left: 2rem;
        }
        .record-timeline::before {
            content: '';
            position: absolute;
            top: 20px;
            bottom: 20px;
            left: 11px;
            width: 3px;
            background: #E2E8F0;
            border-radius: 2px;
        }
        .timeline-node {
            position: absolute;
            left: -2rem;
            top: 22px;
            width: 26px;
            height: 26px;
            border-radius: 50%;
            background: #0891B2;
            border: 4px solid #F0FDFA;
            box-shadow: 0 0 0 2px #0891B2;
            z-index: 2;
        }
        .diagnosis-box {
            background: #F0FDFA;
            border-left: 4px solid #0891B2;
            border-radius: 0 10px 10px 0;
            padding: 1rem 1.25rem;
        }
        .prescription-badge {
            background: #EFF6FF;
            border: 1px solid #BFDBFE;
            color: #1E40AF;
            border-radius: 8px;
            padding: 0.4rem 0.75rem;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.85rem;
            font-weight: 600;
        }
        .treatment-tag {
            background: #F1F5F9;
            border: 1px solid #CBD5E1;
            color: #334155;
            padding: 0.35rem 0.75rem;
            border-radius: 6px;
            font-size: 0.825rem;
            font-weight: 500;
        }
    </style>
</head>

<body class="d-flex flex-column min-vh-100" style="background-color: #F8FAFC;">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

            <main class="col-12 col-md-9 col-lg-10 p-4 p-lg-5">
                
                <!-- Page Header -->
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4 pb-3 border-bottom">
                    <div>
                        <div class="d-flex align-items-center gap-2 mb-1">
                            <span class="fs-4">📋</span>
                            <h3 class="fw-bold text-dark mb-0">Hồ Sơ Bệnh Án Điện Tử</h3>
                        </div>
                        <p class="text-muted small mb-0">
                            Tra cứu chi tiết chẩn đoán lâm sàng, dịch vụ nha khoa đã thực hiện và đơn thuốc cho từng lần điều trị.
                        </p>
                    </div>
                    <div class="d-flex gap-2">
                        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-sm btn-outline-primary rounded-pill px-3 py-2 fw-semibold">
                            + Đặt lịch khám mới
                        </a>
                        <a href="${pageContext.request.contextPath}/customer/revisitation" class="btn btn-sm btn-outline-info rounded-pill px-3 py-2 fw-semibold">
                            🗓️ Lịch tái khám
                        </a>
                    </div>
                </div>

                <!-- Empty State -->
                <c:if test="${empty records}">
                    <div class="dencli-card p-5 text-center bg-white shadow-sm rounded-4 border-0 my-5">
                        <div class="display-4 text-muted mb-3">🩺</div>
                        <h5 class="fw-bold text-dark mb-2">Chưa có dữ liệu hồ sơ bệnh án</h5>
                        <p class="text-muted small mx-auto mb-4" style="max-width: 500px;">
                            Hồ sơ bệnh án điện tử sẽ tự động cập nhật ngay sau khi Bác sĩ hoàn thành phiên khám và ghi nhận kết quả điều trị.
                        </p>
                        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-primary rounded-pill px-4 fw-semibold shadow-sm" style="background: #0891B2; border-color: #0891B2;">
                            Đặt Lịch Khám Ngay
                        </a>
                    </div>
                </c:if>

                <!-- Medical Records Timeline List -->
                <div class="record-timeline">
                    <c:forEach items="${records}" var="rec">
                        <div class="position-relative mb-5">
                            <!-- Timeline Dot Indicator -->
                            <div class="timeline-node"></div>

                            <!-- Card Hồ sơ -->
                            <div class="dencli-card bg-white shadow-sm rounded-4 border-0 overflow-hidden">
                                
                                <!-- Card Header -->
                                <div class="px-4 py-3 bg-light border-bottom d-flex flex-wrap justify-content-between align-items-center gap-2">
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="badge px-3 py-2 rounded-pill fw-bold" style="background: #E0F2FE; color: #0369A1; font-size: 0.85rem;">
                                            📅 Ngày khám: <fmt:formatDate value="${rec.appointment.appointmentDate}" pattern="dd/MM/yyyy"/>
                                        </span>
                                        <span class="text-muted small">Khung giờ: <strong>${rec.appointment.appointmentTime}</strong></span>
                                        <span class="text-muted small">| Phòng: <strong>${rec.appointment.room}</strong></span>
                                    </div>
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="badge bg-success-subtle text-success px-3 py-1 rounded-pill fw-semibold">
                                            ✓ Đã hoàn thành
                                        </span>
                                        <span class="text-muted small">Mã khám: #${rec.appointment.appointmentId}</span>
                                    </div>
                                </div>

                                <!-- Card Body -->
                                <div class="p-4">
                                    <div class="row g-4">
                                        
                                        <!-- Cột Trái: Bác sĩ & Chẩn đoán -->
                                        <div class="col-lg-7">
                                            <div class="d-flex align-items-center gap-2 mb-3">
                                                <div class="rounded-circle d-flex align-items-center justify-content-center text-white fw-bold" style="width: 36px; height: 36px; background: #0891B2;">
                                                    BS
                                                </div>
                                                <div>
                                                    <span class="text-muted small d-block">Bác sĩ phụ trách:</span>
                                                    <strong class="text-dark">BS. <c:out value="${rec.appointment.doctorName}" /></strong>
                                                </div>
                                            </div>

                                            <!-- Khối Chẩn đoán lâm sàng -->
                                            <div class="diagnosis-box mb-3">
                                                <div class="fw-bold text-dark small mb-1 d-flex align-items-center gap-2">
                                                    <span>🔍 Kết quả chẩn đoán lâm sàng:</span>
                                                </div>
                                                <p class="mb-0 text-dark small leading-relaxed fw-medium">
                                                    <c:out value="${rec.diagnosis}" />
                                                </p>
                                            </div>

                                            <!-- Dịch vụ đã thực hiện -->
                                            <c:if test="${not empty rec.services}">
                                                <div class="mb-3">
                                                    <span class="fw-bold text-dark small d-block mb-2">🛠️ Dịch vụ nha khoa đã thực hiện:</span>
                                                    <div class="d-flex flex-wrap gap-2">
                                                        <c:forEach items="${rec.services}" var="ps">
                                                            <div class="treatment-tag d-flex align-items-center gap-2">
                                                                <span><c:out value="${ps.serviceName}" /></span>
                                                                <c:if test="${not empty ps.price and ps.price > 0}">
                                                                    <strong class="text-primary">
                                                                        (<fmt:formatNumber value="${ps.price}" pattern="#,##0" /> đ)
                                                                    </strong>
                                                                </c:if>
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </div>

                                        <!-- Cột Phải: Đơn thuốc & Viện phí -->
                                        <div class="col-lg-5">
                                            <!-- Đơn thuốc điện tử -->
                                            <div class="p-3 bg-light rounded-3 mb-3 border">
                                                <div class="d-flex justify-content-between align-items-center mb-2">
                                                    <span class="fw-bold text-dark small d-flex align-items-center gap-1">
                                                        <span>💊 Đơn thuốc đã kê</span>
                                                    </span>
                                                    <c:if test="${empty rec.prescription}">
                                                        <span class="badge bg-secondary-subtle text-secondary small">Không có đơn thuốc</span>
                                                    </c:if>
                                                </div>

                                                <c:if test="${not empty rec.prescription}">
                                                    <c:if test="${not empty rec.prescription.instructions}">
                                                        <p class="small text-muted mb-2 fst-italic">
                                                            " <c:out value="${rec.prescription.instructions}" /> "
                                                        </p>
                                                    </c:if>
                                                    <div class="d-flex flex-column gap-2">
                                                        <c:forEach items="${rec.prescription.details}" var="medicine">
                                                            <div class="prescription-badge justify-content-between">
                                                                <span><c:out value="${medicine.medicineName}" /></span>
                                                                <span class="badge bg-primary text-white rounded-pill">${medicine.prescribedQuantity} viên</span>
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </c:if>
                                            </div>

                                            <!-- Tóm tắt chi phí -->
                                            <div class="p-3 rounded-3 d-flex justify-content-between align-items-center" style="background: #F0FDFA; border: 1px solid #CCFBF1;">
                                                <div>
                                                    <span class="small text-muted d-block">Tổng chi phí điều trị:</span>
                                                    <span class="fw-bold fs-5 text-dark">
                                                        <fmt:formatNumber value="${rec.invoice.grandTotal}" pattern="#,##0" /> đ
                                                    </span>
                                                </div>
                                                <div>
                                                    <a href="${pageContext.request.contextPath}/customer/invoice?appointmentId=${rec.appointment.appointmentId}" class="btn btn-sm btn-outline-primary rounded-pill px-3 py-1 fw-semibold">
                                                        Xem hóa đơn →
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </main>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>

</html>
