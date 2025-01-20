package com.clone.twitter.project_service.publisher;

import com.clone.twitter.project_service.event.Event;

public interface MessagePublisher<T extends Event> {
    void publish(T event);
}
