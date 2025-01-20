package com.clone.twitter.project_service.service.stage_invitation;

import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationFilterDto;
import com.clone.twitter.project_service.model.stage_invitation.StageInvitation;

import java.util.stream.Stream;

public interface StageInvitationFilterService {
    Stream<StageInvitation> applyAll(Stream<StageInvitation> stageInvitations, StageInvitationFilterDto filterDto);
}
