package com.clone.twitter.user_service.validator.goal;

import com.clone.twitter.user_service.dto.goal.GoalInvitationCreateDto;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.entity.goal.Goal;
import com.clone.twitter.user_service.exception.DataValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GoalInvitationValidatorImpl implements GoalInvitationValidator {

    @Value("${goals.invitations.max-per-user}")
    private static int MAX_GOALS_PER_USER;

    @Override
    public void validateUserIds(GoalInvitationCreateDto goalInvitationDto) {
        if (goalInvitationDto.getInviterId().equals(goalInvitationDto.getInvitedUserId())) {
            throw new DataValidationException("Inviter id and invitedUserId should be different");
        }
    }

    @Override
    public void validateMaxGoals(int currentGoals) {
        if (currentGoals == MAX_GOALS_PER_USER) {
            throw new DataValidationException("Limit of goals per user has been exceeded");
        }
    }

    @Override
    @Transactional
    public void validateGoalAlreadyExistence(Goal goal, User user) {
        if (user.getGoals().contains(goal)) {
            throw new DataValidationException("User already exist this goal");
        }
    }
}
