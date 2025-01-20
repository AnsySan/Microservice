package com.clone.twitter.project_service.mapper;

import com.clone.twitter.project_service.dto.project.ProjectCoverDto;
import com.clone.twitter.project_service.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectCoverMapper {

    ProjectCoverDto toDto(Project project);

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Project toProject(ProjectCoverDto projectDto);
}


