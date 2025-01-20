package com.clone.twitter.user_service.service;

import com.clone.twitter.user_service.dto.user.UserDto;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.exception.NotFoundException;
import com.clone.twitter.user_service.mapper.UserMapper;
import com.clone.twitter.user_service.repository.mentorship.MentorshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorshipService {
    private final MentorshipRepository mentorshipRepository;
    private final UserMapper userMapper;

    public List<UserDto> getMentees(long userId) {
        User user = mentorshipRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("This mentor with id: " + userId + " is not in the database"));
        return user.getMentees().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public List<UserDto> getMentors(long userId) {
        User user = mentorshipRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("This mentee with id: " + userId + " is not in the database"));
        return user.getMentors().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public void deleteMentee(long menteeId, long mentorId) {
        User mentor = mentorshipRepository.findById(mentorId)
                .orElseThrow(() -> new NotFoundException("Mentor with id: " + mentorId + " is not in the database"));
        User mentee = mentorshipRepository.findById(menteeId)
                .orElseThrow(() -> new NotFoundException("Mentee with id: " + menteeId + " is not in the database"));
        mentor.getMentees().remove(mentee);
        mentorshipRepository.save(mentor);
    }

    public void deleteMentor(long menteeId, long mentorId) {
        User mentor = mentorshipRepository.findById(mentorId)
                .orElseThrow(() -> new NotFoundException("Mentor with id: " + mentorId + " is not in the database"));

        User mentee = mentorshipRepository.findById(menteeId)
                .orElseThrow(() -> new NotFoundException("Mentee with id: " + menteeId + " is not in the database"));
        mentee.getMentors().remove(mentor);
        mentorshipRepository.save(mentee);
    }
}
