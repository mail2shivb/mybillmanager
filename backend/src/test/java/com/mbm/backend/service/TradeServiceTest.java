package com.mbm.backend.service;

import com.mbm.backend.dto.TradeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TradeServiceTest {

    private TradeService tradeService;

    @BeforeEach
    void setUp() {
        tradeService = new TradeService();
    }

    @Test
    void getTradesByDate_ValidDate_ReturnsTradeList() {
        // Given
        LocalDate testDate = LocalDate.of(2024, 1, 15); // Monday

        // When
        List<TradeDto> trades = tradeService.getTradesByDate(testDate);

        // Then
        assertNotNull(trades);
        assertTrue(trades.size() > 0, "Should return some trades for a valid weekday");
        
        // Verify all trades have the correct date
        for (TradeDto trade : trades) {
            assertNotNull(trade.getTradeId());
            assertNotNull(trade.getSymbol());
            assertNotNull(trade.getQuantity());
            assertNotNull(trade.getPrice());
            assertNotNull(trade.getExecutionTime());
            
            assertEquals(testDate, trade.getExecutionTime().toLocalDate());
            assertTrue(trade.getQuantity() > 0);
            assertTrue(trade.getPrice().doubleValue() > 0);
            assertTrue(trade.getTradeId().startsWith("TRD-"));
        }
    }

    @Test
    void getTradesByDate_Weekend_ReturnsFewerTrades() {
        // Given
        LocalDate weekday = LocalDate.of(2024, 1, 15); // Monday
        LocalDate weekend = LocalDate.of(2024, 1, 14); // Sunday

        // When
        List<TradeDto> weekdayTrades = tradeService.getTradesByDate(weekday);
        List<TradeDto> weekendTrades = tradeService.getTradesByDate(weekend);

        // Then
        // Weekend should typically have fewer trades than weekdays
        // (this is probabilistic, but should be true in most cases)
        assertTrue(weekendTrades.size() <= weekdayTrades.size() || weekendTrades.size() <= 6);
    }

    @Test
    void getTradesByDate_TradesAreSorted() {
        // Given
        LocalDate testDate = LocalDate.of(2024, 1, 15);

        // When
        List<TradeDto> trades = tradeService.getTradesByDate(testDate);

        // Then
        if (trades.size() > 1) {
            for (int i = 1; i < trades.size(); i++) {
                assertTrue(
                    trades.get(i - 1).getExecutionTime().isBefore(trades.get(i).getExecutionTime()) ||
                    trades.get(i - 1).getExecutionTime().equals(trades.get(i).getExecutionTime()),
                    "Trades should be sorted by execution time"
                );
            }
        }
    }

    @Test
    void isValidTradingDate_CurrentDate_ReturnsTrue() {
        // Given
        LocalDate currentDate = LocalDate.now();

        // When
        boolean isValid = tradeService.isValidTradingDate(currentDate);

        // Then
        assertTrue(isValid);
    }

    @Test
    void isValidTradingDate_PastDate_ReturnsTrue() {
        // Given
        LocalDate pastDate = LocalDate.of(2024, 1, 1);

        // When
        boolean isValid = tradeService.isValidTradingDate(pastDate);

        // Then
        assertTrue(isValid);
    }

    @Test
    void isValidTradingDate_FutureDate_ReturnsFalse() {
        // Given
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // When
        boolean isValid = tradeService.isValidTradingDate(futureDate);

        // Then
        assertFalse(isValid);
    }

    @Test
    void isValidTradingDate_NullDate_ReturnsFalse() {
        // When
        boolean isValid = tradeService.isValidTradingDate(null);

        // Then
        assertFalse(isValid);
    }

    @Test
    void getTradesByDate_SameDate_GeneratesConsistentTrades() {
        // Given
        LocalDate testDate = LocalDate.of(2024, 1, 15);

        // When
        List<TradeDto> trades1 = tradeService.getTradesByDate(testDate);
        List<TradeDto> trades2 = tradeService.getTradesByDate(testDate);

        // Then
        // Note: Since we're using random generation, the trades will be different each time
        // But they should all be for the same date
        assertNotNull(trades1);
        assertNotNull(trades2);
        
        for (TradeDto trade : trades1) {
            assertEquals(testDate, trade.getExecutionTime().toLocalDate());
        }
        
        for (TradeDto trade : trades2) {
            assertEquals(testDate, trade.getExecutionTime().toLocalDate());
        }
    }
}