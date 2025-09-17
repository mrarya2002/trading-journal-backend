package com.tradingjournal.trading_journal.service;

import com.tradingjournal.trading_journal.dtos.TradeDTO;
import com.tradingjournal.trading_journal.models.Trade;
import com.tradingjournal.trading_journal.models.User;
import com.tradingjournal.trading_journal.repository.TradeRepository;
import com.tradingjournal.trading_journal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TradeService {

    private final ImageService imageService;
    private final UserRepository userRepository;
    private final TradeRepository tradeRepository;

    public TradeService(ImageService imageService,
                        UserRepository userRepository,
                        TradeRepository tradeRepository) {
        this.imageService = imageService;
        this.userRepository = userRepository;
        this.tradeRepository = tradeRepository;
    }

    // ✅ Create trade for authenticated user
    public Trade createTrade(TradeDTO tradeDTO, MultipartFile image, String userEmail) throws IOException {
        log.info("Creating trade for user: {}", userEmail);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Trade trade = mapDtoToTrade(tradeDTO, image);
        trade.setUser(user);
        trade.setCreatedAt(LocalDateTime.now());

        // calculate profit/loss
        if (trade.getExitPrice() != null && trade.getQuantity() != null) {
            trade.setProfitLoss((trade.getExitPrice() - trade.getEntryPrice()) * trade.getQuantity());
        }

        return tradeRepository.save(trade);
    }

    // ✅ Update trade only if owned by authenticated user
    public Trade updateTrade(UUID id, TradeDTO tradeDTO, MultipartFile image, String userEmail) throws IOException {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found"));

        if (!trade.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access Denied: You can only update your own trades");
        }

        mapDtoToTrade(tradeDTO, image, trade);

        if (trade.getExitPrice() != null && trade.getQuantity() != null) {
            trade.setProfitLoss((trade.getExitPrice() - trade.getEntryPrice()) * trade.getQuantity());
        }

        return tradeRepository.save(trade);
    }

    // ✅ Delete trade only if owned by user
    public void deleteTrade(UUID id, String userEmail) throws IOException {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found"));

        if (!trade.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access Denied: You can only delete your own trades");
        }

        if (trade.getImageUrl() != null) {
            imageService.deleteImage(trade.getImageUrl());
        }

        tradeRepository.delete(trade);
    }

    // ✅ Get trade only if owned by user
    public Trade getTrade(UUID id, String userEmail) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found"));

        if (!trade.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access Denied: You can only view your own trades");
        }

        return trade;
    }

    // ✅ Get all trades of the authenticated user
    public List<Trade> getAllTrades(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return tradeRepository.findByUser(user);
    }

    // helper: map DTO to trade (for create)
    private Trade mapDtoToTrade(TradeDTO dto, MultipartFile image) throws IOException {
        Trade trade = new Trade();
        return mapDtoToTrade(dto, image, trade);
    }

    // helper: map DTO to trade (for update)
    private Trade mapDtoToTrade(TradeDTO dto, MultipartFile image, Trade trade) throws IOException {
        trade.setDate(dto.getDate());
        trade.setSymbol(dto.getSymbol());
        trade.setEntryPrice(dto.getEntryPrice());
        trade.setExitPrice(dto.getExitPrice());
        trade.setQuantity(dto.getQuantity());
        trade.setStopLoss(dto.getStopLoss());
        trade.setTakeProfit(dto.getTakeProfit());
        trade.setOutcome(dto.getOutcome());
        trade.setNotes(dto.getNotes());
        trade.setStrategy(dto.getStrategy());

        if (image != null && !image.isEmpty()) {
            if (trade.getImageUrl() != null) {
                imageService.deleteImage(trade.getImageUrl());
            }
            trade.setImageUrl(imageService.uploadImage(image, "trading_journal"));
        }
        return trade;
    }



}
