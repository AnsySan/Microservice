package com.clone.twitter.user_service.filter.mentorship;

import com.clone.twitter.user_service.dto.mentorship.RequestFilterDto;
import com.clone.twitter.user_service.entity.MentorshipRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class MentorshipRequestRequesterIdFilter implements MentorshipRequestFilter {
    @Override
    public boolean isApplicable(RequestFilterDto filter) {
        return filter.getRequesterIdPattern() != null;
    }

    @Override
    public Stream<MentorshipRequest> apply(Stream<MentorshipRequest> entities, RequestFilterDto filterDto) {
        var requesterId = filterDto.getRequesterIdPattern();

        return entities.filter(entity -> entity.getRequester().getId() == requesterId);
    }
}
