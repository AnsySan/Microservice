package com.clone.twitter.post_service.service.feed.comment.async;

import com.clone.twitter.post_service.config.context.UserContext;
import com.clone.twitter.post_service.dto.feed.CommentFeedDto;
import com.clone.twitter.post_service.service.feed.comment.CommentFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
@Async("feedTaskExecutor")
public class AsyncCommentFeedServiceImpl implements AsyncCommentFeedService {

    private final CommentFeedService commentFeedService;
    private final UserContext userContext;

    @Override
    public CompletableFuture<List<CommentFeedDto>> getCommentsWithAuthors(long postId, long userId) {

        userContext.setUserId(userId);
        return CompletableFuture.completedFuture(commentFeedService.getCommentsWithAuthors(postId));
    }
}
