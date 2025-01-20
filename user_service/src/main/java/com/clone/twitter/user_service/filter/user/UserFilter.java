package com.clone.twitter.user_service.filter.user;

import com.clone.twitter.user_service.dto.user.UserFilterDto;
import com.clone.twitter.user_service.entity.User;

import java.util.stream.Stream;

public interface UserFilter {

    boolean isAcceptable(UserFilterDto userFilterDto);

    Stream<User> applyFilter(Stream<User> users, UserFilterDto userFilterDto);
}
