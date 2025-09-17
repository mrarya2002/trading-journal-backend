package com.tradingjournal.trading_journal.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
}
