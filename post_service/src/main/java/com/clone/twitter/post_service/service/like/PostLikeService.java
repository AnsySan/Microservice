package com.clone.twitter.post_service.service.like;

import com.clone.twitter.post_service.dto.like.PostLikeDto;
import com.clone.twitter.post_service.kafka.event.State;
import com.clone.twitter.post_service.kafka.event.like.PostLikeEvent;
import com.clone.twitter.post_service.kafka.producer.like.PostLikeProducer;
import com.clone.twitter.post_service.mapper.like.PostLikeMapper;
import com.clone.twitter.post_service.model.Post;
import com.clone.twitter.post_service.model.PostLike;
import com.clone.twitter.post_service.redis.pubsub.event.LikeEvent;
import com.clone.twitter.post_service.redis.pubsub.publisher.LikeEventPublisher;
import com.clone.twitter.post_service.repository.PostLikeRepository;
import com.clone.twitter.post_service.validator.like.LikeValidatorImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService implements LikeService<PostLikeDto> {

    private final PostLikeRepository postLikeRepository;
    private final PostLikeMapper postLikeMapper;
    private final LikeValidatorImpl likeValidator;
    private final LikeEventPublisher likeEventPublisher;
    private final PostLikeProducer postLikeProducer;

    @Override
    @Transactional
    public PostLikeDto addLike(long userId, long id) {

        PostLikeDto likeDto = createLikeDto(userId, id);

        likeValidator.validateUserExistence(userId);
        Post post = likeValidator.validateAndGetPostToLike(userId, id);

        PostLike like = postLikeMapper.toEntity(likeDto);
        like.setPost(post);
        like = postLikeRepository.save(like);

        likeEventPublisher.publish(new LikeEvent(id, post.getAuthorId(), userId, LocalDateTime.now()));
        PostLikeEvent kafkaEvent = postLikeMapper.toKafkaEvent(like, State.ADD);
        postLikeProducer.produce(kafkaEvent);

        log.info("Like with likeId = {} was added on post with postId = {} by user with userId = {}", like.getId(), id, userId);

        return postLikeMapper.toDto(like);
    }

    @Override
    @Transactional
    public void removeLike(long userId, long id) {

        PostLikeDto likeDto = createLikeDto(userId, id);
        PostLike like = postLikeMapper.toEntity(likeDto);

        postLikeRepository.deleteByPostIdAndUserId(id, userId);

        postLikeProducer.produce(postLikeMapper.toKafkaEvent(like, State.DELETE));

        log.info("Like with likeId = {} was removed from post with postId = {} by user with userId = {}", like.getId(), id, userId);
    }

    private PostLikeDto createLikeDto(Long userId, Long postId) {
        PostLikeDto likeDto = new PostLikeDto();
        likeDto.setUserId(userId);
        likeDto.setPostId(postId);
        return likeDto;
    }
}
