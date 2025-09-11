package com.mbm.backend.controller;

import com.mbm.backend.dto.TradeDto;
import com.mbm.backend.dto.TradesResponseDto;
import com.mbm.backend.service.TradeService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for trade-related operations
 */
@RestController
@RequestMapping("/api/v1/trades")
@Validated
public class TradeController {

    @Autowired
    private TradeService tradeService;

    /**
     * Retrieves all trades executed on a specific date
     * 
     * @param date the date to filter trades by (format: yyyy-MM-dd)
     * @return ResponseEntity containing the trades response or error
     */
    @GetMapping
    public ResponseEntity<?> getTradesByDate(
            @RequestParam("date") 
            @NotNull(message = "Date parameter is required")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate date) {
        
        try {
            // Validate the trading date
            if (!tradeService.isValidTradingDate(date)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid trading date");
                error.put("message", "Date cannot be in the future or null");
                error.put("date", date.toString());
                return ResponseEntity.badRequest().body(error);
            }

            // Retrieve trades for the specified date
            List<TradeDto> trades = tradeService.getTradesByDate(date);

            // Create response
            TradesResponseDto response = new TradesResponseDto(date, trades);
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Health check endpoint for the trades API
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "Trade API");
        status.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(status);
    }

    /**
     * Exception handler for validation errors
     */
    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParameter(
            org.springframework.web.bind.MissingServletRequestParameterException ex) {
        
        Map<String, String> error = new HashMap<>();
        error.put("error", "Missing required parameter");
        error.put("message", ex.getMessage());
        error.put("parameter", ex.getParameterName());
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Exception handler for date format errors
     */
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleDateFormatError(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid date format");
        error.put("message", "Date must be in format yyyy-MM-dd (e.g., 2024-01-15)");
        error.put("provided", ex.getValue() != null ? ex.getValue().toString() : "null");
        return ResponseEntity.badRequest().body(error);
    }
}