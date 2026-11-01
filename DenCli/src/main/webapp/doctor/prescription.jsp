<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kê Đơn Thuốc Điện Tử | DenCli Dental Clinic</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Google Fonts: Plus Jakarta Sans & Figtree -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Figtree:wght@400;500;600;700&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dencli-theme.css?v=6">
</head>

<body class="d-flex flex-column min-vh-100" style="background-color: #F8FAFC;">

    <!-- Header dùng chung -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="container-fluid flex-grow-1">
        <div class="row min-vh-100">
            <!-- Sidebar điều hướng theo Role -->
            <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

            <!-- Nội dung chính -->
            <main class="col-12 col-md-9 col-lg-10 p-4 p-lg-5">
                <div class="dencli-card bg-white shadow-sm rounded-4 border-0 p-4 p-md-5 mx-auto" style="max-width: 980px;">
                    
                    <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4 pb-3 border-bottom">
                        <div>
                            <div class="d-flex align-items-center gap-2 mb-1">
                                <span class="fs-4">💊</span>
                                <h3 class="fw-bold text-dark mb-0">Kê Đơn Thuốc Điều Trị Điện Tử</h3>
                            </div>
                            <p class="text-muted small mb-0">
                                Lựa chọn thuốc từ kho dược phòng khám, chỉ định liều lượng và tự động khấu trừ tồn kho sau khi lưu.
                            </p>
                        </div>
                        <a href="${pageContext.request.contextPath}/doctor/examination" class="btn btn-outline-secondary btn-sm rounded-pill px-3 py-2 fw-semibold">
                            ← Quay lại Buồng khám
                        </a>
                    </div>

                    <!-- Thông báo phản hồi -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show mb-4 rounded-3 border-0 shadow-sm" role="alert">
                            <strong>⚠️ Có lỗi xảy ra:</strong>
                            <c:out value="${errorMessage}" />
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.successMessage}">
                        <div class="alert alert-success alert-dismissible fade show mb-4 rounded-3 border-0 shadow-sm" role="alert">
                            <strong>✅ Thành công:</strong>
                            <c:out value="${sessionScope.successMessage}" />
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <c:remove var="successMessage" scope="session" />
                    </c:if>

                    <form id="prescriptionForm" action="${pageContext.request.contextPath}/doctor/prescription" method="POST">
                        <input type="hidden" name="_csrf" value="${csrfToken}">
                        
                        <div class="row g-3 mb-4">
                            <div class="col-md-5">
                                <label for="resultId" class="form-label fw-bold text-dark">
                                    Mã hồ sơ bệnh án (Result ID) <span class="text-danger">*</span>
                                </label>
                                <input type="number" id="resultId" name="result_id" class="form-control form-control-lg rounded-3" placeholder="Nhập mã bệnh án (VD: 1, 2, 3...)" value="<c:out value='${param.result_id}' />" required>
                                <div class="form-text text-muted">Mã Result ID được cấp tự động sau khi Bác sĩ ghi nhận kết quả khám.</div>
                            </div>
                            <div class="col-md-7">
                                <label for="instructions" class="form-label fw-bold text-dark">
                                    Lời dặn & Hướng dẫn sử dụng chung cho bệnh nhân
                                </label>
                                <input type="text" id="instructions" name="instructions" class="form-control form-control-lg rounded-3" placeholder="Ví dụ: Uống sau bữa ăn no 30 phút, kiêng đồ uống có cồn, tái khám sau 7 ngày...">
                                <div class="form-text text-muted">Lời dặn sẽ được in trực tiếp lên đơn thuốc điện tử của bệnh nhân.</div>
                            </div>
                        </div>

                        <!-- Danh sách thuốc chỉ định -->
                        <div class="card bg-light border-0 rounded-4 p-4 mb-4">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <div>
                                    <h5 class="fw-bold mb-0 text-dark">Danh sách thuốc chỉ định</h5>
                                    <small class="text-muted">Đơn giá và tổng tiền sẽ được tính tự động dựa trên danh mục dược phẩm.</small>
                                </div>
                                <button type="button" class="btn btn-sm btn-primary rounded-pill px-3 fw-semibold shadow-sm" style="background: #0891B2; border-color: #0891B2;" onclick="addMedicineRow()">
                                    + Thêm dòng thuốc
                                </button>
                            </div>

                            <div class="table-responsive">
                                <table class="table table-bordered align-middle bg-white mb-0 rounded-3 overflow-hidden" id="medicineTable">
                                    <thead class="table-light">
                                        <tr>
                                            <th style="width: 40%; font-size: 0.85rem; text-transform: uppercase;">Loại thuốc trong kho</th>
                                            <th style="width: 15%; font-size: 0.85rem; text-transform: uppercase;">Số lượng</th>
                                            <th style="width: 25%; font-size: 0.85rem; text-transform: uppercase;">Liều dùng chỉ định</th>
                                            <th style="width: 15%; text-align: right; font-size: 0.85rem; text-transform: uppercase;">Thành tiền</th>
                                            <th style="width: 5%; text-align: center; font-size: 0.85rem; text-transform: uppercase;">Xóa</th>
                                        </tr>
                                    </thead>
                                    <tbody id="medicineTableBody">
                                        <!-- Hàng mặc định đầu tiên -->
                                        <tr class="med-row">
                                            <td>
                                                <label class="visually-hidden" for="medicine-0">Loại thuốc</label>
                                                <select id="medicine-0" name="medicine_id" class="form-select med-select rounded-3" required onchange="updateRowCalculations(this)">
                                                    <option value="" data-price="0">-- Chọn thuốc trong kho --</option>
                                                    <c:forEach items="${medicines}" var="m">
                                                        <option value="${m.medicineId}" data-price="${m.price}" data-unit="${empty m.unit ? 'viên' : m.unit}" data-stock="${m.stockQuantity}">
                                                            <c:out value="${m.medicineName}" /> (Kho: ${m.stockQuantity} ${empty m.unit ? 'viên' : m.unit} - <fmt:formatNumber value="${m.price}" pattern="#,##0" />đ)
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td>
                                                <label class="visually-hidden" for="quantity-0">Số lượng</label>
                                                <input type="number" id="quantity-0" name="quantity" class="form-control med-qty rounded-3" value="1" min="1" required oninput="updateRowCalculations(this)">
                                            </td>
                                            <td>
                                                <label class="visually-hidden" for="dosage-0">Liều dùng chỉ định</label>
                                                <input type="text" id="dosage-0" name="dosage" class="form-control rounded-3" placeholder="2 viên/ngày, sáng-tối" required>
                                            </td>
                                            <td class="text-end fw-bold text-primary med-subtotal">
                                                0 VNĐ
                                            </td>
                                            <td class="text-center">
                                                <button type="button" class="btn btn-sm btn-outline-danger rounded-circle" onclick="removeMedicineRow(this)" title="Xóa dòng">✕</button>
                                            </td>
                                        </tr>
                                    </tbody>
                                    <tfoot>
                                        <tr class="table-light">
                                            <td colspan="3" class="text-end fw-bold text-uppercase fs-6">
                                                Tổng tiền thuốc tạm tính:
                                            </td>
                                            <td colspan="2" class="text-end fw-bold fs-5" style="color: #0891B2;" id="grandTotalText">
                                                0 VNĐ
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>

                        <div class="d-flex justify-content-end gap-2 pt-2 border-top">
                            <a href="${pageContext.request.contextPath}/doctor/examination" class="btn btn-outline-secondary px-4 rounded-pill fw-semibold">Hủy bỏ</a>
                            <button type="submit" class="btn btn-primary px-5 py-2 fw-bold rounded-pill shadow-sm" style="background: #0891B2; border-color: #0891B2;">
                                Xác Nhận Lưu Đơn Thuốc
                            </button>
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
                <label class="visually-hidden" for="medicine-template">Loại thuốc</label>
                <select id="medicine-template" name="medicine_id" class="form-select med-select rounded-3" required onchange="updateRowCalculations(this)">
                    <option value="" data-price="0">-- Chọn thuốc trong kho --</option>
                    <c:forEach items="${medicines}" var="m">
                        <option value="${m.medicineId}" data-price="${m.price}" data-unit="${empty m.unit ? 'viên' : m.unit}" data-stock="${m.stockQuantity}">
                            <c:out value="${m.medicineName}" /> (Kho: ${m.stockQuantity} ${empty m.unit ? 'viên' : m.unit} - <fmt:formatNumber value="${m.price}" pattern="#,##0" />đ)
                        </option>
                    </c:forEach>
                </select>
            </td>
            <td>
                <label class="visually-hidden" for="quantity-template">Số lượng</label>
                <input type="number" id="quantity-template" name="quantity" class="form-control med-qty rounded-3" value="1" min="1" required oninput="updateRowCalculations(this)">
            </td>
            <td>
                <label class="visually-hidden" for="dosage-template">Liều dùng chỉ định</label>
                <input type="text" id="dosage-template" name="dosage" class="form-control rounded-3" placeholder="2 viên/ngày, sáng-tối" required>
            </td>
            <td class="text-end fw-bold text-primary med-subtotal">
                0 VNĐ
            </td>
            <td class="text-center">
                <button type="button" class="btn btn-sm btn-outline-danger rounded-circle" onclick="removeMedicineRow(this)" title="Xóa dòng">✕</button>
            </td>
        </tr>
    </template>

    <!-- Footer dùng chung -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- JavaScript tính tạm tính thời gian thực -->
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

        var nextMedicineRowIndex = 1;

        function addMedicineRow() {
            var template = document.getElementById('rowTemplate');
            var clone = template.content.cloneNode(true);
            var index = nextMedicineRowIndex++;
            ['medicine', 'quantity', 'dosage'].forEach(function(name) {
                var control = clone.querySelector('#' + name + '-template');
                var id = name + '-' + index;
                control.id = id;
                clone.querySelector('label[for="' + name + '-template"]').htmlFor = id;
            });
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
