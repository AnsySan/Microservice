package com.clone.twitter.user_service.service.user.filter;


import com.clone.twitter.user_service.dto.user.UserFilterDto;
import com.clone.twitter.user_service.entity.User;

import java.util.stream.Stream;

public interface UserFilterService {

    Stream<User> applyFilters(Stream<User> users, UserFilterDto userFilterDto);
}
