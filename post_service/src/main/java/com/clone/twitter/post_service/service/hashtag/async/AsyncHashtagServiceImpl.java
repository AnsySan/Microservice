package com.clone.twitter.post_service.service.hashtag.async;

import com.clone.twitter.post_service.dto.post.PostHashtagDto;
import com.clone.twitter.post_service.service.hashtag.HashtagService;
import com.clone.twitter.post_service.util.HashtagSpliterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Async("hashtagTaskExecutor")
@RequiredArgsConstructor
public class AsyncHashtagServiceImpl implements AsyncHashtagService {

    private final HashtagService hashtagService;

    @Override
    public CompletableFuture<List<PostHashtagDto>> getPostsByHashtag(String hashtag, Pageable pageable) {
        return CompletableFuture.completedFuture(hashtagService.getPageOfPostsByHashtag(hashtag, pageable));
    }

    @Override
    public void addHashtags(PostHashtagDto post) {
        Set<String> hashtags = HashtagSpliterator.getHashtags(post.getContent());
        hashtags.forEach(hashtag -> hashtagService.addHashtag(hashtag, post));
    }

    @Override
    public void removeHashtags(PostHashtagDto post) {
        Set<String> hashtags = HashtagSpliterator.getHashtags(post.getContent());
        hashtags.forEach(hashtag -> hashtagService.deleteHashtag(hashtag, post));
    }

    @Override
    public void updateHashtags(PostHashtagDto post) {
        Set<String> hashtags = HashtagSpliterator.getHashtags(post.getContent());
        hashtags.forEach(hashtag -> hashtagService.updateHashtag(hashtag, post));
    }

    @Override
    public void updateScore(PostHashtagDto post) {
        Set<String> hashtags = HashtagSpliterator.getHashtags(post.getContent());
        hashtags.forEach(hashtag -> hashtagService.updateScore(hashtag, post));
    }
}