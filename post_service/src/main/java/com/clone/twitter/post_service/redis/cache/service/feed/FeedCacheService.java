package com.clone.twitter.post_service.redis.cache.service.feed;


import com.clone.twitter.post_service.redis.cache.entity.FeedCache;
import com.clone.twitter.post_service.redis.cache.entity.PostCache;

public interface FeedCacheService {

    void addPostToFeed(PostCache post, long subscriberId);

    void deletePostFromFeed(PostCache post, long subscriberId);

    FeedCache findByUserId(long userId);
}
