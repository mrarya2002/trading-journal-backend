package com.tradingjournal.trading_journal.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tradingjournal.trading_journal.enums.TradeOutcome;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "trades")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Trade {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private Double entryPrice;

    private Double exitPrice;

    @Column(nullable = false)
    private Integer quantity;

    private Double stopLoss;
    private Double takeProfit;

    @Enumerated(EnumType.STRING)
    private TradeOutcome outcome; // SL_HIT, TP_HIT, NONE

    private String notes;

    private Double profitLoss; // (exitPrice - entryPrice) * quantity

    private String strategy;

    private String imageUrl; // Cloudinary URL

    @Column(updatable = false)
    private LocalDateTime createdAt;
}
