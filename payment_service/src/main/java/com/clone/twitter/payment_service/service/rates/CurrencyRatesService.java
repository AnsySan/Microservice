package com.clone.twitter.payment_service.service.rates;

import java.util.Map;

public interface CurrencyRatesService {

    Map<String, Double> fetch();
}