package com.clone.twitter.post_service.service.like;

import com.clone.twitter.post_service.dto.like.CommentLikeDto;
import com.clone.twitter.post_service.kafka.event.State;
import com.clone.twitter.post_service.kafka.producer.like.CommentLikeProducer;
import com.clone.twitter.post_service.mapper.like.CommentLikeMapper;
import com.clone.twitter.post_service.model.Comment;
import com.clone.twitter.post_service.model.CommentLike;
import com.clone.twitter.post_service.repository.CommentLikeRepository;
import com.clone.twitter.post_service.validator.like.LikeValidatorImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentLikeService implements LikeService<CommentLikeDto> {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeMapper commentLikeMapper;
    private final LikeValidatorImpl likeValidator;
    private final CommentLikeProducer commentLikeProducer;

    @Override
    public CommentLikeDto addLike(long userId, long id) {

        CommentLikeDto likeDto = createLikeDto(userId, id);

        likeValidator.validateUserExistence(userId);
        Comment comment = likeValidator.validateAndGetCommentToLike(userId, id);

        CommentLike like = commentLikeMapper.toEntity(likeDto);
        like.setComment(comment);
        like = commentLikeRepository.save(like);

        commentLikeProducer.produce(commentLikeMapper.toKafkaEvent(like, State.ADD));

        log.info("Like with likeId = {} was added on comment with commentId = {} by user with userId = {}", like.getId(), id, userId);

        return commentLikeMapper.toDto(like);
    }

    @Override
    public void removeLike(long userId, long id) {

        CommentLikeDto likeDto = createLikeDto(userId, id);
        CommentLike like = commentLikeMapper.toEntity(likeDto);

        commentLikeRepository.deleteByCommentIdAndUserId(id, userId);

        commentLikeProducer.produce(commentLikeMapper.toKafkaEvent(like, State.DELETE));

        log.info("Like with likeId = {} was removed from comment with commentId = {} by user with userId = {}", like.getId(), id, userId);
    }

    private CommentLikeDto createLikeDto(Long userId, Long commentId) {
        CommentLikeDto likeDto = new CommentLikeDto();
        likeDto.setUserId(userId);
        likeDto.setCommentId(commentId);
        return likeDto;
    }
}
