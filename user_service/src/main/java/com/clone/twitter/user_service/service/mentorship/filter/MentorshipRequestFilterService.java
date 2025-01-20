package com.clone.twitter.user_service.service.mentorship.filter;


import com.clone.twitter.user_service.dto.mentorship.RequestFilterDto;
import com.clone.twitter.user_service.entity.MentorshipRequest;

import java.util.stream.Stream;

public interface MentorshipRequestFilterService {
    public Stream<MentorshipRequest> apply(Stream<MentorshipRequest> entities, RequestFilterDto internshipFilterDto);
}
