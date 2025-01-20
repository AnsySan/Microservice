package com.clone.twitter.user_service.validator;

import com.clone.twitter.user_service.dto.skill.SkillDto;
import com.clone.twitter.user_service.exception.DataValidationException;
import com.clone.twitter.user_service.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillValidator {
    private final SkillRepository skillRepository;

    public void validateSkill(SkillDto skill) {
        if (skill.getTitle() == null) {
            throw new DataValidationException("title doesn't exist");
        } else if (skill.getTitle().isBlank()) {
            throw new DataValidationException("title is empty");
        } else if (skillRepository.existsByTitle(skill.getTitle())) {
            throw new DataValidationException(skill.getTitle() + " already exist");
        }
    }

    public boolean validateSkill(long skillId, long userId) {
        if (skillRepository.findUserSkill(skillId, userId) != null) {
            return false;
        }
        return true;
    }
}