package com.clone.twitter.user_service.validator.profile;

import com.clone.twitter.user_service.validator.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewProfileValidator {
    private final UserValidator userValidator;

    public void validate(long authorId, long receiverId) {
        userValidator.validateUserExistence(authorId);
        userValidator.validateUserExistence(receiverId);
    }
}
