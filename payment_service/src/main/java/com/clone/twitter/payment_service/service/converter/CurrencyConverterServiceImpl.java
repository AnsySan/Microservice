package com.clone.twitter.payment_service.service.converter;

import com.clone.twitter.payment_service.dto.convert.ConvertDto;
import com.clone.twitter.payment_service.enums.Currency;
import com.clone.twitter.payment_service.exception.NotFoundException;
import com.clone.twitter.payment_service.service.rates.CurrencyFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private final RedisTemplate<String, Double> redisTemplate;
    private final CurrencyFetchService currencyFetchService;

    @Value("${currencyConverter.commission}")
    private BigDecimal commission;

    @Override
    public BigDecimal convert(ConvertDto convertDto) {
        Double fromRate = redisTemplate.opsForValue().get(convertDto.getFromCurrency().name());
        Double toRate = redisTemplate.opsForValue().get(convertDto.getToCurrency().name());

        if (fromRate == null || toRate == null) {
            Map<String, Double> rates = currencyFetchService.fetch();
            CompletableFuture<Double> fromRateFuture = getRateAndCacheAsync(convertDto.getFromCurrency(), rates);
            CompletableFuture<Double> toRateFuture = getRateAndCacheAsync(convertDto.getToCurrency(), rates);

            try {
                fromRate = fromRateFuture.get();
                toRate = toRateFuture.get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch rates", e);
            }
        }

        return BigDecimal.valueOf(toRate / fromRate).multiply(convertDto.getAmount()).multiply(commission);
    }

    @Async("getRatesAsync")
    public CompletableFuture<Double> getRateAndCacheAsync(Currency currency, Map<String, Double> rates) {
        Double rate = rates.get(currency.name());
        if (rate != null) {
            redisTemplate.opsForValue().set(currency.name(), rate);
        } else {
            throw new NotFoundException("Currency rate not found for " + currency.name());
        }
        return CompletableFuture.completedFuture(rate);
    }
}