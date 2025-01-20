package com.clone.twitter.post_service.service.like;

public interface LikeService<T> {

    T addLike(long userId, long id);

    void removeLike(long userId, long id);

}
