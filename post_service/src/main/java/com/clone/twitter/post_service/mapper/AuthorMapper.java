package com.clone.twitter.post_service.mapper;

import com.clone.twitter.post_service.dto.feed.UserFeedDto;
import com.clone.twitter.post_service.dto.user.UserDto;
import com.clone.twitter.post_service.redis.cache.entity.AuthorCache;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {

    @Mapping(source = "userProfilePic.fileId", target = "smallFileId")
    AuthorCache toCache(UserDto userDto);

    @Mapping(source = "smallFileId", target = "userProfilePic.fileId")
    UserFeedDto toFeedDto(AuthorCache authorCache);
}
