package com.clone.twitter.payment_service.client.currency;

import com.clone.twitter.payment_service.dto.client.ExchangeRatesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "exchangeRates", url = "https://openexchangerates.org/api")
public interface CurrencyRateFetcher {

    @GetMapping("/latest.json")
    ExchangeRatesResponse getRates(@RequestParam("app_id") String appId);
}