package com.clone.twitter.achievement_service.service.user_achievement;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.dto.achievement.UserAchievementDto;
import com.clone.twitter.achievement_service.mapper.AchievementMapper;
import com.clone.twitter.achievement_service.mapper.UserAchievementMapper;
import com.clone.twitter.achievement_service.model.Achievement;
import com.clone.twitter.achievement_service.model.UserAchievement;
import com.clone.twitter.achievement_service.publisher.AchievementPublisher;
import com.clone.twitter.achievement_service.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAchievementServiceImpl implements UserAchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final UserAchievementMapper userAchievementMapper;
    private final AchievementMapper achievementMapper;
    private final AchievementPublisher achievementPublisher;

    @Override
    @Transactional(readOnly = true)
    public List<UserAchievementDto> getAchievementsByUserId(long userId) {
        return userAchievementRepository.findByUserId(userId).stream()
                .map(userAchievementMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void giveAchievement(long userId, AchievementDto achievement) {

        Achievement achievementEntity = achievementMapper.toEntity(achievement);

        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievementEntity)
                .build();

        userAchievementRepository.save(userAchievement);

        achievementPublisher.publish(achievementMapper.toEvent(userId, achievement));

        log.info("Achievement with achievementId={} was given to user with userId={}", achievement.getId(), userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }
}
