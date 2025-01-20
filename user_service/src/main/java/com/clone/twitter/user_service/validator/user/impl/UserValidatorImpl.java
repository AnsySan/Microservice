package com.clone.twitter.user_service.validator.user.impl;

import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.exception.DataValidationException;
import com.clone.twitter.user_service.repository.UserRepository;
import com.clone.twitter.user_service.validator.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserValidatorImpl implements UserValidator {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User validateUserExistence(Long userId) {
        var optional = userRepository.findById(userId);
        return optional.orElseThrow(() -> {
            var message = String.format("a user with %d does not exist", userId);
            return new DataValidationException(message);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> validateUsersExistence(List<Long> userIds) {
        return userIds.stream()
                .map(this::validateUserExistence)
                .toList();
    }
}
