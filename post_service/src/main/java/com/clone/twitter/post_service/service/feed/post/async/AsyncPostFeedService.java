package com.clone.twitter.post_service.service.feed.post.async;

import com.clone.twitter.post_service.dto.feed.PostFeedDto;

import java.util.concurrent.CompletableFuture;

public interface AsyncPostFeedService {

    CompletableFuture<PostFeedDto> getPostsWithAuthor(long postId, long userId);
}
