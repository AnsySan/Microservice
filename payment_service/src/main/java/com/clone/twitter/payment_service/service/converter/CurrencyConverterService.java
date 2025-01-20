package com.clone.twitter.payment_service.service.converter;

import com.clone.twitter.payment_service.dto.convert.ConvertDto;

import java.math.BigDecimal;

public interface CurrencyConverterService {

    BigDecimal convert(ConvertDto convertDto);
}