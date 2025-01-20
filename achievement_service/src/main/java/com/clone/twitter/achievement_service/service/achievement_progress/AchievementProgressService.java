package com.clone.twitter.achievement_service.service.achievement_progress;

import com.clone.twitter.achievement_service.dto.achievement.AchievementProgressDto;

import java.util.List;

public interface AchievementProgressService {
    List<AchievementProgressDto> getAchievementProgressesByUserId(long userId);

    void createProgressIfNecessary(long userId, long achievementId);

    AchievementProgressDto getProgress(long userId, long achievementId);

    long incrementAndGetProgress(long userId, long achievementId);
}
