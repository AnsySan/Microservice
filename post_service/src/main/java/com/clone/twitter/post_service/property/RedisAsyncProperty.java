package com.clone.twitter.post_service.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "async.settings.redis")
public class RedisAsyncProperty {

    private Map<String, AsyncSettings> settings;

    @Data
    public static class AsyncSettings {
        private String name;
        private int corePoolSize;
        private int maxPoolSize;
        private int queueCapacity;
    }
}
