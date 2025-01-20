package com.clone.twitter.user_service.service.event;


import com.clone.twitter.user_service.dto.event.EventFilterDto;
import com.clone.twitter.user_service.entity.event.Event;

import java.util.stream.Stream;

public interface EventFilterService {
    Stream<Event> apply(Stream<Event> events, EventFilterDto filterDto);
}
