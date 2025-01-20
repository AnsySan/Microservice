package com.clone.twitter.project_service.service.initiative;

import com.clone.twitter.project_service.dto.initiative.InitiativeFilterDto;
import com.clone.twitter.project_service.model.initiative.Initiative;

import java.util.stream.Stream;

public interface InitiativeFilterService {
    Stream<Initiative> applyAll(Stream<Initiative> initiatives, InitiativeFilterDto filterDto);
}
