package com.clone.twitter.user_service.publisher.premium;

import com.clone.twitter.user_service.event.premium.PremiumBoughtEvent;
import com.clone.twitter.user_service.publisher.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumBoughtEventPublisher implements MessagePublisher<PremiumBoughtEvent> {

    @Value("${spring.data.channel.premium_bought.name}")
    private String topic;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(PremiumBoughtEvent event) {
        redisTemplate.convertAndSend(topic, event);
        log.info("Published premium bought event - {}:{}", topic, event);
    }
}
