package com.clone.twitter.payment_service.config.async.rates;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class GetRatesAsync {

    @Bean
    public Executor ratesFetcherExecutorService(
            @Value("${async.currency-rates-fetch.threads-count}") int threadsCount
    ) {
        return Executors.newFixedThreadPool(threadsCount);
    }
}