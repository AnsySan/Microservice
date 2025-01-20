package com.clone.twitter.user_service.service.user;

import com.clone.twitter.user_service.dto.user.UserDto;
import com.clone.twitter.user_service.dto.user.UserFilterDto;
import com.clone.twitter.user_service.entity.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    User findUserById(long id);

    List<UserDto> findPremiumUsers(UserFilterDto filterDto);

    void deactivateUserById(Long id);

    List<UserDto> getUsersByIds(List<Long> ids);

    UserDto getUserById(long userId);

    void banUserByIds(List<Long> userIds);
}
