package com.tradingjournal.trading_journal.service;

import com.tradingjournal.trading_journal.dtos.DashboardDTO;
import com.tradingjournal.trading_journal.enums.TradeOutcome;
import com.tradingjournal.trading_journal.models.Trade;
import com.tradingjournal.trading_journal.models.User;
import com.tradingjournal.trading_journal.repository.TradeRepository;
import com.tradingjournal.trading_journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final TradeRepository tradeRepository;

    @Autowired
    public DashboardService(TradeRepository tradeRepository, UserRepository userRepository) {
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;
    }

    public DashboardDTO getDashboardData(String userEmail) throws RuntimeException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Trade> trades = tradeRepository.findByUser(user);

        if (trades.isEmpty()) {
            return new DashboardDTO(0, 0, 0, 0, 0, 0, 0, 0);
        }

        double totalProfit = 0;
        int winCount = 0;
        int lossCount = 0;
        double bestTrade = Double.NEGATIVE_INFINITY;
        double worstTrade = Double.POSITIVE_INFINITY;

        for (Trade trade : trades) {
            double pl = trade.getProfitLoss();
            totalProfit += pl;

            // Count wins/losses
            if (trade.getOutcome() == TradeOutcome.TP_HIT) {
                winCount++;
            } else if (trade.getOutcome() == TradeOutcome.SL_HIT) {
                lossCount++;
            }

            // Best/Worst trade
            if (pl > bestTrade) bestTrade = pl;
            if (pl < worstTrade) worstTrade = pl;
        }

        int totalTrades = trades.size();
        double accuracy = (winCount * 100.0) / totalTrades;
        double avgProfitPerTrade = totalProfit / totalTrades;

        return new DashboardDTO(
                totalProfit,
                accuracy,
                winCount,
                lossCount,
                totalTrades,
                bestTrade,
                worstTrade,
                avgProfitPerTrade
        );
    }
}
