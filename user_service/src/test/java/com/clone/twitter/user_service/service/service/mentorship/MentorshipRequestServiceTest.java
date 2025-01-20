package com.clone.twitter.user_service.service.service.mentorship;

import com.clone.twitter.user_service.dto.mentorship.MentorshipRequestDto;
import com.clone.twitter.user_service.dto.mentorship.RejectionDto;
import com.clone.twitter.user_service.entity.MentorshipRequest;
import com.clone.twitter.user_service.entity.RequestStatus;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.event.mentorship.MentorshipStartEvent;
import com.clone.twitter.user_service.exception.NotFoundException;
import com.clone.twitter.user_service.mapper.MentorshipRequestMapper;
import com.clone.twitter.user_service.publisher.mentorship.MentorshipStartPublisher;
import com.clone.twitter.user_service.repository.UserRepository;
import com.clone.twitter.user_service.repository.mentorship.MentorshipRequestRepository;
import com.clone.twitter.user_service.service.mentorship.filter.MentorshipRequestFilterService;
import com.clone.twitter.user_service.service.mentorship.impl.MentorshipRequestServiceImpl;
import com.clone.twitter.user_service.validator.mentorship.MentorshipRequestValidator;
import com.clone.twitter.user_service.validator.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MentorshipRequestServiceTest {

    @InjectMocks
    private MentorshipRequestServiceImpl mentorshipRequestService;

    @Mock
    private MentorshipRequestRepository mentorshipRequestRepository;

    @Mock
    private MentorshipRequestFilterService mentorshipRequestFilterService;

    @Mock
    private MentorshipRequestValidator mentorshipRequestValidator;

    @Mock
    private MentorshipRequestMapper mentorshipRequestMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private MentorshipStartPublisher mentorshipStartPublisher;

    private MentorshipRequestDto mentorshipRequestDto;
    private MentorshipRequest mentorshipRequest;
    private User requester;
    private User receiver;

    @BeforeEach
    void setUp() {
        mentorshipRequestDto = new MentorshipRequestDto();
        mentorshipRequestDto.setRequesterId(1L);
        mentorshipRequestDto.setReceiverId(2L);

        mentorshipRequest = new MentorshipRequest();
        requester = new User();
        receiver = new User();
        requester.setId(1L);
        receiver.setId(2L);
    }

    @Test
    void requestMentorship() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(requester));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(mentorshipRequestMapper.toEntity(any(MentorshipRequestDto.class))).thenReturn(mentorshipRequest);
        when(mentorshipRequestRepository.save(any(MentorshipRequest.class))).thenReturn(mentorshipRequest);
        when(mentorshipRequestMapper.toDto(any(MentorshipRequest.class))).thenReturn(mentorshipRequestDto);

        MentorshipRequestDto result = mentorshipRequestService.requestMentorship(mentorshipRequestDto);

        verify(userValidator).validateUsersExistence(List.of(1L, 2L));
        verify(mentorshipRequestValidator).validateMentorshipRequest(mentorshipRequestDto);
        verify(mentorshipRequestRepository).save(mentorshipRequest);
        assertEquals(mentorshipRequestDto, result);
    }

    @Test
    void requestMentorship_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> mentorshipRequestService.requestMentorship(mentorshipRequestDto));
    }

    @Test
    void acceptRequest() {
        requester.setMentors(new ArrayList<>());

        requester.setId(1L);
        receiver.setId(2L);

        mentorshipRequest.setRequester(requester);
        mentorshipRequest.setReceiver(receiver);

        when(mentorshipRequestValidator.validateMentorshipRequestExistence(anyLong())).thenReturn(mentorshipRequest);
        when(mentorshipRequestMapper.toEvent(any(MentorshipRequest.class))).thenReturn(new MentorshipStartEvent(1L, 2L));
        when(mentorshipRequestMapper.toDto(any(MentorshipRequest.class))).thenReturn(mentorshipRequestDto);

        MentorshipRequestDto result = mentorshipRequestService.acceptRequest(1L);

        verify(mentorshipRequestValidator).validateMentorshipRequestExistence(anyLong());
        verify(mentorshipStartPublisher).publish(any(MentorshipStartEvent.class));
        verify(mentorshipRequestRepository).save(any(MentorshipRequest.class));
        assertEquals(mentorshipRequestDto, result);
    }


    @Test
    void rejectRequest() {
        RejectionDto rejectionDto = new RejectionDto();
        rejectionDto.setRejectionReason("Not interested");

        when(mentorshipRequestValidator.validateMentorshipRequestExistence(1L)).thenReturn(mentorshipRequest);
        when(mentorshipRequestMapper.toDto(any(MentorshipRequest.class))).thenReturn(mentorshipRequestDto);

        MentorshipRequestDto result = mentorshipRequestService.rejectRequest(1L, rejectionDto);

        verify(mentorshipRequestValidator).validateMentorshipRequestExistence(1L);
        verify(mentorshipRequestRepository).save(mentorshipRequest);
        assertEquals(RequestStatus.REJECTED, mentorshipRequest.getStatus());
        assertEquals("Not interested", mentorshipRequest.getRejectionReason());
    }
}