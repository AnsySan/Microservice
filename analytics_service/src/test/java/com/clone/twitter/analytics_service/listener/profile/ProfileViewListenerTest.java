package com.clone.twitter.analytics_service.listener.profile;

import com.clone.twitter.analytics_service.event.profile.ProfileViewEvent;
import com.clone.twitter.analytics_service.mapper.profile.ProfileViewEventMapper;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import com.clone.twitter.analytics_service.service.AnalyticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileViewListenerTest {
    @Mock
    private ProfileViewEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsService analyticsService;
    @InjectMocks
    private ProfileViewListener profileViewListener;

    private ProfileViewEvent event;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        event = ProfileViewEvent.builder()
                .viewerId(String.valueOf(1L))
                .userId(String.valueOf(1L))
                .viewedAt(LocalDateTime.now())
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .id(4L)
                .actorId(1L)
                .receiverId(1L)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void onMessage() throws IOException {
        byte[] body = new byte[]{2};
        Message message = new DefaultMessage(new byte[]{1}, body);
        byte[] pattern = new byte[]{3};
        when(objectMapper.readValue(body, ProfileViewEvent.class)).thenReturn(event);
        when(analyticsEventMapper.toAnalyticsEvent(event)).thenReturn(analyticsEvent);
        profileViewListener.onMessage(message, pattern);
        InOrder inOrder = inOrder(analyticsService, analyticsEventMapper);
        inOrder.verify(analyticsEventMapper).toAnalyticsEvent(event);
        inOrder.verify(analyticsService).save(analyticsEvent);
    }
}