package com.clone.twitter.project_service.service.initiative;

import com.clone.twitter.project_service.dto.initiative.InitiativeFilterDto;
import com.clone.twitter.project_service.model.initiative.Initiative;
import com.clone.twitter.project_service.service.initiative.filters.InitiativeFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InitiativeFilterServiceImpl implements InitiativeFilterService {
    private final List<InitiativeFilter> filters;

    public Stream<Initiative> applyAll(Stream<Initiative> initiatives, InitiativeFilterDto filterDto) {
        return filters.stream()
                .filter(filter -> filter.isAcceptable(filterDto))
                .flatMap(filter -> filter.apply(initiatives, filterDto));
    }
}
