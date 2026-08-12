/**
 * Purpose: End-to-End browser UI automation test using Microsoft Playwright.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class E2EBookingTest {
    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    /**
     * Thiết lập môi trường và khởi chạy trình duyệt ảo Chromium trước khi chạy toàn bộ test class.
     */
    @BeforeAll // JUnit 5 annotation chạy một lần duy nhất trước toàn bộ các test cases
    public static void setUpClass() {
        playwright = Playwright.create(); // Khởi tạo môi trường Playwright từ thư viện Microsoft
        
        // Khởi chạy Chromium không hiển thị giao diện để tối ưu tốc độ chạy test (headless)
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true)); // Gọi hàm khởi chạy trình duyệt của Playwright
    }

    /**
     * Thiết lập phiên ngữ cảnh duyệt web riêng biệt cho mỗi kịch bản test.
     */
    @BeforeEach // JUnit 5 annotation chạy trước mỗi hàm test đơn lẻ
    public void setUp() {
        context = browser.newContext(); // Tạo context mới độc lập phiên làm việc qua Playwright
        page = context.newPage(); // Mở một tab trang mới
    }

    /**
     * Kịch bản kiểm thử tự động điều hướng từ trang chủ công cộng sang trang đăng nhập.
     */
    @Test // JUnit 5 annotation khai báo hàm kiểm thử
    public void testHomepageRedirectToLogin() {
        // Điều hướng tới trang chủ của ứng dụng nha khoa
        page.navigate("http://localhost:8888/DenCli/index.jsp"); // Gọi hàm navigate đi tới URL từ Playwright

        // Kiểm tra xem tiêu đề trang hoặc nội dung Hero Banner có chứa tên phòng khám không
        String content = page.textContent("body"); // Trích xuất toàn bộ văn bản của thẻ body từ Playwright
        assertTrue(content.contains("NHA KHOA CÔNG NGHỆ CAO DENCLI")); // Xác nhận thông tin

        // Giả lập tương tác click vào liên kết Đăng nhập
        page.click("text=Đăng nhập"); // Gọi hàm click tìm theo text từ Playwright

        // Xác minh rằng URL hiện tại đã trỏ đến trang đăng nhập login.jsp
        String currentUrl = page.url(); // Lấy địa chỉ URL hiện tại của tab từ Playwright
        assertTrue(currentUrl.contains("login.jsp"));
    }

    /**
     * Dọn dẹp phiên ngữ cảnh duyệt web sau mỗi kịch bản test.
     */
    @AfterEach // JUnit 5 annotation chạy sau mỗi hàm test đơn lẻ
    public void tearDown() {
        if (context != null) {
            context.close(); // Đóng phiên làm việc của trình duyệt qua Playwright
        }
    }

    /**
     * Giải phóng tài nguyên và tắt trình duyệt sau khi chạy xong tất cả các test cases.
     */
    @AfterAll // JUnit 5 annotation chạy duy nhất một lần sau cùng
    public static void tearDownClass() {
        if (browser != null) {
            browser.close(); // Tắt hoàn toàn trình duyệt Chromium
        }
        if (playwright != null) {
            playwright.close(); // Giải phóng tài nguyên tiến trình Playwright
        }
    }
}
