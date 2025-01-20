package com.clone.twitter.post_service.service.comment;

import com.clone.twitter.post_service.dto.comment.CommentDto;
import com.clone.twitter.post_service.validator.comment.CommentValidator;
import com.clone.twitter.post_service.dto.comment.CommentToCreateDto;
import com.clone.twitter.post_service.dto.comment.CommentToUpdateDto;
import com.clone.twitter.post_service.kafka.event.State;
import com.clone.twitter.post_service.kafka.producer.comment.CommentProducer;
import com.clone.twitter.post_service.mapper.comment.CommentMapper;
import com.clone.twitter.post_service.model.Comment;
import com.clone.twitter.post_service.model.Post;
import com.clone.twitter.post_service.repository.CommentRepository;
import com.clone.twitter.post_service.repository.PostRepository;
import com.clone.twitter.post_service.service.commonMethods.CommonServiceMethods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentValidator commentValidator;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommonServiceMethods commonServiceMethods;
    private final CommentProducer commentProducer;

    @Override
    public CommentDto createComment(long postId, long userId, CommentToCreateDto commentDto) {

        Post post = commonServiceMethods.findEntityById(postRepository, postId, "Post");
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setAuthorId(userId);
        comment.setPost(post);

        commentValidator.validateCreateComment(userId);

        comment = commentRepository.save(comment);

        commentProducer.produce(commentMapper.toKafkaEvent(comment, State.ADD));

        log.info("Created comment on post {} authored by {}", postId, userId);

        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllPostComments(long postId) {

        return commentRepository.findAllByPostId(postId).stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto updateComment(long commentId, long userId, CommentToUpdateDto updatedCommentDto) {

        Comment commentToUpdate = commonServiceMethods.findEntityById(commentRepository, commentId, "Comment");

        commentValidator.validateUpdateAlbum(commentToUpdate, userId);

        commentMapper.update(updatedCommentDto, commentToUpdate);
        commentToUpdate = commentRepository.save(commentToUpdate);

        commentProducer.produce(commentMapper.toKafkaEvent(commentToUpdate, State.UPDATE));

        log.info("Updated comment {} on post {} authored by {}", commentId, commentToUpdate.getPost().getId(), userId);

        return commentMapper.toDto(commentToUpdate);
    }

    @Override
    public CommentDto deleteComment(long postId, long commentId, long userId) {

        Comment comment = commonServiceMethods.findEntityById(commentRepository, commentId, "Comment");
        CommentDto commentToDelete = commentMapper.toDto(comment);

        commentValidator.validateDeleteAlbum(postId, userId, comment);

        commentRepository.deleteById(commentId);

        commentProducer.produce(commentMapper.toKafkaEvent(comment, State.DELETE));

        log.info("Deleted comment {} on post {} authored by {}", commentId, comment.getPost().getId(), userId);

        return commentToDelete;
    }
}