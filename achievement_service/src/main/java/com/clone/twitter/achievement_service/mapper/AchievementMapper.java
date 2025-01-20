package com.clone.twitter.achievement_service.mapper;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.event.AchievementReceivedEvent;
import com.clone.twitter.achievement_service.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {LocalDateTime.class})
public interface AchievementMapper {

    AchievementDto toDto(Achievement achievement);

    Achievement toEntity(AchievementDto achievementDto);

    @Mapping(target = "receivedAt", expression = "java(LocalDateTime.now())")
    AchievementReceivedEvent toEvent(long userId, AchievementDto achievement);
}
