package com.clone.twitter.user_service.service.subscription;


import com.clone.twitter.user_service.dto.subscription.SubscriptionRequestDto;
import com.clone.twitter.user_service.dto.user.UserDto;
import com.clone.twitter.user_service.dto.user.UserFilterDto;

import java.util.List;

public interface SubscriptionService {

    void followUser(SubscriptionRequestDto subscriptionRequestDto);

    void unfollowUser(SubscriptionRequestDto subscriptionRequestDto);

    List<UserDto> getFollowers(long followeeId, UserFilterDto filter);

    List<UserDto> getFollowings(long followerId, UserFilterDto filter);

    int getFollowersCount(long followeeId);

    int getFollowingsCount(long followerId);
}
