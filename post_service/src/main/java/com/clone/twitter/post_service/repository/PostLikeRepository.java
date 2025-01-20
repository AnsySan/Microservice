package com.clone.twitter.post_service.repository;

import com.clone.twitter.post_service.model.PostLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends CrudRepository<PostLike, Long> {

    void deleteByPostIdAndUserId(long postId, long userId);

}
