package com.clone.twitter.project_service.validation.stage_invitation;

import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationCreateDto;
import com.clone.twitter.project_service.exceptions.NoAccessException;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.stage_invitation.StageInvitation;
import com.clone.twitter.project_service.validation.stage.StageValidator;
import com.clone.twitter.project_service.validation.team_member.TeamMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StageInvitationValidatorImpl implements StageInvitationValidator {

    private final TeamMemberValidator teamMemberValidator;
    private final StageValidator stageValidator;

    @Override
    public void validateUserInvitePermission(TeamMember user, StageInvitation invitation) {
        if (!invitation.getInvited().equals(user)) {
            throw new NoAccessException("User with id=" + user.getId() + " does not have access to invite with id=" + invitation.getId());
        }
    }

    @Override
    public void validateExistences(StageInvitationCreateDto stageInvitationCreateDto) {
        teamMemberValidator.validateExistence(stageInvitationCreateDto.getAuthorId());
        teamMemberValidator.validateExistence(stageInvitationCreateDto.getInvitedId());
        stageValidator.validateExistence(stageInvitationCreateDto.getStageId());
    }
}
