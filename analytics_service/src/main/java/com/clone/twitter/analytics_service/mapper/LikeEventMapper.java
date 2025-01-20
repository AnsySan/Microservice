package com.clone.twitter.analytics_service.mapper;

import com.clone.twitter.analytics_service.event.LikeEvent;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeEventMapper {

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "completedAt", target = "receivedAt")
    @Mapping(source = "postId", target = "receiverId")
    @Mapping(target = "eventType", constant = "POST_LIKE")
    AnalyticsEvent toAnalyticsEvent(LikeEvent likeEvent);
}
