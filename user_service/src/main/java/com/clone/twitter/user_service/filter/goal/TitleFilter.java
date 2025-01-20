package com.clone.twitter.user_service.filter.goal;

import com.clone.twitter.user_service.dto.goal.GoalFilterDto;
import com.clone.twitter.user_service.entity.goal.Goal;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class TitleFilter implements GoalFilter {

    @Override
    public boolean isAcceptable(GoalFilterDto goalFilterDto) {
        return goalFilterDto.getTitle() != null;
    }

    @Override
    public Stream<Goal> applyFilter(Stream<Goal> goals, GoalFilterDto goalFilterDto) {
        return goals.filter(goal -> goal.getTitle().equals(goalFilterDto.getTitle()));
    }
}