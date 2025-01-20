package com.clone.twitter.project_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InviteSentEvent implements Event {
    private Long userId;
    private Long receiverId;
    private Long projectId;
}
