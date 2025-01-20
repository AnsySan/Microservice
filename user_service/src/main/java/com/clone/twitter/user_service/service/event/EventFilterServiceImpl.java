package com.clone.twitter.user_service.service.event;

import com.clone.twitter.user_service.dto.event.EventFilterDto;
import com.clone.twitter.user_service.entity.event.Event;
import com.clone.twitter.user_service.service.event.filter.EventFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EventFilterServiceImpl implements EventFilterService {
    private final List<EventFilter> filters;

    @Override
    public Stream<Event> apply(Stream<Event> events, EventFilterDto filterDto) {
        return filters.stream()
                .filter(filter -> filter.isAcceptable(filterDto))
                .flatMap(filter -> filter.apply(events, filterDto));
    }
}
