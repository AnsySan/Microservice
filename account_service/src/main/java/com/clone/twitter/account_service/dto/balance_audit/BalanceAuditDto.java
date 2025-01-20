package com.clone.twitter.account_service.dto.balance_audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceAuditDto {
    private Long id;
    private Long accountId;
    private Long authorizationBalance;
    private Long actualBalance;
    private LocalDateTime createdAt;
    private Long version;
}
