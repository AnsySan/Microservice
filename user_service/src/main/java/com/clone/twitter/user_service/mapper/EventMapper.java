package com.clone.twitter.user_service.mapper;

import com.clone.twitter.user_service.dto.event.EventDto;
import com.clone.twitter.user_service.entity.event.Event;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = SkillMapper.class)
public interface EventMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    EventDto toDto(Event event);

    @Mapping(source = "ownerId", target = "owner.id")
    Event toEntity(EventDto eventDto);
}
