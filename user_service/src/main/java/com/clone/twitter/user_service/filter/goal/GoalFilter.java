package com.clone.twitter.user_service.filter.goal;

import com.clone.twitter.user_service.dto.goal.GoalFilterDto;
import com.clone.twitter.user_service.entity.goal.Goal;

import java.util.stream.Stream;

public interface GoalFilter {

    public boolean isAcceptable(GoalFilterDto goalFilterDto);

    public Stream<Goal> applyFilter(Stream<Goal> goals, GoalFilterDto goalFilterDto);

}
