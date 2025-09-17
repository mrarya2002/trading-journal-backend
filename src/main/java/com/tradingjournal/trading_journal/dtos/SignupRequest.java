package com.tradingjournal.trading_journal.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignupRequest {
        private String username;
        private String email;
        private String password;
}
