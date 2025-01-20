package com.clone.twitter.project_service.validation.team_member;

import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.exceptions.NotFoundException;
import com.clone.twitter.project_service.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamMemberValidatorImpl implements TeamMemberValidator {
    private final TeamMemberRepository teamMemberRepository;

    @Override
    public void validateExistence(long id) {
        boolean exists = teamMemberRepository.existsById(id);
        if (!exists) {
            var message = String.format("TeamMember with id=%d does not exist", id);

            throw new NotFoundException(message);
        }
    }

    @Override
    public void validateExistenceByUserIdAndProjectId(long userId, long projectId) {
        boolean exists = teamMemberRepository.existsByUserIdAndProjectId(userId, projectId);
        if (!exists) {
            String message = String.format("TeamMember with %d is not a team member of project with id %d",
                    userId, projectId);
            throw new DataValidationException(message);
        }
    }
}