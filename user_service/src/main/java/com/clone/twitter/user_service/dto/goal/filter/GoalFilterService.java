package com.clone.twitter.user_service.dto.goal.filter;


import com.clone.twitter.user_service.dto.goal.GoalFilterDto;
import com.clone.twitter.user_service.entity.goal.Goal;

import java.util.stream.Stream;

public interface GoalFilterService {

    Stream<Goal> applyFilters(Stream<Goal> goals, GoalFilterDto goalFilterDto);
}
