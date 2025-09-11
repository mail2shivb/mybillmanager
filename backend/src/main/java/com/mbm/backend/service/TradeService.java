package com.mbm.backend.service;

import com.mbm.backend.dto.TradeDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service class for handling trade data operations
 * This implementation provides mock data for demonstration purposes
 */
@Service
public class TradeService {

    // Mock data generator for trades
    private static final String[] SYMBOLS = {"AAPL", "GOOGL", "MSFT", "TSLA", "AMZN", "META", "NFLX", "NVDA", "SPY", "QQQ"};
    private static final Map<String, BigDecimal> SYMBOL_PRICES = Map.of(
            "AAPL", new BigDecimal("175.50"),
            "GOOGL", new BigDecimal("131.20"),
            "MSFT", new BigDecimal("378.85"),
            "TSLA", new BigDecimal("248.42"),
            "AMZN", new BigDecimal("145.30"),
            "META", new BigDecimal("325.67"),
            "NFLX", new BigDecimal("485.20"),
            "NVDA", new BigDecimal("875.30"),
            "SPY", new BigDecimal("450.15"),
            "QQQ", new BigDecimal("380.90")
    );

    /**
     * Retrieves all trades executed on a specific date
     * @param date the date to filter trades by
     * @return list of trades executed on the given date
     */
    public List<TradeDto> getTradesByDate(LocalDate date) {
        List<TradeDto> trades = new ArrayList<>();

        // Generate mock trades for the requested date
        // Number of trades varies based on day of week (more on weekdays)
        int numberOfTrades = date.getDayOfWeek().getValue() <= 5 ? 
                             (int) (Math.random() * 15) + 5 : // 5-20 trades on weekdays
                             (int) (Math.random() * 5) + 1;   // 1-6 trades on weekends

        for (int i = 0; i < numberOfTrades; i++) {
            trades.add(generateMockTrade(date, i));
        }

        // Sort trades by execution time
        trades.sort((t1, t2) -> t1.getExecutionTime().compareTo(t2.getExecutionTime()));

        return trades;
    }

    /**
     * Generates a mock trade for the given date
     */
    private TradeDto generateMockTrade(LocalDate date, int index) {
        String symbol = SYMBOLS[(int) (Math.random() * SYMBOLS.length)];
        BigDecimal basePrice = SYMBOL_PRICES.get(symbol);
        
        // Add some price variation (+/- 5%)
        double variation = (Math.random() - 0.5) * 0.1; // -5% to +5%
        BigDecimal price = basePrice.multiply(BigDecimal.valueOf(1 + variation))
                                   .setScale(2, java.math.RoundingMode.HALF_UP);

        // Generate random quantity between 1 and 1000
        int quantity = (int) (Math.random() * 1000) + 1;

        // Generate random time during market hours (9:30 AM to 4:00 PM EST)
        int hour = (int) (Math.random() * 6) + 9; // 9 to 15 (3PM)
        int minute = (int) (Math.random() * 60);
        int second = (int) (Math.random() * 60);
        
        // Adjust for half hour offset at market open
        if (hour == 9) {
            minute = Math.max(30, minute);
        }

        LocalDateTime executionTime = LocalDateTime.of(date, LocalTime.of(hour, minute, second));

        // Generate unique trade ID
        String tradeId = String.format("TRD-%s-%04d", date.toString().replace("-", ""), index + 1);

        return new TradeDto(tradeId, symbol, quantity, price, executionTime);
    }

    /**
     * Validates if the given date is a valid trading date
     * For simplicity, this allows all dates but could be enhanced to exclude weekends/holidays
     */
    public boolean isValidTradingDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }
}