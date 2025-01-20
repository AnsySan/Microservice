package com.clone.twitter.analytics_service.service;

import com.clone.twitter.analytics_service.dto.AnalyticsEventDto;
import com.clone.twitter.analytics_service.mapper.AnalyticsEventMapper;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import com.clone.twitter.analytics_service.model.EventType;
import com.clone.twitter.analytics_service.model.Interval;
import com.clone.twitter.analytics_service.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsServiceImpl analyticsServiceImpl;

    private final long receiverId = 1L;
    private final EventType eventType = EventType.GOAL_COMPLETED;
    private AnalyticsEventDto analyticsEventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        analyticsEvent = AnalyticsEvent.builder()
                .id(4L)
                .actorId(1L)
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(LocalDateTime.now())
                .build();

        analyticsEventDto = AnalyticsEventDto.builder()
                .id(4L)
                .actorId(1L)
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void save() {
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);

        analyticsServiceImpl.save(analyticsEvent);

        InOrder inOrder = inOrder(analyticsEventRepository, analyticsEventMapper);
        inOrder.verify(analyticsEventRepository).save(analyticsEvent);
    }

    @Test
    void getAnalyticsEventsNotNullInterval() {
        Interval interval = Interval.MONTH;

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(Stream.of(analyticsEvent));
        when(analyticsEventMapper.toDto(analyticsEvent)).thenReturn(analyticsEventDto);

        List<AnalyticsEventDto> actual = analyticsServiceImpl.getAnalytics(receiverId, eventType, interval, null, null);
        assertIterableEquals(List.of(analyticsEventDto), actual);

        InOrder inOrder = inOrder(analyticsEventRepository, analyticsEventMapper);
        inOrder.verify(analyticsEventRepository).findByReceiverIdAndEventType(receiverId, eventType);
        inOrder.verify(analyticsEventMapper).toDto(analyticsEvent);
    }

    @Test
    void getAnalyticsEventsNullInterval() {
        LocalDateTime from = LocalDateTime.now().minusMonths(3);
        LocalDateTime to = LocalDateTime.now().minusMonths(1);
        analyticsEvent.setReceivedAt(from.plusMonths(1));

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(Stream.of(analyticsEvent));
        when(analyticsEventMapper.toDto(analyticsEvent)).thenReturn(analyticsEventDto);

        List<AnalyticsEventDto> actual = analyticsServiceImpl.getAnalytics(receiverId, eventType, null, from, to);
        assertIterableEquals(List.of(analyticsEventDto), actual);

        InOrder inOrder = inOrder(analyticsEventRepository, analyticsEventMapper);
        inOrder.verify(analyticsEventRepository).findByReceiverIdAndEventType(receiverId, eventType);
        inOrder.verify(analyticsEventMapper).toDto(analyticsEvent);
    }
}