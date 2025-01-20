package com.clone.twitter.payment_service.scheduler.payment;

import com.clone.twitter.payment_service.model.Payment;
import com.clone.twitter.payment_service.repository.PaymentRepository;
import com.clone.twitter.payment_service.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentClearingScheduler {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    @Scheduled(cron = "${scheduler.payments.clear.cron}")
    public void clearPayments() {
        List<Payment> payments = paymentRepository.findReadyToClearTransactions();

        if (!payments.isEmpty()) {
            for (Payment payment : payments) {
                try {
                    paymentService.clearPayment(payment.getId());
                } catch (Exception e) {
                    log.error("Failed to clear payment with ID {}: {}", payment.getId(), e.getMessage());
                }
            }
        }
    }
}
