package com.clone.twitter.user_service.filter.goal;


import com.clone.twitter.user_service.dto.goal.InvitationFilterDto;
import com.clone.twitter.user_service.entity.goal.GoalInvitation;

import java.util.stream.Stream;

public interface GoalInvitationFilter {

    boolean isAcceptable(InvitationFilterDto filterDto);

    Stream<GoalInvitation> applyFilter(Stream<GoalInvitation> invitations, InvitationFilterDto filterDto);
}
