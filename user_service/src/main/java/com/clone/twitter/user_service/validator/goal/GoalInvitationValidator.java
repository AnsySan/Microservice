package com.clone.twitter.user_service.validator.goal;


import com.clone.twitter.user_service.dto.goal.GoalInvitationCreateDto;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.entity.goal.Goal;

public interface GoalInvitationValidator {

    void validateUserIds(GoalInvitationCreateDto goalInvitationDto);

    void validateMaxGoals(int currentGoals);

    void validateGoalAlreadyExistence(Goal goal, User user);
}
