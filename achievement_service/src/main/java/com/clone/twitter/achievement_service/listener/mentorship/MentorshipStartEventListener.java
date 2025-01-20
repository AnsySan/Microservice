package com.clone.twitter.achievement_service.listener.mentorship;

import com.clone.twitter.achievement_service.event.mentorship.MentorshipStartEvent;
import com.clone.twitter.achievement_service.handler.mentorship.SenseiAchievementHandler;
import com.clone.twitter.achievement_service.listener.AbstractEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MentorshipStartEventListener extends AbstractEventListener<MentorshipStartEvent> {

    private final SenseiAchievementHandler senseiAchievementHandler;

    public MentorshipStartEventListener(ObjectMapper objectMapper, SenseiAchievementHandler senseiAchievementHandler) {
        super(objectMapper);
        this.senseiAchievementHandler = senseiAchievementHandler;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, MentorshipStartEvent.class, senseiAchievementHandler::handle);
    }
}
