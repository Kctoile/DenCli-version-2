package com.devjava.dencli.dao;

import com.devjava.dencli.dao.impl.MedicineDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("ISTQB Boundary Value Analysis (BVA) - Kiểm thử Giá trị biên Tồn kho Thuốc")
class MedicineInventoryBVATest {

    private MedicineDAOImpl medicineDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    private static final int MEDICINE_ID = 101;
    private static final int INITIAL_STOCK = 50;

    @BeforeEach
    void setUp() throws SQLException {
        medicineDAO = new MedicineDAOImpl();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    @DisplayName("BVA: Kiểm thử trừ kho biên dưới hợp lệ (Min valid = 1)")
    void testDeductStockMinValidBoundary() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = medicineDAO.deductStockQuantity(MEDICINE_ID, 1, mockConnection);

        assertTrue(result, "Trừ kho số lượng 1 phải thành công");
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setInt(2, MEDICINE_ID);
        verify(mockPreparedStatement).setInt(3, 1);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    @DisplayName("BVA: Kiểm thử trừ kho giá trị thông thường (Nominal = 25)")
    void testDeductStockNominalValue() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = medicineDAO.deductStockQuantity(MEDICINE_ID, 25, mockConnection);

        assertTrue(result, "Trừ kho số lượng 25 phải thành công");
        verify(mockPreparedStatement).setInt(1, 25);
        verify(mockPreparedStatement).setInt(2, MEDICINE_ID);
        verify(mockPreparedStatement).setInt(3, 25);
    }

    @Test
    @DisplayName("BVA: Kiểm thử trừ kho biên trên hợp lệ (Max valid = stock_quantity = 50)")
    void testDeductStockMaxValidBoundary() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = medicineDAO.deductStockQuantity(MEDICINE_ID, INITIAL_STOCK, mockConnection);

        assertTrue(result, "Trừ kho đúng bằng tồn kho hiện tại (50) phải thành công");
        verify(mockPreparedStatement).setInt(1, INITIAL_STOCK);
        verify(mockPreparedStatement).setInt(2, MEDICINE_ID);
        verify(mockPreparedStatement).setInt(3, INITIAL_STOCK);
    }

    @Test
    @DisplayName("BVA: Kiểm thử vượt biên trên (stock_quantity + 1 = 51) -> DB không cập nhật dòng nào (0 rows)")
    void testDeductStockExceedsLimit() throws SQLException {
        int exceedQuantity = INITIAL_STOCK + 1; // 51
        // DB constraint WHERE stock_quantity >= ? làm cho câu lệnh UPDATE ảnh hưởng 0 dòng
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        boolean result = medicineDAO.deductStockQuantity(MEDICINE_ID, exceedQuantity, mockConnection);

        assertFalse(result, "Trừ kho vượt số lượng tồn kho (51 > 50) phải trả về false");
    }

    @Test
    @DisplayName("BVA: Kiểm thử xử lý ngoại lệ SQLException khi trừ kho")
    void testDeductStockSQLException() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Deadlock / Connection error"));

        boolean result = medicineDAO.deductStockQuantity(MEDICINE_ID, 5, mockConnection);

        assertFalse(result, "Khi gặp SQLException phải trả về false an toàn không gây sập ứng dụng");
    }
}
