package com.clone.twitter.achievement_service.controller;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.dto.achievement.AchievementProgressDto;
import com.clone.twitter.achievement_service.model.Rarity;
import com.clone.twitter.achievement_service.service.achievement_progress.AchievementProgressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AchievementProgressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AchievementProgressService achievementProgressService;

    @InjectMocks
    private AchievementProgressController achievementProgressController;

    private final long userId = 1L;
    private AchievementProgressDto achievementProgressDto;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        AchievementDto achievementDto = AchievementDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .points(10L)
                .rarity(Rarity.RARE)
                .build();

        achievementProgressDto = AchievementProgressDto.builder()
                .id(1L)
                .achievement(achievementDto)
                .userId(userId)
                .currentPoints(10L)
                .build();

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(achievementProgressController).build();
    }

    @Test
    void getAchievementProgressesByUserId() throws Exception {
        String json = objectMapper.writeValueAsString(List.of(achievementProgressDto));

        when(achievementProgressService.getAchievementProgressesByUserId(userId)).thenReturn(List.of(achievementProgressDto));

        mockMvc.perform(get("/achievements/progress/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        InOrder inOrder = inOrder(achievementProgressService);
        inOrder.verify(achievementProgressService).getAchievementProgressesByUserId(userId);
    }
}