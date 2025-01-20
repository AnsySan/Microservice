package com.clone.twitter.payment_service.dto.payment;

import com.clone.twitter.payment_service.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentDtoToCreate {

    @NotNull
    private UUID idempotencyKey;

    @NotNull
    @NotBlank
    private String senderAccountNumber;

    @NotNull
    @NotBlank
    private String receiverAccountNumber;

    @NotNull
    private Currency currency;

    @NotNull
    @Positive
    private BigDecimal amount;
}