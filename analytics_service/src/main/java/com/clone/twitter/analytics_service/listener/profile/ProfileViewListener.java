package com.clone.twitter.analytics_service.listener.profile;

import com.clone.twitter.analytics_service.event.profile.ProfileViewEvent;
import com.clone.twitter.analytics_service.listener.AbstractEventListener;
import com.clone.twitter.analytics_service.mapper.profile.ProfileViewEventMapper;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import com.clone.twitter.analytics_service.model.EventType;
import com.clone.twitter.analytics_service.service.AnalyticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class ProfileViewListener extends AbstractEventListener<ProfileViewEvent> {

    private final ProfileViewEventMapper profileViewEventMapper;
    private final AnalyticsService analyticsService;

    public ProfileViewListener(ObjectMapper objectMapper, ProfileViewEventMapper profileViewEventMapper, AnalyticsService analyticsService) {
        super(objectMapper);
        this.profileViewEventMapper = profileViewEventMapper;
        this.analyticsService = analyticsService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEvent.class, event -> {
            AnalyticsEvent analyticsEvent = profileViewEventMapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.PROFILE_VIEW);
            analyticsService.save(analyticsEvent);
        });
    }
}