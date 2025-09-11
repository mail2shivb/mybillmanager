package com.mbm.backend.controller;

import com.mbm.backend.dto.TradeDto;
import com.mbm.backend.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TradeController.class)
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<TradeDto> mockTrades;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);
        
        mockTrades = Arrays.asList(
            new TradeDto("TRD-20240115-0001", "AAPL", 100, new BigDecimal("175.50"), 
                        LocalDateTime.of(2024, 1, 15, 9, 30, 0)),
            new TradeDto("TRD-20240115-0002", "GOOGL", 50, new BigDecimal("131.20"), 
                        LocalDateTime.of(2024, 1, 15, 10, 15, 0)),
            new TradeDto("TRD-20240115-0003", "MSFT", 200, new BigDecimal("378.85"), 
                        LocalDateTime.of(2024, 1, 15, 14, 45, 0))
        );
    }

    @Test
    void getTradesByDate_ValidDate_ReturnsTradeList() throws Exception {
        // Given
        when(tradeService.isValidTradingDate(testDate)).thenReturn(true);
        when(tradeService.getTradesByDate(testDate)).thenReturn(mockTrades);

        // When & Then
        mockMvc.perform(get("/api/v1/trades")
                .param("date", "2024-01-15")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tradeDate").value("2024-01-15"))
                .andExpect(jsonPath("$.totalTrades").value(3))
                .andExpect(jsonPath("$.trades").isArray())
                .andExpect(jsonPath("$.trades.length()").value(3))
                .andExpect(jsonPath("$.trades[0].tradeId").value("TRD-20240115-0001"))
                .andExpect(jsonPath("$.trades[0].symbol").value("AAPL"))
                .andExpect(jsonPath("$.trades[0].quantity").value(100))
                .andExpect(jsonPath("$.trades[0].price").value(175.50))
                .andExpect(jsonPath("$.trades[0].executionTime").value("2024-01-15T09:30:00"));
    }

    @Test
    void getTradesByDate_NoTrades_ReturnsEmptyList() throws Exception {
        // Given
        LocalDate emptyDate = LocalDate.of(2024, 1, 20);
        when(tradeService.isValidTradingDate(emptyDate)).thenReturn(true);
        when(tradeService.getTradesByDate(emptyDate)).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/v1/trades")
                .param("date", "2024-01-20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tradeDate").value("2024-01-20"))
                .andExpect(jsonPath("$.totalTrades").value(0))
                .andExpect(jsonPath("$.trades").isArray())
                .andExpect(jsonPath("$.trades.length()").value(0));
    }

    @Test
    void getTradesByDate_InvalidDate_ReturnsBadRequest() throws Exception {
        // Given
        LocalDate futureDate = LocalDate.now().plusDays(1);
        when(tradeService.isValidTradingDate(any(LocalDate.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/api/v1/trades")
                .param("date", futureDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid trading date"))
                .andExpect(jsonPath("$.message").value("Date cannot be in the future or null"));
    }

    @Test
    void getTradesByDate_MissingDateParameter_ReturnsBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/trades")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing required parameter"))
                .andExpect(jsonPath("$.parameter").value("date"));
    }

    @Test
    void getTradesByDate_InvalidDateFormat_ReturnsBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/trades")
                .param("date", "invalid-date")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid date format"))
                .andExpect(jsonPath("$.message").value("Date must be in format yyyy-MM-dd (e.g., 2024-01-15)"));
    }

    @Test
    void health_ReturnsHealthStatus() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/trades/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Trade API"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}