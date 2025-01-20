package com.clone.twitter.project_service.service.initiative.filters;

import com.clone.twitter.project_service.dto.initiative.InitiativeFilterDto;
import com.clone.twitter.project_service.model.initiative.Initiative;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class InitiativeCuratorIdFilter implements InitiativeFilter {
    @Override
    public boolean isAcceptable(InitiativeFilterDto filters) {
        return filters.getCuratorId() != null;
    }

    @Override
    public Stream<Initiative> apply(Stream<Initiative> initiatives, InitiativeFilterDto filters) {
        return initiatives.filter(initiative -> initiative.getCurator().getUserId().equals(filters.getCuratorId()));
    }
}
