package com.clone.twitter.post_service.redis.pubsub.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserBanEvent implements RedisEvent {
    private List<Long> userIds;
}
