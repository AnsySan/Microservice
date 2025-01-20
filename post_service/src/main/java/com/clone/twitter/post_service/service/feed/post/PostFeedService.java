package com.clone.twitter.post_service.service.feed.post;

import com.clone.twitter.post_service.dto.feed.PostFeedDto;

public interface PostFeedService {

    PostFeedDto getPostsWithAuthor(long postId);
}
