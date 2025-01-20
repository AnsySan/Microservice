package com.clone.twitter.achievement_service.handler.mentorship;

import static org.junit.jupiter.api.Assertions.*;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.event.mentorship.MentorshipStartEvent;
import com.clone.twitter.achievement_service.mapper.AchievementMapper;
import com.clone.twitter.achievement_service.model.Achievement;
import com.clone.twitter.achievement_service.model.AchievementProgress;
import com.clone.twitter.achievement_service.service.achievement.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SenseiAchievementHandlerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private SenseiAchievementHandler senseiAchievementHandler;

    private final long mentorId = 2L;
    private final long achievementId = 4L;

    @Value("${achievements.sensei.name}")
    private String ACHIEVEMENT;
    private MentorshipStartEvent mentorshipStartEvent;
    private AchievementProgress achievementProgress;
    private Achievement achievement;
    private AchievementDto achievementDto;

    @BeforeEach
    void setUp() {
        mentorshipStartEvent = MentorshipStartEvent.builder()
                .menteeId(1L)
                .mentorId(mentorId)
                .build();

        achievement = Achievement.builder()
                .id(achievementId)
                .title(ACHIEVEMENT)
                .points(20L)
                .build();

        achievementDto = AchievementDto.builder()
                .id(achievementId)
                .title(ACHIEVEMENT)
                .points(20L)
                .build();

        achievementProgress = AchievementProgress.builder()
                .id(5L)
                .userId(mentorId)
                .achievement(achievement)
                .currentPoints(19L)
                .build();
    }

    @Test
    void handle() {
        when(achievementService.getAchievementByTitle(ACHIEVEMENT)).thenReturn(achievementDto);
        when(achievementMapper.toEntity(achievementDto)).thenReturn(achievement);
        when(achievementService.hasAchievement(mentorId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(mentorId, achievementId)).thenReturn(achievementProgress);

        senseiAchievementHandler.handle(mentorshipStartEvent);
        assertEquals(20L, achievementProgress.getCurrentPoints());

        InOrder inOrder = inOrder(achievementService);
        inOrder.verify(achievementService).getAchievementByTitle(ACHIEVEMENT);
        inOrder.verify(achievementService).hasAchievement(mentorId, achievementId);
        inOrder.verify(achievementService).createProgressIfNecessary(mentorId, achievementId);
        inOrder.verify(achievementService).getProgress(mentorId, achievementId);
        inOrder.verify(achievementService).giveAchievement(mentorId, achievement);
    }
}