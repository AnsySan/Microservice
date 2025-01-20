package com.clone.twitter.achievement_service.controller;

import com.clone.twitter.achievement_service.dto.achievement.AchievementDto;
import com.clone.twitter.achievement_service.dto.achievement.AchievementFilterDto;
import com.clone.twitter.achievement_service.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping
    public List<AchievementDto> getAchievements(@RequestParam(required = false) AchievementFilterDto filters) {
        return achievementService.getAchievements(filters);
    }

    @GetMapping("/{achievementId}")
    public AchievementDto getAchievementByAchievementId(@PathVariable long achievementId) {
        return achievementService.getAchievementByAchievementId(achievementId);
    }
}
