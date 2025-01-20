package com.clone.twitter.user_service.service.event.filter;

import com.clone.twitter.user_service.dto.event.EventFilterDto;
import com.clone.twitter.user_service.dto.skill.SkillDto;
import com.clone.twitter.user_service.entity.Skill;
import com.clone.twitter.user_service.entity.event.Event;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EventRelatedSkillsFilter implements EventFilter {
    @Override
    public boolean isAcceptable(EventFilterDto filterDto) {
        return filterDto.getRelatedSkills() != null && !filterDto.getRelatedSkills().isEmpty();
    }

    @Override
    @Transactional
    public Stream<Event> apply(Stream<Event> events, EventFilterDto filters) {
        Set<Long> filterSkillIds = filters.getRelatedSkills().stream()
                .map(SkillDto::getId)
                .collect(Collectors.toSet());

        return events.filter(event -> event.getRelatedSkills().stream()
                .map(Skill::getId)
                .collect(Collectors.toSet())
                .containsAll(filterSkillIds));
    }
}
