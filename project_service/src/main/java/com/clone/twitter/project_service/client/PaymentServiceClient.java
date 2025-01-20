package com.clone.twitter.project_service.client;

import com.clone.twitter.project_service.dto.client.PaymentRequest;
import com.clone.twitter.project_service.dto.client.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "${services.payment-service.host}:${services.payment-service.port}")
public interface PaymentServiceClient {

    @PostMapping("api/v1/payments/payment")
    PaymentResponse sendPayment(@RequestBody PaymentRequest paymentRequest);
}
