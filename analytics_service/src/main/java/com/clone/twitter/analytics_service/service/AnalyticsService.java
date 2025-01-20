package com.clone.twitter.analytics_service.service;

import com.clone.twitter.analytics_service.dto.AnalyticsEventDto;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import com.clone.twitter.analytics_service.model.EventType;
import com.clone.twitter.analytics_service.model.Interval;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsService {

    void save(AnalyticsEvent event);

    List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to);
}
