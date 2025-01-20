package com.clone.twitter.payment_service.publisher;

import com.clone.twitter.payment_service.event.NewPaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewPaymentPublisher implements MessagePublisher<NewPaymentEvent> {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final NewTopic newPaymentTopic;


    @Override
    public void publish(NewPaymentEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(newPaymentTopic.name(), message);
            log.info("Published new payment event to Kafka - {}: {}", newPaymentTopic.name(), message);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing profile view event", e);
        }
    }
}