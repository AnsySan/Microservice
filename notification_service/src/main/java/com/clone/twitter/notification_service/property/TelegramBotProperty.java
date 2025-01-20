package com.clone.twitter.notification_service.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "telegram.api")
public class TelegramBotProperty {
    private String name;
    private String token;
}
