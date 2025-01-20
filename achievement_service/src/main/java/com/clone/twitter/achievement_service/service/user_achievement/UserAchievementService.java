package com.clone.twitter.achievement_service.service.user_achievement;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.dto.achievement.UserAchievementDto;

import java.util.List;

public interface UserAchievementService {

    List<UserAchievementDto> getAchievementsByUserId(long userId);

    void giveAchievement(long userId, AchievementDto achievement);

    boolean hasAchievement(long userId, long achievementId);
}
