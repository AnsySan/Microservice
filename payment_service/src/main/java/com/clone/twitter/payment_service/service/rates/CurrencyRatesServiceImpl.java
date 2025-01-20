package com.clone.twitter.payment_service.service.rates;

import com.clone.twitter.payment_service.client.currency.CurrencyRateFetcher;
import com.clone.twitter.payment_service.dto.client.ExchangeRatesResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyRatesServiceImpl implements CurrencyRatesService{

    private final CurrencyRateFetcher currencyRateFetcher;

    @Value("${services.openexchangerates.app_id}")
    private String appId;

    @Override
    @Retryable(retryFor = FeignException.class, backoff = @Backoff(delay = 1000, multiplier = 3))
    public Map<String, Double> fetch() {
        ExchangeRatesResponse exchangeRatesResponse = currencyRateFetcher.getRates(appId);
        return exchangeRatesResponse.getRates();
    }
}