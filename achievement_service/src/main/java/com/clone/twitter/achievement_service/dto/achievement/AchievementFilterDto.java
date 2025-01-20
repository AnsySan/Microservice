package com.clone.twitter.achievement_service.dto.achievement;

import com.clone.twitter.achievement_service.model.Rarity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementFilterDto {
    private String titlePrefix;
    private String descriptionPrefix;
    private Rarity rarity;
}
