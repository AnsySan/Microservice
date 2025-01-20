package com.clone.twitter.achievement_service.service.achievement;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.dto.achievement.AchievementFilterDto;
import com.clone.twitter.achievement_service.exception.NotFoundException;
import com.clone.twitter.achievement_service.mapper.AchievementMapper;
import com.clone.twitter.achievement_service.model.Achievement;
import com.clone.twitter.achievement_service.model.AchievementProgress;
import com.clone.twitter.achievement_service.model.UserAchievement;
import com.clone.twitter.achievement_service.repository.AchievementProgressRepository;
import com.clone.twitter.achievement_service.repository.AchievementRepository;
import com.clone.twitter.achievement_service.repository.UserAchievementRepository;
import com.clone.twitter.achievement_service.service.achievement.filter.AchievementFilterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final AchievementFilterService achievementFilterService;
    private final AchievementMapper achievementMapper;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AchievementDto> getAchievements(AchievementFilterDto filters) {

        Stream<Achievement> achievements = achievementRepository.findAll().stream();

        return achievementFilterService.applyFilters(achievements, filters)
                .map(achievementMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AchievementDto getAchievementByAchievementId(long achievementId) {

        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new NotFoundException("Achievement with achievementId " + achievementId + " not found"));

        return achievementMapper.toDto(achievement);
    }

    @Override
    @Transactional(readOnly = true)
    public AchievementDto getAchievementByTitle(String title) {

        Achievement achievement = achievementRepository.findByTitle(title)
                .orElseThrow(() -> new NotFoundException("Achievement with title=" + title + " not found"));

        return achievementMapper.toDto(achievement);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Override
    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Override
    @Transactional(readOnly = true)
    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Achievement progress with userId: %d " +
                                      "and achievementId: %d not found", userId, achievementId)));
    }

    @Override
    @Transactional
    public void giveAchievement(long userId, Achievement achievement) {

        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();

        userAchievementRepository.save(userAchievement);

        log.info("Achievement with achievementId={} was given to user with userId={}", achievement.getId(), userId);
    }
}
