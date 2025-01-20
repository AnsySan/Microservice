package com.clone.twitter.analytics_service.mapper.profile;

import com.clone.twitter.analytics_service.event.profile.ProfileViewEvent;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProfileViewEventMapper {

    @Mapping(source = "viewerId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "viewedAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(ProfileViewEvent profileViewEvent);
}