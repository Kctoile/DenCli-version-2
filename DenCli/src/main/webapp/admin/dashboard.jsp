<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bảng điều khiển Quản trị | DenCli</title>
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
                <div class="container-fluid p-0">

                    <!-- Tiêu đề Dashboard -->
                    <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 gap-2">
                        <div>
                            <h3 class="fw-bold text-primary mb-1">📊 Bảng điều khiển Quản trị Phòng khám</h3>
                            <p class="text-muted small mb-0">Thống kê chỉ số tăng trưởng người dùng, nhân sự và báo cáo doanh thu tài chính.</p>
                        </div>
                        <div class="d-flex gap-2 align-items-center">
                            <span class="badge bg-primary px-3 py-2 fs-6">Năm báo cáo: <c:out value="${currentYear}" /></span>
                        </div>
                    </div>

                    <!-- Hàng thẻ thống kê nhanh (KPI Cards) -->
                    <div class="row g-3 mb-4">
                        <div class="col-6 col-lg-2">
                            <div class="kpi-card">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <span class="text-muted small fw-semibold text-uppercase">Lịch hẹn</span>
                                    <div class="kpi-icon-badge kpi-icon-primary">📅</div>
                                </div>
                                <div class="fs-2 fw-bold text-navy my-1"><c:out value="${totalAppointments}" /></div>
                                <div class="text-muted small">Tổng ca đăng ký</div>
                            </div>
                        </div>

                        <div class="col-6 col-lg-2">
                            <div class="kpi-card">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <span class="text-muted small fw-semibold text-uppercase">Bệnh nhân</span>
                                    <div class="kpi-icon-badge kpi-icon-success">👥</div>
                                </div>
                                <div class="fs-2 fw-bold text-success my-1"><c:out value="${totalCustomers}" /></div>
                                <div class="text-muted small">Hồ sơ khách hàng</div>
                            </div>
                        </div>

                        <div class="col-6 col-lg-2">
                            <div class="kpi-card">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <span class="text-muted small fw-semibold text-uppercase">Bác sĩ</span>
                                    <div class="kpi-icon-badge kpi-icon-info">🩺</div>
                                </div>
                                <div class="fs-2 fw-bold text-info my-1"><c:out value="${totalDoctors}" /></div>
                                <div class="text-muted small">Đội ngũ nha sĩ</div>
                            </div>
                        </div>

                        <div class="col-6 col-lg-3">
                            <div class="kpi-card">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <span class="text-muted small fw-semibold text-uppercase">Nhân viên Lễ tân</span>
                                    <div class="kpi-icon-badge kpi-icon-warning">💁</div>
                                </div>
                                <div class="fs-2 fw-bold text-warning my-1"><c:out value="${totalStaff}" /></div>
                                <div class="text-muted small">Tiếp đón &amp; Điều phối</div>
                            </div>
                        </div>

                        <div class="col-6 col-lg-3">
                            <div class="kpi-card">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <span class="text-muted small fw-semibold text-uppercase">Tổng Tài khoản</span>
                                    <div class="kpi-icon-badge kpi-icon-navy">🏢</div>
                                </div>
                                <div class="fs-2 fw-bold text-navy my-1"><c:out value="${totalUsers}" /></div>
                                <div class="text-muted small">Người dùng hệ thống</div>
                            </div>
                        </div>
                    </div>

                    <!-- Hàng trạng thái lịch hẹn thực tế (Phase 4.3) -->
                    <div class="row g-3 mb-4">
                        <div class="col-6 col-md-3">
                            <div class="kpi-card flex-row align-items-center justify-content-between p-3">
                                <div>
                                    <div class="text-muted small fw-semibold">Chờ xác nhận</div>
                                    <div class="fs-3 fw-bold text-warning"><c:out value="${pendingAppointments}" /></div>
                                </div>
                                <div class="kpi-icon-badge kpi-icon-warning fs-4">⏳</div>
                            </div>
                        </div>
                        <div class="col-6 col-md-3">
                            <div class="kpi-card flex-row align-items-center justify-content-between p-3">
                                <div>
                                    <div class="text-muted small fw-semibold">Đã xác nhận</div>
                                    <div class="fs-3 fw-bold text-info"><c:out value="${confirmedAppointments}" /></div>
                                </div>
                                <div class="kpi-icon-badge kpi-icon-info fs-4">📅</div>
                            </div>
                        </div>
                        <div class="col-6 col-md-3">
                            <div class="kpi-card flex-row align-items-center justify-content-between p-3">
                                <div>
                                    <div class="text-muted small fw-semibold">Đã hoàn tất</div>
                                    <div class="fs-3 fw-bold text-success"><c:out value="${completedAppointments}" /></div>
                                </div>
                                <div class="kpi-icon-badge kpi-icon-success fs-4">✅</div>
                            </div>
                        </div>
                        <div class="col-6 col-md-3">
                            <div class="kpi-card flex-row align-items-center justify-content-between p-3">
                                <div>
                                    <div class="text-muted small fw-semibold">Đã hủy</div>
                                    <div class="fs-3 fw-bold text-danger"><c:out value="${cancelledAppointments}" /></div>
                                </div>
                                <div class="kpi-icon-badge kpi-icon-danger fs-4">❌</div>
                            </div>
                        </div>
                    </div>

                    <!-- Khung Biểu đồ Doanh thu Chart.js -->
                    <div class="card card-custom bg-white p-4 mb-4">
                        <div class="d-flex flex-wrap justify-content-between align-items-center border-bottom pb-3 mb-3 gap-2">
                            <div>
                                <h5 class="fw-bold text-dark mb-1">📈 Biểu đồ Doanh thu & Lượng khám 12 tháng</h5>
                                <p class="text-muted small mb-0">Dữ liệu doanh thu thực thu từ dịch vụ nha khoa và thuốc điều trị.</p>
                            </div>
                            <div class="d-flex align-items-center gap-2">
                                <label for="yearSelect" class="small fw-semibold text-muted">Chọn năm:</label>
                                <select id="yearSelect" class="form-select form-select-sm" style="width: auto;" onchange="loadRevenueChart(this.value)">
                                    <option value="2025" ${currentYear == 2025 ? 'selected' : ''}>Năm 2025</option>
                                    <option value="2026" ${currentYear == 2026 || empty currentYear ? 'selected' : ''}>Năm 2026</option>
                                    <option value="2027" ${currentYear == 2027 ? 'selected' : ''}>Năm 2027</option>
                                </select>
                            </div>
                        </div>

                        <!-- Vùng hiển thị Canvas Chart.js -->
                        <div class="position-relative" style="min-height: 360px;">
                            <canvas id="revenueChart"></canvas>
                        </div>
                    </div>

                </div>
            </main>
        </div>
    </div>

    <!-- Footer dùng chung (đã bao gồm Bootstrap JS và Chart.js CDN) -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- Script nạp dữ liệu qua Fetch API /admin/api/revenue và render Chart.js -->
    <script>
        var chartInstance = null;

        function loadRevenueChart(year) {
            var selectedYear = year || '<c:out value="${currentYear}" default="2026" />';
            var apiUrl = '${pageContext.request.contextPath}/admin/api/revenue?year=' + selectedYear;

            fetch(apiUrl)
                .then(function(response) {
                    if (!response.ok) {
                        throw new Error('Lỗi tải dữ liệu API: ' + response.status);
                    }
                    return response.json();
                })
                .then(function(result) {
                    var labels = [];
                    var revenueData = [];
                    var appointmentData = [];

                    // Khởi tạo mảng 12 tháng mặc định
                    for (var m = 1; m <= 12; m++) {
                        labels.push('T' + m);
                        revenueData.push(0);
                        appointmentData.push(0);
                    }

                    if (result && result.data && Array.isArray(result.data)) {
                        result.data.forEach(function(item) {
                            var monthIdx = item.month - 1;
                            if (monthIdx >= 0 && monthIdx < 12) {
                                var rev = (item.revenue !== undefined) ? item.revenue : (item.total_revenue || 0);
                                var appt = (item.totalAppointments !== undefined) ? item.totalAppointments : (item.appointment_count || 0);
                                revenueData[monthIdx] = rev;
                                appointmentData[monthIdx] = appt;
                            }
                        });
                    }

                    renderChart(labels, revenueData, appointmentData);
                })
                .catch(function(err) {
                    console.error('Không thể nạp dữ liệu biểu đồ doanh thu:', err);
                    showToast('danger', 'Không thể nạp dữ liệu thống kê doanh thu.');
                });
        }

        function renderChart(labels, revenues, appointments) {
            var ctx = document.getElementById('revenueChart').getContext('2d');

            if (chartInstance) {
                chartInstance.destroy();
            }

            chartInstance = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Doanh thu (VNĐ)',
                            data: revenues,
                            backgroundColor: 'rgba(2, 132, 199, 0.75)',
                            borderColor: 'rgba(2, 132, 199, 1)',
                            borderWidth: 1.5,
                            borderRadius: 6,
                            yAxisID: 'y',
                            order: 2
                        },
                        {
                            label: 'Số ca khám bệnh',
                            data: appointments,
                            type: 'line',
                            borderColor: 'rgba(245, 158, 11, 1)',
                            backgroundColor: 'rgba(245, 158, 11, 0.15)',
                            pointBackgroundColor: 'rgba(245, 158, 11, 1)',
                            pointRadius: 4,
                            pointHoverRadius: 6,
                            borderWidth: 3,
                            tension: 0.35,
                            fill: true,
                            yAxisID: 'y1',
                            order: 1
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    interaction: {
                        mode: 'index',
                        intersect: false
                    },
                    plugins: {
                        legend: {
                            position: 'top',
                            labels: {
                                font: { family: 'Plus Jakarta Sans', size: 12, weight: '600' }
                            }
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    if (context.dataset.yAxisID === 'y') {
                                        return context.dataset.label + ': ' + new Intl.NumberFormat('vi-VN').format(context.raw) + ' VNĐ';
                                    }
                                    return context.dataset.label + ': ' + context.raw + ' ca';
                                }
                            }
                        }
                    },
                    scales: {
                        x: {
                            grid: { display: false }
                        },
                        y: {
                            type: 'linear',
                            display: true,
                            position: 'left',
                            title: {
                                display: true,
                                text: 'Doanh thu (VNĐ)',
                                font: { family: 'Plus Jakarta Sans', size: 12, weight: '600' }
                            },
                            ticks: {
                                callback: function(value) {
                                    return (value / 1000000).toLocaleString('vi-VN') + ' tr';
                                }
                            }
                        },
                        y1: {
                            type: 'linear',
                            display: true,
                            position: 'right',
                            grid: { drawOnChartArea: false },
                            title: {
                                display: true,
                                text: 'Số ca khám',
                                font: { family: 'Plus Jakarta Sans', size: 12, weight: '600' }
                            },
                            ticks: {
                                stepSize: 1,
                                precision: 0
                            }
                        }
                    }
                }
            });
        }

        // Tự động nạp dữ liệu khi tải trang
        document.addEventListener('DOMContentLoaded', function() {
            var currentYear = document.getElementById('yearSelect').value;
            loadRevenueChart(currentYear);
        });
    </script>
</body>
</html>
