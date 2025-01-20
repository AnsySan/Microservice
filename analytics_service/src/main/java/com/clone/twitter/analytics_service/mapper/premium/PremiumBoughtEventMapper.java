package com.clone.twitter.analytics_service.mapper.premium;

import com.clone.twitter.analytics_service.event.premium.PremiumBoughtEvent;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PremiumBoughtEventMapper {

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "boughtAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(PremiumBoughtEvent premiumBoughtEvent);
}
