package com.clone.twitter.user_service.mapper;

import com.clone.twitter.user_service.entity.premium.Premium;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface PremiumMapper {

    @Mapping(source = "id", target = "user.id")
    Premium toEntity(Long id, LocalDateTime startDate, LocalDateTime endDate);
}
