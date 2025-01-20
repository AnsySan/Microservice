package com.clone.twitter.achievement_service.mapper;

import com.clone.twitter.achievement_service.dto.achievement.AchievementProgressDto;
import com.clone.twitter.achievement_service.model.AchievementProgress;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {AchievementMapper.class})
public interface AchievementProgressMapper {

    AchievementProgressDto toDto(AchievementProgress achievementProgress);

    AchievementProgress toEntity(AchievementProgressDto achievementProgressDto);
}
