package com.clone.twitter.post_service.service.hashtag.cache;

import com.clone.twitter.post_service.dto.post.PostHashtagDto;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface HashtagCacheService {

    Set<PostHashtagDto> getPostsByHashtag(String hashtag, Pageable pageable);

    void addPostToHashtag(String hashtag, PostHashtagDto post);

    void removePostFromHashtag(String hashtag, PostHashtagDto post);

    void updateScore(String hashtag, PostHashtagDto post);
}
