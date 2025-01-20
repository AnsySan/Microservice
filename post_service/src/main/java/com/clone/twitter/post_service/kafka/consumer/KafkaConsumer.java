package com.clone.twitter.post_service.kafka.consumer;

import com.clone.twitter.post_service.kafka.event.KafkaEvent;
import org.springframework.kafka.support.Acknowledgment;

public interface KafkaConsumer<T extends KafkaEvent> {

    void consume(T event, Acknowledgment ack);
}
