package com.clone.twitter.payment_service.validator.payment;

import com.clone.twitter.payment_service.dto.payment.PaymentDtoToCreate;
import com.clone.twitter.payment_service.enums.PaymentStatus;
import com.clone.twitter.payment_service.exception.DataValidationException;
import com.clone.twitter.payment_service.model.Balance;
import com.clone.twitter.payment_service.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentValidator {

    public void validatePaymentOnSameIdempotencyToken(PaymentDtoToCreate dto, Payment payment) {
        if (!checkPaymentWithSameUUID(dto, payment)) {
            throw new DataValidationException("This payment has already been made with other details! Try again!");
        }
    }

    public void validateSenderHaveEnoughMoneyOnAuthorizationBalance(Balance senderBalance, PaymentDtoToCreate dto) {
        if (senderBalance.getAuthorizationBalance().compareTo(dto.getAmount()) < 0) {
            throw new DataValidationException("Not enough money");
        }
    }

    public void validatePaymentStatusIsAlreadyCorrect(Payment payment, PaymentStatus status) {
        if(payment.getPaymentStatus().equals(status)) {
            throw new DataValidationException(
                    String.format("Payment status is already %s", payment.getPaymentStatus()));
        }
    }

    private boolean checkPaymentWithSameUUID(PaymentDtoToCreate newPayment, Payment oldPayment) {
        return newPayment.getSenderAccountNumber().equals(oldPayment.getSenderAccountNumber())
                && newPayment.getReceiverAccountNumber().equals(oldPayment.getReceiverAccountNumber())
                && newPayment.getAmount().compareTo(oldPayment.getAmount()) == 0
                && newPayment.getCurrency() == oldPayment.getCurrency();
    }

    public void validateNumbersAreDifferent(PaymentDtoToCreate dto) {
        if(Objects.equals(dto.getSenderAccountNumber(), dto.getReceiverAccountNumber())){
            throw new DataValidationException("Sender and Receiver number should be different");
        }
    }
}
