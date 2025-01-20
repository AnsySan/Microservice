package com.clone.twitter.post_service.kafka.consumer.feed_heater;

import com.clone.twitter.post_service.kafka.consumer.KafkaConsumer;
import com.clone.twitter.post_service.kafka.event.feed_heater.FeedHeaterEvent;
import com.clone.twitter.post_service.service.feed.heater.FeedHeaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedHeaterConsumer implements KafkaConsumer<FeedHeaterEvent> {

    private final FeedHeaterService feedHeaterService;

    @Override
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.feed-heater.name}", groupId = "${spring.data.kafka.group-id}")
    public void consume(FeedHeaterEvent event, Acknowledgment ack) {

        log.info("Received new comment like event {}", event);

        feedHeaterService.handleBatch(event.getUsersAndSubscribers());

        ack.acknowledge();
    }
}
