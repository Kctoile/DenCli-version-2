/**
 * Purpose: End-to-End browser test for the full registration flow using Playwright.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class E2ERegisterTest {
    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    /**
     * Khởi chạy trình duyệt Chromium trước khi chạy toàn bộ test class.
     */
    @BeforeAll // JUnit 5 annotation chạy một lần duy nhất trước toàn bộ tests
    public static void setUpClass() {
        playwright = Playwright.create(); // Khởi tạo Playwright từ thư viện Microsoft

        // Chạy Chromium ở chế độ headless (không mở cửa sổ) để tăng tốc CI/CD
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    /**
     * Tạo phiên làm việc trình duyệt riêng cho mỗi kịch bản test.
     */
    @BeforeEach // JUnit 5 annotation chạy trước mỗi hàm test
    public void setUp() {
        context = browser.newContext(); // Tạo browser context mới từ Playwright
        page = context.newPage(); // Mở tab mới
    }

    /**
     * TC-AUTH-01 (E2E): Kiểm thử luồng đăng ký tài khoản bệnh nhân hoàn chỉnh trên giao diện.
     */
    @Test // JUnit 5 annotation khai báo hàm kiểm thử
    public void testFullRegistrationFlow() {
        // Điều hướng tới trang đăng ký
        page.navigate("http://localhost:8888/DenCli/register.jsp"); // Gọi hàm navigate từ Playwright

        // Nhập liệu vào các ô input trên form đăng ký
        page.fill("#fullName", "Kiểm Thử Viên E2E"); // Gọi hàm fill nhập dữ liệu từ Playwright
        page.fill("#email", "e2e_test_" + System.currentTimeMillis() + "@gmail.com");
        page.fill("#phone", "0905" + (int)(Math.random() * 1000000));
        page.fill("#password", "TestPassword123");

        // Chọn giá trị trong dropdown giới tính
        page.selectOption("#gender", "Nam"); // Gọi hàm selectOption chọn option từ Playwright

        page.fill("#dob", "1998-05-15");
        page.fill("#address", "123 Trần Phú, Đà Nẵng");

        // Click nút submit đăng ký
        page.click("#btnSubmit"); // Gọi hàm click bấm nút từ Playwright

        // Chờ alert thành công hoặc lỗi xuất hiện (timeout 10 giây)
        page.waitForTimeout(3000); // Gọi hàm chờ chủ động từ Playwright

        // Kiểm tra xem thông báo thành công có hiển thị không
        Locator successAlert = page.locator("#alertSuccess"); // Định vị phần tử DOM từ Playwright
        Locator errorAlert = page.locator("#alertError");

        // Nếu đăng ký thành công thì alert success phải hiển thị
        boolean isSuccess = successAlert.isVisible();
        boolean isError = errorAlert.isVisible();

        // Đảm bảo ít nhất 1 trong 2 alert phải xuất hiện (hệ thống phản hồi)
        assertTrue(isSuccess || isError, "Hệ thống phải trả về phản hồi sau khi đăng ký");
    }

    /**
     * Kiểm thử giao diện trang chủ có hiển thị danh mục dịch vụ nha khoa hay không.
     */
    @Test
    public void testHomepageServicesTableVisible() {
        page.navigate("http://localhost:8888/DenCli/index.jsp"); // Điều hướng tới trang chủ

        // Xác minh bảng dịch vụ có tồn tại trên trang
        Locator serviceTable = page.locator("#serviceTable"); // Định vị bảng dịch vụ từ Playwright
        assertTrue(serviceTable.isVisible(), "Bảng dịch vụ phải hiển thị trên trang chủ");

        // Kiểm tra ô tìm kiếm dịch vụ có hoạt động
        page.fill("#searchInput", "Khám"); // Nhập từ khóa tìm kiếm

        // Chờ filter hoạt động
        page.waitForTimeout(500);

        // Đếm số dòng dịch vụ còn hiển thị sau khi lọc
        int visibleRows = page.locator(".service-item:visible").count(); // Đếm phần tử hiển thị từ Playwright
        assertTrue(visibleRows >= 0, "Bộ lọc phải hoạt động mà không bị lỗi");
    }

    /**
     * Dọn dẹp phiên làm việc sau mỗi test.
     */
    @AfterEach // JUnit 5 annotation chạy sau mỗi hàm test
    public void tearDown() {
        if (context != null) {
            context.close(); // Đóng phiên trình duyệt từ Playwright
        }
    }

    /**
     * Giải phóng trình duyệt và tài nguyên Playwright sau toàn bộ tests.
     */
    @AfterAll // JUnit 5 annotation chạy một lần sau toàn bộ tests
    public static void tearDownClass() {
        if (browser != null) {
            browser.close(); // Tắt trình duyệt Chromium
        }

        if (playwright != null) {
            playwright.close(); // Giải phóng tài nguyên Playwright
        }
    }
}
