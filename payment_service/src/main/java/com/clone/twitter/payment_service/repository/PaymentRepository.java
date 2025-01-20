package com.clone.twitter.payment_service.repository;

import com.clone.twitter.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findPaymentByIdempotencyKey(UUID idempotencyKey);

    @Query(value = "SELECT * FROM payment WHERE scheduled_at <= NOW() AND payment_status = 'READY_TO_CLEAR' ORDER BY scheduled_at LIMIT 10", nativeQuery = true)
    List<Payment> findReadyToClearTransactions();
}