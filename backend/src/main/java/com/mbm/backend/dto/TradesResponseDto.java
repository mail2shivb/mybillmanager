package com.mbm.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * Response wrapper for trades API endpoint
 */
public class TradesResponseDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tradeDate;

    private Integer totalTrades;

    private List<TradeDto> trades;

    public TradesResponseDto() {}

    public TradesResponseDto(LocalDate tradeDate, List<TradeDto> trades) {
        this.tradeDate = tradeDate;
        this.trades = trades;
        this.totalTrades = trades != null ? trades.size() : 0;
    }

    // Getters and setters
    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Integer getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(Integer totalTrades) {
        this.totalTrades = totalTrades;
    }

    public List<TradeDto> getTrades() {
        return trades;
    }

    public void setTrades(List<TradeDto> trades) {
        this.trades = trades;
        this.totalTrades = trades != null ? trades.size() : 0;
    }

    @Override
    public String toString() {
        return "TradesResponseDto{" +
                "tradeDate=" + tradeDate +
                ", totalTrades=" + totalTrades +
                ", trades=" + trades +
                '}';
    }
}