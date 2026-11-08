package com.devjava.dencli.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * E2E UI Automation Test Suite cho màn hình Đặt Lịch Khám (customer/book.jsp).
 * Bao phủ:
 * - Kịch bản 1: Live Estimate Calculator (Tính tạm tính viện phí trực quan khi chọn dịch vụ)
 * - Kịch bản 2: Tương tác Lưới Slot Giờ (Slot Grid Selection & Quick Date buttons)
 * - Kịch bản 3: Chặn lỗi Form Validation khi thiếu thông tin bắt buộc
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("E2E UI Automation Test - Booking Flow & Live Calculator")
class BookingFlowAutomationTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8888/DenCli";
    private static final String TEST_PATIENT_EMAIL = "patient1@gmail.com";
    private static final String TEST_PATIENT_PASS = "123";

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1280,800");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        wait = new WebDriverWait(driver, Duration.ofSeconds(8));

        // Đăng nhập tài khoản Bệnh nhân để thiết lập Session Cookie trước khi test
        driver.get(BASE_URL + "/login.jsp");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("loginForm")));
        driver.findElement(By.name("email_or_phone")).sendKeys(TEST_PATIENT_EMAIL);
        driver.findElement(By.name("password")).sendKeys(TEST_PATIENT_PASS);
        driver.findElement(By.id("btnSubmit")).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login.jsp")));
    }

    @AfterAll
    static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("TC-BOOKING-UI-01: Kiểm tra tải trang Đặt lịch và hiển thị các thành phần chính")
    void testBookingPageLoadsSuccessfully() {
        driver.get(BASE_URL + "/customer/book");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appointmentBookingForm")));

        assertTrue(driver.getTitle().contains("Đặt Lịch Khám") || driver.getTitle().contains("DenCli"),
                "Tiêu đề trang đặt lịch không đúng");
        assertTrue(driver.findElement(By.id("doctorId")).isDisplayed(), "Dropdown Bác sĩ không hiển thị");
        assertTrue(driver.findElement(By.id("appointmentDate")).isDisplayed(), "Input Ngày khám không hiển thị");
        assertTrue(driver.findElements(By.className("slot-card")).size() >= 8, "Lưới slot khám phải có ít nhất 8 khung giờ");
        assertTrue(driver.findElement(By.id("totalEstimatePrice")).isDisplayed(), "Hộp tạm tính viện phí không hiển thị");
    }

    @Test
    @Order(2)
    @DisplayName("TC-BOOKING-UI-02: Kiểm tra tương tác Quick Date Buttons và Lưới Slot Giờ")
    void testQuickDateAndSlotGridInteraction() {
        driver.get(BASE_URL + "/customer/book");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appointmentBookingForm")));

        // 1. Thử click nút "Ngày mai"
        List<WebElement> quickDateBtns = driver.findElements(By.className("quick-date-btn"));
        assertTrue(quickDateBtns.size() >= 2, "Cần có ít nhất nút Hôm nay và Ngày mai");

        WebElement btnTomorrow = quickDateBtns.get(1); // Nút "Ngày mai"
        btnTomorrow.click();
        assertTrue(btnTomorrow.getDomAttribute("class").contains("active"), "Nút Ngày mai phải có class active sau khi click");

        // 2. Thử chọn Slot "09:00"
        WebElement slot9 = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".slot-card[data-slot='09:00']")));
        slot9.click();

        assertTrue(slot9.getDomAttribute("class").contains("selected"), "Slot 09:00 phải có class selected sau khi click");

        // Kiểm tra hidden select đã được đồng bộ giá trị 09:00
        WebElement hiddenTimeSelect = driver.findElement(By.id("appointmentTime"));
        assertEquals("09:00", hiddenTimeSelect.getDomProperty("value"), "Hidden select appointmentTime phải được đồng bộ thành 09:00");
    }

    @Test
    @Order(3)
    @DisplayName("TC-BOOKING-UI-03: Kiểm tra Hộp Tạm Tính Viện Phí nhảy số tự động (Live Calculator)")
    void testLiveEstimateCalculator() {
        driver.get(BASE_URL + "/customer/book");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appointmentBookingForm")));

        WebElement totalElem = driver.findElement(By.id("totalEstimatePrice"));
        assertEquals("0 đ", totalElem.getText().trim(), "Giá khởi điểm khi chưa chọn dịch vụ phải là 0 đ");

        // Tích chọn checkbox dịch vụ đầu tiên
        List<WebElement> checkboxes = driver.findElements(By.className("service-checkbox"));
        if (!checkboxes.isEmpty()) {
            WebElement firstSvc = checkboxes.get(0);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstSvc);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstSvc);

            // Giá phải khác 0 đ
            String newPrice = totalElem.getText().trim();
            assertNotEquals("0 đ", newPrice, "Tổng tiền tạm tính phải được cập nhật khi chọn dịch vụ");
            assertTrue(newPrice.contains("đ"), "Đơn vị tiền tệ phải hiển thị đúng (đ)");

            // Bỏ chọn lại -> quay về 0 đ
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstSvc);
            assertEquals("0 đ", totalElem.getText().trim(), "Khi bỏ chọn dịch vụ thì tổng tiền phải về 0 đ");
        }
    }

    @Test
    @Order(4)
    @DisplayName("TC-BOOKING-UI-04: Kiểm tra Form Validation khi chưa chọn Bác sĩ mà bấm Đặt lịch")
    void testFormValidationMissingDoctorShowsErrorAlert() {
        driver.get(BASE_URL + "/customer/book");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appointmentBookingForm")));

        // Bỏ trống bác sĩ, bấm submit
        WebElement btnSubmit = driver.findElement(By.id("btnSubmitBooking"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnSubmit);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnSubmit);

        // Chờ alert lỗi xuất hiện và hiển thị
        WebElement alertBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bookingAlert")));
        WebElement alertText = driver.findElement(By.id("bookingAlertText"));

        assertTrue(alertBox.isDisplayed(), "Hộp cảnh báo lỗi bookingAlert phải hiển thị khi thiếu Bác sĩ");
        assertTrue(alertText.getText().contains("Bác sĩ"), "Nội dung cảnh báo phải nhắc người dùng chọn Bác sĩ");
    }
}

