package com.clone.twitter.notification_service.service.telegram.command;

import com.clone.twitter.notification_service.client.UserServiceClient;
import com.clone.twitter.notification_service.entity.TelegramProfile;
import com.clone.twitter.notification_service.service.telegram.TelegramProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@Slf4j
@Component(value = "/start")
public class StartCommand extends Command {

    public StartCommand(MessageSource messageSource,
                        UserServiceClient userServiceClient,
                        TelegramProfileService telegramProfileService,
                        CommandBuilder commandBuilder) {
        super(messageSource, userServiceClient, telegramProfileService, commandBuilder);
    }

    @Override
    public SendMessage sendMessage(long chatId, String userName) {
        log.info("Executing START command for chatId: {} with userName: {}", chatId, userName);

        Optional<TelegramProfile> optionalProfile = telegramProfileService.findByChatId(chatId);

        String message;
        if (optionalProfile.isEmpty()) {
            message = messageSource.getMessage("telegram.not_registered_corporationX", null, defaultLocale);
            return commandBuilder.buildMessage(chatId, message);
        }

        TelegramProfile profile = optionalProfile.get();
        profile.setActive(!profile.isActive());
        telegramProfileService.save(profile);

        log.info("Telegram profile is active: {}", userName);

        String code = profile.isActive() ? "telegram.start" : "telegram.stop";
        message = messageSource.getMessage(code, new String[]{userName}, defaultLocale);

        return commandBuilder.buildMessage(chatId, message);
    }
}
