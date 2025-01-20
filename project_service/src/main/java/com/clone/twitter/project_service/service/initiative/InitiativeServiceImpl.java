package com.clone.twitter.project_service.service.initiative;

import com.clone.twitter.project_service.dto.initiative.InitiativeDto;
import com.clone.twitter.project_service.dto.initiative.InitiativeFilterDto;
import com.clone.twitter.project_service.mapper.InitiativeMapper;
import com.clone.twitter.project_service.model.initiative.Initiative;
import com.clone.twitter.project_service.model.initiative.InitiativeStatus;
import com.clone.twitter.project_service.repository.InitiativeRepository;
import com.clone.twitter.project_service.service.moment.MomentService;
import com.clone.twitter.project_service.validation.initiative.InitiativeValidatorImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InitiativeServiceImpl implements InitiativeService {
    private final InitiativeMapper mapper;
    private final InitiativeValidatorImpl validator;
    private final InitiativeRepository initiativeRepository;
    private final InitiativeFilterService filterService;
    private final MomentService momentService;

    @Override
    public InitiativeDto create(InitiativeDto initiative) {
        validator.validateCurator(initiative);

        Initiative entity = mapper.toEntity(initiative);
        Initiative saved = initiativeRepository.save(entity);

        return mapper.toDto(saved);
    }

    @Override
    public InitiativeDto update(InitiativeDto initiative) {
        validator.validateCurator(initiative);

        Initiative entity = mapper.toEntity(initiative);

        if (initiative.getStatus() == InitiativeStatus.DONE) {
            validator.validateClosedInitiative(initiative);
            momentService.createFromInitiative(entity);
        }

        Initiative saved = initiativeRepository.save(entity);

        return mapper.toDto(saved);
    }

    @Override
    public List<InitiativeDto> getAllByFilter(InitiativeFilterDto filter) {
        Stream<Initiative> initiatives = initiativeRepository.findAll().stream();
        return filterService.applyAll(initiatives, filter)
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<InitiativeDto> getAll() {
        return initiativeRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public InitiativeDto getById(long id) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("can't find initiative with id:" + id));
        return mapper.toDto(initiative);
    }
}
