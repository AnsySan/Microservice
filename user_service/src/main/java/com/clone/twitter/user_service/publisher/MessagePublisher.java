package com.clone.twitter.user_service.publisher;

public interface MessagePublisher<T> {
    void publish(T event);
}
