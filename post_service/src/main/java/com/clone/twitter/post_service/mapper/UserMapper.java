package com.clone.twitter.post_service.mapper;

import com.clone.twitter.post_service.dto.feed.UserFeedDto;
import com.clone.twitter.post_service.dto.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserFeedDto toFeedDto(UserDto user);
}
