package com.clone.twitter.project_service.service.stage_invitation.filter;

import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationFilterDto;
import com.clone.twitter.project_service.model.stage_invitation.StageInvitation;

import java.util.stream.Stream;

public interface StageInvitationFilter {

    boolean isAcceptable(StageInvitationFilterDto stageInvitationFilterDto);

    Stream<StageInvitation> apply(Stream<StageInvitation> stageInvitations, StageInvitationFilterDto stageInvitationFilterDto);
}
