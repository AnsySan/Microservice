package com.clone.twitter.payment_service.dto;

import com.clone.twitter.payment_service.enums.Currency;
import com.clone.twitter.payment_service.enums.PaymentStatus;

import java.math.BigDecimal;
public record PaymentResponse(
        PaymentStatus status,
        int verificationCode,
        long paymentNumber,
        BigDecimal amount,
        Currency currency,
        String message
) {
}
