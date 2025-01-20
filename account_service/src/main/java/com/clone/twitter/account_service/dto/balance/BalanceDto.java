package com.clone.twitter.account_service.dto.balance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BalanceDto {
    private Long id;
    private Long accountId;
    private Long authorizedBalance;
    private Long actualBalance;
}