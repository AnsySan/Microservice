package com.clone.twitter.post_service.redis.cache.service.author;

import com.clone.twitter.post_service.config.context.UserContext;
import com.clone.twitter.post_service.dto.user.UserDto;
import com.clone.twitter.post_service.mapper.AuthorMapper;
import com.clone.twitter.post_service.redis.cache.entity.AuthorCache;
import com.clone.twitter.post_service.redis.cache.repository.AuthorCacheRepository;
import com.clone.twitter.post_service.redis.cache.service.RedisOperations;
import com.clone.twitter.post_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
@Async("authorsCacheTaskExecutor")
public class AuthorCacheServiceImpl implements AuthorCacheService {

    private final AuthorCacheRepository authorCacheRepository;
    private final RedisOperations redisOperations;
    private final UserService userService;
    private final UserContext userContext;
    private final AuthorMapper authorMapper;

    @Override
    public CompletableFuture<UserDto> save(AuthorCache entity) {

        userContext.setUserId(entity.getId());

        UserDto userDto = userService.getUserById(entity.getId());
        AuthorCache redisUser = authorMapper.toCache(userDto);

        entity = redisOperations.updateOrSave(authorCacheRepository, redisUser, redisUser.getId());

        log.info("Saved author with id {} to cache: {}", entity.getId(), redisUser);

        return CompletableFuture.completedFuture(userDto);
    }

    @Override
    public CompletableFuture<UserDto> getUserDtoByCache(AuthorCache entity) {

        userContext.setUserId(entity.getId());

        UserDto userDto = userService.getUserById(entity.getId());

        return CompletableFuture.completedFuture(userDto);
    }
}
