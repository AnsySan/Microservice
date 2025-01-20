package com.clone.twitter.post_service.service.post;

import com.clone.twitter.post_service.dto.post.PostCreateDto;
import com.clone.twitter.post_service.dto.post.PostDto;
import com.clone.twitter.post_service.dto.post.PostUpdateDto;
import com.clone.twitter.post_service.model.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostDto findById(Long id);

    PostDto create(PostCreateDto postCreateDto);

    PostDto publish(Long id);

    PostDto update(Long id, PostUpdateDto postUpdateDto);

    void deleteById(Long id);

    List<PostDto> findAllByHashtag(String hashtag, Pageable pageable);

    List<PostDto> findPostDraftsByUserAuthorId(Long id);

    List<PostDto> findPostDraftsByProjectAuthorId(Long id);

    List<PostDto> findPostPublicationsByUserAuthorId(Long id);

    List<PostDto> findPostPublicationsByProjectAuthorId(Long id);

    void verifyPost(List<Post> posts);

    List<Long> findAllAuthorIdsWithNotVerifiedPosts();

    void correctPosts();
}
