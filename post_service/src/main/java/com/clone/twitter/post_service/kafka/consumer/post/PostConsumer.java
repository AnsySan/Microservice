package com.clone.twitter.post_service.kafka.consumer.post;

import com.clone.twitter.post_service.kafka.consumer.KafkaConsumer;
import com.clone.twitter.post_service.kafka.event.post.PostEvent;
import com.clone.twitter.post_service.mapper.PostMapper;
import com.clone.twitter.post_service.redis.cache.service.post.PostCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostConsumer implements KafkaConsumer<PostEvent> {

    private final PostMapper postMapper;
    private final PostCacheService postCacheService;

    @Override
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.posts.name}", groupId = "${spring.data.kafka.group-id}")
    public void consume(@Payload PostEvent event, Acknowledgment ack) {

        log.info("Received new post event {}", event);

        switch (event.getState()) {
            case ADD, UPDATE -> postCacheService.save(postMapper.toRedisCache(event));
            case DELETE -> postCacheService.deleteById(event.getPostId());
        }

        ack.acknowledge();
    }
}
