package com.clone.twitter.project_service.pattern.strategy.stage;

import com.clone.twitter.project_service.model.TaskStatus;
import com.clone.twitter.project_service.repository.TaskRepository;
import com.clone.twitter.project_service.validation.stage.StageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("CLOSE")
@RequiredArgsConstructor
public class StrategyCloseForDeletingStage implements StrategyForDeletingStage {
    private final TaskRepository taskRepository;
    private final StageValidator stageValidator;

    @Override
    public void manageTasksOfStage(long stageId, Long stageToMigrateId) {
        stageValidator.validateExistence(stageId);
        taskRepository.updateStatusByStageId(stageId, TaskStatus.DONE);
    }
}
