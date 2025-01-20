package com.clone.twitter.achievement_service.handler;

import com.clone.twitter.achievement_service.event.Event;

public interface EventHandler<T extends Event> {
    void handle(T event);
}
