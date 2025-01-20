package com.clone.twitter.payment_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class NewPaymentEvent implements Event {
    private Long userId;
    private Long paymentId;
}