<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="serviceDAO" class="com.devjava.dencli.dao.impl.ServiceDAOImpl" scope="page" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Phòng Khám Nha Khoa Công Nghệ Cao DenCli - Kiến tạo nụ cười rạng rỡ, tự tin tỏa sáng với công nghệ chuẩn Châu Âu.">
    <title>Nha Khoa Quốc Tế DenCli | Nụ Cười Rạng Rỡ, Tự Tin Tỏa Sáng</title>

    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        :root {
            --dencli-primary: #0EA5E9;
            --dencli-primary-dark: #0284C7;
            --dencli-navy: #1E3A5F;
            --dencli-coral: #FF6B6B;
            --dencli-coral-hover: #fa5252;
            --dencli-bg: #F8FAFC;
        }

        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: var(--dencli-bg);
            color: #334155;
            overflow-x: hidden;
        }

        /* Hero Section */
        .hero-section {
            background: radial-gradient(100% 120% at 85% 10%, #E0F2FE 0%, #F0F9FF 40%, #FFFFFF 100%);
            padding: 5rem 0 4rem;
            position: relative;
        }
        .hero-badge {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            background: #E0F2FE;
            color: #0369A1;
            padding: 0.35rem 1rem;
            border-radius: 9999px;
            font-size: 0.875rem;
            font-weight: 600;
        }
        .hero-title {
            font-size: 3.25rem;
            font-weight: 800;
            line-height: 1.15;
            color: var(--dencli-navy);
            letter-spacing: -0.03em;
        }
        .hero-title span {
            color: var(--dencli-primary);
        }
        .hero-subtitle {
            font-size: 1.15rem;
            color: #64748B;
            line-height: 1.6;
        }
        .btn-cta-primary {
            background-color: var(--dencli-coral);
            color: #ffffff;
            font-weight: 700;
            padding: 0.875rem 2rem;
            border-radius: 9999px;
            box-shadow: 0 10px 25px -3px rgba(255, 107, 107, 0.4);
            border: none;
            transition: all 0.25s ease;
        }
        .btn-cta-primary:hover {
            background-color: var(--dencli-coral-hover);
            color: #ffffff;
            transform: translateY(-2px);
            box-shadow: 0 15px 30px -3px rgba(255, 107, 107, 0.5);
        }
        .btn-cta-secondary {
            background-color: #FFFFFF;
            color: var(--dencli-navy);
            font-weight: 600;
            padding: 0.875rem 1.75rem;
            border-radius: 9999px;
            border: 1px solid #CBD5E1;
            transition: all 0.25s ease;
        }
        .btn-cta-secondary:hover {
            background-color: #F1F5F9;
            color: var(--dencli-primary);
            border-color: var(--dencli-primary);
        }

        /* Hero Visual Card */
        .hero-card {
            background: #FFFFFF;
            border-radius: 1.5rem;
            padding: 2rem;
            box-shadow: 0 20px 40px -15px rgba(30, 58, 95, 0.12);
            border: 1px solid #E2E8F0;
            position: relative;
        }
        .floating-stat-box {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(8px);
            border: 1px solid #E0F2FE;
            border-radius: 1rem;
            padding: 1rem 1.25rem;
            box-shadow: 0 10px 25px -5px rgba(14, 165, 233, 0.2);
            position: absolute;
            bottom: -15px;
            left: -15px;
        }

        /* Trust Band */
        .trust-band {
            background: var(--dencli-navy);
            color: #FFFFFF;
            border-radius: 1.25rem;
            padding: 2.5rem 1.5rem;
            box-shadow: 0 15px 30px -5px rgba(30, 58, 95, 0.25);
            margin-top: -2rem;
            position: relative;
            z-index: 10;
        }
        .stat-number {
            font-size: 2.5rem;
            font-weight: 800;
            color: #38BDF8;
            line-height: 1;
        }
        .stat-label {
            font-size: 0.925rem;
            color: #94A3B8;
            margin-top: 0.35rem;
            font-weight: 500;
        }

        /* Services Cards */
        .service-card {
            background: #FFFFFF;
            border-radius: 1.25rem;
            border: 1px solid #E2E8F0;
            padding: 2rem;
            height: 100%;
            transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
            display: flex;
            flex-column: column;
            justify-content: space-between;
        }
        .service-card:hover {
            transform: translateY(-6px);
            box-shadow: 0 20px 35px -10px rgba(14, 165, 233, 0.15);
            border-color: #BAE6FD;
        }
        .service-icon-wrapper {
            width: 56px;
            height: 56px;
            border-radius: 1rem;
            background: #E0F2FE;
            color: var(--dencli-primary);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.75rem;
            margin-bottom: 1.25rem;
        }
        .service-price {
            font-size: 1.25rem;
            font-weight: 700;
            color: var(--dencli-primary);
        }

        /* Before / After Section */
        .before-after-card {
            background: #FFFFFF;
            border-radius: 1.25rem;
            border: 1px solid #E2E8F0;
            overflow: hidden;
            box-shadow: 0 8px 20px -5px rgba(0,0,0,0.05);
            transition: all 0.3s ease;
        }
        .before-after-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 15px 30px -5px rgba(0,0,0,0.1);
        }
        .case-tag {
            font-size: 0.75rem;
            font-weight: 700;
            text-transform: uppercase;
            padding: 0.25rem 0.65rem;
            border-radius: 9999px;
        }

        /* How It Works Steps */
        .step-circle {
            width: 64px;
            height: 64px;
            border-radius: 50%;
            background: linear-gradient(135deg, #0EA5E9 0%, #0369A1 100%);
            color: #FFFFFF;
            font-size: 1.5rem;
            font-weight: 800;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1.5rem;
            box-shadow: 0 10px 20px -3px rgba(14, 165, 233, 0.4);
        }

        /* Doctor Cards */
        .doctor-card {
            background: #FFFFFF;
            border-radius: 1.25rem;
            border: 1px solid #E2E8F0;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0,0,0,0.04);
            transition: all 0.3s ease;
        }
        .doctor-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(30, 58, 95, 0.1);
        }
        .doctor-avatar {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            background: #E0F2FE;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            margin: 1.5rem auto 1rem;
            border: 4px solid #FFFFFF;
            box-shadow: 0 6px 15px rgba(14, 165, 233, 0.2);
        }

        /* Sticky CTA Mobile Bar */
        .mobile-sticky-cta {
            z-index: 1080;
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-top: 1px solid #E2E8F0;
        }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- Header dùng chung -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 1. SPLIT HERO SECTION -->
    <section class="hero-section">
        <div class="container">
            <div class="row align-items-center g-5">
                <div class="col-lg-7">
                    <div class="hero-badge mb-3">
                        <span>✨</span>
                        <span>Nha Khoa Công Nghệ Tiên Tiến Chuẩn Châu Âu</span>
                    </div>
                    <h1 class="hero-title mb-4">
                        Nụ Cười Rạng Rỡ,<br>
                        <span>Tự Tin Tỏa Sáng</span>
                    </h1>
                    <p class="hero-subtitle mb-4 pe-lg-5">
                        Chăm sóc nha khoa toàn diện cho cả gia đình. Đội ngũ bác sĩ chuyên khoa giàu kinh nghiệm, trang thiết bị hiện đại không đau, bảo hành dài lâu.
                    </p>
                    <div class="d-flex flex-wrap gap-3 mb-5">
                        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-cta-primary d-inline-flex align-items-center gap-2">
                            <span>📅</span>
                            <span>Đặt Lịch Khám Mới</span>
                        </a>
                        <a href="#services" class="btn btn-cta-secondary d-inline-flex align-items-center gap-2">
                            <span>📋</span>
                            <span>Xem Dịch Vụ Nha Khoa</span>
                        </a>
                    </div>
                    <!-- Trust Mini Badges -->
                    <div class="d-flex flex-wrap align-items-center gap-4 text-muted small">
                        <div class="d-flex align-items-center gap-2">
                            <span class="text-success fs-5">✓</span>
                            <span>Khám & Tư vấn 1-1 chuyên sâu</span>
                        </div>
                        <div class="d-flex align-items-center gap-2">
                            <span class="text-success fs-5">✓</span>
                            <span>Thiết bị CT Cone Beam 3D</span>
                        </div>
                        <div class="d-flex align-items-center gap-2">
                            <span class="text-success fs-5">✓</span>
                            <span>Hỗ trợ trả góp 0%</span>
                        </div>
                    </div>
                </div>

                <!-- Right Hero Visual Card -->
                <div class="col-lg-5">
                    <div class="hero-card text-center p-4 p-md-5">
                        <div class="mb-4">
                            <span class="d-inline-flex align-items-center justify-content-center bg-primary-subtle text-primary rounded-circle" style="width: 120px; height: 120px; font-size: 4.5rem;">
                                🦷
                            </span>
                        </div>
                        <h4 class="fw-bold text-navy mb-2">Phòng Khám Quốc Tế DenCli</h4>
                        <p class="text-muted small mb-4">
                            Điểm tựa nụ cười cho hơn 10.000+ bệnh nhân tại TP. Đà Nẵng và khu vực miền Trung.
                        </p>
                        <div class="p-3 bg-light rounded-3 text-start mb-3 border">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <span class="fw-semibold small">Bác sĩ trực buồng khám hôm nay:</span>
                                <span class="badge bg-success-subtle text-success">Đang trực</span>
                            </div>
                            <div class="d-flex align-items-center gap-2 text-primary fw-bold">
                                <span>🩺</span>
                                <span>5 Bác sĩ Chuyên khoa Sẵn sàng tiếp đón</span>
                            </div>
                        </div>

                        <!-- Floating Stat Box -->
                        <div class="floating-stat-box d-none d-sm-flex align-items-center gap-3">
                            <span class="fs-2 text-warning">⭐</span>
                            <div>
                                <div class="fw-bold text-dark fs-6">4.9 / 5.0 Điểm Đánh Giá</div>
                                <div class="text-muted small">Từ 3,200+ đánh giá thực tế</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 2. TRUST BAND (4 STATS) -->
    <section class="container mb-5">
        <div class="trust-band">
            <div class="row g-4 text-center">
                <div class="col-6 col-md-3 border-end border-secondary border-opacity-25">
                    <div class="stat-number">10,000+</div>
                    <div class="stat-label">Khách hàng hài lòng</div>
                </div>
                <div class="col-6 col-md-3 border-end-md border-secondary border-opacity-25">
                    <div class="stat-number">15+</div>
                    <div class="stat-label">Bác sĩ Chuyên khoa</div>
                </div>
                <div class="col-6 col-md-3 border-end border-secondary border-opacity-25">
                    <div class="stat-number">100%</div>
                    <div class="stat-label">Thiết bị Chuẩn Châu Âu</div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="stat-number">10+ Năm</div>
                    <div class="stat-label">Kinh nghiệm lâm sàng</div>
                </div>
            </div>
        </div>
    </section>

    <!-- 3. DANH MỤC DỊCH VỤ & BẢNG GIÁ (3-COLUMN GRID) -->
    <section id="services" class="container py-5">
        <div class="text-center mb-5">
            <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-semibold mb-2">DANH MỤC DỊCH VỤ</span>
            <h2 class="display-6 fw-bold text-navy mb-3">Giải Pháp Nha Khoa Toàn Diện</h2>
            <p class="text-muted mx-auto" style="max-width: 620px;">
                DenCli ứng dụng các phác đồ điều trị tân tiến nhất giúp bảo tồn răng thật tối đa và đem lại nụ cười hoàn mỹ.
            </p>

            <!-- Bộ lọc tìm kiếm nhanh dịch vụ -->
            <div class="mx-auto mt-4" style="max-width: 420px;">
                <div class="input-group shadow-sm rounded-pill overflow-hidden border">
                    <span class="input-group-text bg-white border-0 ps-3">🔍</span>
                    <input type="text" id="filterServiceInput" class="form-control border-0 py-2" placeholder="Tìm tên dịch vụ nha khoa..." onkeyup="filterServicesGrid()">
                </div>
            </div>
        </div>

        <!-- Grid dịch vụ 3 cột -->
        <div class="row g-4" id="servicesGrid">
            <c:choose>
                <c:when test="${empty serviceDAO.allServices}">
                    <div class="col-12 text-center py-5 text-muted">
                        <p class="fs-5">Đang cập nhật danh mục dịch vụ nha khoa...</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${serviceDAO.allServices}" var="s" varStatus="loop">
                        <div class="col-md-6 col-lg-4 service-item">
                            <div class="service-card">
                                <div>
                                    <div class="service-icon-wrapper">
                                        <c:choose>
                                            <c:when test="${loop.index % 4 == 0}">🦷</c:when>
                                            <c:when test="${loop.index % 4 == 1}">✨</c:when>
                                            <c:when test="${loop.index % 4 == 2}">💎</c:when>
                                            <c:otherwise>🩺</c:otherwise>
                                        </c:choose>
                                    </div>
                                    <h5 class="fw-bold text-navy mb-2 service-title">
                                        <c:out value="${s.serviceName}" />
                                    </h5>
                                    <p class="text-muted small mb-4">
                                        <c:out value="${not empty s.description ? s.description : 'Quy trình chuẩn y khoa, an toàn, không đau và bảo hành chính hãng.'}" />
                                    </p>
                                </div>
                                <div class="border-top pt-3 d-flex align-items-center justify-content-between">
                                    <div>
                                        <span class="text-muted small d-block">Chi phí niêm yết:</span>
                                        <span class="service-price">
                                            <fmt:formatNumber value="${s.price}" pattern="#,##0" /> VNĐ
                                        </span>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-sm btn-outline-primary rounded-pill px-3 py-1 fw-semibold">
                                        Đặt hẹn →
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <!-- 4. BEFORE / AFTER SLIDER (KẾT QUẢ ĐIỀU TRỊ THỰC TẾ) -->
    <section class="py-5 bg-white border-top border-bottom">
        <div class="container">
            <div class="text-center mb-5">
                <span class="badge bg-info-subtle text-info-emphasis px-3 py-2 rounded-pill fw-semibold mb-2">HÌNH ẢNH THỰC TẾ</span>
                <h2 class="display-6 fw-bold text-navy mb-3">Hiệu Quả Điều Trị Trước & Sau</h2>
                <p class="text-muted mx-auto" style="max-width: 620px;">
                    Những nụ cười đã được thay đổi kỳ diệu tại DenCli nhờ tay nghề chuyên môn cao của đội ngũ y bác sĩ.
                </p>
            </div>

            <div class="row g-4">
                <!-- Case 1: Niềng răng Chỉnh nha -->
                <div class="col-lg-4 col-md-6">
                    <div class="before-after-card p-3">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="case-tag bg-primary-subtle text-primary">Niềng Răng Mắc Cài</span>
                            <span class="text-muted small">Thời gian: 18 tháng</span>
                        </div>
                        <div class="p-3 bg-light rounded-3 text-center mb-3">
                            <div class="row g-2">
                                <div class="col-6 border-end">
                                    <div class="badge bg-secondary mb-1">TRƯỚC</div>
                                    <div class="text-muted small">Răng khấp khểnh độ 3, sai khớp cắn hở</div>
                                </div>
                                <div class="col-6">
                                    <div class="badge bg-success mb-1">SAU</div>
                                    <div class="text-primary fw-semibold small">Cung răng đều đẹp, khớp cắn chuẩn lồng múi</div>
                                </div>
                            </div>
                        </div>
                        <p class="small text-muted mb-0"><strong>Bệnh nhân N.V.A (24 tuổi):</strong> "Tự tin cười tươi hơn hẳn sau khi tháo niềng tại DenCli!"</p>
                    </div>
                </div>

                <!-- Case 2: Bọc Răng Sứ Thẩm Mỹ -->
                <div class="col-lg-4 col-md-6">
                    <div class="before-after-card p-3">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="case-tag bg-success-subtle text-success">Bọc Sứ Cercon HT</span>
                            <span class="text-muted small">Thời gian: 3 ngày</span>
                        </div>
                        <div class="p-3 bg-light rounded-3 text-center mb-3">
                            <div class="row g-2">
                                <div class="col-6 border-end">
                                    <div class="badge bg-secondary mb-1">TRƯỚC</div>
                                    <div class="text-muted small">Răng nhiễm kháng sinh nặng, mòn men cạnh cắn</div>
                                </div>
                                <div class="col-6">
                                    <div class="badge bg-success mb-1">SAU</div>
                                    <div class="text-success fw-semibold small">Dáng răng tự nhiên, trong bóng chuẩn tự nhiên</div>
                                </div>
                            </div>
                        </div>
                        <p class="small text-muted mb-0"><strong>Bệnh nhân T.T.H (32 tuổi):</strong> "Răng sứ ăn nhai rất chắc chắn, màu sắc trong trẻo tự nhiên."</p>
                    </div>
                </div>

                <!-- Case 3: Tẩy Trắng Laser Whitening -->
                <div class="col-lg-4 col-md-6">
                    <div class="before-after-card p-3">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="case-tag bg-warning-subtle text-warning-emphasis">Tẩy Trắng Laser</span>
                            <span class="text-muted small">Thời gian: 45 phút</span>
                        </div>
                        <div class="p-3 bg-light rounded-3 text-center mb-3">
                            <div class="row g-2">
                                <div class="col-6 border-end">
                                    <div class="badge bg-secondary mb-1">TRƯỚC</div>
                                    <div class="text-muted small">Màu răng ố vàng sậm do cà phê, thuốc lá</div>
                                </div>
                                <div class="col-6">
                                    <div class="badge bg-success mb-1">SAU</div>
                                    <div class="text-primary fw-semibold small">Bật 3 tone sáng, hoàn toàn không ê buốt</div>
                                </div>
                            </div>
                        </div>
                        <p class="small text-muted mb-0"><strong>Bệnh nhân L.M.K (29 tuổi):</strong> "Chỉ mất 45 phút mà hàm răng sáng bóng rõ rệt, bác sĩ rất êm tay."</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 5. QUY TRÌNH 3 BƯỚC KHÁM CHỮA BỆNH (HOW IT WORKS) -->
    <section class="container py-5">
        <div class="text-center mb-5">
            <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-semibold mb-2">QUY TRÌNH TINH GỌN</span>
            <h2 class="display-6 fw-bold text-navy mb-3">3 Bước Thăm Khám Chuẩn Y Khoa</h2>
            <p class="text-muted mx-auto" style="max-width: 620px;">
                Tiết kiệm tối đa thời gian chờ đợi với quy trình đặt hẹn và tiếp đón thông minh tại DenCli.
            </p>
        </div>

        <div class="row g-4 text-center">
            <div class="col-md-4">
                <div class="p-4 bg-white rounded-4 border h-100 shadow-sm">
                    <div class="step-circle">1</div>
                    <h5 class="fw-bold text-navy mb-2">Đặt Lịch Trực Tuyến</h5>
                    <p class="text-muted small mb-0">
                        Chọn bác sĩ phụ trách, ngày khám và khung giờ thuận tiện chỉ trong 1 phút qua website. Không cần chờ bốc số.
                    </p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="p-4 bg-white rounded-4 border h-100 shadow-sm">
                    <div class="step-circle">2</div>
                    <h5 class="fw-bold text-navy mb-2">Thăm Khám & Chẩn Đoán</h5>
                    <p class="text-muted small mb-0">
                        Được chụp phim CT Cone Beam 3D miễn phí, bác sĩ chuyên khoa chẩn đoán lâm sàng và lập phác đồ cá nhân hóa.
                    </p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="p-4 bg-white rounded-4 border h-100 shadow-sm">
                    <div class="step-circle">3</div>
                    <h5 class="fw-bold text-navy mb-2">Điều Trị & Chăm Sóc</h5>
                    <p class="text-muted small mb-0">
                        Tiến hành thủ thuật nhẹ nhàng bằng trang thiết bị Châu Âu, cấp phát thuốc tận tâm và theo dõi định kỳ dài lâu.
                    </p>
                </div>
            </div>
        </div>
    </section>

    <!-- 6. ĐỘI NGŨ BÁC SĨ & FAQ ACCORDION -->
    <section id="doctors" class="py-5 bg-white border-top">
        <div class="container">
            <div class="text-center mb-5">
                <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-semibold mb-2">CHUYÊN GIA NHA KHOA</span>
                <h2 class="display-6 fw-bold text-navy mb-3">Đội Ngũ Bác Sĩ Chuyên Khoa Giàu Kinh Nghiệm</h2>
                <p class="text-muted mx-auto" style="max-width: 620px;">
                    100% bác sĩ tốt nghiệp Đại học Y Dược chính quy, có chứng chỉ hành nghề và tu nghiệp chuyên sâu trong và ngoài nước.
                </p>
            </div>

            <!-- Thẻ bác sĩ -->
            <div class="row g-4 mb-5">
                <div class="col-md-4">
                    <div class="doctor-card text-center p-4">
                        <div class="doctor-avatar">👨‍⚕️</div>
                        <h5 class="fw-bold text-navy mb-1">BS. CKI Nguyễn Văn Hoàng</h5>
                        <p class="text-primary small fw-semibold mb-2">Chuyên gia Chỉnh nha & Niềng răng</p>
                        <p class="text-muted small mb-3">Hơn 12 năm kinh nghiệm niềng răng mắc cài và khay trong suốt Invisalign. Thành viên Hội Nắn chỉnh Răng Việt Nam.</p>
                        <span class="badge bg-light text-secondary border">1,500+ ca niềng thành công</span>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="doctor-card text-center p-4">
                        <div class="doctor-avatar">👩‍⚕️</div>
                        <h5 class="fw-bold text-navy mb-1">ThS. BS Trần Thị Mai Lan</h5>
                        <p class="text-primary small fw-semibold mb-2">Chuyên gia Phục hình Răng sứ & Thẩm mỹ</p>
                        <p class="text-muted small mb-3">Tốt nghiệp Thạc sĩ Răng Hàm Mặt, tu nghiệp chuyên sâu về dán sứ Veneer và răng sứ Cercon tại Đức.</p>
                        <span class="badge bg-light text-secondary border">10 năm kinh nghiệm thẩm mỹ</span>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="doctor-card text-center p-4">
                        <div class="doctor-avatar">👨‍⚕️</div>
                        <h5 class="fw-bold text-navy mb-1">BS. CKI Lê Quang Dũng</h5>
                        <p class="text-primary small fw-semibold mb-2">Chuyên gia Cấy ghép Implant & Tiểu phẫu</p>
                        <p class="text-muted small mb-3">Chứng chỉ Implant Quốc tế ICOI, thực hiện thành công hàng nghìn ca cấy ghép răng tức thì không đau.</p>
                        <span class="badge bg-light text-secondary border">2,000+ trụ Implant cấy ghép</span>
                    </div>
                </div>
            </div>

            <!-- FAQ Section -->
            <div id="faq" class="pt-4">
                <div class="text-center mb-4">
                    <h3 class="fw-bold text-navy mb-2">Câu Hỏi Thường Gặp (FAQ)</h3>
                    <p class="text-muted small">Những thắc mắc phổ biến nhất của bệnh nhân khi tới thăm khám tại DenCli.</p>
                </div>

                <div class="accordion mx-auto" id="dencliFaqAccordion" style="max-width: 820px;">
                    <!-- FAQ 1 -->
                    <div class="accordion-item border-0 mb-3 rounded-3 overflow-hidden shadow-sm">
                        <h2 class="accordion-header" id="headingOne">
                            <button class="accordion-button fw-bold text-navy" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                1. Chi phí khám ban đầu và chụp phim tại DenCli là bao nhiêu?
                            </button>
                        </h2>
                        <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#dencliFaqAccordion">
                            <div class="accordion-body text-muted small leading-relaxed">
                                Tại DenCli, chi phí thăm khám tổng quát và chụp phim X-Quang / CT Cone Beam 3D hoàn toàn <strong>miễn phí 100%</strong> cho mọi khách hàng đặt lịch hẹn trước qua website. Bác sĩ sẽ trực tiếp giải thích phác đồ điều trị và chi phí rõ ràng trước khi thực hiện.
                            </div>
                        </div>
                    </div>

                    <!-- FAQ 2 -->
                    <div class="accordion-item border-0 mb-3 rounded-3 overflow-hidden shadow-sm">
                        <h2 class="accordion-header" id="headingTwo">
                            <button class="accordion-button collapsed fw-bold text-navy" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                2. Phòng khám có áp dụng Bảo hiểm Y tế hoặc Bảo hiểm Bảo lãnh tư nhân không?
                            </button>
                        </h2>
                        <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#dencliFaqAccordion">
                            <div class="accordion-body text-muted small leading-relaxed">
                                DenCli hỗ trợ xuất hóa đơn điện tử đỏ (VAT) đầy đủ để bệnh nhân thanh toán với các công ty Bảo hiểm Bảo lãnh tư nhân (như Bảo Việt, PVI, Manulife, Prudential...). Lễ tân sẽ hỗ trợ toàn bộ thủ tục giấy tờ nhanh gọn.
                            </div>
                        </div>
                    </div>

                    <!-- FAQ 3 -->
                    <div class="accordion-item border-0 mb-3 rounded-3 overflow-hidden shadow-sm">
                        <h2 class="accordion-header" id="headingThree">
                            <button class="accordion-button collapsed fw-bold text-navy" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                                3. Thời gian niềng răng trung bình kéo dài bao lâu và có đau không?
                            </button>
                        </h2>
                        <div id="collapseThree" class="accordion-collapse collapse" aria-labelledby="headingThree" data-bs-parent="#dencliFaqAccordion">
                            <div class="accordion-body text-muted small leading-relaxed">
                                Thời gian niềng răng trung bình dao động từ 12 đến 24 tháng tùy thuộc vào mức độ sai lệch khớp cắn. Với công nghệ mắc cài tự buộc và khay trong suốt hiện đại, lực tác động dàn đều nên chỉ gây cảm giác hơi ê nhẹ trong 2-3 ngày đầu khi mới gắn khí cụ.
                            </div>
                        </div>
                    </div>

                    <!-- FAQ 4 -->
                    <div class="accordion-item border-0 mb-3 rounded-3 overflow-hidden shadow-sm">
                        <h2 class="accordion-header" id="headingFour">
                            <button class="accordion-button collapsed fw-bold text-navy" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                                4. DenCli có chính sách trả góp 0% lãi suất cho dịch vụ Niềng răng & Cấy Implant không?
                            </button>
                        </h2>
                        <div id="collapseFour" class="accordion-collapse collapse" aria-labelledby="headingFour" data-bs-parent="#dencliFaqAccordion">
                            <div class="accordion-body text-muted small leading-relaxed">
                                Có! DenCli áp dụng chương trình <strong>trả góp 0% lãi suất</strong> liên kết với hơn 20 ngân hàng lớn. Khách hàng chỉ cần thanh toán trước từ 30% chi phí, phần còn lại được chia đều trả góp linh hoạt từ 6 đến 12 tháng.
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 7. MOBILE STICKY CTA BAR -->
    <div class="mobile-sticky-cta d-md-none position-fixed bottom-0 start-0 end-0 p-3 shadow-lg">
        <a href="${pageContext.request.contextPath}/customer/book" class="btn btn-cta-primary w-100 py-3 d-flex align-items-center justify-content-center gap-2 shadow">
            <span>📅</span>
            <span>Đặt Lịch Khám Nha Khoa Ngay</span>
        </a>
    </div>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- Script tìm kiếm dịch vụ nhanh không reload trang -->
    <script>
        function filterServicesGrid() {
            var input = document.getElementById('filterServiceInput');
            var filter = input.value.toLowerCase().trim();
            var items = document.querySelectorAll('#servicesGrid .service-item');

            items.forEach(function(item) {
                var title = item.querySelector('.service-title');
                if (title) {
                    var text = title.textContent.toLowerCase();
                    item.style.display = (text.indexOf(filter) > -1) ? '' : 'none';
                }
            });
        }
    </script>
</body>
</html>
