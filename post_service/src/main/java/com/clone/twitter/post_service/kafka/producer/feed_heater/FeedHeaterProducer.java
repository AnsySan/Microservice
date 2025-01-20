package com.clone.twitter.post_service.kafka.producer.feed_heater;

import com.clone.twitter.post_service.kafka.event.feed_heater.FeedHeaterEvent;
import com.clone.twitter.post_service.kafka.producer.AbstractKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class FeedHeaterProducer extends AbstractKafkaProducer<FeedHeaterEvent> {

    @Value("${spring.data.kafka.topics.topic-settings.feed-heater.name}")
    private String channelTopic;

    public FeedHeaterProducer(KafkaTemplate<String, Object> kafkaTemplate, Map<String, NewTopic> topicMap) {
        super(kafkaTemplate, topicMap);
    }

    @Override
    public String getTopic() {
        return channelTopic;
    }
}
