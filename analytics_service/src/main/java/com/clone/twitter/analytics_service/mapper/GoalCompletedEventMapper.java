package com.clone.twitter.analytics_service.mapper;

import com.clone.twitter.analytics_service.event.GoalCompletedEvent;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalCompletedEventMapper {

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "completedAt", target = "receivedAt")
    @Mapping(source = "goalId", target = "receiverId")
    AnalyticsEvent toAnalyticsEvent(GoalCompletedEvent goalCompletedEvent);
}
