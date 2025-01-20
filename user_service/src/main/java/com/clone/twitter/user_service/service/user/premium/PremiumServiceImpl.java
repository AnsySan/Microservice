package com.clone.twitter.user_service.service.user.premium;

import com.clone.twitter.user_service.client.PaymentServiceClient;
import com.clone.twitter.user_service.dto.payment.PaymentRequest;
import com.clone.twitter.user_service.dto.payment.PaymentResponse;
import com.clone.twitter.user_service.dto.payment.types.Currency;
import com.clone.twitter.user_service.dto.payment.types.PaymentStatus;
import com.clone.twitter.user_service.dto.types.PremiumPeriod;
import com.clone.twitter.user_service.entity.premium.Premium;
import com.clone.twitter.user_service.event.premium.PremiumBoughtEvent;
import com.clone.twitter.user_service.exception.PaymentException;
import com.clone.twitter.user_service.mapper.PremiumMapper;
import com.clone.twitter.user_service.publisher.premium.PremiumBoughtEventPublisher;
import com.clone.twitter.user_service.repository.premium.PremiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PremiumServiceImpl implements PremiumService {

    private final PremiumRepository premiumRepository;
    private final PremiumMapper premiumMapper;
    private final PaymentServiceClient paymentServiceClient;
    private final PremiumBoughtEventPublisher premiumBoughtEventPublisher;

    @Override
    @Transactional
    public void buyPremium(Long userId, PremiumPeriod premiumPeriod) {

        ResponseEntity<PaymentResponse> paymentResult = paymentServiceClient.sendPayment(new PaymentRequest(1, premiumPeriod.getPrice(), Currency.USD));

        if (!paymentResult.getStatusCode().equals(HttpStatusCode.valueOf(200))
                || !paymentResult.getBody().status().equals(PaymentStatus.SUCCESS)
        ) {
            throw new PaymentException("Payment declined");
        }

        if (!premiumRepository.existsByUserId(userId)) {
            Premium premium = premiumMapper.toEntity(userId, LocalDateTime.now(), LocalDateTime.now().plusDays(premiumPeriod.getDays()));
            premiumRepository.save(premium);
            return;
        }

        premiumRepository.findByUserId(userId)
                .ifPresentOrElse(
                        premium -> {
                            premium.setEndDate(premium.getEndDate().plusDays(premiumPeriod.getDays()));
                            premiumRepository.save(premium);
                        },
                        () -> {
                            Premium premium = premiumMapper.toEntity(userId, LocalDateTime.now(), LocalDateTime.now().plusDays(premiumPeriod.getDays()));
                            premiumRepository.save(premium);
                        }
                );

        PremiumBoughtEvent event = PremiumBoughtEvent.builder()
                .userId(userId)
                .amount(premiumPeriod.getPrice())
                .premiumPeriod(premiumPeriod.getDays())
                .boughtAt(LocalDateTime.now())
                .build();
        premiumBoughtEventPublisher.publish(event);
    }

    @Override
    @Transactional
    public void deleteAllExpiredPremium() {
        List<Long> expiredIds = premiumRepository.findAllExpiredIds();
        premiumRepository.deleteAllById(expiredIds);
    }
}
