package com.clone.twitter.account_service.dto.balance;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateDto {

    @NotNull(message = "id should not be null")
    private Long id;

    @NotNull(message = "accountId should not be null")
    private Long accountId;

    @NotNull(message = "authorizedBalance should not be null")
    private Long authorizedBalance;

    @NotNull(message = "actualBalance should not be null")
    private Long actualBalance;

    @NotNull(message = "paymentNumber should not be null")
    private Long paymentNumber;
}
