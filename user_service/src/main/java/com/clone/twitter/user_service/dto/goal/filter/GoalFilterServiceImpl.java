package com.clone.twitter.user_service.dto.goal.filter;

import com.clone.twitter.user_service.dto.goal.GoalFilterDto;
import com.clone.twitter.user_service.entity.goal.Goal;
import com.clone.twitter.user_service.filter.goal.GoalFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GoalFilterServiceImpl implements GoalFilterService{

    private final List<GoalFilter> goalFilters;

    @Override
    public Stream<Goal> applyFilters(Stream<Goal> goals, GoalFilterDto goalFilterDto) {
        if (goalFilterDto != null) {
            for (GoalFilter goalFilter : goalFilters) {
                if (goalFilter.isAcceptable(goalFilterDto)) {
                    goals = goalFilter.applyFilter(goals, goalFilterDto);
                }
            }
        }

        return goals;
    }
}