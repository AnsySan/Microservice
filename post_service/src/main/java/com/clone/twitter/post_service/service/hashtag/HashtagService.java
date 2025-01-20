package com.clone.twitter.post_service.service.hashtag;

import com.clone.twitter.post_service.dto.post.PostHashtagDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HashtagService {

    List<PostHashtagDto> getPageOfPostsByHashtag(String hashtag, Pageable pageable);

    void addHashtag(String hashtag, PostHashtagDto post);

    void deleteHashtag(String hashtag, PostHashtagDto post);

    void updateHashtag(String hashtag, PostHashtagDto post);

    void updateScore(String hashtag, PostHashtagDto post);
}
