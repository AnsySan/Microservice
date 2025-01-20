package com.clone.twitter.notification_service.messaging;

import com.clone.twitter.notification_service.client.UserServiceClient;
import com.clone.twitter.notification_service.event.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        return messageSource.getMessage("notification.new_like", new Object[]{event.getPostId(), userServiceClient.getUser(event.getUserId()).getUsername()}, locale);
    }
}
