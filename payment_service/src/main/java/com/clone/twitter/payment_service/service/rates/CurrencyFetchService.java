package com.clone.twitter.payment_service.service.rates;

import java.util.Map;

public interface CurrencyFetchService {

    Map<String, Double> fetch();
}