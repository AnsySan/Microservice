package com.clone.twitter.post_service.validator.post;

import com.clone.twitter.post_service.model.Post;

public interface PostValidator {

    void validateAuthor(Long userId, Long projectId);

    void validatePostContent(String content);

    void validatePublicationPost(Post post);
}
