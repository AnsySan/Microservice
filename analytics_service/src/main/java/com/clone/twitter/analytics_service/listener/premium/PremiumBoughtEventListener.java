package com.clone.twitter.analytics_service.listener.premium;

import com.clone.twitter.analytics_service.event.premium.PremiumBoughtEvent;
import com.clone.twitter.analytics_service.listener.AbstractEventListener;
import com.clone.twitter.analytics_service.mapper.premium.PremiumBoughtEventMapper;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import com.clone.twitter.analytics_service.model.EventType;
import com.clone.twitter.analytics_service.service.AnalyticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class PremiumBoughtEventListener extends AbstractEventListener<PremiumBoughtEvent> {

    private final PremiumBoughtEventMapper premiumBoughtEventMapper;
    private final AnalyticsService analyticsService;

    public PremiumBoughtEventListener(ObjectMapper objectMapper, PremiumBoughtEventMapper premiumBoughtEventMapper, AnalyticsService analyticsService) {
        super(objectMapper);
        this.premiumBoughtEventMapper = premiumBoughtEventMapper;
        this.analyticsService = analyticsService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, PremiumBoughtEvent.class, event -> {
            AnalyticsEvent analyticsEvent = premiumBoughtEventMapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.PREMIUM_BOUGHT_EVENT);
            analyticsService.save(analyticsEvent);
        });
    }
}
