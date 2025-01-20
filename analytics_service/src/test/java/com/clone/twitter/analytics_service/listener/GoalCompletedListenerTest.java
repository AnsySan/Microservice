package com.clone.twitter.analytics_service.listener;

import com.clone.twitter.analytics_service.event.GoalCompletedEvent;
import com.clone.twitter.analytics_service.mapper.GoalCompletedEventMapper;
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
class GoalCompletedListenerTest {

    @Mock
    private GoalCompletedEventMapper goalCompletedEventMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private GoalCompletedListener goalCompletedListener;

    private GoalCompletedEvent event;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        event = GoalCompletedEvent.builder()
                .goalId(1L)
                .userId(1L)
                .completedAt(LocalDateTime.now())
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

        when(objectMapper.readValue(body, GoalCompletedEvent.class)).thenReturn(event);
        when(goalCompletedEventMapper.toAnalyticsEvent(event)).thenReturn(analyticsEvent);

        goalCompletedListener.onMessage(message, pattern);

        InOrder inOrder = inOrder(analyticsService, goalCompletedEventMapper);
        inOrder.verify(goalCompletedEventMapper).toAnalyticsEvent(event);
        inOrder.verify(analyticsService).save(analyticsEvent);
    }
}