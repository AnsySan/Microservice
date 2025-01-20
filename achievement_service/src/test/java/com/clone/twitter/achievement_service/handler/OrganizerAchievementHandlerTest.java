package com.clone.twitter.achievement_service.handler;

import com.clone.twitter.achievement_service.cache.AchievementCache;
import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.event.InviteSentEvent;
import com.clone.twitter.achievement_service.mapper.AchievementMapper;
import com.clone.twitter.achievement_service.model.Achievement;
import com.clone.twitter.achievement_service.service.achievement_progress.AchievementProgressService;
import com.clone.twitter.achievement_service.service.user_achievement.UserAchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizerAchievementHandlerTest {

    @Mock
    private AchievementProgressService achievementProgressService;
    @Mock
    private UserAchievementService userAchievementService;
    @Mock
    private AchievementCache achievementCache;
    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private OrganizerAchievementHandler organizerAchievementHandler;

    private final long userId = 1L;
    private final long achievementId = 4L;
    private InviteSentEvent inviteSentEvent;
    private Achievement achievement;
    private AchievementDto achievementDto;

    @BeforeEach
    void setUp() {
        inviteSentEvent = InviteSentEvent.builder()
                .userId(userId)
                .projectId(2L)
                .receiverId(3L)
                .build();

        achievement = Achievement.builder()
                .id(achievementId)
                .title("title")
                .points(100L)
                .build();

        achievementDto = AchievementDto.builder()
                .id(achievementId)
                .title("title")
                .points(100L)
                .build();
    }

    @Test
    void handle() {
        when(achievementCache.get(any())).thenReturn(achievement);
        when(userAchievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementProgressService.incrementAndGetProgress(userId, achievementId)).thenReturn(100L);
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        organizerAchievementHandler.handle(inviteSentEvent);

        InOrder inOrder = inOrder(achievementCache, achievementProgressService, userAchievementService);
        inOrder.verify(achievementCache).get(any());
        inOrder.verify(userAchievementService).hasAchievement(userId, achievementId);
        inOrder.verify(achievementProgressService).createProgressIfNecessary(userId, achievementId);
        inOrder.verify(userAchievementService).giveAchievement(userId, achievementDto);
    }
}