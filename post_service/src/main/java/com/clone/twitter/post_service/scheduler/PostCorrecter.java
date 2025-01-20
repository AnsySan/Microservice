package com.clone.twitter.post_service.scheduler;

import com.clone.twitter.post_service.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCorrecter {

    private final PostService postService;

    @Scheduled(cron = "${post.correcter.scheduler.cron}")
    public void sendForCheck(){
        postService.correctPosts();
    }
}
