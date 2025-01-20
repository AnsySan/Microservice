package com.clone.twitter.user_service.validator.mentorship;


import com.clone.twitter.user_service.dto.mentorship.MentorshipRequestDto;
import com.clone.twitter.user_service.entity.MentorshipRequest;

public interface MentorshipRequestValidator {
    void validateMentorshipRequest(MentorshipRequestDto dto);

    MentorshipRequest validateMentorshipRequestExistence(long id);

    void validateMentor(MentorshipRequest entity);
}
