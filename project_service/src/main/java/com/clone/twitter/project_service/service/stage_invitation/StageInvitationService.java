package com.clone.twitter.project_service.service.stage_invitation;

import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationCreateDto;
import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationDto;
import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationFilterDto;

import java.util.List;

public interface StageInvitationService {

    StageInvitationDto sendInvitation(StageInvitationCreateDto stageInvitationCreateDto);

    StageInvitationDto acceptInvitation(long userId, long inviteId);

    StageInvitationDto rejectInvitation(long userId, long inviteId, String reason);

    List<StageInvitationDto> getInvitationsWithFilters(StageInvitationFilterDto stageInvitationFilterDto);
}
