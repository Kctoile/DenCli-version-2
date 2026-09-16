<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kê đơn thuốc | DenCli</title>
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
                <div class="card card-custom bg-white p-4 mx-auto" style="max-width: 960px;">
                    <div class="border-bottom pb-3 mb-4 d-flex justify-content-between align-items-center">
                        <div>
                            <h3 class="fw-bold text-primary mb-1">💊 Kê đơn thuốc điều trị</h3>
                            <p class="text-muted mb-0">Chọn thuốc và số lượng, hệ thống sẽ tự động trừ tồn kho qua Transaction an toàn.</p>
                        </div>
                        <a href="${pageContext.request.contextPath}/doctor/examination" class="btn btn-outline-secondary btn-sm">
                            ← Quay lại Buồng khám
                        </a>
                    </div>

                    <!-- Thông báo phản hồi -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
                            <strong>⚠️ Có lỗi:</strong> <c:out value="${errorMessage}" />
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

                    <form id="prescriptionForm" action="${pageContext.request.contextPath}/doctor/prescription" method="POST">
                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label for="resultId" class="form-label fw-semibold">Mã hồ sơ bệnh án (Result ID) <span class="text-danger">*</span></label>
                                <input type="number" id="resultId" name="result_id" class="form-control" 
                                       placeholder="Nhập mã bệnh án vừa tạo sau khi khám (VD: 1, 2, 3...)" 
                                       value="${param.result_id}" required>
                                <div class="form-text">Mã Result ID được cấp sau khi bạn thực hiện chẩn đoán lâm sàng.</div>
                            </div>
                            <div class="col-md-6">
                                <label for="instructions" class="form-label fw-semibold">Lời dặn & Hướng dẫn sử dụng chung</label>
                                <input type="text" id="instructions" name="instructions" class="form-control" 
                                       placeholder="Uống sau bữa ăn, kiêng đồ uống có cồn, tái khám sau 7 ngày...">
                            </div>
                        </div>

                        <!-- Danh sách thuốc kê đơn -->
                        <div class="card bg-light border p-3 mb-4">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h5 class="fw-bold mb-0 text-dark">Danh sách thuốc chỉ định</h5>
                                <button type="button" class="btn btn-sm btn-outline-primary fw-semibold" onclick="addMedicineRow()">
                                    + Thêm dòng thuốc
                                </button>
                            </div>

                            <div class="table-responsive">
                                <table class="table table-bordered align-middle bg-white mb-0" id="medicineTable">
                                    <thead class="table-light">
                                        <tr>
                                            <th style="width: 40%;">Loại thuốc (Kho dược)</th>
                                            <th style="width: 15%;">Số lượng</th>
                                            <th style="width: 25%;">Liều dùng chỉ định</th>
                                            <th style="width: 15%; text-align: right;">Thành tiền</th>
                                            <th style="width: 5%; text-align: center;">Xóa</th>
                                        </tr>
                                    </thead>
                                    <tbody id="medicineTableBody">
                                        <!-- Hàng mặc định đầu tiên -->
                                        <tr class="med-row">
                                            <td>
                                                <select name="medicine_id" class="form-select med-select" required onchange="updateRowCalculations(this)">
                                                    <option value="" data-price="0">-- Chọn thuốc trong kho --</option>
                                                    <c:forEach items="${medicines}" var="m">
                                                        <option value="${m.medicineId}" data-price="${m.price}" data-unit="${m.unit}" data-stock="${m.stockQuantity}">
                                                            <c:out value="${m.medicineName}" /> (Kho: ${m.stockQuantity} ${m.unit} - <fmt:formatNumber value="${m.price}" pattern="#,##0" />đ)
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td>
                                                <input type="number" name="quantity" class="form-control med-qty" value="1" min="1" required oninput="updateRowCalculations(this)">
                                            </td>
                                            <td>
                                                <input type="text" name="dosage" class="form-control" placeholder="2 viên/ngày, sáng-tối" required>
                                            </td>
                                            <td class="text-end fw-semibold text-primary med-subtotal">
                                                0 VNĐ
                                            </td>
                                            <td class="text-center">
                                                <button type="button" class="btn btn-sm btn-outline-danger" onclick="removeMedicineRow(this)" title="Xóa dòng">✕</button>
                                            </td>
                                        </tr>
                                    </tbody>
                                    <tfoot>
                                        <tr class="table-light">
                                            <td colspan="3" class="text-end fw-bold text-uppercase fs-6">
                                                Tổng tiền thuốc tạm tính:
                                            </td>
                                            <td colspan="2" class="text-end fw-bold text-danger fs-5" id="grandTotalText">
                                                0 VNĐ
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>

                        <div class="d-flex justify-content-end gap-2">
                            <a href="${pageContext.request.contextPath}/doctor/examination" class="btn btn-outline-secondary px-4">Hủy bỏ</a>
                            <button type="submit" class="btn btn-primary px-5 fw-semibold">Xác nhận Lưu Đơn Thuốc</button>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </div>

    <!-- Template hàng thuốc ẩn để clone bằng JS -->
    <template id="rowTemplate">
        <tr class="med-row">
            <td>
                <select name="medicine_id" class="form-select med-select" required onchange="updateRowCalculations(this)">
                    <option value="" data-price="0">-- Chọn thuốc trong kho --</option>
                    <c:forEach items="${medicines}" var="m">
                        <option value="${m.medicineId}" data-price="${m.price}" data-unit="${m.unit}" data-stock="${m.stockQuantity}">
                            <c:out value="${m.medicineName}" /> (Kho: ${m.stockQuantity} ${m.unit} - <fmt:formatNumber value="${m.price}" pattern="#,##0" />đ)
                        </option>
                    </c:forEach>
                </select>
            </td>
            <td>
                <input type="number" name="quantity" class="form-control med-qty" value="1" min="1" required oninput="updateRowCalculations(this)">
            </td>
            <td>
                <input type="text" name="dosage" class="form-control" placeholder="2 viên/ngày, sáng-tối" required>
            </td>
            <td class="text-end fw-semibold text-primary med-subtotal">
                0 VNĐ
            </td>
            <td class="text-center">
                <button type="button" class="btn btn-sm btn-outline-danger" onclick="removeMedicineRow(this)" title="Xóa dòng">✕</button>
            </td>
        </tr>
    </template>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- JavaScript tính tạm tính thời gian thực (Client-side Realtime Calculation) -->
    <script>
        function formatVND(amount) {
            return new Intl.NumberFormat('vi-VN').format(amount) + ' VNĐ';
        }

        function updateRowCalculations(element) {
            var row = element.closest('.med-row');
            if (!row) return;

            var select = row.querySelector('.med-select');
            var qtyInput = row.querySelector('.med-qty');
            var subtotalCell = row.querySelector('.med-subtotal');

            var selectedOpt = select.options[select.selectedIndex];
            var price = selectedOpt ? parseFloat(selectedOpt.getAttribute('data-price') || 0) : 0;
            var qty = parseInt(qtyInput.value) || 0;

            var subtotal = price * qty;
            subtotalCell.textContent = formatVND(subtotal);

            recalculateGrandTotal();
        }

        function recalculateGrandTotal() {
            var rows = document.querySelectorAll('#medicineTableBody .med-row');
            var grandTotal = 0;

            rows.forEach(function(row) {
                var select = row.querySelector('.med-select');
                var qtyInput = row.querySelector('.med-qty');
                var selectedOpt = select.options[select.selectedIndex];
                var price = selectedOpt ? parseFloat(selectedOpt.getAttribute('data-price') || 0) : 0;
                var qty = parseInt(qtyInput.value) || 0;
                grandTotal += (price * qty);
            });

            document.getElementById('grandTotalText').textContent = formatVND(grandTotal);
        }

        function addMedicineRow() {
            var template = document.getElementById('rowTemplate');
            var clone = template.content.cloneNode(true);
            document.getElementById('medicineTableBody').appendChild(clone);
            recalculateGrandTotal();
        }

        function removeMedicineRow(btn) {
            var rows = document.querySelectorAll('#medicineTableBody .med-row');
            if (rows.length <= 1) {
                alert('Đơn thuốc cần có ít nhất một loại thuốc.');
                return;
            }
            var row = btn.closest('.med-row');
            row.remove();
            recalculateGrandTotal();
        }
    </script>
</body>
</html>
