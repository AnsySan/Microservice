package com.clone.twitter.payment_service.enums;

public enum PaymentStatus {
    NEW,
    READY_TO_CLEAR,
    CLEAR,
    CANCELED,
    FAILURE;
}