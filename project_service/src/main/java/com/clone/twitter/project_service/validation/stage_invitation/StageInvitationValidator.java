package com.clone.twitter.project_service.validation.stage_invitation;

import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationCreateDto;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.stage_invitation.StageInvitation;

public interface StageInvitationValidator {

    void validateUserInvitePermission(TeamMember user, StageInvitation invitation);

    void validateExistences(StageInvitationCreateDto stageInvitationCreateDto);
}
