package com.tradingjournal.trading_journal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tradingjournal.trading_journal.dtos.TradeDTO;
import com.tradingjournal.trading_journal.models.Trade;
import com.tradingjournal.trading_journal.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/trades")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Trade> createTrade(
            @RequestPart("trade") String tradeJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TradeDTO tradeDTO = objectMapper.readValue(tradeJson, TradeDTO.class);
        Trade trade = tradeService.createTrade(tradeDTO, image, getCurrentUserEmail());
        return new ResponseEntity<>(trade, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Trade> updateTrade(
            @PathVariable UUID id,
            @RequestPart("trade") String tradeJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TradeDTO tradeDTO = objectMapper.readValue(tradeJson, TradeDTO.class);
        Trade trade = tradeService.updateTrade(id, tradeDTO, image, getCurrentUserEmail());
        return ResponseEntity.ok(trade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrade(@PathVariable UUID id) throws IOException {
        tradeService.deleteTrade(id, getCurrentUserEmail());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trade> getTrade(@PathVariable UUID id) {
        Trade trade = tradeService.getTrade(id, getCurrentUserEmail());
        return ResponseEntity.ok(trade);
    }

    @GetMapping
    public ResponseEntity<List<Trade>> getAllTrades() {
        List<Trade> trades = tradeService.getAllTrades(getCurrentUserEmail());
        return ResponseEntity.ok(trades);
    }
}
