package com.tradingjournal.trading_journal.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashboardDTO {
    private double totalProfit;
    private double accuracy;
    private int winCount;
    private int lossCount;
    private int totalTrades;
    private double bestTrade;
    private double worstTrade;
    private double avgProfitPerTrade;
}
