package com.clone.twitter.payment_service.service.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RedisCacheServiceImplTest {

    @Mock
    private RedisTemplate<String, Double> redisTemplate;

    @Mock
    private ValueOperations<String, Double> valueOperations;

    @InjectMocks
    private RedisCacheServiceImpl redisCacheService;

    @Test
    void fetchRates_returnsCorrectMap() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        Set<String> keys = Set.of("USD", "EUR");
        when(redisTemplate.keys("*")).thenReturn(keys);
        when(valueOperations.get("USD")).thenReturn(1.0);
        when(valueOperations.get("EUR")).thenReturn(0.9);

        Map<String, Double> expectedRates = new HashMap<>();
        expectedRates.put("USD", 1.0);
        expectedRates.put("EUR", 0.9);

        Map<String, Double> rates = redisCacheService.fetchRates();

        assertEquals(expectedRates, rates);
        verify(redisTemplate, times(1)).keys("*");
        verify(valueOperations, times(1)).get("USD");
        verify(valueOperations, times(1)).get("EUR");
    }

    @Test
    void fetchRates_returnsEmptyMap_whenNoKeys() {
        redisCacheService = new RedisCacheServiceImpl(redisTemplate);

        when(redisTemplate.keys("*")).thenReturn(null);

        Map<String, Double> rates = redisCacheService.fetchRates();

        assertTrue(rates.isEmpty());
        verify(redisTemplate, times(1)).keys("*");
    }

    @Test
    void putRateAsync_setsValue() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        String key = "USD";
        Double value = 1.0;

        redisCacheService.putRateAsync(key, value);

        verify(valueOperations, times(1)).set(key, value);
    }
}