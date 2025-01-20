package com.clone.twitter.analytics_service.service;

import com.clone.twitter.analytics_service.dto.AnalyticsEventDto;
import com.clone.twitter.analytics_service.mapper.AnalyticsEventMapper;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import com.clone.twitter.analytics_service.model.EventType;
import com.clone.twitter.analytics_service.model.Interval;
import com.clone.twitter.analytics_service.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    @Transactional
    public void save(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Saved event: {}", event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {

        Stream<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        if (interval != null) {
            LocalDateTime fromDate = Interval.getFromDate(interval);
            analyticsEvents = analyticsEvents.filter(event -> event.getReceivedAt().isAfter(fromDate));

        } else {
            analyticsEvents = analyticsEvents.filter(event -> event.getReceivedAt().isAfter(from) && event.getReceivedAt().isBefore(to));
        }

        return analyticsEvents
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(analyticsEventMapper::toDto)
                .toList();
    }
}
