package com.clone.twitter.post_service.redis.cache.service.comment;


import com.clone.twitter.post_service.redis.cache.entity.CommentCache;

public interface CommentCacheService {

    void save(CommentCache entity);

    void incrementLikes(long commentId);

    void decrementLikes(long commentId);

    void deleteById(long postId);
}
