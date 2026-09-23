package com.devjava.dencli.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UI Automation Test - Luồng Đăng nhập Hệ thống")
public class LoginAutomationTest {

    private static WebDriver driver;
    private static final String BASE_URL = "http://localhost:8080/DenCli";

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Chạy chế độ headless không bật GUI Chrome
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @AfterAll
    static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Test 01: Đăng nhập thành công với tài khoản Admin")
    void testLoginSuccess() {
        driver.get(BASE_URL + "/login.jsp");

        WebElement usernameInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameInput.sendKeys("admin@dencli.com");
        passwordInput.sendKeys("admin123");
        submitBtn.click();

        // Xác minh chuyển hướng thành công tới Admin Dashboard
        assertTrue(driver.getCurrentUrl().contains("/admin/dashboard"));
    }
}
