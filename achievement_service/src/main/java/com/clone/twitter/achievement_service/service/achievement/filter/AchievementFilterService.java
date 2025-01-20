package com.clone.twitter.achievement_service.service.achievement.filter;

import com.clone.twitter.achievement_service.dto.achievement.AchievementFilterDto;
import com.clone.twitter.achievement_service.model.Achievement;

import java.util.stream.Stream;

public interface AchievementFilterService {
    Stream<Achievement> applyFilters(Stream<Achievement> achievements, AchievementFilterDto filters);
}
