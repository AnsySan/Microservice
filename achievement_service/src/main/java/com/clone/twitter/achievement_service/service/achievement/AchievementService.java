package com.clone.twitter.achievement_service.service.achievement;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.dto.achievement.AchievementFilterDto;
import com.clone.twitter.achievement_service.model.Achievement;
import com.clone.twitter.achievement_service.model.AchievementProgress;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AchievementService {

    List<AchievementDto> getAchievements(AchievementFilterDto filters);

    AchievementDto getAchievementByAchievementId(long achievementId);

    @Transactional(readOnly = true)
    AchievementDto getAchievementByTitle(String title);

    boolean hasAchievement(long userId, long achievementId);

    void createProgressIfNecessary(long userId, long achievementId);

    AchievementProgress getProgress(long userId, long achievementId);

    void giveAchievement(long userId, Achievement achievement);
}
