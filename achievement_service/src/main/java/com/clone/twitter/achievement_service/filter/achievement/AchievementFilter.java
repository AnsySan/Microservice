package com.clone.twitter.achievement_service.filter.achievement;

import com.clone.twitter.achievement_service.dto.achievement.AchievementFilterDto;
import com.clone.twitter.achievement_service.model.Achievement;

import java.util.stream.Stream;

public interface AchievementFilter {

    boolean isAcceptable(AchievementFilterDto filters);

    Stream<Achievement> apply(Stream<Achievement> achievements, AchievementFilterDto filters);
}
