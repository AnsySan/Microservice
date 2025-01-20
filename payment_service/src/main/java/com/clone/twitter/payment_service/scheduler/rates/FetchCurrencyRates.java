package com.clone.twitter.payment_service.scheduler.rates;

import com.clone.twitter.payment_service.service.rates.CurrencyFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class FetchCurrencyRates {
    private final RedisTemplate<String, Double> redisTemplate;
    private final CurrencyFetchService currencyFetchService;

    @Scheduled(cron = "${scheduler.fetch.currency-rates.cron}")
    public void fetchCurrencyRatesScheduled() {
        Map<String, Double> rates = currencyFetchService.fetch();

        rates.forEach((key, value) -> redisTemplate.opsForValue().set(key, value));
        log.info("Actual currency rates fetched and updated in Redis by schedule");
    }
}