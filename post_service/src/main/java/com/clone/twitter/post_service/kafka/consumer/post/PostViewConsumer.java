package com.clone.twitter.post_service.kafka.consumer.post;

import com.clone.twitter.post_service.kafka.consumer.KafkaConsumer;
import com.clone.twitter.post_service.kafka.event.post.PostViewEvent;
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
public class PostViewConsumer implements KafkaConsumer<PostViewEvent> {

    private final PostCacheService postCacheService;

    @Override
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.post-views.name}", groupId = "${spring.data.kafka.group-id}")
    public void consume(@Payload PostViewEvent event, Acknowledgment ack) {

        log.info("Received new post view event {}", event);

        postCacheService.incrementViews(event.getPostId());

        ack.acknowledge();
    }
}
