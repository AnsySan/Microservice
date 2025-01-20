package com.clone.twitter.payment_service.controller;

import com.clone.twitter.payment_service.config.context.UserContext;
import com.clone.twitter.payment_service.dto.PaymentRequest;
import com.clone.twitter.payment_service.dto.PaymentResponse;
import com.clone.twitter.payment_service.dto.convert.ConvertDto;
import com.clone.twitter.payment_service.dto.payment.PaymentDto;
import com.clone.twitter.payment_service.dto.payment.PaymentDtoToCreate;
import com.clone.twitter.payment_service.enums.Currency;
import com.clone.twitter.payment_service.enums.PaymentStatus;
import com.clone.twitter.payment_service.exception.PaymentException;
import com.clone.twitter.payment_service.service.converter.CurrencyConverterService;
import com.clone.twitter.payment_service.service.payment.PaymentService;
import com.clone.twitter.payment_service.service.rates.CurrencyFetchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final CurrencyConverterService currencyConverterService;
    private final CurrencyFetchService currencyFetchService;
    private final PaymentService paymentService;
    private final UserContext userContext;

    @Value("${default.target.currency}")
    private Currency targetCurrency;

    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> sendPayment(@RequestBody @Validated PaymentRequest dto) {
        try {
            BigDecimal convertedAmount = currencyConverterService.convert(
                    new ConvertDto(dto.amount(), dto.currency(), targetCurrency));

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedSum = decimalFormat.format(convertedAmount);
            int verificationCode = new Random().nextInt(1000, 10000);
            String message = String.format("Dear friend! Thank you for your purchase! " +
                                           "Your payment of %s %s was accepted.",
                    formattedSum, targetCurrency.name());

            return ResponseEntity.ok(new PaymentResponse(
                    PaymentStatus.NEW,
                    verificationCode,
                    dto.paymentNumber(),
                    convertedAmount,
                    targetCurrency,
                    message)
            );
        } catch (Exception e) {
            throw new PaymentException("Error processing payment");
        }
    }

    @PostMapping ("/convert")
    public BigDecimal convert(@RequestBody ConvertDto convertDto) {
        return currencyConverterService.convert(convertDto);
    }

    @GetMapping("/rates")
    public Map<String, Double> getRates() {
        return currencyFetchService.fetch();
    }

    @PostMapping
    public Long createPayment(@RequestBody @Valid PaymentDtoToCreate dto) {
        Long userId = userContext.getUserId();
        return paymentService.createPayment(userId, dto);
    }

    @GetMapping("/{paymentId}")
    public PaymentDto getPayment(@PathVariable("paymentId") long id) {
        return paymentService.getPayment(id);
    }

    @GetMapping("/clear/{paymentId}")
    public void clearPayment(@PathVariable("paymentId") long id) {
        paymentService.clearPayment(id);
    }

    @GetMapping("/cancel/{paymentId}")
    public void cancelPayment(@PathVariable("paymentId") long id) {
        Long userId = userContext.getUserId();
        paymentService.cancelPayment(userId, id);
    }
}