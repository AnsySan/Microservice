package com.clone.twitter.user_service.dto;

import com.clone.twitter.user_service.dto.skill.SkillDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkillCandidateDto {
    @NotNull
    private SkillDto skill;

    @NotNull
    @Positive
    private Long offersAmount;
}
