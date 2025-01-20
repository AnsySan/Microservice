package com.clone.twitter.project_service.exceptions;

public class ExpiredTimeException extends RuntimeException {
    public ExpiredTimeException(String message) {
        super(message);
    }
}
