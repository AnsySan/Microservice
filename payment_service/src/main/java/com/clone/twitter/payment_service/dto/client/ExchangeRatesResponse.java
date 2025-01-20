package com.clone.twitter.payment_service.dto.client;

import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRatesResponse {
    private String disclaimer;
    private String license;
    private long timestamp;
    private String base;
    private Map<String, Double> rates;
}