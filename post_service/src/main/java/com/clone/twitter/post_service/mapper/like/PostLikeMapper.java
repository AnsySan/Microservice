package com.clone.twitter.post_service.mapper.like;

import com.clone.twitter.post_service.dto.like.PostLikeDto;
import com.clone.twitter.post_service.kafka.event.State;
import com.clone.twitter.post_service.kafka.event.like.PostLikeEvent;
import com.clone.twitter.post_service.model.PostLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostLikeMapper {

    @Mapping(source = "postId", target = "post.id")
    PostLike toEntity(PostLikeDto likeDto);

    @Mapping(source = "post.id", target = "postId")
    PostLikeDto toDto(PostLike like);

    @Mapping(source = "like.post.id", target = "postId")
    PostLikeEvent toKafkaEvent(PostLike like, State state);
}
