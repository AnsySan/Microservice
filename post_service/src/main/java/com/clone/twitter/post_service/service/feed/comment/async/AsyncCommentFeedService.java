package com.clone.twitter.post_service.service.feed.comment.async;

import com.clone.twitter.post_service.dto.feed.CommentFeedDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncCommentFeedService {

    CompletableFuture<List<CommentFeedDto>> getCommentsWithAuthors(long postId, long userId);
}
