package com.clone.twitter.post_service.redis.cache.service.post;

import com.clone.twitter.post_service.redis.cache.entity.PostCache;

public interface PostCacheService {

    void save(PostCache entity);

    void incrementLikes(long postId);

    void incrementViews(long postId);

    void decrementLikes(long postId);

    void deleteById(long postId);
}
