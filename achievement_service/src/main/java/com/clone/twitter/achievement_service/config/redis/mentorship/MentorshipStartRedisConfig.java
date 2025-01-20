package com.clone.twitter.achievement_service.config.redis.mentorship;

import com.clone.twitter.achievement_service.listener.mentorship.MentorshipStartEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class MentorshipStartRedisConfig {

    @Bean
    public Pair<Topic, MessageListenerAdapter> getMentorshipStartListenerAdapterPair(
            @Value("${spring.data.redis.channel.mentorship}") String channelTopicName,
            MessageListenerAdapter mentorshipStartMessageAdapter) {

        return Pair.of(new ChannelTopic(channelTopicName), mentorshipStartMessageAdapter);
    }

    @Bean
    public MessageListenerAdapter mentorshipStartMessageAdapter(MentorshipStartEventListener listener) {
        return new MessageListenerAdapter(listener);
    }
}