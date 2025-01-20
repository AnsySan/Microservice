package com.clone.twitter.user_service.service.mentorship.impl;

import com.clone.twitter.user_service.dto.mentorship.MentorshipRequestDto;
import com.clone.twitter.user_service.dto.mentorship.RejectionDto;
import com.clone.twitter.user_service.dto.mentorship.RequestFilterDto;
import com.clone.twitter.user_service.entity.MentorshipRequest;
import com.clone.twitter.user_service.entity.RequestStatus;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.event.mentorship.MentorshipStartEvent;
import com.clone.twitter.user_service.exception.NotFoundException;
import com.clone.twitter.user_service.mapper.MentorshipRequestMapper;
import com.clone.twitter.user_service.publisher.mentorship.MentorshipStartPublisher;
import com.clone.twitter.user_service.repository.UserRepository;
import com.clone.twitter.user_service.repository.mentorship.MentorshipRequestRepository;
import com.clone.twitter.user_service.service.mentorship.MentorshipRequestService;
import com.clone.twitter.user_service.service.mentorship.filter.MentorshipRequestFilterService;
import com.clone.twitter.user_service.validator.mentorship.MentorshipRequestValidator;
import com.clone.twitter.user_service.validator.user.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorshipRequestServiceImpl implements MentorshipRequestService {
    private final MentorshipRequestRepository mentorshipRequestRepository;
    private final MentorshipRequestFilterService mentorshipRequestFilterService;
    private final MentorshipRequestValidator mentorshipRequestValidator;
    private final MentorshipRequestMapper mentorshipRequestMapper;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final MentorshipStartPublisher mentorshipStartPublisher;

    @Override
    @Transactional
    public MentorshipRequestDto requestMentorship(MentorshipRequestDto dto) {
        userValidator.validateUsersExistence(List.of(dto.getRequesterId(), dto.getReceiverId()));
        mentorshipRequestValidator.validateMentorshipRequest(dto);

        MentorshipRequest entity = mentorshipRequestMapper.toEntity(dto);
        User requesterEntity = findById(dto.getRequesterId());
        User receiverEntity = findById(dto.getReceiverId());
        entity.setRequester(requesterEntity);
        entity.setReceiver(receiverEntity);
        MentorshipRequest entityFromDB = mentorshipRequestRepository.save(entity);

        return mentorshipRequestMapper.toDto(entityFromDB);
    }

    private User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MentorshipRequestDto> getRequests(RequestFilterDto requestFilterDto) {
        var entitiesStream = mentorshipRequestRepository.findAll().stream();

        entitiesStream = mentorshipRequestFilterService.apply(entitiesStream, requestFilterDto);

        return mentorshipRequestMapper.toDtoList(entitiesStream.toList());
    }

    @Override
    @Transactional
    public MentorshipRequestDto acceptRequest(Long id) {
        MentorshipRequest request = mentorshipRequestValidator.validateMentorshipRequestExistence(id);

        addMentor(request);
        request.setStatus(RequestStatus.ACCEPTED);
        mentorshipRequestRepository.save(request);

        MentorshipStartEvent event = mentorshipRequestMapper.toEvent(request);
        mentorshipStartPublisher.publish(event);

        return mentorshipRequestMapper.toDto(request);
    }

    @Override
    @Transactional
    public MentorshipRequestDto rejectRequest(Long id, RejectionDto rejection) {
        var entity = mentorshipRequestValidator.validateMentorshipRequestExistence(id);

        entity.setStatus(RequestStatus.REJECTED);
        entity.setRejectionReason(rejection.getRejectionReason());
        entity = mentorshipRequestRepository.save(entity);

        return mentorshipRequestMapper.toDto(entity);
    }

    private void addMentor(MentorshipRequest entity) {
        mentorshipRequestValidator.validateMentor(entity);
        var requester = entity.getRequester();
        var receiver = entity.getReceiver();
        requester.getMentors().add(receiver);
        userRepository.save(requester);
    }
}
