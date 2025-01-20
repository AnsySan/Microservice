package com.clone.twitter.user_service.validator;


import com.clone.twitter.user_service.dto.subscription.SubscriptionRequestDto;

public interface SubscriptionValidator {

    void validateSubscriptionExistence(SubscriptionRequestDto subscriptionRequestDto);

    void validateFollowerAndFolloweeIds(SubscriptionRequestDto subscriptionRequestDto);
}
