package com.clone.twitter.payment_service.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements RedisCacheService {

    private final RedisTemplate<String, Double> redisTemplate;

    @Override
    public Map<String, Double> fetchRates() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null || keys.isEmpty()) return new HashMap<>();

        return keys.stream()
                .map(key -> Map.entry(key, Optional.ofNullable(redisTemplate.opsForValue().get(key))))
                .filter(entry -> entry.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));
    }

    @Async("putRatesInMap")
    @Override
    public void putRateAsync(String key, Double value) {
        redisTemplate.opsForValue().set(key, value);
        CompletableFuture.completedFuture(null);
    }
}