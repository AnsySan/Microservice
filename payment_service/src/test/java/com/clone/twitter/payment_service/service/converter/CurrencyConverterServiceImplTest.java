package com.clone.twitter.payment_service.service.converter;

import com.clone.twitter.payment_service.dto.convert.ConvertDto;
import com.clone.twitter.payment_service.enums.Currency;
import com.clone.twitter.payment_service.service.rates.CurrencyFetchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyConverterServiceImplTest {

    @Mock
    private RedisTemplate<String, Double> redisTemplate;

    @Mock
    private ValueOperations<String, Double> valueOperations;

    @Mock
    private CurrencyFetchService currencyFetchService;

    @InjectMocks
    private CurrencyConverterServiceImpl currencyConverterService;

    @BeforeEach
    public void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testConvertWithFetch() {
        when(valueOperations.get(anyString())).thenReturn(null);
        when(currencyFetchService.fetch()).thenReturn(getMockRates());
        BigDecimal commission = BigDecimal.valueOf(1.01);
        ReflectionTestUtils.setField(currencyConverterService, "commission", commission);

        ConvertDto convertDto = new ConvertDto(BigDecimal.valueOf(100), Currency.USD, Currency.EUR);

        BigDecimal result = currencyConverterService.convert(convertDto);

        BigDecimal expected = BigDecimal.valueOf(121.200);
        assertEquals(expected.setScale(3, RoundingMode.HALF_UP), result);
    }

    @Test
    public void testConvert() {
        when(valueOperations.get(anyString())).thenReturn(null);
        BigDecimal commission = BigDecimal.valueOf(1.01);
        ReflectionTestUtils.setField(currencyConverterService, "commission", commission);

        ConvertDto convertDto = new ConvertDto(BigDecimal.valueOf(100), Currency.USD, Currency.EUR);
        when(redisTemplate.opsForValue().get(Currency.USD.name())).thenReturn(1.0);
        when(redisTemplate.opsForValue().get(Currency.EUR.name())).thenReturn(1.2);

        BigDecimal result = currencyConverterService.convert(convertDto);

        BigDecimal expected = BigDecimal.valueOf(121.200);
        assertEquals(expected.setScale(3, RoundingMode.HALF_UP), result);
    }

    @Test
    public void testGetRateAndCacheAsync() throws ExecutionException, InterruptedException {
        Currency currency = Currency.USD;
        Map<String, Double> rates = Map.of(currency.name(), 1.0);

        CompletableFuture<Double> future = currencyConverterService.getRateAndCacheAsync(currency, rates);

        verify(valueOperations, times(1)).set(eq(currency.name()), eq(1.0));
        assert future.get().equals(1.0);
    }

    private Map<String, Double> getMockRates() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.0);
        rates.put("EUR", 1.2);
        return rates;
    }
}