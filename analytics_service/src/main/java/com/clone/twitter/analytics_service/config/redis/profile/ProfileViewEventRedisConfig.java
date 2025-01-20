package com.clone.twitter.analytics_service.config.redis.profile;

import com.clone.twitter.analytics_service.listener.profile.ProfileViewListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class ProfileViewEventRedisConfig {

    @Value("${spring.data.redis.channel.profile-view.name}")
    private String profileViewEventTopic;

    @Bean
    public MessageListenerAdapter profileViewListenerAdapter(ProfileViewListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> profileViewListenerAdapterPair(
            @Qualifier("profileViewListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(profileViewEventTopic), messageListenerAdapter);
    }
}
