package com.clone.twitter.payment_service.service.rates;

import com.clone.twitter.payment_service.client.currency.CurrencyRateFetcher;
import com.clone.twitter.payment_service.dto.client.ExchangeRatesResponse;
import com.clone.twitter.payment_service.service.cache.RedisCacheService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyFetchServiceImplTest {

    @Mock
    private CurrencyRateFetcher currencyRateFetcher;

    @Mock
    private RedisCacheService redisCacheService;

    @InjectMocks
    private CurrencyFetchServiceImpl currencyFetchService;

    private final String appId = "testAppId";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(currencyFetchService, "appId", appId);
    }

    @Test
    void fetch_returnsCachedRates_whenCacheIsNotEmpty() {
        Map<String, Double> cachedRates = new HashMap<>();
        cachedRates.put("USD", 1.0);
        cachedRates.put("EUR", 0.9);

        when(redisCacheService.fetchRates()).thenReturn(cachedRates);

        Map<String, Double> rates = currencyFetchService.fetch();

        assertEquals(cachedRates, rates);
        verify(redisCacheService, times(1)).fetchRates();
        verify(currencyRateFetcher, times(0)).getRates(appId);
    }

    @Test
    void fetch_fetchesRatesFromApiAndCachesThem_whenCacheIsEmpty() {
        Map<String, Double> apiRates = new HashMap<>();
        apiRates.put("USD", 1.0);
        apiRates.put("EUR", 0.9);

        ExchangeRatesResponse response = new ExchangeRatesResponse();
        response.setRates(apiRates);

        when(redisCacheService.fetchRates()).thenReturn(new HashMap<>());
        when(currencyRateFetcher.getRates(appId)).thenReturn(response);

        Map<String, Double> rates = currencyFetchService.fetch();

        assertEquals(apiRates, rates);
        verify(redisCacheService, times(1)).fetchRates();
        verify(currencyRateFetcher, times(1)).getRates(appId);
        verify(redisCacheService, times(1)).putRateAsync("USD", 1.0);
        verify(redisCacheService, times(1)).putRateAsync("EUR", 0.9);
    }

    @Test
    void fetch_returnsEmptyMap_whenCacheIsEmptyAndApiFails() {
        when(redisCacheService.fetchRates()).thenReturn(new HashMap<>());
        when(currencyRateFetcher.getRates(appId)).thenThrow(FeignException.class);

        Map<String, Double> rates = currencyFetchService.fetch();

        assertTrue(rates.isEmpty());
        verify(redisCacheService, times(1)).fetchRates();
        verify(currencyRateFetcher, times(1)).getRates(appId);
    }
}