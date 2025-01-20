package com.clone.twitter.project_service.service.stage.impl;

import com.clone.twitter.project_service.dto.stage.NewStageDto;
import com.clone.twitter.project_service.dto.stage.StageDto;
import com.clone.twitter.project_service.dto.stagerole.NewStageRolesDto;
import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.mapper.StageMapper;
import com.clone.twitter.project_service.mapper.StageRolesMapper;
import com.clone.twitter.project_service.model.StageDeleteMode;
import com.clone.twitter.project_service.model.StageStatus;
import com.clone.twitter.project_service.model.stage.Stage;
import com.clone.twitter.project_service.model.stage.StageRoles;
import com.clone.twitter.project_service.pattern.strategy.stage.StrategyForDeletingStage;
import com.clone.twitter.project_service.repository.StageRepository;
import com.clone.twitter.project_service.repository.StageRolesRepository;
import com.clone.twitter.project_service.service.stage.StageService;
import com.clone.twitter.project_service.validation.project.ProjectValidator;
import com.clone.twitter.project_service.validation.stage.StageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StageServiceImpl implements StageService {
    private final Map<StageDeleteMode, StrategyForDeletingStage> stageDeleteModeStrategyForDeletingStage;
    private final StageRolesRepository stageRolesRepository;
    private final StageRepository stageRepository;
    private final ProjectValidator projectValidator;
    private final StageRolesMapper stageRolesMapper;
    private final StageValidator stageValidator;
    private final StageMapper stageMapper;

    @Override
    @Transactional
    public StageDto createStage(final NewStageDto newStageDto) {
        stageValidator.validateCreation(newStageDto);

        Stage stageEntity = stageMapper.toEntity(newStageDto);
        stageEntity = stageRepository.save(stageEntity);

        return stageMapper.toDto(stageEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StageDto> getAllStages(Long projectId, StageStatus statusFilter) {
        projectValidator.validateProjectExistence(projectId);
        List<Stage> stageEntities = stageRepository.findAllByProjectIdAndStageStatus(projectId, statusFilter);

        return stageMapper.toDtoList(stageEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StageDto> getAllStages(Long projectId) {
        projectValidator.validateProjectExistence(projectId);
        List<Stage> stageEntities = stageRepository.findAllByProjectId(projectId);

        return stageMapper.toDtoList(stageEntities);
    }

    @Override
    @Transactional
    public void deleteStage(long stageId, Long stageToMigrateId, StageDeleteMode stageDeleteMode) {
        var strategy = stageDeleteModeStrategyForDeletingStage.get(stageDeleteMode);
        strategy.manageTasksOfStage(stageId, stageToMigrateId);

        stageRepository.deleteById(stageId);
    }

    @Override
    @Transactional
    public StageDto updateStage(Long stageId, List<NewStageRolesDto> newStageRolesDtoList) {
        Stage stageEntity = stageValidator.validateExistence(stageId);
        List<StageRoles> stageRolesEntities = stageRolesMapper.toEntityList(newStageRolesDtoList);

        stageRolesEntities.forEach(s -> s.setStage(stageEntity));
        stageRolesRepository.saveAll(stageRolesEntities);
        Stage updatedStage = getStage(stageId);

        return stageMapper.toDto(updatedStage);
    }

    @Override
    @Transactional(readOnly = true)
    public StageDto getStageById(Long stageId) {
        Stage stageEntity = stageValidator.validateExistence(stageId);

        return stageMapper.toDto(stageEntity);
    }


    public Stage getStage(long id) {
        var optional = stageRepository.findById(id);
        return optional.orElseThrow(() -> {
            var message = String.format("a stage with %d does not exist", id);

            return new DataValidationException(message);
        });
    }
}
