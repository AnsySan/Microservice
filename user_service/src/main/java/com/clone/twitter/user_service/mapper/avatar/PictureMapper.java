package com.clone.twitter.user_service.mapper.avatar;

import com.clone.twitter.user_service.dto.avatar.UserProfilePicDto;
import com.clone.twitter.user_service.entity.UserProfilePic;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PictureMapper {

    UserProfilePicDto toDto(UserProfilePic userProfilePic);

    UserProfilePic toEntity(UserProfilePicDto userProfilePicDto);
}
