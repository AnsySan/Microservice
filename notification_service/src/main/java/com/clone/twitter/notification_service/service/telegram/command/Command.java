package com.clone.twitter.notification_service.service.telegram.command;

import com.clone.twitter.notification_service.client.UserServiceClient;
import com.clone.twitter.notification_service.service.telegram.TelegramProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@Slf4j
@AllArgsConstructor
public abstract class Command {

    protected final MessageSource messageSource;
    protected final Locale defaultLocale = Locale.getDefault();
    protected final UserServiceClient userServiceClient;
    protected final TelegramProfileService telegramProfileService;
    protected final CommandBuilder commandBuilder;

    public abstract SendMessage sendMessage(long chatId, String userName);
}
