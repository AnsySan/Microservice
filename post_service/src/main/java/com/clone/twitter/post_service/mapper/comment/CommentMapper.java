package com.clone.twitter.post_service.mapper.comment;

import com.clone.twitter.post_service.dto.comment.CommentDto;
import com.clone.twitter.post_service.dto.comment.CommentToCreateDto;
import com.clone.twitter.post_service.dto.comment.CommentToUpdateDto;
import com.clone.twitter.post_service.kafka.event.State;
import com.clone.twitter.post_service.kafka.event.comment.CommentEvent;
import com.clone.twitter.post_service.model.Comment;
import com.clone.twitter.post_service.model.CommentLike;
import com.clone.twitter.post_service.redis.cache.entity.CommentCache;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "postId", target = "post.id")
    Comment toEntity(CommentToCreateDto commentDto);

    @Mapping(source = "likes", target = "likesCount", qualifiedByName = "getCountFromLikeList")
    @Mapping(source = "post.id", target = "postId")
    CommentDto toDto(Comment comment);

    @Mapping(source = "comment.likes", target = "likesCount", qualifiedByName = "getCountFromLikeList")
    @Mapping(source = "comment.post.id", target = "postId")
    @Mapping(source = "comment.authorId", target = "userId")
    CommentEvent toKafkaEvent(Comment comment, State state);

    @Mapping(source = "userId", target = "author.id")
    CommentCache toRedisCache(CommentEvent comment);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "likes", target = "likesCount", qualifiedByName = "getCountFromLikeList")
    CommentCache toRedisCache(Comment comment);

    @Mapping(target = "id", ignore = true)
    void update(CommentToUpdateDto commentDto, @MappingTarget Comment comment);

    @Named("getCountFromLikeList")
    default long getCountFromLikeList(List<CommentLike> likes) {
        return likes != null ? likes.size() : 0;
    }

}
