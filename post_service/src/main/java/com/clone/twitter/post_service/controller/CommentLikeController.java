package com.clone.twitter.post_service.controller;

import com.clone.twitter.post_service.config.context.UserContext;
import com.clone.twitter.post_service.dto.like.CommentLikeDto;
import com.clone.twitter.post_service.service.like.CommentLikeService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes/comment")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService likeService;
    private final UserContext userContext;

    @PostMapping("/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Parameter(in = ParameterIn.HEADER, name = "x-user-id", required = true)
    public CommentLikeDto likeComment(@Positive @PathVariable long commentId) {
        long userId = userContext.getUserId();
        return likeService.addLike(userId, commentId);
    }

    @DeleteMapping("/{commentId}")
    @Parameter(in = ParameterIn.HEADER, name = "x-user-id", required = true)
    public void deleteLikeFromComment(@Positive @PathVariable long commentId) {
        long userId = userContext.getUserId();
        likeService.removeLike(userId, commentId);
    }
}
