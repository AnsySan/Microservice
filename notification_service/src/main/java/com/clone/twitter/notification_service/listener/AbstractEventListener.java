package com.clone.twitter.notification_service.listener;

import com.clone.twitter.notification_service.client.UserServiceClient;
import com.clone.twitter.notification_service.dto.UserDto;
import com.clone.twitter.notification_service.exception.DeserializeException;
import com.clone.twitter.notification_service.messaging.MessageBuilder;
import com.clone.twitter.notification_service.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    private final MessageBuilder<T> messageBuilder;
    private final List<NotificationService> notificationServices;
    private final UserServiceClient userServiceClient;

    protected String getMessage(T event, Locale locale) {
        return messageBuilder.buildMessage(event, locale);
    }

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
            log.info("Received event from channel: {}", new String(message.getChannel(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("Error deserializing JSON to object: ", e);
            throw new DeserializeException("Error deserializing JSON to object: " + e.getMessage());
        }
    }

    protected void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreferredContact()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Preferred contact not found"))
                .send(userDto, message);
    }
}
