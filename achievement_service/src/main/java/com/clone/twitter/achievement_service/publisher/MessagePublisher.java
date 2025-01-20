package com.clone.twitter.achievement_service.publisher;

public interface MessagePublisher<T> {
    void publish(T event);
}
