package com.clone.twitter.user_service.controller;

import com.clone.twitter.user_service.dto.SkillCandidateDto;
import com.clone.twitter.user_service.dto.skill.SkillDto;
import com.clone.twitter.user_service.service.SkillService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
public class SkillController {
    private final SkillService skillService;

    public SkillDto create(@Valid SkillDto skill) {
        return skillService.create(skill);
    }

    public List<SkillDto> getUserSkills(@Positive long userId) {
        return skillService.getUserSkills(userId);
    }

    public List<SkillCandidateDto> getOfferedSkills(@Positive long userId) {
        return skillService.getOfferedSkills(userId);
    }

    public SkillDto acquireSkillFromOffers(@Positive long skillId, @Positive long userId) {
        return skillService.acquireSkillFromOffers(skillId, userId);
    }
}