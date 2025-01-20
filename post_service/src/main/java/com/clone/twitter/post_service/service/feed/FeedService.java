package com.clone.twitter.post_service.service.feed;

import com.clone.twitter.post_service.dto.feed.FeedPublicationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedService {
    List<FeedPublicationDto> getNewsFeed(long userId, Pageable pageable);
}
