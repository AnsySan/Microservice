package com.clone.twitter.project_service.validation.stage;

import com.clone.twitter.project_service.dto.stage.NewStageDto;
import com.clone.twitter.project_service.model.stage.Stage;

public interface StageValidator {
  
    Stage validateExistence(long id);

    Stage validateStageExistence(long id);

    void validateStageForToMigrateExistence(Long stageToMigrateId);

    void validateCreation(NewStageDto stageDto);
}
