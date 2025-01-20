package com.clone.twitter.achievement_service.publisher;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.event.AchievementReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import java.time.LocalDateTime;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementPublisherTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ChannelTopic channelTopic;

    @InjectMocks
    private AchievementPublisher achievementPublisher;

    private AchievementReceivedEvent event;

    @BeforeEach
    void setUp() {

        event = AchievementReceivedEvent.builder()
                .userId(1L)
                .achievement(AchievementDto.builder()
                        .id(1L)
                        .title("title")
                        .build())
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void publish() {
        String topic = "topic";

        when(channelTopic.getTopic()).thenReturn(topic);

        achievementPublisher.publish(event);

        InOrder inOrder = inOrder(redisTemplate, channelTopic);
        inOrder.verify(redisTemplate).convertAndSend(topic, event);
    }
}