package com.clone.twitter.achievement_service.event.mentorship;

import com.clone.twitter.achievement_service.event.Event;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class MentorshipStartEvent implements Event {
    private long mentorId;
    private long menteeId;
}
