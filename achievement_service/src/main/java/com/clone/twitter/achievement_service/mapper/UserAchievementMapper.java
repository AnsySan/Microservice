package com.clone.twitter.achievement_service.mapper;

import com.clone.twitter.achievement_service.dto.achievement.UserAchievementDto;
import com.clone.twitter.achievement_service.model.UserAchievement;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {AchievementMapper.class})
public interface UserAchievementMapper {

    UserAchievementDto toDto(UserAchievement achievement);

    UserAchievement toEntity(UserAchievementDto achievementDto);
}
