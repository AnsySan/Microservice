package com.clone.twitter.project_service.service.stage;

import com.clone.twitter.project_service.dto.stage.NewStageDto;
import com.clone.twitter.project_service.dto.stage.StageDto;
import com.clone.twitter.project_service.dto.stagerole.NewStageRolesDto;
import com.clone.twitter.project_service.model.StageDeleteMode;
import com.clone.twitter.project_service.model.StageStatus;

import java.util.List;

public interface StageService {
    StageDto createStage(NewStageDto dto);

    List<StageDto> getAllStages(Long projectId, StageStatus statusFilter);

    List<StageDto> getAllStages(Long projectId);

    void deleteStage(long stageId, Long stageToMigrateId, StageDeleteMode stageDeleteMode);

    StageDto updateStage(Long stageId, List<NewStageRolesDto> newStageRolesDtoList);

    StageDto getStageById(Long stageId);
}
