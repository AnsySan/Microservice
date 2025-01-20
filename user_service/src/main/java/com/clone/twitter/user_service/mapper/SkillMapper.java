package com.clone.twitter.user_service.mapper;

import com.clone.twitter.user_service.dto.skill.SkillDto;
import com.clone.twitter.user_service.entity.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillMapper {

    SkillDto toDto(Skill skill);

    Skill toEntity(SkillDto skillDto);

    void update(SkillDto dto, @MappingTarget Skill entity);
}
