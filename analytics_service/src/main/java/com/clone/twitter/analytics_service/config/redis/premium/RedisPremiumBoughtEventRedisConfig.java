package com.clone.twitter.analytics_service.config.redis.premium;

import com.clone.twitter.analytics_service.listener.premium.PremiumBoughtEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class RedisPremiumBoughtEventRedisConfig {

    @Value("${spring.data.redis.channel.premium-bought}")
    private String premiumBoughtEventTopic;

    @Bean
    public MessageListenerAdapter premiumBoughtListenerAdapter(PremiumBoughtEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> premiumBoughtListenerAdapterPair(
            @Qualifier("premiumBoughtListenerAdapter") MessageListenerAdapter messageListenerAdapter
    ) {
        return Pair.of(new ChannelTopic(premiumBoughtEventTopic), messageListenerAdapter);
    }
}
