package com.clone.twitter.user_service.mapper;

import com.clone.twitter.user_service.dto.SkillCandidateDto;
import com.clone.twitter.user_service.entity.Skill;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SkillMapper.class})
public interface SkillCandidateMapper {
    SkillMapper skillMapper = Mappers.getMapper(SkillMapper.class);

    default SkillCandidateDto toDto(Skill skill, long offersAmount) {
        SkillCandidateDto skillCandidate = new SkillCandidateDto();
        skillCandidate.setSkill(skillMapper.toDto(skill));
        skillCandidate.setOffersAmount(offersAmount);
        return skillCandidate;
    }
}