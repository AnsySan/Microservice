package com.clone.twitter.payment_service.service.rates;

import com.clone.twitter.payment_service.client.currency.CurrencyRateFetcher;
import com.clone.twitter.payment_service.dto.client.ExchangeRatesResponse;
import com.clone.twitter.payment_service.service.cache.RedisCacheService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyFetchServiceImpl implements CurrencyFetchService {

    private final CurrencyRateFetcher currencyRateFetcher;
    private final RedisCacheService redisCacheService;

    @Value("${services.openexchangerates.app_id}")
    private String appId;

    @Override
    @Retryable(retryFor = FeignException.class, backoff = @Backoff(delay = 1000, multiplier = 3))
    public Map<String, Double> fetch() {
        Map<String, Double> rates = redisCacheService.fetchRates();

        if (rates.isEmpty()) {
            try {
                ExchangeRatesResponse response = currencyRateFetcher.getRates(appId);
                if (response != null && response.getRates() != null) {
                    rates = response.getRates();
                    rates.forEach(redisCacheService::putRateAsync);
                }
                log.info("Actual currency rates fetched and updated in Redis");
            } catch (FeignException e) {
                log.error("Failed to fetch rates from external API", e);
                return new HashMap<>();
            }
        }

        return rates;
    }
}