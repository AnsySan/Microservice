package com.clone.twitter.user_service.service.avatar;

import com.clone.twitter.user_service.dto.avatar.UserProfilePicDto;
import com.clone.twitter.user_service.dto.user.UserDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface ProfilePicService {

    void generateAndSetPic(UserDto user);

    UserProfilePicDto saveProfilePic(long userId, MultipartFile file);

    InputStreamResource getProfilePic(long userId);

    UserProfilePicDto deleteProfilePic(long userId);
}
