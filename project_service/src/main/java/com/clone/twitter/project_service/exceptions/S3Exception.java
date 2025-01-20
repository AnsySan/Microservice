package com.clone.twitter.project_service.exceptions;

public class S3Exception extends RuntimeException {
    public S3Exception(String message) {
        super(message);
    }
}
