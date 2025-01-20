package com.clone.twitter.achievement_service.controller;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.dto.achievement.UserAchievementDto;
import com.clone.twitter.achievement_service.model.Rarity;
import com.clone.twitter.achievement_service.service.user_achievement.UserAchievementService;
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
class UserAchievementControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserAchievementService userAchievementService;

    @InjectMocks
    private UserAchievementController userAchievementController;

    private final long userId = 1L;
    private UserAchievementDto userAchievementDto;
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

        userAchievementDto = UserAchievementDto.builder()
                .id(1L)
                .achievement(achievementDto)
                .userId(userId)
                .build();

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(userAchievementController).build();
    }

    @Test
    void getAchievementsByUserId() throws Exception {
        String json = objectMapper.writeValueAsString(List.of(userAchievementDto));

        when(userAchievementService.getAchievementsByUserId(userId)).thenReturn(List.of(userAchievementDto));

        mockMvc.perform(get("/achievements/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        InOrder inOrder = inOrder(userAchievementService);
        inOrder.verify(userAchievementService).getAchievementsByUserId(userId);
    }
}