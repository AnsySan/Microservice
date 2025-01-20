package com.clone.twitter.user_service.service.event.filter;

import com.clone.twitter.user_service.dto.event.EventFilterDto;
import com.clone.twitter.user_service.entity.event.Event;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class EventStartDateFilter implements EventFilter {
    @Override
    public boolean isAcceptable(EventFilterDto filterDto) {
        return filterDto.getStartDate() != null;
    }

    @Override
    public Stream<Event> apply(Stream<Event> events, EventFilterDto filters) {
        return events.filter(event -> event.getStartDate().isAfter(filters.getStartDate()));
    }
}
