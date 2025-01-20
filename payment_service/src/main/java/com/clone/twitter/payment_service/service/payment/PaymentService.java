package com.clone.twitter.payment_service.service.payment;

import com.clone.twitter.payment_service.dto.payment.PaymentDto;
import com.clone.twitter.payment_service.dto.payment.PaymentDtoToCreate;
import org.springframework.stereotype.Component;

@Component
public interface PaymentService {

   Long createPayment(Long userId, PaymentDtoToCreate dto);

    PaymentDto getPayment(Long id);

    void cancelPayment(Long userId, Long paymentId);

    void clearPayment(Long paymentId);
}