# TÀI LIỆU HƯỚNG DẪN THIẾT LẬP MÔI TRƯỜNG PHÁT TRIỂN - HỆ THỐNG DENCLI (v1.0)

Tài liệu này hướng dẫn chi tiết các bước cài đặt và cấu hình môi trường phát triển (Development Environment Setup) cho dự án Web Nha khoa **DenCli**, đảm bảo toàn bộ thành viên trong đội ngũ phát triển sử dụng chung một cấu hình chuẩn, hạn chế tối đa lỗi môi trường ("works on my machine").

---

## 1. PHẦN MỀM YÊU CẦU & PHIÊN BẢN CHUẨN (STACK SPECIFICATIONS)

| Phần mềm / Công cụ | Phiên bản yêu cầu | Vai trò trong dự án |
| :--- | :--- | :--- |
| **Java Development Kit (JDK)** | OpenJDK 17 LTS | Nền tảng thực thi ngôn ngữ Java |
| **Apache Tomcat** | 10.1.x (Hỗ trợ Servlet 6.0 / JSP 3.1) | Web Server / Servlet Container |
| **Database Server** | Microsoft SQL Server 2019 hoặc mới hơn | Hệ quản trị cơ sở dữ liệu quan hệ |
| **Build Tool (Tùy chọn)** | Maven 3.8+ (hoặc Dynamic Web Project thuần) | Quản lý thư viện phụ thuộc (Dependencies) |
| **IDE Khuyến nghị** | IntelliJ IDEA Ultimate hoặc Eclipse for Enterprise | Công cụ lập trình tích hợp |

---

## 2. BƯỚC 1: THIẾT LẬP JDK 17 & BIẾN MÔI TRƯỜNG

1. **Tải xuống & Cài đặt:** Tải bản phân phối OpenJDK 17 (khuyến nghị dùng Eclipse Temurin hoặc Amazon Corretto) phù hợp với hệ điều hành (Windows/macOS/Linux).
2. **Cấu hình Biến môi trường (Environment Variables) trên Windows:**
   - Tạo biến `JAVA_HOME` trỏ tới thư mục cài đặt JDK (Ví dụ: `C:\Program Files\Eclipse Adoptium\jdk-17.x.x`).
   - Thêm `%JAVA_HOME%in` vào biến hệ thống `Path`.
3. **Kiểm tra cài đặt:**
   - Mở Terminal/Command Prompt và chạy lệnh:
     ```bash
     java -version
     javac -version
     ```
   - Đảm bảo cả hai lệnh đều trả về đúng thông tin phiên bản Java 17.

---

## 3. BƯỚC 2: CÀI ĐẶT DATABASE & IMPORT SCHEMA

1. **Khởi động SQL Server:** Đảm bảo dịch vụ SQL Server đang chạy (hỗ trợ SQL Server Authentication).
2. **Import cấu trúc dữ liệu:**
   - Mở công cụ **SQL Server Management Studio (SSMS)**.
   - Kết nối tới Database Server của bạn.
   - Mở tệp tin `db_optimized.sql` vừa được tải về từ thư mục artifacts.
   - Chạy lệnh (`Execute` hoặc nhấn `F5`) để tự động khởi tạo cơ sở dữ liệu `[Dental]`, tạo 11 bảng nghiệp vụ chuẩn hóa, thiết lập index và cài đặt các ràng buộc dữ liệu thông minh.

---

## 4. BƯỚC 3: CẤU HÌNH THƯ VIỆN PHỤ THUỘC (DEPENDENCIES)

Dự án sử dụng cơ chế Servlet/JSP thuần không dùng Spring Boot, do đó bạn cần nạp các thư viện JDBC Driver và Servlet API thủ công hoặc qua Maven.

### Cách A: Sử dụng cấu hình Maven (`pom.xml`)
Nếu dự án khởi tạo dưới dạng dự án Maven, hãy bổ sung các dependency sau vào tệp `pom.xml`:

```xml
<dependencies>
    <!-- Servlet API (Tomcat 10+ dùng jakarta.servlet) -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>

    <!-- JSP API -->
    <dependency>
        <groupId>jakarta.servlet.jsp</groupId>
        <artifactId>jakarta.servlet.jsp-api</artifactId>
        <version>3.1.1</version>
        <scope>provided</scope>
    </dependency>

    <!-- Microsoft SQL Server JDBC Driver -->
    <dependency>
        <groupId>com.microsoft.sqlserver</groupId>
        <artifactId>mssql-jdbc</artifactId>
        <version>12.2.0.jre11</version>
    </dependency>

    <!-- Thư viện mã hóa BCrypt bảo mật mật khẩu -->
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>
</dependencies>
```

### Cách B: Thêm file thư viện thủ công (`.jar`)
Nếu sử dụng dự án Dynamic Web Project truyền thống của Eclipse, hãy tải và đặt các tệp tin `.jar` sau vào thư mục `src/main/webapp/WEB-INF/lib/`:
1. `mssql-jdbc-12.2.0.jre11.jar` (Bộ nạp kết nối SQL Server).
2. `jbcrypt-0.4.jar` (Thư viện mã hóa mật khẩu).

---

## 5. BƯỚC 4: CẤU HÌNH KẾT NỐI DATABASE (JDBC UTILS)

Tạo lớp tiện ích `DBContext.java` hoặc `SQLServerConnection.java` trong gói nguồn `com.dencli.utils` để quản lý phiên kết nối:

```java
package com.dencli.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    private static final String HOST = "localhost";
    private static final String PORT = "1433"; // Cổng mặc định của SQL Server
    private static final String DB_NAME = "Dental";
    private static final String USER = "sa"; // Tài khoản SQL Server của bạn
    private static final String PASSWORD = "YourStrongPassword123"; // Mật khẩu của bạn

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        // Khai báo Driver kết nối của Microsoft SQL Server
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        // Chuỗi kết nối JDBC bypass SSL certificate cho môi trường localhost
        String url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;", 
                HOST, PORT, DB_NAME);
        
        return DriverManager.getConnection(url, USER, PASSWORD);
    }

    // Đoạn code kiểm tra kết nối nhanh tại console
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("Chúc mừng! Kết nối cơ sở dữ liệu [Dental] thành công.");
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }
}
```

---

## 6. BƯỚC 5: TÍCH HỢP APACHE TOMCAT VÀO IDE

1. **Tải Apache Tomcat:** Tải bản phân phối Tomcat 10.1.x (định dạng tệp zip/tar.gz) và giải nén vào ổ đĩa làm việc của bạn (ví dụ: `C:\apache-tomcat-10.1.x`).
2. **Cấu hình trên IntelliJ IDEA:**
   - Vào `Run` -> `Edit Configurations...` -> Nhấp biểu tượng dấu cộng `+` -> Chọn `Tomcat Server` -> Chọn `Local`.
   - Tại mục **Application Server**, trỏ tới đường dẫn thư mục cài đặt Tomcat đã giải nén.
   - Tại thẻ **Deployment**, nhấp dấu cộng `+` -> Chọn `Artifact...` -> Chọn bản build có hậu tố `:war` hoặc `:war exploded` của dự án DenCli.
   - Nhấp `Apply` và chạy thử máy chủ bằng cách nhấp nút `Run` hoặc `Debug` (biểu tượng con bọ).

---
*Hết tài liệu Hướng dẫn thiết lập môi trường.*