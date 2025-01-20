package com.clone.twitter.post_service.redis.pubsub.publisher;

import com.clone.twitter.post_service.redis.pubsub.event.RedisEvent;

public interface RedisPublisher<T extends RedisEvent> {
    void publish(T event);
}
