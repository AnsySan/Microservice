package com.clone.twitter.user_service.publisher;

import com.clone.twitter.user_service.event.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompletedGoalPublisher implements MessagePublisher<GoalCompletedEvent> {

    @Value("${spring.data.channel.goal_complete.name}")
    private String channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(GoalCompletedEvent event) {
        redisTemplate.convertAndSend(channelTopic, event);
        log.info("Published goal completed event - {}:{}", channelTopic, event);
    }
}
