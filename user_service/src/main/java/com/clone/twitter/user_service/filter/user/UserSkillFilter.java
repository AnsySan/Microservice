package com.clone.twitter.user_service.filter.user;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;import com.clone.twitter.user_service.dto.user.UserFilterDto;
import com.clone.twitter.user_service.entity.User;

import java.util.stream.Stream;

@Component
public class UserSkillFilter implements UserFilter {

    @Override
    public boolean isAcceptable(UserFilterDto userFilterDto) {
        return userFilterDto.getSkillPattern() != null;
    }

    @Override
    @Transactional
    public Stream<User> applyFilter(Stream<User> users, UserFilterDto userFilterDto) {
        return users.filter(user -> user.getSkills().stream()
                .anyMatch(skill -> skill.getTitle().startsWith(userFilterDto.getSkillPattern())));
    }
}
