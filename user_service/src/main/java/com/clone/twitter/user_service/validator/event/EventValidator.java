package com.clone.twitter.user_service.validator.event;


import com.clone.twitter.user_service.dto.event.EventDto;

public interface EventValidator {
    void validate(EventDto eventDto);

    void validateOwnersRequiredSkills(EventDto event);

    void validateUserId(long userId);
}
