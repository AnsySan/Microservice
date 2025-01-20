package com.clone.twitter.achievement_service.publisher;

import com.clone.twitter.achievement_service.event.AchievementReceivedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementPublisher implements MessagePublisher<AchievementReceivedEvent> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    @Override
    public void publish(AchievementReceivedEvent event) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), event);
        log.info("Published achievement event: {}", event);
    }
}
