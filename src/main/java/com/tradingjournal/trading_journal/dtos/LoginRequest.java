package com.tradingjournal.trading_journal.dtos;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}

