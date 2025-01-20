package com.clone.twitter.post_service.dto.user;

import com.clone.twitter.post_service.model.PreferredContact;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private UserProfilePic userProfilePic;
    private List<Long> subscriberIds;
    private String phone;
    private PreferredContact preferredContact;

}
