package com.clone.twitter.analytics_service.listener.like;

import com.clone.twitter.analytics_service.event.LikeEvent;
import com.clone.twitter.analytics_service.mapper.LikeEventMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {
    @Mock
    private LikeEventMapper likeEventMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private LikeEventListener likeListener;

    private LikeEvent event;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        event = LikeEvent.builder()
                .postId(1L)
                .authorId(1L)
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

        when(objectMapper.readValue(body, LikeEvent.class)).thenReturn(event);
        when(likeEventMapper.toAnalyticsEvent(event)).thenReturn(analyticsEvent);
        doNothing().when(analyticsService).save(analyticsEvent);

        likeListener.onMessage(message, pattern);

        InOrder inOrder = inOrder(analyticsService, likeEventMapper, objectMapper);
        inOrder.verify(objectMapper, times(1)).readValue(any(byte[].class), eq(LikeEvent.class));
        inOrder.verify(likeEventMapper, times(1)).toAnalyticsEvent(event);
        inOrder.verify(analyticsService, times(1)).save(analyticsEvent);
    }

}
