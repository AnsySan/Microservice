package com.clone.twitter.achievement_service.service.achievement.filter;

import com.clone.twitter.achievement_service.dto.achievement.AchievementFilterDto;
import com.clone.twitter.achievement_service.filter.achievement.AchievementFilter;
import com.clone.twitter.achievement_service.model.Achievement;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AchievementsFilterServiceImpl implements AchievementFilterService {

    private final List<AchievementFilter> filters;

    @Override
    public Stream<Achievement> applyFilters(Stream<Achievement> achievements, @NonNull AchievementFilterDto filtersDto) {

        return filters.stream()
                .filter(achievementFilter -> achievementFilter.isAcceptable(filtersDto))
                .flatMap(achievementFilter -> achievementFilter.apply(achievements, filtersDto));
    }
}
