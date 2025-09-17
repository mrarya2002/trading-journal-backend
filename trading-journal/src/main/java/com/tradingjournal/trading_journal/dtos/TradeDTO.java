package com.tradingjournal.trading_journal.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tradingjournal.trading_journal.enums.TradeOutcome;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TradeDTO {
    private String symbol;
    private Double entryPrice;
    private Double exitPrice;
    private Integer quantity;
    private Double stopLoss;
    private Double takeProfit;
    private TradeOutcome outcome; // Enum: SL_HIT, TP_HIT, NONE
    private String notes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String strategy;
    private String imageUrl; // optional, for reference
}
