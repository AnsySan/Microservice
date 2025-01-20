package com.clone.twitter.project_service.service.initiative.filters;

import com.clone.twitter.project_service.dto.initiative.InitiativeFilterDto;
import com.clone.twitter.project_service.model.initiative.Initiative;

import java.util.stream.Stream;

public interface InitiativeFilter {
    boolean isAcceptable(InitiativeFilterDto filters);

    Stream<Initiative> apply(Stream<Initiative> initiatives, InitiativeFilterDto filters);
}
