package com.clone.twitter.project_service.validation.initiative;

import com.clone.twitter.project_service.dto.initiative.InitiativeDto;
import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.model.TaskStatus;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.TeamRole;
import com.clone.twitter.project_service.model.stage.Stage;
import com.clone.twitter.project_service.repository.StageRepository;
import com.clone.twitter.project_service.repository.TeamMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitiativeValidatorImpl implements InitiativeValidator {

    private final StageRepository stageRepository;
    private final TeamMemberRepository teamMemberRepository;

    public void validateCurator(InitiativeDto initiative) {
        TeamMember curator = teamMemberRepository.findById(initiative.getCuratorId()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Team member doesn't exist by id: %s", initiative.getCuratorId())));

        if (!curator.getRoles().contains(TeamRole.OWNER)) {
            throw new DataValidationException("curator must have owner role");
        }

        if (!curator.getTeam().getProject().getId().equals(initiative.getProjectId())) {
            throw new DataValidationException("curator not in the initiative project");
        }
    }

    public void validateClosedInitiative(InitiativeDto initiative) {
        List<Stage> stages = stageRepository.findAll().stream()
                .filter(stage -> initiative.getStageIds().contains(stage.getStageId()))
                .toList();

        boolean areAllClosed = stages.stream()
                .flatMap(stage -> stage.getTasks().stream())
                .allMatch(task -> task.getStatus() == TaskStatus.DONE);

        if (!areAllClosed) {
            throw new DataValidationException("All tasks in all stages must be done to close the initiative");
        }
    }
}
