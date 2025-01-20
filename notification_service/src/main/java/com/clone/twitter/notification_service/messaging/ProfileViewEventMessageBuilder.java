package com.clone.twitter.notification_service.messaging;

import com.clone.twitter.notification_service.client.UserServiceClient;
import com.clone.twitter.notification_service.dto.UserDto;
import com.clone.twitter.notification_service.event.profile.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewEventMessageBuilder implements MessageBuilder<ProfileViewEvent> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public String buildMessage(ProfileViewEvent event, Locale locale) {
        UserDto viewer = userServiceClient.getUser(event.getViewerId());
        String defaultMessage = messageSource.getMessage("profile.view", new Object[]{viewer.getUsername()}, Locale.ENGLISH);
        return messageSource.getMessage("profile.view", new Object[]{viewer.getUsername()}, defaultMessage, locale);
    }

    @Override
    public Class<?> getInstance() {
        return ProfileViewEvent.class;
    }
}