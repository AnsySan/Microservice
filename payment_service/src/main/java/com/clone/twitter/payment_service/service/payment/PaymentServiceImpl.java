package com.clone.twitter.payment_service.service.payment;

import com.clone.twitter.payment_service.dto.payment.PaymentDto;
import com.clone.twitter.payment_service.dto.payment.PaymentDtoToCreate;
import com.clone.twitter.payment_service.enums.PaymentStatus;
import com.clone.twitter.payment_service.event.CancelPaymentEvent;
import com.clone.twitter.payment_service.event.ClearPaymentEvent;
import com.clone.twitter.payment_service.event.NewPaymentEvent;
import com.clone.twitter.payment_service.exception.NotFoundException;
import com.clone.twitter.payment_service.exception.PaymentException;
import com.clone.twitter.payment_service.mapper.PaymentMapper;
import com.clone.twitter.payment_service.model.Balance;
import com.clone.twitter.payment_service.model.Payment;
import com.clone.twitter.payment_service.publisher.CancelPaymentPublisher;
import com.clone.twitter.payment_service.publisher.ClearPaymentPublisher;
import com.clone.twitter.payment_service.publisher.NewPaymentPublisher;
import com.clone.twitter.payment_service.repository.BalanceRepository;
import com.clone.twitter.payment_service.repository.PaymentRepository;
import com.clone.twitter.payment_service.validator.payment.PaymentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BalanceRepository balanceRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final NewPaymentPublisher newPaymentPublisher;
    private final CancelPaymentPublisher cancelPaymentPublisher;
    private final ClearPaymentPublisher clearPaymentPublisher;
    private final PaymentValidator paymentValidator;

    @Override
    @Transactional
    public Long createPayment(Long userId, PaymentDtoToCreate dto) {
        UUID idempotencyKey = dto.getIdempotencyKey();
        log.info("Got idempotency key");

        try {
            Balance senderBalance = balanceRepository.findBalanceByAccountNumber(dto.getSenderAccountNumber())
                    .orElseThrow(() -> new NotFoundException("Sender balance hasn't been found"));

            balanceRepository.findBalanceByAccountNumber(dto.getReceiverAccountNumber())
                    .orElseThrow(() -> new NotFoundException("Receiver balance hasn't been found"));
            paymentValidator.validateNumbersAreDifferent(dto);
            paymentValidator.validateSenderHaveEnoughMoneyOnAuthorizationBalance(senderBalance, dto);

            Optional<Payment> optionalPayment = paymentRepository.findPaymentByIdempotencyKey(idempotencyKey);
            if (optionalPayment.isPresent()) {
                Payment payment = optionalPayment.get();
                paymentValidator.validatePaymentOnSameIdempotencyToken(dto, payment);
                log.info("Payment with UUID={} is idempotency and already been processed", idempotencyKey);
                return payment.getId();
            }

            Payment payment = paymentMapper.toEntity(dto);
            payment.setPaymentStatus(PaymentStatus.NEW);
            payment.setScheduledAt(LocalDateTime.now().plusHours(8));
            payment = paymentRepository.save(payment);
            log.info("Payment with UUID={} was saved in DB successfully", idempotencyKey);

            NewPaymentEvent event = new NewPaymentEvent(userId, payment.getId());
            newPaymentPublisher.publish(event);

            return payment.getId();
        } catch (Exception e) {
            log.error("Error occurred while creating payment: ", e);
            saveFailedPayment(dto);
            throw new PaymentException(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedPayment(PaymentDtoToCreate dto) {
        Payment failedPayment = paymentMapper.toEntity(dto);
        failedPayment.setPaymentStatus(PaymentStatus.FAILURE);
        paymentRepository.save(failedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPayment(Long id) {
        return paymentMapper.toDto(paymentRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("Not found payment with id %d", id))));
    }

    @Override
    @Transactional(readOnly = true)
    public void cancelPayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("This payment doesn't exist"));

        paymentValidator.validatePaymentStatusIsAlreadyCorrect(payment, PaymentStatus.CANCELED);

        CancelPaymentEvent event = new CancelPaymentEvent(userId, paymentId);

        cancelPaymentPublisher.publish(event);
    }

    @Transactional(readOnly = true)
    public void clearPayment(Long paymentId) {
        paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("This payment doesn't exist"));

        ClearPaymentEvent event = new ClearPaymentEvent(paymentId);

        clearPaymentPublisher.publish(event);
    }
}