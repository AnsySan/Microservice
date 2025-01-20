package com.clone.twitter.achievement_service.config.redis;

import com.clone.twitter.achievement_service.listener.InviteEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class InviteEventRedisConfig {

    @Bean
    public Pair<Topic, MessageListenerAdapter> getCompleteGoalListenerAdapterPair(
            @Value("${spring.data.redis.channel.invitation}") String channelTopicName,
            @Qualifier("inviteEventMessageAdapter") MessageListenerAdapter messageListenerAdapter) {

        return Pair.of(new ChannelTopic(channelTopicName), messageListenerAdapter);
    }

    @Bean
    public MessageListenerAdapter inviteEventMessageAdapter(InviteEventListener listener) {
        return new MessageListenerAdapter(listener);
    }
}
