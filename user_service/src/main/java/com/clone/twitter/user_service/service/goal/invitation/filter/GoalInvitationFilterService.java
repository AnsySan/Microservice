package com.clone.twitter.user_service.service.goal.invitation.filter;


import com.clone.twitter.user_service.dto.goal.InvitationFilterDto;
import com.clone.twitter.user_service.entity.goal.GoalInvitation;

import java.util.stream.Stream;

public interface GoalInvitationFilterService {

    Stream<GoalInvitation> applyFilters(Stream<GoalInvitation> goalInvitations, InvitationFilterDto filterDto);
}
