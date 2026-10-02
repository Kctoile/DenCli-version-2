# DenCli-version-2 — Ghi chú vấn đề hiện tại & Root Cause

> Ngày ghi: 24/09/2026
> Máy: Windows, Tomcat 10.1 chạy dạng **Windows Service** (Tomcat10.exe), port **8888**
> Project: `C:\Users\ad\OneDrive\Desktop\DenCli` (repo ngoài) → Maven project trong `DenCli\`

---

## 1. Vấn đề đang gặp (hiện tượng)

- Trang chủ `http://localhost:8888/DenCli/index.jsp` hiển thị **màn hình đen** (React không mount, `<div id="root">` rỗng).
- Console F12 trên trang chủ báo lỗi đỏ duy nhất:
  ```
  Failed to load resource: the server responded with a status of 404 ()
  → index-CiHI0Cxn.js:1
  ```
- Trong khi đó trang `/DenCli/login` **render hoàn hảo** (tiếng Việt, hoạt động bình thường).

## 2. Chuỗi điều tra & những gì đã xác nhận

| # | Kiểm chứng | Kết quả |
|---|---|---|
| 1 | Tìm file bundle cũ `index-BRT5tyk-.js` toàn ổ C | **Không tồn tại** → webapp không deploy từ `webapps\` |
| 2 | `conf\Catalina\localhost\DenCli.xml` | Có! → `docBase="C:\Users\ad\OneDrive\Desktop\DenCli\DenCli\target\DenCli-1.0-SNAPSHOT"` — **thư mục deploy thật** |
| 3 | `dir target\DenCli-1.0-SNAPSHOT\assets` | **Có file** `index-CiHI0Cxn.js` (279.482 bytes) và `index-Cq-uH76G.css` |
| 4 | `index.jsp` trong source | Đã trỏ đúng `${ctx}/assets/index-CiHI0Cxn.js` |
| 5 | Mở `/DenCli/assets/index-Cq-uH76G.css` | Không 404 (assets folder hoạt động) |
| 6 | Mở `/DenCli/login` bằng chính bundle mới | Render OK → bundle JS không hỏng |
| 7 | Reload trang chủ Ctrl+Shift+R, xem Console | **Vẫn 404 `index-CiHI0Cxn.js`** ❌ |

## 3. Root Cause (kết luận)

**Paradox 404: file tồn tại trên đĩa nhưng Tomcat vẫn trả 404.**

Nguyên nhân gốc: **Tomcat Windows Service giữ trạng thái cũ** — sau khi copy file mới vào `docBase`, service không tự nhận diện (không có `reloadable` với file tĩnh thay tên bundle; hoặc Tomcat đã cache resource/permission của file trong OneDrive bị cloud-only). Do đó:

1. `index.jsp` mới được đọc (title tiếng Việt đúng, nó request đúng `index-CiHI0Cxn.js`) ✅
2. Nhưng request GET `/DenCli/assets/index-CiHI0Cxn.js` → Tomcat trả **404** ❌ dù file nằm đúng chỗ.
3. React không tải được bundle → `#root` rỗng → màn hình đen.
4. Trang `/login` dùng **file JSP khác** (login.jsp) trỏ bundle khác/đã cache → không bị ảnh hưởng.

**Yếu tố rủi ro cộng hưởng**: thư mục project nằm trong **OneDrive** — file mới copy có thể đang ở trạng thái cloud-only/đồng bộ chậm, Tomcat (chạy như service với quyền SYSTEM) có thể không đọc được file mới.

## 4. Cách xử lý (theo thứ tự)

1. **Restart Tomcat service** (bắt buộc, mong đợi cao nhất giúp được):
   ```cmd
   net stop Tomcat10
   net start Tomcat10
   ```
   → mở lại `index.jsp` + Ctrl+Shift+R.
2. Nếu vẫn 404 → **tắt OneDrive sync tạm thời** (Pause syncing) hoặc move project ra ngoài OneDrive (vd. `C:\dev\DenCli`), copy lại 2 file bundle, restart Tomcat.
3. Nếu vẫn 404 → kiểm tra `DenCli.xml` có `antiResourceLocking="false"`; hoặc xóa Tomcat work cache:
   ```cmd
   rmdir /s /q "C:\Program Files\Apache Software Foundation\Tomcat 10.1\work\Catalina\localhost\DenCli"
   ```
4. Cuối cùng: rebuild backend (Clean and Build) + Run project từ NetBeans để deploy cả `SessionServlet.java` mới (chưa có trong target → `/auth/session` chưa tồn tại).

## 5. Việc còn treo khác (không liên quan 404)

- `SessionServlet.java` (endpoint `/auth/session`, trả user đăng nhập cho Navbar hiển thị "Xin chào, {tên}") **chỉ mới có trong source, chưa build/deploy**. Navbar có `.catch()` guard nên không crash app, nhưng cho đến khi build backend, trạng thái đăng nhập trên trang chủ sẽ không hiển thị.
- Yêu cầu **thống nhất UI/UX giữa các trang JSP**: hiện mỗi trang JSP (index, login, register, book, admin, doctor, staff…) tự import CSS/tự dựng layout riêng → style không đồng nhất. Cần chốt **1 design system duy nhất** (cùng palette `#060C14` nền tối + `#0BB8B8` accent, font Plus Jakarta Sans, cùng Navbar/Footer components) áp dụng cho mọi trang — khuyến nghị: chuyển toàn bộ trang JSP sang route của React SPA, hoặc tối thiểu dùng chung 1 `head.jsp` include cùng CSS bundle + cấu trúc layout.

## 6. Bài học đặt lại quy trình deploy (chống lặp lại)

Luôn deploy vào **`target\DenCli-1.0-SNAPSHOT`** (docBase thật), KHÔNG copy vào `Tomcat\webapps\` (sẽ bị bỏ qua vì có Context XML riêng trong `conf\Catalina\localhost\DenCli.xml`).
