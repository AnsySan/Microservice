package com.clone.twitter.achievement_service.handler.mentorship;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.event.mentorship.MentorshipStartEvent;
import com.clone.twitter.achievement_service.handler.EventHandler;
import com.clone.twitter.achievement_service.mapper.AchievementMapper;
import com.clone.twitter.achievement_service.model.Achievement;
import com.clone.twitter.achievement_service.model.AchievementProgress;
import com.clone.twitter.achievement_service.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SenseiAchievementHandler implements EventHandler<MentorshipStartEvent> {

    @Value("${achievements.sensei.name}")
    private String achievement;

    private final AchievementService achievementService;
    private final AchievementMapper achievementMapper;

    @Async("achievementExecutorService")
    public void handle(MentorshipStartEvent event) {
        AchievementDto achievementDto = achievementService.getAchievementByTitle(this.achievement);
        Achievement achievement = achievementMapper.toEntity(achievementDto);

        if (!achievementService.hasAchievement(event.getMentorId(), achievement.getId())) {
            achievementService.createProgressIfNecessary(event.getMentorId(), achievement.getId());
        }

        AchievementProgress progress = achievementService.getProgress(event.getMentorId(), achievement.getId());
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);

        if (progress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(event.getMentorId(), achievement);
        }
    }
}
