package com.clone.twitter.achievement_service.service.achievement_progress;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.dto.achievement.AchievementProgressDto;
import com.clone.twitter.achievement_service.exception.NotFoundException;
import com.clone.twitter.achievement_service.mapper.AchievementProgressMapper;
import com.clone.twitter.achievement_service.model.Achievement;
import com.clone.twitter.achievement_service.model.AchievementProgress;
import com.clone.twitter.achievement_service.repository.AchievementProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementProgressServiceImplTest {

    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private AchievementProgressMapper achievementProgressMapper;

    @InjectMocks
    private AchievementProgressServiceImpl achievementProgressService;

    private final long userId = 1L;
    private final long achievementId = 2L;
    private AchievementProgress achievementProgress;
    private AchievementProgressDto achievementProgressDto;

    @BeforeEach
    void setUp() {
        Achievement achievement = Achievement.builder().id(achievementId).build();
        AchievementDto achievementDto = AchievementDto.builder().id(achievementId).build();

        achievementProgress = AchievementProgress.builder()
                .id(1L)
                .achievement(achievement)
                .userId(userId)
                .currentPoints(10L)
                .build();

        achievementProgressDto = AchievementProgressDto.builder()
                .id(1L)
                .achievement(achievementDto)
                .userId(userId)
                .currentPoints(10L)
                .build();
    }

    @Test
    void getAchievementProgressesByUserId() {
        when(achievementProgressRepository.findByUserId(userId)).thenReturn(List.of(achievementProgress));
        when(achievementProgressMapper.toDto(achievementProgress)).thenReturn(achievementProgressDto);

        List<AchievementProgressDto> actual = achievementProgressService.getAchievementProgressesByUserId(userId);
        assertIterableEquals(List.of(achievementProgressDto), actual);

        InOrder inOrder = inOrder(achievementProgressRepository, achievementProgressMapper);
        inOrder.verify(achievementProgressRepository).findByUserId(userId);
        inOrder.verify(achievementProgressMapper).toDto(achievementProgress);
    }

    @Test
    void createProgressIfNecessary() {
        achievementProgressService.createProgressIfNecessary(userId, achievementId);

        InOrder inOrder = inOrder(achievementProgressRepository);
        inOrder.verify(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void getProgress() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.of(achievementProgress));
        when(achievementProgressMapper.toDto(achievementProgress)).thenReturn(achievementProgressDto);

        AchievementProgressDto actual = achievementProgressService.getProgress(userId, achievementId);
        assertEquals(achievementProgressDto, actual);

        InOrder inOrder = inOrder(achievementProgressRepository);
        inOrder.verify(achievementProgressRepository).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void getProgressNotFound() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> achievementProgressService.getProgress(userId, achievementId));
        assertEquals("Achievement progress with userId=" + userId + " and achievementId=" + achievementId + " not found", e.getMessage());
    }

    @Test
    void incrementAndGetProgress() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.of(achievementProgress));

        long actual = achievementProgressService.incrementAndGetProgress(userId, achievementId);
        assertEquals(11L, actual);
        assertEquals(11L, achievementProgress.getCurrentPoints());

        InOrder inOrder = inOrder(achievementProgressRepository);
        inOrder.verify(achievementProgressRepository).findByUserIdAndAchievementId(userId, achievementId);
        inOrder.verify(achievementProgressRepository).save(achievementProgress);
    }
}