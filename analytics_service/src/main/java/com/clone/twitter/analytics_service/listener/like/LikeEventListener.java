package com.clone.twitter.analytics_service.listener.like;

import com.clone.twitter.analytics_service.event.LikeEvent;
import com.clone.twitter.analytics_service.listener.AbstractEventListener;
import com.clone.twitter.analytics_service.mapper.LikeEventMapper;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import com.clone.twitter.analytics_service.service.AnalyticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> {
    private final LikeEventMapper mapper;
    private final AnalyticsService analyticsService;

    @Autowired
    public LikeEventListener(ObjectMapper objectMapper, LikeEventMapper mapper, AnalyticsService analyticsService) {
        super(objectMapper);
        this.mapper = mapper;
        this.analyticsService = analyticsService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, LikeEvent.class, event -> {
            AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(event);
            analyticsService.save(analyticsEvent);
        });
    }
}
