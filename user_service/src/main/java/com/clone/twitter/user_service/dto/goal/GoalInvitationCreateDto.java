package com.clone.twitter.user_service.dto.goal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalInvitationCreateDto {
    private Long id;
    private Long inviterId;
    private Long invitedUserId;
    private Long goalId;
}
