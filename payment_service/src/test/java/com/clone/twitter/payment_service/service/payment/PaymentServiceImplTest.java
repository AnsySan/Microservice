package com.clone.twitter.payment_service.service.payment;

import com.clone.twitter.payment_service.dto.payment.PaymentDto;
import com.clone.twitter.payment_service.dto.payment.PaymentDtoToCreate;
import com.clone.twitter.payment_service.event.CancelPaymentEvent;
import com.clone.twitter.payment_service.event.ClearPaymentEvent;
import com.clone.twitter.payment_service.event.NewPaymentEvent;
import com.clone.twitter.payment_service.exception.NotFoundException;
import com.clone.twitter.payment_service.mapper.PaymentMapper;
import com.clone.twitter.payment_service.model.Balance;
import com.clone.twitter.payment_service.model.Payment;
import com.clone.twitter.payment_service.publisher.CancelPaymentPublisher;
import com.clone.twitter.payment_service.publisher.ClearPaymentPublisher;
import com.clone.twitter.payment_service.publisher.NewPaymentPublisher;
import com.clone.twitter.payment_service.repository.BalanceRepository;
import com.clone.twitter.payment_service.repository.PaymentRepository;
import com.clone.twitter.payment_service.validator.payment.PaymentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentServiceImplTest {

    @Mock
    private BalanceRepository balanceRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private NewPaymentPublisher newPaymentPublisher;
    @Mock
    private CancelPaymentPublisher cancelPaymentPublisher;
    @Mock
    private ClearPaymentPublisher clearPaymentPublisher;
    @Mock
    private PaymentValidator paymentValidator;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPayment_ShouldCreatePaymentSuccessfully() {
        Long userId = 1L;
        PaymentDtoToCreate dto = new PaymentDtoToCreate();
        dto.setIdempotencyKey(UUID.randomUUID());
        dto.setSenderAccountNumber("12345");
        dto.setReceiverAccountNumber("67890");
        dto.setAmount(new BigDecimal("100.00"));

        Balance senderBalance = new Balance();
        senderBalance.setAuthorizationBalance(new BigDecimal("200.00"));

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setIdempotencyKey(dto.getIdempotencyKey());

        when(balanceRepository.findBalanceByAccountNumber(dto.getSenderAccountNumber())).thenReturn(Optional.of(senderBalance));
        when(balanceRepository.findBalanceByAccountNumber(dto.getReceiverAccountNumber())).thenReturn(Optional.of(new Balance()));
        when(paymentRepository.findPaymentByIdempotencyKey(dto.getIdempotencyKey())).thenReturn(Optional.empty());
        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);

        Long paymentId = paymentService.createPayment(userId, dto);

        assertEquals(payment.getId(), paymentId);
        verify(newPaymentPublisher, times(1)).publish(any(NewPaymentEvent.class));
    }

    @Test
    void getPayment_ShouldReturnPaymentDto() {
        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(paymentId);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentMapper.toDto(payment)).thenReturn(paymentDto);

        PaymentDto result = paymentService.getPayment(paymentId);

        assertEquals(paymentDto, result);
    }

    @Test
    void getPayment_ShouldThrowNotFoundExceptionWhenPaymentNotFound() {
        Long paymentId = 1L;

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.getPayment(paymentId));
    }

    @Test
    void cancelPayment_ShouldCancelPayment() {
        Long userId = 1L;
        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setId(paymentId);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        paymentService.cancelPayment(userId, paymentId);

        verify(cancelPaymentPublisher, times(1)).publish(any(CancelPaymentEvent.class));
    }

    @Test
    void cancelPayment_ShouldThrowNotFoundExceptionWhenPaymentNotFound() {
        Long paymentId = 1L;

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.cancelPayment(1L, paymentId));
    }

    @Test
    void clearPayment_ShouldClearPayment() {
        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setId(paymentId);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        paymentService.clearPayment(paymentId);

        verify(clearPaymentPublisher, times(1)).publish(any(ClearPaymentEvent.class));
    }

    @Test
    void clearPayment_ShouldThrowNotFoundExceptionWhenPaymentNotFound() {
        Long paymentId = 1L;

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.clearPayment(paymentId));
    }
}
