package com.clone.twitter.user_service.event.mentorship;

import com.clone.twitter.user_service.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MentorshipStartEvent implements Event {
    private long mentorId;
    private long menteeId;
}