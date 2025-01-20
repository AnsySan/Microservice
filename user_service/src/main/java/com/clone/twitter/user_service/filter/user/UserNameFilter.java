package com.clone.twitter.user_service.filter.user;

import org.springframework.stereotype.Component;
import com.clone.twitter.user_service.dto.user.UserFilterDto;
import com.clone.twitter.user_service.entity.User;

import java.util.stream.Stream;

@Component
public class UserNameFilter implements UserFilter {

    @Override
    public boolean isAcceptable(UserFilterDto userFilterDto) {
        return userFilterDto.getNamePattern() != null;
    }

    @Override
    public Stream<User> applyFilter(Stream<User> users, UserFilterDto userFilterDto) {
        return users.filter(user -> user.getUsername().startsWith(userFilterDto.getNamePattern()));
    }
}
