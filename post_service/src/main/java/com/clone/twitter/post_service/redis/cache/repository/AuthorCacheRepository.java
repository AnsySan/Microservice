package com.clone.twitter.post_service.redis.cache.repository;

import com.clone.twitter.post_service.redis.cache.entity.AuthorCache;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorCacheRepository extends KeyValueRepository<AuthorCache, Long> {
}
