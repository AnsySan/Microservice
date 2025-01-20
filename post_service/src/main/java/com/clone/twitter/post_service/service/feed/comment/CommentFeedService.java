package com.clone.twitter.post_service.service.feed.comment;

import com.clone.twitter.post_service.dto.feed.CommentFeedDto;

import java.util.List;

public interface CommentFeedService {

    List<CommentFeedDto> getCommentsWithAuthors(long postId);
}
