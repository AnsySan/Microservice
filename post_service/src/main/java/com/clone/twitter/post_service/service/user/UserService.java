package com.clone.twitter.post_service.service.user;

import com.clone.twitter.post_service.dto.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserById(long userId);

    List<UserDto> getAllUsers();
}
