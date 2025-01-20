package com.clone.twitter.user_service.service.goal.invitation.filter;

import com.clone.twitter.user_service.dto.goal.InvitationFilterDto;
import com.clone.twitter.user_service.entity.goal.GoalInvitation;
import com.clone.twitter.user_service.filter.goal.GoalInvitationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GoalInvitationFilterServiceImpl implements GoalInvitationFilterService {

    private final List<GoalInvitationFilter> filters;

    @Override
    public Stream<GoalInvitation> applyFilters(Stream<GoalInvitation> goalInvitations, InvitationFilterDto filterDto) {
        if (filterDto != null) {
            for (GoalInvitationFilter filter : filters) {
                if (filter.isAcceptable(filterDto)) {
                    goalInvitations = filter.applyFilter(goalInvitations, filterDto);
                }
            }
        }

        return goalInvitations;
    }
}
