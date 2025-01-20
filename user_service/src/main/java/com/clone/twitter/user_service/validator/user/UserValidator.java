package com.clone.twitter.user_service.validator.user;


import com.clone.twitter.user_service.entity.User;

import java.util.List;

public interface UserValidator {
    User validateUserExistence(Long userId);

    List<User> validateUsersExistence(List<Long> userIds);

}
