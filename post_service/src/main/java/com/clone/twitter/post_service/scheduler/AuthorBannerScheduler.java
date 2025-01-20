package com.clone.twitter.post_service.scheduler;

import com.clone.twitter.post_service.redis.pubsub.event.UserBanEvent;
import com.clone.twitter.post_service.redis.pubsub.publisher.UserBanEventPublisher;
import com.clone.twitter.post_service.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorBannerScheduler {

    private final PostService postService;
    private final UserBanEventPublisher userBanEventPublisher;

    @Scheduled(cron = "${post.user-ban.scheduler.cron}")
    @Async
    public void banUser() {
        List<Long> authorIdsToBan = postService.findAllAuthorIdsWithNotVerifiedPosts().stream()
                .collect(Collectors.groupingBy(elem -> elem, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .toList();

        if (!authorIdsToBan.isEmpty()) {
            userBanEventPublisher.publish(new UserBanEvent(authorIdsToBan));
        }
    }
}
