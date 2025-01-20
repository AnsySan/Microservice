package com.clone.twitter.notification_service.listener.profile;

import com.clone.twitter.notification_service.client.UserServiceClient;
import com.clone.twitter.notification_service.event.profile.ProfileViewEvent;
import com.clone.twitter.notification_service.exception.ListenerException;
import com.clone.twitter.notification_service.listener.AbstractEventListener;
import com.clone.twitter.notification_service.messaging.MessageBuilder;
import com.clone.twitter.notification_service.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class ProfileViewListener extends AbstractEventListener<ProfileViewEvent> {

    public ProfileViewListener(ObjectMapper objectMapper, MessageBuilder<ProfileViewEvent> messageBuilder, List<NotificationService> notificationServices, UserServiceClient userServiceClient) {
        super(objectMapper, messageBuilder, notificationServices, userServiceClient);
    }

    @KafkaListener(topics = "${spring.data.channel.profile-view.name}", groupId = "${spring.data.kafka.group-id}")
    public void listen(String event) {
        if (event == null || event.trim().isEmpty()) {
            log.error("Received empty or null event");
            return;
        }

        try {
            ProfileViewEvent profileViewEvent = objectMapper.readValue(event, ProfileViewEvent.class);
            log.info("Received new profileViewEvent {}", event);
                sendNotification(profileViewEvent.getUserId(), getMessage(profileViewEvent, Locale.ENGLISH));
        } catch (JsonProcessingException e) {
            log.error("Error processing event JSON: {}", event, e);
            throw new SerializationException(e);
        } catch (Exception e) {
            throw new ListenerException(e.getMessage());
        }
    }
}
