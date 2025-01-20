package com.clone.twitter.project_service.validation.stage;

import com.clone.twitter.project_service.dto.stage.NewStageDto;
import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.exceptions.NotFoundException;
import com.clone.twitter.project_service.model.stage.Stage;
import com.clone.twitter.project_service.repository.StageRepository;
import com.clone.twitter.project_service.validation.project.ProjectValidator;
import com.clone.twitter.project_service.validation.task.TaskValidator;
import com.clone.twitter.project_service.validation.team_member.TeamMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StageValidatorImpl implements StageValidator {

    private final StageRepository stageRepository;
    private final ProjectValidator projectValidator;
    private final TaskValidator taskValidator;
    private final TeamMemberValidator teamMemberValidator;

    @Override
    public Stage validateExistence(long id) {
        if (!stageRepository.existsById(id)) {
            throw new NotFoundException("Stage with id=" + id + " does not exist");
        }
        return null;
    }

    @Override
    public void validateCreation(NewStageDto stageDto) {
        projectValidator.validateProjectExistence(stageDto.getProjectId());
        validateTasksExistence(stageDto);
        validateExecutorsExistence(stageDto);
    }

    @Override
    public Stage validateStageExistence(long id) {
        Optional<Stage> optional = stageRepository.findById(id);
        return optional.orElseThrow(() -> {
            String message = String.format("a stage with %d does not exist", id);

            return new DataValidationException(message);
        });
    }

    @Override
    public void validateStageForToMigrateExistence(Long stageToMigrateId) {
        if (ObjectUtils.isEmpty(stageToMigrateId)) {
            var message = "a stage does not exist";

            throw new DataValidationException(message);
        }

        validateStageExistence(stageToMigrateId);
    }


    private void validateTasksExistence(NewStageDto stageDto) {
        stageDto.getTasksIds()
                .forEach(taskValidator::validateTaskExistence);
    }

    private void validateExecutorsExistence(NewStageDto stageDto) {
        stageDto.getExecutorsIds()
                .forEach(teamMemberValidator::validateExistence);
    }
}
