package com.clone.twitter.post_service.validator.like;

import com.clone.twitter.post_service.model.Comment;
import com.clone.twitter.post_service.model.Post;

public interface LikeValidator {

    Post validateAndGetPostToLike(long userId, long postId);

    Comment validateAndGetCommentToLike(long userId, long commentId);

    void validateUserExistence(long userId);
}
