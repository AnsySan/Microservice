package com.clone.twitter.notification_service.service.telegram;

import com.clone.twitter.notification_service.entity.TelegramProfile;

import java.util.Optional;

public interface TelegramProfileService {

    void save(TelegramProfile telegramProfile);

    TelegramProfile findByUserId(Long userId);

    Optional<TelegramProfile> findByChatId(long chatId);

    boolean existsByChatId(long chatId);
}
