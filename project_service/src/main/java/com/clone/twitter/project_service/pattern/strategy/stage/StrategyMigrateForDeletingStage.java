package com.clone.twitter.project_service.pattern.strategy.stage;

import com.clone.twitter.project_service.repository.TaskRepository;
import com.clone.twitter.project_service.validation.stage.StageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("MIGRATE")
@RequiredArgsConstructor
public class StrategyMigrateForDeletingStage implements StrategyForDeletingStage {
    private final TaskRepository taskRepository;
    private final StageValidator stageValidator;

    @Override
    public void manageTasksOfStage(long stageId, Long stageToMigrateId) {
        stageValidator.validateExistence(stageId);
        stageValidator.validateStageForToMigrateExistence(stageToMigrateId);
        taskRepository.updateStageId(stageId, stageToMigrateId);
    }
}
