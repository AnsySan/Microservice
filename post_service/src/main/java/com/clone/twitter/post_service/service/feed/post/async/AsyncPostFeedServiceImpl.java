package com.clone.twitter.post_service.service.feed.post.async;

import com.clone.twitter.post_service.config.context.UserContext;
import com.clone.twitter.post_service.dto.feed.PostFeedDto;
import com.clone.twitter.post_service.service.feed.post.PostFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
@Async("feedTaskExecutor")
public class AsyncPostFeedServiceImpl implements AsyncPostFeedService {

    private final PostFeedService postFeedService;
    private final UserContext userContext;

    @Override
    public CompletableFuture<PostFeedDto> getPostsWithAuthor(long postId, long userId) {

        userContext.setUserId(userId);
        return CompletableFuture.completedFuture(postFeedService.getPostsWithAuthor(postId));
    }
}
