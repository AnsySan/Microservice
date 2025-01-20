package com.clone.twitter.payment_service.publisher;

import com.clone.twitter.payment_service.event.Event;

public interface MessagePublisher<T extends Event> {
    void publish(T event);
}
