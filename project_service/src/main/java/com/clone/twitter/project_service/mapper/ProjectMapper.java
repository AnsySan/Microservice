package com.clone.twitter.project_service.mapper;

import com.clone.twitter.project_service.dto.project.ProjectDto;
import com.clone.twitter.project_service.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    ProjectDto toDto(Project project);

    Project toProject(ProjectDto projectDto);

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateProject(ProjectDto projectDto, @MappingTarget Project project);

    List<ProjectDto> toDtos(List<Project> projects);
}


