package com.clone.twitter.user_service.mapper.recommendation;

import com.clone.twitter.user_service.dto.recommendation.RecommendationDto;
import com.clone.twitter.user_service.entity.recommendation.Recommendation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecommendationMapper {

    RecommendationDto toDto(Recommendation recommendation);

    Recommendation toEntity(RecommendationDto recommendationDto);
}
