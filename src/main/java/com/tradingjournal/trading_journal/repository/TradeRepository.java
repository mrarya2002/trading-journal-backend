package com.tradingjournal.trading_journal.repository;

import com.tradingjournal.trading_journal.models.Trade;
import com.tradingjournal.trading_journal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TradeRepository extends JpaRepository<Trade, UUID> {
    // Fetch all trades for a specific user
    List<Trade> findByUser(User user);
}
