package com.clone.twitter.post_service.dto.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeDto {
    private Long id;
    private Long userId;
    private Long postId;
}
