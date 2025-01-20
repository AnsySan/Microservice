package com.clone.twitter.analytics_service.config.redis;

import com.clone.twitter.analytics_service.listener.GoalCompletedListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class CompletedGoalRedisConfig {

    @Bean
    public Pair<Topic, MessageListenerAdapter> getCompleteGoalListenerAdapterPair(
            @Value("${spring.data.redis.channel.goal-complete.name}") String channelTopicName,
            @Qualifier("completedGoalMessageAdapter") MessageListenerAdapter messageListenerAdapter) {

        return Pair.of(new ChannelTopic(channelTopicName), messageListenerAdapter);
    }

    @Bean
    public MessageListenerAdapter completedGoalMessageAdapter(GoalCompletedListener listener) {
        return new MessageListenerAdapter(listener);
    }
}
