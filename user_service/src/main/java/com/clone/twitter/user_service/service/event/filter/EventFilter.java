package com.clone.twitter.user_service.service.event.filter;


import com.clone.twitter.user_service.dto.event.EventFilterDto;
import com.clone.twitter.user_service.entity.event.Event;

import java.util.stream.Stream;

public interface EventFilter {
    boolean isAcceptable(EventFilterDto filterDto);

    Stream<Event> apply(Stream<Event> events, EventFilterDto filters);
}
