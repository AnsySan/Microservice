package com.clone.twitter.user_service.validator;

import com.clone.twitter.user_service.dto.goal.GoalDto;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.entity.goal.Goal;
import com.clone.twitter.user_service.entity.goal.GoalStatus;
import com.clone.twitter.user_service.exception.DataValidationException;
import com.clone.twitter.user_service.exception.NotFoundException;
import com.clone.twitter.user_service.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GoalValidator {

    private final SkillRepository skillRepository;

    @Value("${goals.limit.max-per-user}")
    private int MAX_GOALS_AMOUNT_PER_USER;

    public void validateGoalId(long id) {
        if (id < 1) {
            throw new DataValidationException("Id cannot be less than 1");
        }
    }

    @Transactional
    public void validateUserGoalsAmount(User user) {
        if (user.getGoals().size() >= MAX_GOALS_AMOUNT_PER_USER) {
            throw new DataValidationException("User has reached the maximum number of goals");
        }
    }

    @Transactional
    public void validateAllSkillsExist(GoalDto goalDto) {
        goalDto.getSkillIds()
                .forEach(id -> skillRepository.findById(id).orElseThrow(
                        () -> new NotFoundException("Skill not found for ID: " + id)));
    }


    public void validateGoalNotCompleted(Goal goal) {
        if (goal.getStatus().equals(GoalStatus.COMPLETED)) {
            throw new DataValidationException("Goal is already completed");
        }
    }
}
