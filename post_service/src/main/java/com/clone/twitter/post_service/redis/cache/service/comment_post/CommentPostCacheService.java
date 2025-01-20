package com.clone.twitter.post_service.redis.cache.service.comment_post;

import com.clone.twitter.post_service.redis.cache.entity.CommentCache;

public interface CommentPostCacheService {

    void tryDeleteCommentFromPost(CommentCache comment);

    void tryAddCommentToPost(CommentCache comment);
}
