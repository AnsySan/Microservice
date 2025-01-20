package com.clone.twitter.user_service.service.user.premium;


import com.clone.twitter.user_service.dto.types.PremiumPeriod;

import java.util.List;

public interface PremiumService {

    void buyPremium(Long userId, PremiumPeriod premiumPeriod);

    void deleteAllExpiredPremium();
}
