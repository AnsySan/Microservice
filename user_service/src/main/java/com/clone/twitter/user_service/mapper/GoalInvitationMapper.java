package com.clone.twitter.user_service.mapper;

import com.clone.twitter.user_service.dto.goal.GoalInvitationDto;
import com.clone.twitter.user_service.entity.RequestStatus;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.entity.goal.Goal;
import com.clone.twitter.user_service.entity.goal.GoalInvitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalInvitationMapper {

    @Mapping(source = "inviter", target = "inviter")
    @Mapping(source = "invited", target = "invited")
    @Mapping(source = "goal", target = "goal")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GoalInvitation toEntity(User inviter, User invited, Goal goal, RequestStatus status);

    GoalInvitationDto toDto(GoalInvitation goalInvitation);
}
