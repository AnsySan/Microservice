package com.clone.twitter.user_service.service.profile;

import com.clone.twitter.user_service.config.context.UserContext;
import com.clone.twitter.user_service.dto.event.profile.ProfileViewEvent;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.mapper.UserMapper;
import com.clone.twitter.user_service.publisher.profile.ProfileViewEventPublisher;
import com.clone.twitter.user_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserMapper userMapper;
    private final UserContext userContext;
    private final ProfileViewEventPublisher profileViewEventPublisher;
    private final UserService userService;

    @Override
    @Transactional
    public void addView(long userId) {
        User user = userService.findUserById(userId);
        long viewerId = userContext.getUserId();
        ProfileViewEvent event = new ProfileViewEvent(userId, viewerId, LocalDateTime.now());
        if(event.getViewerId() != event.getUserId()){
            profileViewEventPublisher.publish(event);
        }
        userMapper.toDto(user);
    }
}
