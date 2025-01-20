package com.clone.twitter.user_service.controller.goal;

import com.clone.twitter.user_service.config.context.UserContext;
import com.clone.twitter.user_service.dto.goal.GoalDto;
import com.clone.twitter.user_service.dto.goal.GoalFilterDto;
import com.clone.twitter.user_service.service.goal.GoalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
@Validated
public class GoalController {

    private final GoalService goalService;
    private final UserContext userContext;

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public GoalDto createGoal(@Positive @PathVariable("userId") Long userId,
                              @Valid @RequestBody GoalDto goal) {
        return goalService.createGoal(userId, goal);
    }

    @PutMapping("/goal/{goalId}")
    @ResponseStatus(HttpStatus.OK)
    public GoalDto updateGoal(@Positive @PathVariable("goalId") Long goalId,
                              @Valid @RequestBody GoalDto goal) {
        long userId = userContext.getUserId();
        return goalService.updateGoal(userId, goalId, goal);
    }

    @DeleteMapping("/delete/{goalId}")
    @ResponseStatus(HttpStatus.OK)
    public GoalDto deleteGoal(@Positive @PathVariable("goalId") Long goalId) {
        return goalService.deleteGoal(goalId);
    }

    @GetMapping("/goal/{goalId}/subtasks")
    @ResponseStatus(HttpStatus.OK)
    public List<GoalDto> findSubtasksByGoalId(@Positive @PathVariable("goalId") long goalId,
                                              @Valid @RequestBody(required = false) GoalFilterDto filter) {
        return goalService.getSubtasksByGoalId(goalId, filter);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GoalDto> getGoalsByUser(@Positive @PathVariable("userId") Long userId,
                                        @Valid @RequestBody(required = false) GoalFilterDto filter) {
        return goalService.getGoalsByUser(userId, filter);
    }
}
