package com.clone.twitter.achievement_service.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class InviteSentEvent implements Event{
    private Long userId;
    private Long receiverId;
    private Long projectId;
}
