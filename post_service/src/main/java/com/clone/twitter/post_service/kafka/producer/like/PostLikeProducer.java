package com.clone.twitter.post_service.kafka.producer.like;

import com.clone.twitter.post_service.kafka.event.like.PostLikeEvent;
import com.clone.twitter.post_service.kafka.producer.AbstractKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class PostLikeProducer extends AbstractKafkaProducer<PostLikeEvent> {

    @Value("${spring.data.kafka.topics.topic-settings.post-likes.name}")
    private String channelTopic;

    public PostLikeProducer(KafkaTemplate<String, Object> kafkaTemplate,
                            Map<String, NewTopic> topicMap) {
        super(kafkaTemplate, topicMap);
    }

    @Override
    public String getTopic() {
        return channelTopic;
    }
}
