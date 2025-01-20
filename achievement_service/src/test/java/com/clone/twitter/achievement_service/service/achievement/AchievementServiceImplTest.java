package com.clone.twitter.achievement_service.service.achievement;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.exception.NotFoundException;
import com.clone.twitter.achievement_service.mapper.AchievementMapper;
import com.clone.twitter.achievement_service.model.Achievement;
import com.clone.twitter.achievement_service.model.Rarity;
import com.clone.twitter.achievement_service.repository.AchievementRepository;
import com.clone.twitter.achievement_service.service.achievement.filter.AchievementFilterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTest {

    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private AchievementFilterService achievementFilterService;
    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private AchievementServiceImpl achievementService;

    private final long achievementId = 1L;
    private Achievement achievement;
    private AchievementDto achievementDto;

    @BeforeEach
    void setUp() {
        String title = "title";

        achievement = Achievement.builder()
                .id(achievementId)
                .title(title)
                .description("description")
                .points(10L)
                .rarity(Rarity.RARE)
                .build();

        achievementDto = AchievementDto.builder()
                .id(achievementId)
                .title(title)
                .description("description")
                .points(10L)
                .rarity(Rarity.RARE)
                .build();
    }

    @Test
    void getAchievements() {
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        when(achievementFilterService.applyFilters(any(), any())).thenReturn(Stream.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        List<AchievementDto> actual = achievementService.getAchievements(null);
        assertIterableEquals(List.of(achievementDto), actual);

        InOrder inOrder = inOrder(achievementFilterService, achievementRepository, achievementMapper);
        inOrder.verify(achievementRepository).findAll();
        inOrder.verify(achievementFilterService).applyFilters(any(), any());
        inOrder.verify(achievementMapper).toDto(achievement);
    }

    @Test
    void getAchievementByAchievementId() {
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        AchievementDto actual = achievementService.getAchievementByAchievementId(achievementId);
        assertEquals(achievementDto, actual);

        InOrder inOrder = inOrder(achievementMapper, achievementRepository, achievementFilterService);
        inOrder.verify(achievementRepository).findById(achievementId);
        inOrder.verify(achievementMapper).toDto(achievement);
    }

    @Test
    void getAchievementByAchievementIdNotFoundException() {
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> achievementService.getAchievementByAchievementId(achievementId));
        assertEquals("Achievement with achievementId " + achievementId + " not found", e.getMessage());
    }
}