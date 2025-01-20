package com.clone.twitter.post_service.dto.feed;

import com.clone.twitter.post_service.dto.user.UserProfilePic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFeedDto {
    private Long id;
    private String username;
    private UserProfilePic userProfilePic;
}
