package com.clone.twitter.achievement_service.listener;

import com.clone.twitter.achievement_service.event.InviteSentEvent;
import com.clone.twitter.achievement_service.handler.OrganizerAchievementHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InviteEventListener extends AbstractEventListener<InviteSentEvent> {

    private final OrganizerAchievementHandler organizerAchievementHandler;

    public InviteEventListener(ObjectMapper objectMapper, OrganizerAchievementHandler organizerAchievementHandler) {
        super(objectMapper);
        this.organizerAchievementHandler = organizerAchievementHandler;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, InviteSentEvent.class, organizerAchievementHandler::handle);
    }
}
