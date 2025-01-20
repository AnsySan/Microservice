package com.clone.twitter.project_service.pattern.strategy.stage;

public interface StrategyForDeletingStage {
    void manageTasksOfStage(long stageId, Long stageToMigrateId);
}
