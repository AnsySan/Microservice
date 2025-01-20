package com.clone.twitter.notification_service.service.telegram;

import com.clone.twitter.notification_service.dto.UserDto;
import com.clone.twitter.notification_service.entity.PreferredContact;
import com.clone.twitter.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramNotificationBot telegramNotificationBot;

    @Override
    public void send(UserDto user, String message) {
        telegramNotificationBot.sendMessage(user.getId(), message);
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.TELEGRAM;
    }
}
