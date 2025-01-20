package com.clone.twitter.post_service.mapper.like;

import com.clone.twitter.post_service.dto.like.CommentLikeDto;
import com.clone.twitter.post_service.kafka.event.State;
import com.clone.twitter.post_service.kafka.event.like.CommentLikeEvent;
import com.clone.twitter.post_service.model.CommentLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentLikeMapper {

    @Mapping(source = "commentId", target = "comment.id")
    CommentLike toEntity(CommentLikeDto likeDto);

    @Mapping(source = "comment.id", target = "commentId")
    CommentLikeDto toDto(CommentLike like);

    @Mapping(source = "like.comment.id", target = "commentId")
    CommentLikeEvent toKafkaEvent(CommentLike like, State state);
}
