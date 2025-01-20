package com.clone.twitter.achievement_service.filter.achievement;

import com.clone.twitter.achievement_service.dto.achievement.AchievementFilterDto;
import com.clone.twitter.achievement_service.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AchievementTitleFilter implements AchievementFilter {

    @Override
    public boolean isAcceptable(AchievementFilterDto filters) {
        return filters.getTitlePrefix() != null && !filters.getTitlePrefix().isBlank();
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievements, AchievementFilterDto filters) {
        return achievements.filter(achievement -> achievement.getTitle().startsWith(filters.getTitlePrefix()));
    }
}
