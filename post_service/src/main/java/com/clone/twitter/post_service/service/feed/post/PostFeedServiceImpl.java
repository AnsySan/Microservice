package com.clone.twitter.post_service.service.feed.post;

import com.clone.twitter.post_service.dto.feed.PostFeedDto;
import com.clone.twitter.post_service.dto.user.UserDto;
import com.clone.twitter.post_service.exception.NotFoundException;
import com.clone.twitter.post_service.mapper.PostFeedMapper;
import com.clone.twitter.post_service.model.Post;
import com.clone.twitter.post_service.repository.PostRepository;
import com.clone.twitter.post_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostFeedServiceImpl implements PostFeedService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final PostFeedMapper postFeedMapper;

    @Override
    @Transactional(readOnly = true)
    public PostFeedDto getPostsWithAuthor(long postId) {

        Post post = findById(postId);
        UserDto author = userService.getUserById(post.getAuthorId());

        return postFeedMapper.toDto(post, author);
    }

    private Post findById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));
    }
}
