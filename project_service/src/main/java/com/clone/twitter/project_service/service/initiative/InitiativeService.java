package com.clone.twitter.project_service.service.initiative;

import com.clone.twitter.project_service.dto.initiative.InitiativeDto;
import com.clone.twitter.project_service.dto.initiative.InitiativeFilterDto;

import java.util.List;

public interface InitiativeService {
    InitiativeDto create(InitiativeDto initiative);

    InitiativeDto update(InitiativeDto initiative);

    List<InitiativeDto> getAllByFilter(InitiativeFilterDto filter);

    List<InitiativeDto> getAll();

    InitiativeDto getById(long id);
}
