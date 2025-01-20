package com.clone.twitter.payment_service.service.cache;


import java.util.Map;

public interface RedisCacheService {
    Map<String, Double> fetchRates();

    void putRateAsync(String key, Double value);
}