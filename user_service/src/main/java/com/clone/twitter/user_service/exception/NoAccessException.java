package com.clone.twitter.user_service.exception;

public class NoAccessException extends RuntimeException{
    public NoAccessException(String message) {
        super(message);
    }
}