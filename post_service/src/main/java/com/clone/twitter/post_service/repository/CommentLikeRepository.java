package com.clone.twitter.post_service.repository;

import com.clone.twitter.post_service.model.CommentLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends CrudRepository<CommentLike, Long> {

    void deleteByCommentIdAndUserId(long commentId, long userId);

}
