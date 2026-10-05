<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Nha Khoa Quốc Tế DenCli - Niềng răng trong suốt Invisalign & mắc cài chuẩn Châu Âu cho cả gia đình. Khám tư vấn miễn phí, trả góp 0%.">
    <title>Nha Khoa Quốc Tế DenCli | Straighter Teeth, Brighter Confidence</title>

    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts: Plus Jakarta Sans -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    
    <!-- DenCli Pure CSS Stylesheet & Framer Motion Engine -->
    <link rel="stylesheet" href="${ctx}/assets/css/style.css?v=3">
    <link rel="stylesheet" href="${ctx}/css/dencli-theme.css?v=3">
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- 1. NAVBAR WITH "BOOK FREE CONSULTATION" CTA -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 2. SPLIT HERO SECTION (Headline Left, Office Photo Right) -->
    <section class="hero-section">
        <div class="container">
            <div class="row align-items-center g-5">
                <!-- Headline Left with 0.6s Framer Motion Fade-Up -->
                <div class="col-lg-6">
                    <div class="hero-badge mb-3 motion-fade-up motion-delay-1">
                        <span>✨</span>
                        <span>Straighter Teeth, Brighter Confidence</span>
                    </div>
                    <h1 class="hero-title mb-4 motion-fade-up motion-delay-2">
                        Răng Đều Đẹp,<br>
                        <span>Tự Tin Tỏa Sáng</span>
                    </h1>
                    <p class="hero-subtitle mb-4 pe-lg-4 motion-fade-up motion-delay-3">
                        Niềng răng trong suốt Invisalign và mắc cài cho cả gia đình. Thăm khám tư vấn 1-1 miễn phí cùng chuyên gia, hỗ trợ kế hoạch trả góp 0% lãi suất linh hoạt.
                    </p>
                    <div class="d-flex flex-wrap gap-3 mb-5 motion-fade-up motion-delay-4">
                        <a href="${ctx}/customer/book" class="btn btn-cta-primary">
                            <span>📅</span>
                            <span>Đặt Lịch Tư Vấn Miễn Phí</span>
                        </a>
                        <a href="#services" class="btn btn-cta-secondary">
                            <span>📋</span>
                            <span>Khám Phá Dịch Vụ</span>
                        </a>
                    </div>
                    <!-- Trust Mini Badges -->
                    <div class="d-flex flex-wrap align-items-center gap-4 text-muted small motion-fade-up motion-delay-4">
                        <div class="d-flex align-items-center gap-2">
                            <span class="text-success fs-5">✓</span>
                            <span>Chụp CT 3D & Tư vấn 0đ</span>
                        </div>
                        <div class="d-flex align-items-center gap-2">
                            <span class="text-success fs-5">✓</span>
                            <span>Bác sĩ Invisalign Platinum</span>
                        </div>
                        <div class="d-flex align-items-center gap-2">
                            <span class="text-success fs-5">✓</span>
                            <span>Trả góp 0% liên kết 20+ Bank</span>
                        </div>
                    </div>
                </div>

                <!-- Office Photo Right with Modern Floating Badges -->
                <div class="col-lg-6">
                    <div class="hero-photo-wrapper motion-fade-up motion-delay-3">
                        <img src="${ctx}/assets/images/dental_office_hero.jpg" alt="Phòng Khám Nha Khoa Hiện Đại DenCli" class="hero-photo-img" loading="eager">
                        
                        <!-- Floating Review Box -->
                        <div class="floating-stat-box d-none d-sm-flex align-items-center gap-3">
                            <span class="fs-2 text-warning">⭐</span>
                            <div>
                                
                                <div class="text-muted small">Từ 3,200+ ca niềng thực tế tại Hà Nội</div>
                            </div>
                        </div>

                        <!-- Top Floating Clinic Badge -->
                        <div class="floating-badge-top d-none d-md-flex align-items-center gap-2">
                            <span class="text-primary fs-5">🛡️</span>
                            <span>Phòng Khám Chuẩn Châu Âu</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 3. TRUST BAND (4 STATS) -->
    <section class="container mb-5">
        <div class="trust-band fade-on-scroll">
            <div class="row g-4 text-center">
                <div class="col-6 col-md-3 border-end border-secondary border-opacity-25">
                    <div class="stat-number">10,000+</div>
                    <div class="stat-label">Ca Niềng Thành Công</div>
                </div>
                <div class="col-6 col-md-3 border-end-md border-secondary border-opacity-25">
                    <div class="stat-number">15+ Năm</div>
                    <div class="stat-label">Kinh Nghiệm Lâm Sàng</div>
                </div>
                <div class="col-6 col-md-3 border-end border-secondary border-opacity-25">
                    <div class="stat-number">100%</div>
                    <div class="stat-label">Bác Sĩ CKI &amp; Thạc Sĩ RHM</div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="stat-number">0%</div>
                    <div class="stat-label">Trả Góp Lãi Suất Linh Hoạt</div>
                </div>
            </div>
        </div>
    </section>

    <!-- 4. 3-COLUMN SERVICES GRID (Invisalign, Traditional Braces, Teen Braces) -->
    <section id="services" class="container py-5">
        <div class="text-center mb-5 fade-on-scroll">
            <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-semibold mb-2">GIẢI PHÁP CHỈNH NHA TOÀN DIỆN</span>
            <h2 class="display-6 fw-bold mb-3" style="color: var(--dencli-navy);">Dịch Vụ Niềng Răng Chuyên Sâu</h2>
            <p class="text-muted mx-auto" style="max-width: 650px;">
                Ứng dụng công nghệ quét hàm iTero 5D và vật liệu SmartTrack tiên tiến giúp tối ưu hóa thời gian niềng, không đau và bảo tồn răng tối đa.
            </p>

            <!-- Bộ lọc tìm kiếm nhanh dịch vụ -->
            <div class="mx-auto mt-4" style="max-width: 440px;">
                <div class="input-group shadow-sm rounded-pill overflow-hidden border">
                    <span class="input-group-text bg-white border-0 ps-3">🔍</span>
                    <input type="text" id="filterServiceInput" class="form-control border-0 py-2" placeholder="Tìm kiếm nhanh loại niềng răng...">
                </div>
            </div>
        </div>

        <div class="row g-4" id="servicesGrid">
            <!-- Service 1: Invisalign -->
            <div class="col-md-6 col-lg-4 service-item fade-on-scroll">
                <div class="service-card">
                    <div>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="service-icon-wrapper">✨</div>
                            <span class="service-tag">Công nghệ Hoa Kỳ</span>
                        </div>
                        <h4 class="fw-bold mb-2 service-title" style="color: var(--dencli-navy);">Niềng Răng Trong Suốt Invisalign</h4>
                        <p class="text-muted small mb-4">
                            Khay niềng trong suốt gần như vô hình, tháo lắp linh hoạt khi ăn uống và vệ sinh. Biết trước nụ cười tương lai qua video 3D ClinCheck độc quyền.
                        </p>
                        <ul class="list-unstyled small text-muted mb-4 d-flex flex-column gap-2">
                            <li><span class="text-success fw-bold">✓</span> Khay nhựa sinh học SmartTrack ôm sát khít</li>
                            <li><span class="text-success fw-bold">✓</span> Không vướng víu, không gây nhiệt miệng</li>
                            <li><span class="text-success fw-bold">✓</span> Tái khám linh hoạt 6-8 tuần/lần</li>
                        </ul>
                    </div>
                    <div class="border-top pt-3 d-flex align-items-center justify-content-between">
                        <div>
                            <span class="text-muted small d-block">Chi phí từ:</span>
                            <span class="service-price">45.000.000 VNĐ</span>
                        </div>
                        <a href="${ctx}/customer/book" class="btn btn-sm btn-outline-primary rounded-pill px-3 py-1 fw-semibold">
                            Tư vấn ngay →
                        </a>
                    </div>
                </div>
            </div>

            <!-- Service 2: Traditional Braces -->
            <div class="col-md-6 col-lg-4 service-item fade-on-scroll">
                <div class="service-card">
                    <div>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="service-icon-wrapper">🦷</div>
                            <span class="service-tag">Hiệu Quả Vượt Trội</span>
                        </div>
                        <h4 class="fw-bold mb-2 service-title" style="color: var(--dencli-navy);">Niềng Răng Mắc Cài Truyền Thống</h4>
                        <p class="text-muted small mb-4">
                            Mắc cài kim loại tự buộc và mắc cài sứ thẩm mỹ cao cấp. Lực kéo cơ học ổn định giúp giải quyết dứt điểm các ca chen chúc, hô, móm phức tạp nhất.
                        </p>
                        <ul class="list-unstyled small text-muted mb-4 d-flex flex-column gap-2">
                            <li><span class="text-success fw-bold">✓</span> Khóa trượt tự buộc giảm ma sát, hạn chế đau</li>
                            <li><span class="text-success fw-bold">✓</span> Mắc cài sứ tiệp màu răng thẩm mỹ tự nhiên</li>
                            <li><span class="text-success fw-bold">✓</span> Chi phí hợp lý, tiết kiệm tối đa ngân sách</li>
                        </ul>
                    </div>
                    <div class="border-top pt-3 d-flex align-items-center justify-content-between">
                        <div>
                            <span class="text-muted small d-block">Chi phí từ:</span>
                            <span class="service-price">25.000.000 VNĐ</span>
                        </div>
                        <a href="${ctx}/customer/book" class="btn btn-sm btn-outline-primary rounded-pill px-3 py-1 fw-semibold">
                            Tư vấn ngay →
                        </a>
                    </div>
                </div>
            </div>

            <!-- Service 3: Teen Braces -->
            <div class="col-md-6 col-lg-4 service-item fade-on-scroll">
                <div class="service-card">
                    <div>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="service-icon-wrapper">🌟</div>
                            <span class="service-tag">Lứa Tuổi 7 - 16</span>
                        </div>
                        <h4 class="fw-bold mb-2 service-title" style="color: var(--dencli-navy);">Niềng Răng Thanh Thiếu Niên (Teen Braces)</h4>
                        <p class="text-muted small mb-4">
                            Phác đồ can thiệp sớm tận dụng giai đoạn phát triển vàng của xương hàm. Định hình cung hàm chuẩn, ngừa sai lệch mặt và tạo nền tảng nụ cười hoàn mỹ.
                        </p>
                        <ul class="list-unstyled small text-muted mb-4 d-flex flex-column gap-2">
                            <li><span class="text-success fw-bold">✓</span> Khí cụ tăng trưởng chỉnh xương không cần phẫu thuật</li>
                            <li><span class="text-success fw-bold">✓</span> Rút ngắn thời gian niềng khi trưởng thành</li>
                            <li><span class="text-success fw-bold">✓</span> Bác sĩ tâm lý, nhẹ nhàng với các bạn trẻ</li>
                        </ul>
                    </div>
                    <div class="border-top pt-3 d-flex align-items-center justify-content-between">
                        <div>
                            <span class="text-muted small d-block">Chi phí từ:</span>
                            <span class="service-price">18.000.000 VNĐ</span>
                        </div>
                        <a href="${ctx}/customer/book" class="btn btn-sm btn-outline-primary rounded-pill px-3 py-1 fw-semibold">
                            Tư vấn ngay →
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 5. BEFORE / AFTER SLIDER (3 PAIRS) -->
    <section class="py-5 bg-white border-top border-bottom">
        <div class="container">
            <div class="text-center mb-5 fade-on-scroll">
                <span class="badge bg-info-subtle text-info-emphasis px-3 py-2 rounded-pill fw-semibold mb-2">HÌNH ẢNH LÂM SÀNG THỰC TẾ</span>
                <h2 class="display-6 fw-bold mb-3" style="color: var(--dencli-navy);">Hiệu Quả Điều Trị Trước &amp; Sau</h2>
                <p class="text-muted mx-auto" style="max-width: 650px;">
                    Chứng kiến sự thay đổi ngoạn mục của các ca chỉnh nha thực tế được điều trị trực tiếp bởi đội ngũ bác sĩ chuyên khoa tại DenCli.
                </p>
            </div>

            <div class="row g-4">
                <!-- Case 1: Khớp Cắn Hở & Khấp Khểnh -->
                <div class="col-lg-4 col-md-6 fade-on-scroll" id="case1">
                    <div class="before-after-card p-3 h-100">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="case-badge-type bg-primary-subtle text-primary">Invisalign Toàn Diện</span>
                            <span class="text-muted small">Thời gian: 15 tháng</span>
                        </div>
                        <!-- Toggle Controls -->
                        <div class="btn-group w-100 mb-3" role="group">
                            <button type="button" class="btn btn-sm btn-primary active btn-toggle-before" onclick="toggleCaseView('case1', 'before')">Trước điều trị</button>
                            <button type="button" class="btn btn-sm btn-outline-secondary btn-toggle-after" onclick="toggleCaseView('case1', 'after')">Sau điều trị ✨</button>
                        </div>
                        <!-- State Box Before -->
                        <div class="case-split-box case-box-before p-3 text-center mb-3">
                            <span class="badge bg-secondary mb-2">TÌNH TRẠNG BAN ĐẦU</span>
                            <p class="text-danger fw-semibold small mb-1">Răng khấp khểnh độ 3, khớp cắn hở 4mm</p>
                            <p class="text-muted small mb-0">Ăn nhai khó khăn, phát âm không chuẩn, thiếu tự tin khi giao tiếp.</p>
                        </div>
                        <!-- State Box After -->
                        <div class="case-split-box case-box-after p-3 text-center mb-3" style="display: none; background: #ECFDF5; border-color: #A7F3D0;">
                            <span class="badge bg-success mb-2">KẾT QUẢ ĐẠT ĐƯỢC</span>
                            <p class="text-success fw-bold small mb-1">Cung răng đều tăm tắp, lồng múi chuẩn 100%</p>
                            <p class="text-muted small mb-0">Khớp cắn kín khít, khuôn mặt V-line cân đối, cười rạng rỡ.</p>
                        </div>
                        <p class="small text-muted mb-0"><strong>BN. Hoàng Thảo (24 tuổi):</strong> "Không ai nghĩ niềng răng trong suốt lại thay đổi khuôn mặt kỳ diệu đến vậy!"</p>
                    </div>
                </div>

                <!-- Case 2: Hô Hàm & Chen Chúc -->
                <div class="col-lg-4 col-md-6 fade-on-scroll" id="case2">
                    <div class="before-after-card p-3 h-100">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="case-badge-type bg-success-subtle text-success">Mắc Cài Sứ Tự Buộc</span>
                            <span class="text-muted small">Thời gian: 18 tháng</span>
                        </div>
                        <div class="btn-group w-100 mb-3" role="group">
                            <button type="button" class="btn btn-sm btn-primary active btn-toggle-before" onclick="toggleCaseView('case2', 'before')">Trước điều trị</button>
                            <button type="button" class="btn btn-sm btn-outline-secondary btn-toggle-after" onclick="toggleCaseView('case2', 'after')">Sau điều trị ✨</button>
                        </div>
                        <div class="case-split-box case-box-before p-3 text-center mb-3">
                            <span class="badge bg-secondary mb-2">TÌNH TRẠNG BAN ĐẦU</span>
                            <p class="text-danger fw-semibold small mb-1">Hô xương hàm trên, chen chúc hàm dưới</p>
                            <p class="text-muted small mb-0">Môi không khép kín tự nhiên khi nghỉ, góc nghiêng bị nhô.</p>
                        </div>
                        <div class="case-split-box case-box-after p-3 text-center mb-3" style="display: none; background: #ECFDF5; border-color: #A7F3D0;">
                            <span class="badge bg-success mb-2">KẾT QUẢ ĐẠT ĐƯỢC</span>
                            <p class="text-success fw-bold small mb-1">Kéo lùi cung hàm 6mm, góc nghiêng chuẩn thẩm mỹ</p>
                            <p class="text-muted small mb-0">Hết hô hoàn toàn, môi khép kín tự nhiên, ăn nhai chắc khỏe.</p>
                        </div>
                        <p class="small text-muted mb-0"><strong>BN. Quốc Huy (27 tuổi):</strong> "Bác sĩ kiểm soát lực kéo rất êm, góc nghiêng thay đổi rõ rệt."</p>
                    </div>
                </div>

                <!-- Case 3: Teen Braces (Răng Thưa & Lệch Lạc) -->
                <div class="col-lg-4 col-md-6 fade-on-scroll" id="case3">
                    <div class="before-after-card p-3 h-100">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="case-badge-type bg-warning-subtle text-warning-emphasis">Niềng Răng Tuổi Teen</span>
                            <span class="text-muted small">Thời gian: 12 tháng</span>
                        </div>
                        <div class="btn-group w-100 mb-3" role="group">
                            <button type="button" class="btn btn-sm btn-primary active btn-toggle-before" onclick="toggleCaseView('case3', 'before')">Trước điều trị</button>
                            <button type="button" class="btn btn-sm btn-outline-secondary btn-toggle-after" onclick="toggleCaseView('case3', 'after')">Sau điều trị ✨</button>
                        </div>
                        <div class="case-split-box case-box-before p-3 text-center mb-3">
                            <span class="badge bg-secondary mb-2">TÌNH TRẠNG BAN ĐẦU</span>
                            <p class="text-danger fw-semibold small mb-1">Khe thưa cửa 3mm, lệch đường giữa 2mm</p>
                            <p class="text-muted small mb-0">Bé ngại cười, phát âm bị lọt gió, thức ăn thường giắt vào kẽ răng.</p>
                        </div>
                        <div class="case-split-box case-box-after p-3 text-center mb-3" style="display: none; background: #ECFDF5; border-color: #A7F3D0;">
                            <span class="badge bg-success mb-2">KẾT QUẢ ĐẠT ĐƯỢC</span>
                            <p class="text-success fw-bold small mb-1">Đóng kín hoàn toàn khe thưa, cân đối đường giữa</p>
                            <p class="text-muted small mb-0">Nụ cười tươi rạng rỡ, khớp cắn chắc khỏe, bé tự tin đến trường.</p>
                        </div>
                        <p class="small text-muted mb-0"><strong>Mẹ bé Gia Hân (14 tuổi):</strong> "Chỉ 1 năm mà hàm răng con gái thẳng tắp, cả nhà ai cũng mừng!"</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 6. 3-STEP HOW-IT-WORKS -->
    <section class="container py-5">
        <div class="text-center mb-5 fade-on-scroll">
            <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-semibold mb-2">QUY TRÌNH TINH GỌN</span>
            <h2 class="display-6 fw-bold mb-3" style="color: var(--dencli-navy);">3 Bước Sở Hữu Nụ Cười Hoàn Hảo</h2>
            <p class="text-muted mx-auto" style="max-width: 620px;">
                Đơn giản hóa trải nghiệm chỉnh nha với phác đồ số hóa 3D không đau, không chờ đợi.
            </p>
        </div>

        <div class="row g-4">
            <div class="col-md-4 fade-on-scroll">
                <div class="step-card">
                    <div class="step-circle">1</div>
                    <h5 class="fw-bold mb-2" style="color: var(--dencli-navy);">Khám &amp; Chụp Phim 3D Miễn Phí</h5>
                    <p class="text-muted small mb-0">
                        Thăm khám 1-1 cùng bác sĩ chuyên khoa, quét dấu răng kỹ thuật số iTero 5D và chụp CT Cone Beam 3D hoàn toàn miễn phí.
                    </p>
                </div>
            </div>
            <div class="col-md-4 fade-on-scroll">
                <div class="step-card">
                    <div class="step-circle">2</div>
                    <h5 class="fw-bold mb-2" style="color: var(--dencli-navy);">Mô Phỏng 3D ClinCheck</h5>
                    <p class="text-muted small mb-0">
                        Xem trước video 3D mô phỏng từng bước dịch chuyển của răng và nụ cười sau khi tháo niềng trước khi bạn quyết định điều trị.
                    </p>
                </div>
            </div>
            <div class="col-md-4 fade-on-scroll">
                <div class="step-card">
                    <div class="step-circle">3</div>
                    <h5 class="fw-bold mb-2" style="color: var(--dencli-navy);">Gắn Khí Cụ &amp; Tỏa Sáng</h5>
                    <p class="text-muted small mb-0">
                        Nhận khay Invisalign hoặc gắn mắc cài nhẹ nhàng. Bác sĩ đồng hành theo dõi sát sao tiến độ định kỳ cho đến ngày hoàn thiện.
                    </p>
                </div>
            </div>
        </div>
    </section>

    <!-- 7. 3 TESTIMONIAL CARDS -->
    <section class="py-5 bg-white border-top border-bottom">
        <div class="container">
            <div class="text-center mb-5 fade-on-scroll">
                <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-semibold mb-2">CẢM NHẬN KHÁCH HÀNG</span>
                <h2 class="display-6 fw-bold mb-3" style="color: var(--dencli-navy);">10.000+ Nụ Cười Đã Thay Đổi</h2>
                <p class="text-muted mx-auto" style="max-width: 620px;">
                    Lắng nghe những chia sẻ chân thực từ các bệnh nhân đã hoàn tất hành trình niềng răng tại DenCli.
                </p>
            </div>

            <div class="row g-4">
                <!-- Review 1 -->
                <div class="col-md-4 fade-on-scroll">
                    <div class="testimonial-card">
                        <div>
                            <div class="d-flex text-warning fs-5 mb-3">★★★★★</div>
                            <p class="text-muted small mb-4 leading-relaxed">
                                "Mình làm MC sự kiện nên ngoại hình rất quan trọng. Nhờ niềng Invisalign tại DenCli, suốt 14 tháng đi làm không ai nhận ra mình đang chỉnh nha. Răng vào đều đẹp đúng hẹn!"
                            </p>
                        </div>
                        <div class="d-flex align-items-center gap-3 pt-3 border-top">
                            <div class="testimonial-avatar">Y</div>
                            <div>
                                <h6 class="fw-bold mb-0" style="color: var(--dencli-navy);">Nguyễn Hải Yến, 23 tuổi</h6>
                                <span class="text-muted small">Niềng Invisalign Platinum</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Review 2 -->
                <div class="col-md-4 fade-on-scroll">
                    <div class="testimonial-card">
                        <div>
                            <div class="d-flex text-warning fs-5 mb-3">★★★★★</div>
                            <p class="text-muted small mb-4 leading-relaxed">
                                "Rất ấn tượng với sự tận tụy của BS Hoàng. Trước khi niềng mình được xem trước ClinCheck 3D chuẩn từng milimet. Chính sách trả góp 0% chia nhỏ theo tháng rất nhẹ nhàng."
                            </p>
                        </div>
                        <div class="d-flex align-items-center gap-3 pt-3 border-top">
                            <div class="testimonial-avatar">Q</div>
                            <div>
                                <h6 class="fw-bold mb-0" style="color: var(--dencli-navy);">Trần Minh Quân, 28 tuổi</h6>
                                <span class="text-muted small">Niềng Mắc Cài Tự Buộc</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Review 3 -->
                <div class="col-md-4 fade-on-scroll">
                    <div class="testimonial-card">
                        <div>
                            <div class="d-flex text-warning fs-5 mb-3">★★★★★</div>
                            <p class="text-muted small mb-4 leading-relaxed">
                                "Cho con gái niềng răng tuổi dậy thì ở DenCli là quyết định đúng đắn nhất của mình. Bé không hề bị đau nhức hay sụt cân, phòng khám sạch đẹp chuẩn khách sạn 5 sao."
                            </p>
                        </div>
                        <div class="d-flex align-items-center gap-3 pt-3 border-top">
                            <div class="testimonial-avatar">N</div>
                            <div>
                                <h6 class="fw-bold mb-0" style="color: var(--dencli-navy);">Chị Lê Thanh Nga</h6>
                                <span class="text-muted small">Phụ huynh bé Bảo Nam (13t)</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 8. INSURANCE & FINANCING SECTION -->
    <section class="container py-5">
        <div class="row g-4 align-items-stretch">
            <div class="col-lg-6 fade-on-scroll">
                <div class="finance-card h-100">
                    <div class="d-flex align-items-center gap-3 mb-3">
                        <span class="fs-1 text-primary">💳</span>
                        <div>
                            <h4 class="fw-bold mb-1" style="color: var(--dencli-navy);">Trả Góp 0% Lãi Suất Linh Hoạt</h4>
                            <p class="text-muted small mb-0">Chỉ từ 1.000.000 VNĐ / tháng, không lo gánh nặng tài chính.</p>
                        </div>
                    </div>
                    <p class="text-secondary small leading-relaxed mb-4">
                        DenCli hợp tác cùng <strong>20+ ngân hàng uy tín hàng đầu</strong> (Vietcombank, Techcombank, VPBank, MB Bank, TPBank...). Thủ tục chỉ mất 3 phút với thẻ tín dụng hoặc CCCD gắn chip, trả dần trong 6 đến 18 tháng không phát sinh thêm chi phí.
                    </p>
                    <div class="d-flex flex-wrap gap-2">
                        <span class="badge bg-light text-secondary border">0% Lãi suất</span>
                        <span class="badge bg-light text-secondary border">Kỳ hạn 6-18 tháng</span>
                        <span class="badge bg-light text-secondary border">Thủ tục 3 phút</span>
                    </div>
                </div>
            </div>

            <div class="col-lg-6 fade-on-scroll">
                <div class="finance-card h-100">
                    <div class="d-flex align-items-center gap-3 mb-3">
                        <span class="fs-1 text-success">🛡️</span>
                        <div>
                            <h4 class="fw-bold mb-1" style="color: var(--dencli-navy);">Bảo Hiểm Bảo Lãnh Trực Tiếp</h4>
                            <p class="text-muted small mb-0">Hỗ trợ xuất hóa đơn điện tử đỏ (VAT) thanh toán viện phí.</p>
                        </div>
                    </div>
                    <p class="text-secondary small leading-relaxed mb-4">
                        DenCli hỗ trợ tối đa thủ tục hoàn tiền viện phí với các đơn vị bảo hiểm sức khỏe tư nhân hàng đầu tại Việt Nam như <strong>Bảo Việt, PVI, Manulife, Prudential, PTI, Dai-ichi Life</strong>. Lễ tân phòng khám trực tiếp chuẩn bị đầy đủ chứng từ và hồ sơ điều trị.
                    </p>
                    <div class="d-flex flex-wrap gap-2">
                        <span class="badge bg-light text-secondary border">Xuất hóa đơn VAT</span>
                        <span class="badge bg-light text-secondary border">Bảo lãnh tư nhân</span>
                        <span class="badge bg-light text-secondary border">Hỗ trợ hồ sơ 100%</span>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 9. DOCTOR BIO WITH CREDENTIALS -->
    <section id="doctors" class="py-5 bg-white border-top">
        <div class="container">
            <div class="text-center mb-5 fade-on-scroll">
                <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-semibold mb-2">ĐỘI NGŨ CHUYÊN GIA</span>
                <h2 class="display-6 fw-bold mb-3" style="color: var(--dencli-navy);">Bác Sĩ Trực Tiếp Điều Trị</h2>
                <p class="text-muted mx-auto" style="max-width: 620px;">
                    100% bác sĩ chính quy tốt nghiệp Đại học Y Dược, tốt nghiệp quốc tế chuyên sâu và sở hữu chứng chỉ nắn chỉnh răng chính thức.
                </p>
            </div>

            <div class="row g-4 justify-content-center">
                <!-- Doctor 1 -->
                <div class="col-md-6 col-lg-5 fade-on-scroll">
                    <div class="doctor-card">
                        <div class="doctor-avatar">👨‍⚕️</div>
                        <h4 class="fw-bold mb-1" style="color: var(--dencli-navy);">BS. CKI Nguyễn Văn Hoàng</h4>
                        <p class="text-primary fw-semibold small mb-3">Chuyên gia Chỉnh nha &amp; Niềng răng Invisalign</p>
                        <p class="text-muted small mb-4 leading-relaxed">
                            Hơn 12 năm kinh nghiệm lâm sàng chỉnh nha chuyên sâu. Đạt danh hiệu Bác sĩ Invisalign Platinum Elite danh giá từ Hoa Kỳ. Thành viên chính thức Hội Nắn chỉnh Răng Việt Nam (VAO).
                        </p>
                        <div class="d-flex justify-content-center gap-2 flex-wrap">
                            <span class="badge bg-light text-secondary border">2,500+ Ca Niềng Hoàn Tất</span>
                            <span class="badge bg-light text-secondary border">Invisalign Platinum Elite</span>
                        </div>
                    </div>
                </div>

                <!-- Doctor 2 -->
                <div class="col-md-6 col-lg-5 fade-on-scroll">
                    <div class="doctor-card">
                        <div class="doctor-avatar">👩‍⚕️</div>
                        <h4 class="fw-bold mb-1" style="color: var(--dencli-navy);">ThS. BS Trần Thị Mai Lan</h4>
                        <p class="text-primary fw-semibold small mb-3">Chuyên gia Chỉnh nha Thẩm mỹ &amp; Phục hình</p>
                        <p class="text-muted small mb-4 leading-relaxed">
                            Tốt nghiệp Thạc sĩ Răng Hàm Mặt loại Giỏi, tốt nghiệp chuyên sâu về khớp cắn sinh lý và nắn chỉnh răng thẩm mỹ tại Cộng hòa Liên bang Đức. Hơn 10 năm kinh nghiệm điều trị niềng răng tuổi teen.
                        </p>
                        <div class="d-flex justify-content-center gap-2 flex-wrap">
                            <span class="badge bg-light text-secondary border">Thạc Sĩ RHM Chính Quy</span>
                            <span class="badge bg-light text-secondary border">10 Năm Kinh Nghiệm</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 10. FAQ ACCORDION (4 QUESTIONS) -->
    <section id="faq" class="container py-5">
        <div class="text-center mb-5 fade-on-scroll">
            <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-semibold mb-2">GIẢI ĐÁP THẮC MẮC</span>
            <h2 class="display-6 fw-bold mb-3" style="color: var(--dencli-navy);">Câu Hỏi Thường Gặp (FAQ)</h2>
            <p class="text-muted mx-auto" style="max-width: 620px;">
                Những điều bệnh nhân quan tâm nhất trước khi quyết định niềng răng tại DenCli.
            </p>
        </div>

        <div class="accordion mx-auto fade-on-scroll" id="dencliFaqAccordion" style="max-width: 840px;">
            <!-- FAQ 1 -->
            <div class="accordion-item shadow-sm">
                <h2 class="accordion-header" id="headingOne">
                    <button class="accordion-button fw-bold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                        1. Chi phí khám ban đầu và chụp phim tư vấn niềng răng tại DenCli có mất phí không?
                    </button>
                </h2>
                <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#dencliFaqAccordion">
                    <div class="accordion-body text-muted small leading-relaxed">
                        Tại DenCli, chi phí thăm khám tổng quát 1-1, lấy dấu răng kỹ thuật số iTero 5D và chụp phim CT Cone Beam 3D hoàn toàn <strong>miễn phí 100%</strong> cho khách hàng đặt lịch hẹn trước qua website. Bác sĩ sẽ trực tiếp giải thích phác đồ điều trị và chi phí rõ ràng trước khi bạn quyết định.
                    </div>
                </div>
            </div>

            <!-- FAQ 2 -->
            <div class="accordion-item shadow-sm">
                <h2 class="accordion-header" id="headingTwo">
                    <button class="accordion-button collapsed fw-bold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                        2. Niềng răng trong suốt Invisalign khác gì so với niềng răng mắc cài truyền thống?
                    </button>
                </h2>
                <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#dencliFaqAccordion">
                    <div class="accordion-body text-muted small leading-relaxed">
                        Khác với mắc cài truyền thống phải gắn cố định trên răng, khay Invisalign trong suốt gần như vô hình và có thể tháo lắp linh hoạt khi ăn uống, đánh răng. Đồng thời, lực tác động sinh học được tính toán dàn đều nên giảm thiểu tối đa cảm giác ê buốt và hạn chế trầy xước mô mềm trong miệng.
                    </div>
                </div>
            </div>

            <!-- FAQ 3 -->
            <div class="accordion-item shadow-sm">
                <h2 class="accordion-header" id="headingThree">
                    <button class="accordion-button collapsed fw-bold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                        3. Thời gian niềng răng trung bình mất bao lâu và có bắt buộc phải nhổ răng không?
                    </button>
                </h2>
                <div id="collapseThree" class="accordion-collapse collapse" aria-labelledby="headingThree" data-bs-parent="#dencliFaqAccordion">
                    <div class="accordion-body text-muted small leading-relaxed">
                        Thời gian niềng răng trung bình dao động từ 12 đến 24 tháng tùy thuộc vào độ phức tạp của từng ca. Tại DenCli, bác sĩ luôn tuân thủ nguyên tắc <strong>bảo tồn răng thật tối đa</strong>: ưu tiên kỹ thuật nong hàm, di xa cung răng để tạo khoảng trống, chỉ chỉ định nhổ răng khi thực sự cần thiết nhằm bảo vệ thẩm mỹ góc nghiêng.
                    </div>
                </div>
            </div>

            <!-- FAQ 4 -->
            <div class="accordion-item shadow-sm">
                <h2 class="accordion-header" id="headingFour">
                    <button class="accordion-button collapsed fw-bold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                        4. Chính sách trả góp 0% tại DenCli hoạt động như thế nào?
                    </button>
                </h2>
                <div id="collapseFour" class="accordion-collapse collapse" aria-labelledby="headingFour" data-bs-parent="#dencliFaqAccordion">
                    <div class="accordion-body text-muted small leading-relaxed">
                        DenCli áp dụng chương trình <strong>trả góp 0% lãi suất</strong> liên kết với hơn 20 ngân hàng lớn. Khách hàng chỉ cần thanh toán trước từ 30% chi phí điều trị, phần còn lại được chia đều trả dần từ 6 đến 18 tháng (khoảng 1 - 2 triệu VNĐ/tháng) mà không phải chịu bất kỳ khoản phụ phí phát sinh nào.
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 11. CTA BANNER SECTION -->
    <section class="container my-5">
        <div class="cta-banner text-center fade-on-scroll">
            <h2 class="display-6 fw-bold mb-3 text-white" style="color: #FFFFFF !important;">Sẵn Sàng Kiến Tạo Nụ Cười Rạng Rỡ &amp; Tự Tin?</h2>
            <p class="mx-auto mb-4" style="max-width: 640px; font-size: 1.15rem; color: #E0F2FE !important;">
                Đặt hẹn ngay hôm nay để nhận gói chụp phim CT Cone Beam 3D và tư vấn phác đồ chỉnh nha khoa cá nhân hóa hoàn toàn miễn phí cùng chuyên gia đầu ngành!
            </p>
            <div class="d-flex justify-content-center gap-3 flex-wrap">
                <a href="${ctx}/customer/book" class="btn btn-cta-primary btn-lg shadow-lg">
                    <span>📅</span>
                    <span>Đặt Lịch Tư Vấn Miễn Phí</span>
                </a>
            </div>
        </div>
    </section>

    <!-- 12. MOBILE STICKY "BOOK NOW" BAR -->
    <div class="mobile-sticky-cta d-md-none position-fixed bottom-0 start-0 end-0 p-3">
        <a href="${ctx}/customer/book" class="btn btn-cta-primary w-100 py-3 d-flex align-items-center justify-content-center gap-2 shadow">
            <span>📅</span>
            <span>Đặt Lịch Tư Vấn Ngay</span>
        </a>
    </div>

    <!-- 13. FOOTER DÙNG CHUNG -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Motion Engine JS -->
    <script src="${ctx}/assets/js/main.js"></script>
</body>
</html>
