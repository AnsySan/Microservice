package com.clone.twitter.post_service.service.hashtag;

import com.clone.twitter.post_service.dto.post.PostHashtagDto;
import com.clone.twitter.post_service.mapper.PostMapper;
import com.clone.twitter.post_service.model.Hashtag;
import com.clone.twitter.post_service.model.Post;
import com.clone.twitter.post_service.repository.HashtagRepository;
import com.clone.twitter.post_service.service.hashtag.cache.HashtagCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final PostMapper postMapper;
    private final HashtagCacheService hashtagCacheService;

    @Override
    @Transactional(readOnly = true)
    public List<PostHashtagDto> getPageOfPostsByHashtag(String hashtag, Pageable pageable) {

        Set<PostHashtagDto> posts = hashtagCacheService.getPostsByHashtag(hashtag, pageable);

        if (posts == null) {
            Page<Hashtag> hashtags = hashtagRepository.findPageByHashtagAndSortedByLikes(hashtag, pageable);

            return hashtags.stream()
                    .map(Hashtag::getPost)
                    .map(postMapper::toHashtagDto)
                    .toList();
        }

        return posts.stream()
                .toList();
    }

    @Override
    @Transactional
    public void addHashtag(String hashtag, PostHashtagDto post) {

        Post postEntity = postMapper.toEntity(post);
        Hashtag hashtagEntity = build(hashtag, postEntity);
        hashtagRepository.save(hashtagEntity);

        log.info("Hashtag #{} added for post with postId={}", hashtag, post.getId());

        hashtagCacheService.addPostToHashtag(hashtag, post);
    }

    @Override
    @Transactional
    public void deleteHashtag(String hashtag, PostHashtagDto post) {

        hashtagRepository.deleteByHashtagAndPostId(hashtag, post.getId());

        log.info("Post with postId={} deleted from hashtag #{}", post.getId(), hashtag);

        hashtagCacheService.removePostFromHashtag(hashtag, post);
    }

    @Override
    @Transactional
    public void updateHashtag(String hashtag, PostHashtagDto post) {

        if (hashtagRepository.existsByHashtag(hashtag)) {
            deleteHashtag(hashtag, post);
        } else {
            addHashtag(hashtag, post);
        }
    }

    @Override
    @Transactional
    public void updateScore(String hashtag, PostHashtagDto post) {
        hashtagCacheService.updateScore(hashtag, post);
    }

    private Hashtag build(String hashtag, Post post) {
        return Hashtag.builder()
                .hashtag(hashtag)
                .post(post)
                .build();
    }
}