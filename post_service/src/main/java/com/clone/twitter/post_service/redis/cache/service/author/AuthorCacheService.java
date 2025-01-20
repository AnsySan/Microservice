package com.clone.twitter.post_service.redis.cache.service.author;


import com.clone.twitter.post_service.dto.user.UserDto;
import com.clone.twitter.post_service.redis.cache.entity.AuthorCache;

import java.util.concurrent.CompletableFuture;

public interface AuthorCacheService {

    CompletableFuture<UserDto> save(AuthorCache entity);

    CompletableFuture<UserDto> getUserDtoByCache(AuthorCache entity);
}
