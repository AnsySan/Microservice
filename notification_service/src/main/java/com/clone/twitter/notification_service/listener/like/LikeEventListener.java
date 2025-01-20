package com.clone.twitter.notification_service.listener.like;

import com.clone.twitter.notification_service.client.UserServiceClient;
import com.clone.twitter.notification_service.event.LikeEvent;
import com.clone.twitter.notification_service.listener.AbstractEventListener;
import com.clone.twitter.notification_service.messaging.MessageBuilder;
import com.clone.twitter.notification_service.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {

    public LikeEventListener(ObjectMapper objectMapper,
                             MessageBuilder<LikeEvent> messageBuilder,
                             List<NotificationService> notificationServices,
                             UserServiceClient userServiceClient) {
        super(objectMapper, messageBuilder, notificationServices, userServiceClient);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, LikeEvent.class, event -> {
            String text = getMessage(event, Locale.getDefault());
            sendNotification(event.getAuthorId(), text);
        });
    }
}
