package com.clone.twitter.post_service.service.feed.comment;

import com.clone.twitter.post_service.dto.feed.CommentFeedDto;
import com.clone.twitter.post_service.dto.user.UserDto;
import com.clone.twitter.post_service.mapper.CommentFeedMapper;
import com.clone.twitter.post_service.model.Comment;
import com.clone.twitter.post_service.repository.CommentRepository;
import com.clone.twitter.post_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentFeedServiceImpl implements CommentFeedService {

    @Value("${spring.data.redis.cache.settings.max-post-comments-size}")
    private int maxPostCommentsSize;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final CommentFeedMapper commentFeedMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CommentFeedDto> getCommentsWithAuthors(long postId) {

        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId, maxPostCommentsSize);

        return comments.stream()
                .map(comment -> {
                    UserDto author = userService.getUserById(comment.getAuthorId());
                    return commentFeedMapper.toDto(comment, author);
                })
                .toList();
    }
}
