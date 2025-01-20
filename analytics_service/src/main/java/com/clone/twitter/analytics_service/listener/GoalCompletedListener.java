package com.clone.twitter.analytics_service.listener;

import com.clone.twitter.analytics_service.event.GoalCompletedEvent;
import com.clone.twitter.analytics_service.mapper.GoalCompletedEventMapper;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import com.clone.twitter.analytics_service.model.EventType;
import com.clone.twitter.analytics_service.service.AnalyticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class GoalCompletedListener extends AbstractEventListener<GoalCompletedEvent> {

    private final GoalCompletedEventMapper mapper;
    private final AnalyticsService analyticsService;

    public GoalCompletedListener(ObjectMapper objectMapper,
                                 GoalCompletedEventMapper mapper,
                                 AnalyticsService analyticsService) {
        super(objectMapper);
        this.mapper = mapper;
        this.analyticsService = analyticsService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, GoalCompletedEvent.class, event -> {
            AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.GOAL_COMPLETED);
            analyticsService.save(analyticsEvent);
        });
    }
}
