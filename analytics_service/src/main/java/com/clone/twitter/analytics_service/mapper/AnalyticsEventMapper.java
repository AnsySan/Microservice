package com.clone.twitter.analytics_service.mapper;

import com.clone.twitter.analytics_service.dto.AnalyticsEventDto;
import com.clone.twitter.analytics_service.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
}