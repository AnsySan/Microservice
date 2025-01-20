package com.clone.twitter.post_service.mapper;

import com.clone.twitter.post_service.dto.feed.CommentFeedDto;
import com.clone.twitter.post_service.dto.feed.FeedPublicationDto;
import com.clone.twitter.post_service.dto.feed.PostFeedDto;
import com.clone.twitter.post_service.redis.cache.entity.PostCache;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {CommentFeedMapper.class, PostFeedMapper.class})
public interface FeedPublicationMapper {

    FeedPublicationDto toDto(PostFeedDto post, List<CommentFeedDto> comments);

    @Mapping(source = "comments", target = "comments")
    @Mapping(source = "postCache", target = "post")
    FeedPublicationDto toDto(PostCache postCache);
}
