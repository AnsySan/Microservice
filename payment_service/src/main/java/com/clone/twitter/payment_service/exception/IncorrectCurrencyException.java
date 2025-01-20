package com.clone.twitter.payment_service.exception;

public class IncorrectCurrencyException extends RuntimeException {
    public IncorrectCurrencyException(String message) {
        super(message);
    }
}
