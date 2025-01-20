package com.clone.twitter.user_service.service.mentorship.filter.impl;

import com.clone.twitter.user_service.dto.mentorship.RequestFilterDto;
import com.clone.twitter.user_service.entity.MentorshipRequest;
import com.clone.twitter.user_service.filter.mentorship.MentorshipRequestFilter;
import com.clone.twitter.user_service.service.mentorship.filter.MentorshipRequestFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MentorshipRequestFilterServiceImpl implements MentorshipRequestFilterService {
    private final List<MentorshipRequestFilter> filters;

    @Override
    public Stream<MentorshipRequest> apply(Stream<MentorshipRequest> entities, RequestFilterDto internshipFilterDto) {
        for (MentorshipRequestFilter filter : filters) {
            if (filter.isApplicable(internshipFilterDto)) {
                entities = filter.apply(entities, internshipFilterDto);
            }
        }

        return entities;
    }
}
