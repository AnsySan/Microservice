package com.clone.twitter.achievement_service.controller;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.model.Rarity;
import com.clone.twitter.achievement_service.service.achievement.AchievementService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AchievementControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AchievementController achievementController;

    private final long achievementId = 1L;
    private AchievementDto achievementDto;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        achievementDto = AchievementDto.builder()
                .id(achievementId)
                .title("title")
                .description("description")
                .points(10L)
                .rarity(Rarity.RARE)
                .build();

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(achievementController).build();
    }

    @Test
    void getAchievements() throws Exception {
        String json = objectMapper.writeValueAsString(List.of(achievementDto));

        when(achievementService.getAchievements(any())).thenReturn(List.of(achievementDto));

        mockMvc.perform(get("/achievements"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        InOrder inOrder = inOrder(achievementService);
        inOrder.verify(achievementService).getAchievements(any());
    }

    @Test
    void getAchievementByAchievementId() throws Exception {
        String json = objectMapper.writeValueAsString(achievementDto);

        when(achievementService.getAchievementByAchievementId(achievementId)).thenReturn(achievementDto);

        mockMvc.perform(get("/achievements/" + achievementId))
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        InOrder inOrder = inOrder(achievementService);
        inOrder.verify(achievementService).getAchievementByAchievementId(achievementId);
    }
}